package com.project.date.controller;

import com.amazonaws.services.kms.model.NotFoundException;
import com.project.date.dto.request.ChatMessageDto;
import com.project.date.jwt.JwtDecoder;
import com.project.date.jwt.TokenProvider;
import com.project.date.model.User;
import com.project.date.repository.RefreshTokenRepository;
import com.project.date.repository.UserRepository;
import com.project.date.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final JwtDecoder jwtDecoder;
//    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;


    /**
     * websocket "/pub/chat/enter"로 들어오는 메시징을 처리한다.
     * 채팅방에 입장했을 경우
     */
    @MessageMapping("/enter")
    public void enter(ChatMessageDto chatMessageDto, @Header("Authorization") String token) {
        String nickname = jwtDecoder.decodeUsername(token);
//        String nickname = tokenProvider.validateToken(token);
        User user = userRepository.findByNickname(nickname).orElseThrow(
                () -> new NotFoundException("해당 유저를 찾을 수 없습니다.")
        );
        chatService.enter(user.getId(), chatMessageDto.getRoomId());
    }

    /**
     * websocket "/sub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/message")
    public void message(ChatMessageDto chatMessageDto, @Header("Authorization") String token) {
        String username = jwtDecoder.decodeUsername(token);
        User user = userRepository.findByNickname(username).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );
        chatService.sendMessage(chatMessageDto, user);
        chatService.updateUnReadMessageCount(chatMessageDto);
    }

//    private final TokenProvider TokenProvider;
//    private final ChatRoomRepository chatRoomRepository;
//    private final ChatService chatService;
//    private final JwtDecoder jwtDecoder;
//    private final UserRepository userRepository;
//
//    @MessageMapping("/chat/enter")
//    public void enter(ChatMessage chatMessageDto, @Header("token") String token) {
//        String nickname = jwtDecoder.decodeUsername(token);
//        User user = userRepository.findByNickname(nickname).orElseThrow(
//                () -> new NotFoundException("해당 유저를 찾을 수 없습니다.")
//        );
//        chatService.enter(user.getId(), chatMessageDto.getRoomId());
//    }
//
//    /**
//     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
//     */
//    @MessageMapping("/chat/message")
//    public void message(ChatMessage message, @Header("token") String token) {
//        String nickname = String.valueOf(TokenProvider.validateToken(token));
////        String nickname = TokenProvider.getUserNameFromJwt(token);
//
//        // 로그인 회원 정보로 대화명 설정
//        message.setSender(nickname);
//
//        // 채팅방 인원수 세팅
//        message.setUserCount(chatRoomRepository.getUserCount(message.getRoomId()));
//
//        // Websocket에 발행된 메시지를 redis로 발행(publish)
//        chatService.sendChatMessage(message);
//    }
}