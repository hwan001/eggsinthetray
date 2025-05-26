package websocket;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint("/chat/{roomId}")
public class ChatSocket {
    // 방마다 사용자 세션 목록 저장
    private static Map<String, Set<Session>> roomSessions = new ConcurrentHashMap<>();

    /* 입장 처리 */
    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId) {
        roomSessions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);
        //broadcast(roomId, "사용자가 입장했습니다. : " + session.getId());
        String senderId = session.getId();
        String htmlMessage = String.format(
                "<p class='chat-message'><strong>사용자가 입장했습니다. : </strong> %s</p>",
                session.getId()
        );
    }

    /* 이 부분에 작성하신 태그가 상황에 맞게 나오도록 작성하시면 됩니다. */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("roomId") String roomId) {
        // broadcast(roomId, "사용자" + session.getId() + ": " + message);
        String senderId = session.getId();
        String htmlMessage = String.format(
                "<p class='chat-message'><strong>사용자 (%s) : </strong> %s</p>",
                senderId,
                escapeHtml(message)  // XSS 방지용
        );
        broadcast(roomId, htmlMessage);
    }

    @OnClose
    public void onClose(Session session, @PathParam("roomId") String roomId) {
        Set<Session> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            sessions.remove(session);
            String senderId = session.getId();
            String htmlMessage = String.format(
                    "<p class='chat-message'><strong>사용자가 퇴장했습니다. : </strong> %s</p>",
                    session.getId()
            );
        }
    }

    /* 전체 사용자에게 메시지를 전달 합니다.*/
    private void broadcast(String roomId, String message) {
        Set<Session> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            for (Session s : sessions) {
                if (s.isOpen()) {
                    try {
                        s.getBasicRemote().sendText(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /* 사용자가 입력한 채팅에 <script>등을 만들 수 있는 특수 기호가 들어오면 안전하게 엔티티 참조 형태로 변경합니다*/
    private String escapeHtml(String input) {
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}