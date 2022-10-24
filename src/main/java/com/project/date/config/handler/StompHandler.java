package com.project.date.config.handler;

import com.project.date.jwt.TokenProvider;
import com.project.date.model.User;
import com.project.date.repository.RedisRepository;
import com.project.date.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final RedisRepository redisRepository;
//    private final JwtDecoder jwtDecoder;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    // websocket을 통해 들어온 요청이 처리 되기전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("Stomp Handler Pre Send ----- ");

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        String jwtToken = "";

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // toDo : 모든 화면에서 socket이 뚫려 있기 때문에 대화방에서 온 connect라는 것을 알 수 있는 것이 있어야 한다.

            String type = accessor.getFirstNativeHeader("type");
            if (type !=null && type.equals("TALK")) {
                // 사용자 확인
                jwtToken = Objects.requireNonNull(accessor.getFirstNativeHeader("token"));

                String username = tokenProvider.decodeUsername(jwtToken) ;

                User user = userRepository.findByUsername(username).orElseThrow(
                        () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
                );

                Long userId = user.getId();
                String sessionId = (String) message.getHeaders().get("simpSessionId");
                redisRepository.saveMyInfo(sessionId, userId);
            }

        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) { // Websocket 연결 종료
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String type = accessor.getFirstNativeHeader("type");
            // 채팅방에서 나가는 것이 맞는지 확인 작업
            if (redisRepository.existMyInfo(sessionId)) {
                Long userId = redisRepository.getMyInfo(sessionId);

                // 채팅방 퇴장 정보 저장
                if (redisRepository.existChatRoomUserInfo(userId)) {
                    redisRepository.exitUserEnterRoomId(userId);
                }

                redisRepository.deleteMyInfo(sessionId);
            }
        }
        return message;
    }
//
/////////////////////////////////////////////////////////////////////
// StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        System.out.println("message:" + message);
//        System.out.println("헤더 : " + message.getHeaders());
//        System.out.println("토큰" + accessor.getNativeHeader("Authorization"));
//        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//            tokenProvider.validateToken(Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")).substring(7));
//        }
//
//////////////////////////////////////////////////////////////////////
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        if(accessor.getCommand() == StompCommand.CONNECT) {
//            if(!tokenProvider.validateToken(accessor.getFirstNativeHeader("Authorization")))
//                throw new AccessDeniedException("");
//        }
//
//////////////////////////////////////////////////////////////////////
//    private final TokenProvider TokenProvider;
//    private final ChatRoomRepository chatRoomRepository;
//    private final ChatService chatService;
//
//    // websocket을 통해 들어온 요청이 처리 되기전 실행된다.
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        if (StompCommand.CONNECT == accessor.getCommand()) { // websocket 연결요청
//            String jwtToken = accessor.getFirstNativeHeader("token");
//            log.info("CONNECT {}", jwtToken);
//
//            // Header의 jwt token 검증
//            TokenProvider.validateToken(jwtToken);
//        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청
//
//            // header정보에서 구독 destination정보를 얻고, roomId를 추출한다.
//            String roomId = chatService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
//
//            // 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
//            String sessionId = (String) message.getHeaders().get("simpSessionId");
//            chatRoomRepository.setUserEnterInfo(sessionId, roomId);
//
//            // 채팅방의 인원수를 +1한다.
//            chatRoomRepository.plusUserCount(roomId);
//
//            // 클라이언트 입장 메시지를 채팅방에 발송한다.(redis publish)
//            String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
//            chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.ENTER).roomId(roomId).sender(name).build());
//            log.info("SUBSCRIBED {}, {}", name, roomId);
//        } else if (StompCommand.DISCONNECT == accessor.getCommand()) { // Websocket 연결 종료
//
//            // 연결이 종료된 클라이언트 sesssionId로 채팅방 id를 얻는다.
//            String sessionId = (String) message.getHeaders().get("simpSessionId");
//            String roomId = chatRoomRepository.getUserEnterRoomId(sessionId);
//
//            // 채팅방의 인원수를 -1한다.
//            chatRoomRepository.minusUserCount(roomId);
//
//            // 클라이언트 퇴장 메시지를 채팅방에 발송한다.(redis publish)
//            String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
//            chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.QUIT).roomId(roomId).sender(name).build());
//
//            // 퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
//            chatRoomRepository.removeUserEnterInfo(sessionId);
//            log.info("DISCONNECTED {}, {}", sessionId, roomId);
//        }
//        return message;
//    }
}