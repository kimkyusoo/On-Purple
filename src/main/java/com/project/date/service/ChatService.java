package com.project.date.service;


import com.project.date.dto.request.ChatMessageDto;
import com.project.date.model.ChatMessage;
import com.project.date.model.ChatRoom;
import com.project.date.model.ChatRoomUser;
import com.project.date.model.User;
import com.project.date.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RedisRepository redisRepository;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatRoomRepository chatRoomRepository;

    public void enter(Long memberId, String roomId) {
        // 채팅방 입장 정보 저장
        redisRepository.userEnterRoomInfo(memberId, roomId);
        // 채팅방의 안 읽은 메세지의 수 초기화
        redisRepository.initChatRoomMessageInfo(roomId, memberId);
    }

    //채팅
    @Transactional
    public void sendMessage(ChatMessageDto chatMessageDto, User user) {
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomUuid(chatMessageDto.getRoomId()).orElseThrow(
                () -> new IllegalArgumentException("채팅방이 존재하지 않습니다.")
        );
        ChatMessage chatMessage = new ChatMessage(user, chatMessageDto, chatRoom);
        chatMessageRepository.save(chatMessage);


        List<ChatRoomUser> chatRoomUser = chatRoomUserRepository.findAllByUserNotAndChatRoom(user, chatRoom);
        String topic = channelTopic.getTopic();
        String createdAt = getCurrentTime();
        chatMessageDto.setCreatedAt(createdAt);
        chatMessageDto.setMessageId(chatMessage.getId());
        chatMessageDto.setOtherUserId(chatRoomUser.get(0).getUser().getId());
        chatMessageDto.setType(ChatMessageDto.MessageType.TALK);
        // front에서 요청해서 진행한 작업 나의 userId 넣어주기
        chatMessageDto.setUserId(user.getId());

        redisTemplate.convertAndSend(topic, chatMessageDto);
    }

    //현재시간 추출 메소드
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return sdf.format(date);
    }

    //안읽은 메세지 업데이트
    public void updateUnReadMessageCount(ChatMessageDto requestChatMessageDto) {
        Long otherUserId = requestChatMessageDto.getOtherUserId();
        String roomId = requestChatMessageDto.getRoomId();
        // 상대방이 채팅방에 들어가 있지 않거나 들어가 있어도 나와 같은 대화방이 아닌 경우 안 읽은 메세지 처리를 할 것이다.
        if (!redisRepository.existChatRoomUserInfo(otherUserId) || !redisRepository.getUserEnterRoomId(otherUserId).equals(roomId)) {

            redisRepository.addChatRoomMessageCount(roomId, otherUserId);
            int unReadMessageCount = redisRepository
                    .getChatRoomMessageCount(roomId, otherUserId);
            String topic = channelTopic.getTopic();

            ChatMessageDto responseChatMessageDto = new ChatMessageDto(requestChatMessageDto, unReadMessageCount);

              redisTemplate.convertAndSend(topic, responseChatMessageDto);
        }
    }
}

