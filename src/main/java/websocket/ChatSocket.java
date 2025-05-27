package websocket;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/* WebSocket 서버 엔드포인트 */
// 채팅방 URL 예시: ws://localhost:8080/eggsinthetray/chat/방ID
@ServerEndpoint("/chat/{roomId}")
public class ChatSocket {

    // 채팅방별로 클라이언트 세션들을 저장하는 맵 (roomId → 세션 목록)
    private static final Map<String, Set<Session>> roomSessions = new ConcurrentHashMap<>();

    /* 클라이언트가 WebSocket 연결을 열었을 때 실행 */
    // 해당 roomId에 세션을 등록
    // 초기화 메시지 전송 (클라이언트의 sessionId 전달)
    // 시스템 메시지로 입장 알림
    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId) {
        roomSessions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);
        try {
            // 클라이언트에 본인의 sessionId 전달
            JsonObject initMessage = Json.createObjectBuilder()
                    .add("type", "init")
                    .add("sessionId", session.getId())
                    .build();
            session.getBasicRemote().sendText(initMessage.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 입장 시스템 메시지 브로드캐스트
        broadcast(roomId, createSystemMessage("사용자가 입장했습니다.", session.getId()));
    }

    /* 클라이언트가 메시지를 보냈을 때 실행 */
    // 메시지를 JSON 형태로 만들어 같은 방의 모든 세션에 전송
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("roomId") String roomId) {
        String filtered = curseWords(message);
        String safe = escapeHtml(filtered);

        JsonObject chat = Json.createObjectBuilder()
                .add("type", "chat")
                .add("senderId", session.getId())
                .add("message", safe)
                .build();

        broadcast(roomId, chat.toString());
    }

    /* 클라이언트 연결이 닫혔을 때 실행 */
    // 해당 세션을 세션 목록에서 제거
    // 마지막 유저였다면 해당 방도 삭제
    // 퇴장 시스템 메시지 브로드캐스트
    @OnClose
    public void onClose(Session session, @PathParam("roomId") String roomId) {
        Set<Session> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                roomSessions.remove(roomId); // 유저 없으면 방 제거
            }
            broadcast(roomId, createSystemMessage("사용자가 퇴장했습니다.", session.getId()));
        }
    }

    /* 해당 방에 속한 모든 세션에 메시지를 전송 */
    // 열린 세션에게만 전송
    private void broadcast(String roomId, String message) {
        Set<Session> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            for (Session s : sessions) {
                if (s.isOpen()) {
                    try {
                        s.getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /* 시스템 메시지를 JSON 형식으로 생성 */
    // type: "system", message: 내용, senderId 포함
    private String createSystemMessage(String content, String senderId) {
        JsonObject systemMsg = Json.createObjectBuilder()
                .add("type", "system")
                .add("message", content)
                .add("senderId", senderId)
                .build();
        return systemMsg.toString();
    }

    /* HTML 태그를 이스케이프 처리 (XSS 공격 방지) */
    private String escapeHtml(String input) {
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private static final Map<String, String> BAD_WORDS = Map.ofEntries(
            Map.entry("개새끼", "나쁜아이"),
            Map.entry("ㄱㅅㄲ", "나쁜아이"),
            Map.entry("새끼", "아이"),
            Map.entry("ㅅㄲ", "아이"),
            Map.entry("존나", "많이"),
            Map.entry("ㅈㄴ", "많이"),
            Map.entry("병신", "착한"),
            Map.entry("ㅂㅅ", "착하다"),
            Map.entry("시발", "어머"),
            Map.entry("ㅅㅂ", "어머"));

    private String curseWords(String input) {
        for (Map.Entry<String, String> entry : BAD_WORDS.entrySet()) {
            input = input.replace(entry.getKey(), entry.getValue());
        }
        return input;
    }
}