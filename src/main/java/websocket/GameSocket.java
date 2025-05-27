package websocket;

import org.json.JSONObject;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


@ServerEndpoint("/game/{roomId}")
public class GameSocket {
    // 방마다 사용자 세션 목록 저장
    private static Map<String, Set<Session>> roomSessions = new ConcurrentHashMap<>();

    private static final Logger logger = Logger.getLogger(GameSocket.class.getName());

    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId) {
        roomSessions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);
    }

    // 여기서 처리할 거, 좌표 받아서 검사
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("roomId") String roomId) {
        System.out.println("GameSocket ▶ onMessage(): " + message);

        JSONObject json = new JSONObject(message);
        String type = json.getString("type");

        if ("start".equals(type)) {
            int row_size = json.getInt("rows");
            int col_size = json.getInt("cols");

            JSONObject reply = new JSONObject();

            reply.put("color", "");


            broadcast(roomId, reply.toString());
        }else if ("move".equals(type)) {
            String color = json.getString("color"); //
            int x = json.getInt("x");
            int y = json.getInt("y");

            // 5) 모든 클라이언트에 브로드캐스트
            JSONObject out = new JSONObject();
            out.put("type", "move");
            out.put("x", x);
            out.put("y", y);
            out.put("color", color);
            broadcast(roomId, out.toString());
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("roomId") String roomId) {
        System.out.println("GameSocket ▶ onClose(): " + roomId);
        Set<Session> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                roomSessions.remove(roomId);
            }
        }
    }

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

}