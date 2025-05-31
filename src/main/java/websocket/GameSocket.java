package websocket;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ServerEndpoint("/game/{roomId}")
public class GameSocket {
    private static Map<String, Set<Session>> roomSessions = new ConcurrentHashMap<>(); // 방별로 세션을 Set으로 관리
    private static Map<String, List<Session>> roomSessionsOrder = new ConcurrentHashMap<>(); // 방 별로 세션 접속을 List로 관리 (접속 순서)
    private static Map<Session, String> sessionUserMap = new ConcurrentHashMap<>(); // 세션별로 유저이름을 맵핑
    private static Map<String, Stack<Move>> boardStackMap = new ConcurrentHashMap<>(); // 방별로 게임 진행 상황을 스택으로 관리함
    private static Map<String, String> roomTurnMap = new ConcurrentHashMap<>(); // 방별로 턴을 관리함
    private static Map<String, JSONObject> roomPlayersMap = new ConcurrentHashMap<>(); // 방별로 플레이어 정보를 관리함

    private static final int BOARD_ROWS = 15;
    private static final int BOARD_COLS = 15;

    // 서버 실행 시 1회만 실행
    // private static final ScheduledExecutorService scheduler =
    // Executors.newSingleThreadScheduledExecutor();// 비데몬 스레드라 메인 스레드가 종료되어도 살아남는
    // 경우가 있음, 아래처럼 데몬 스레드로 변경해줘야 같이 종료됨
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

    static {
        scheduler.scheduleAtFixedRate(() -> {
            for (String roomId : roomSessions.keySet()) {
                int[][] board = getCurrentBoard(roomId);
                String currentTurn = roomTurnMap.get(roomId);

                JSONObject msg = new JSONObject();
                msg.put("type", "board");
                msg.put("map", boardToJsonArray(board));
                msg.put("turn", currentTurn);

                broadcast(roomId, msg.toString());
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId) {
        roomSessions.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);
        roomSessionsOrder.computeIfAbsent(roomId, k -> Collections.synchronizedList(new ArrayList<>())).add(session);

        String query = session.getQueryString(); // userId=u001
        String userId = null;

        if (query != null) {
            for (String param : query.split("&")) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && keyValue[0].equals("userId")) {
                    userId = keyValue[1];
                    break;
                }
            }
        }

        if (userId != null) {
            sessionUserMap.put(session, userId);
            System.out.println("WebSocket Opened: " + userId);
        } else {
            System.err.println("WebSocket Opened without userId!");
        }
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("roomId") String roomId) throws IOException {
        System.out.println("GameSocket ▶ onMessage(): " + message);

        JSONObject json = new JSONObject(message);
        String type = json.getString("type");

        if ("start".equals(type)) {
            List<Session> order = roomSessionsOrder.get(roomId);
            String userId = sessionUserMap.get(session);
            
            // 현재 플레이어의 색상 결정
            int index = order.indexOf(session);
            String color = (index == 0) ? "W" : (index == 1) ? "B" : "E";

            // 응답 메시지 구성
            JSONObject out = new JSONObject();
            out.put("type", "start");
            out.put("color", color);

            // 기존 플레이어 정보 추가
            if (index == 1) { // 두 번째 플레이어인 경우
                Session firstPlayerSession = order.get(0);
                String firstPlayerId = sessionUserMap.get(firstPlayerSession);
                out.put("existingPlayerId", firstPlayerId);
                out.put("existingPlayerColor", "W");
            }

            // 현재 세션에 메시지 전송
            if (session.isOpen()) {
                session.getBasicRemote().sendText(out.toString());
            }

            // 다른 플레이어들에게 새로운 플레이어 입장을 알림
            if (color.equals("B") || color.equals("W")) {
                JSONObject notification = new JSONObject();
                notification.put("type", "player_joined");
                notification.put("color", color);
                notification.put("userId", userId);

                for (Session s : roomSessions.get(roomId)) {
                    if (s != session && s.isOpen()) {
                        s.getBasicRemote().sendText(notification.toString());
                    }
                }
            }

            roomTurnMap.put(roomId, "B"); // 흑돌부터 시작
        } else if ("move".equals(type)) {
            String senderId = sessionUserMap.get(session);
            int x = json.getInt("x");
            int y = json.getInt("y");

            String currentTurn = roomTurnMap.get(roomId); // 현재 차례
            String myColor = getColorForSession(session, roomId); // 현재 세션의 색상

            // 본인 차례가 아니면 무시
            if (!currentTurn.equals(myColor)) {
                return;
            }

            System.out.println("Move received: " + senderId + " → (" + x + ", " + y + ") as " + myColor);

            // 현재 보드 상태 복원
            int[][] board = getCurrentBoard(roomId);

            // 이미 돌이 있는 자리인지 확인
            if (board[y][x] != 0) {
                System.out.println("이미 돌이 놓인 자리입니다."); // 프론트에서 disabled 되므로 따로 없어도 되긴하지만, 일단 추가
                return;
            }

            // 돌이 없으면 일단 놓은 뒤 승패를 판단, 금수는 이 코드 직전이나 승패 판단 이후에 진행 (이 경우 스택에서 pop)
            boardStackMap.computeIfAbsent(roomId, k -> new Stack<>())
                    .push(new Move(x, y, (myColor.equals("B")) ? 1 : 2, senderId));

            // 승패 판단
            board = getCurrentBoard(roomId);
            int winner = gameMapCheck(board, x, y);
            if (winner != 0) {
                String winnerColor = (winner == 1) ? "B" : "W";
                System.out.printf("승자 발생 : %s", winnerColor);
                handleGameEnd(roomId, winnerColor);
                return;
            }

            // 턴 전환
            roomTurnMap.put(roomId, myColor.equals("B") ? "W" : "B");
        } else if ("undo".equals(type)) {
            String senderId = sessionUserMap.get(session);
            Set<Session> sessions = roomSessions.get(roomId);
            List<Session> order = roomSessionsOrder.get(roomId);
            int index = order.indexOf(session);

            if (sessions != null) {
                for (Session s : new HashSet<>(sessions)) {
                    if (!sessionUserMap.get(s).equals(senderId) && (index == 1 || index == 0)){ // 보낸사람이 본인이 아니고, 해당 유저의 색상이 B 또는 W면
                        JSONObject out = new JSONObject();
                        out.put("type", "undo");
                        out.put("message", senderId + "님이 무르기 요청을 보냈습니다.");

                        try {
                            s.getBasicRemote().sendText(out.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }else if ("undo_result".equals(type)) {
            String result = json.getString("result"); // "ok" 또는 "deny"
            String senderId = json.getString("sender"); // 무르기 요청한 유저
            
            Set<Session> sessions = roomSessions.get(roomId);

            if (sessions != null) {
                if ("ok".equals(result)) {
                    // 무르기 수락 시 → pop 수행
                    Stack<Move> stack = boardStackMap.get(roomId);
                    String myColor = getColorForSession(session, roomId);

                    Move undone = null;
                    if (stack != null && !stack.isEmpty()) {
                        undone = stack.pop();
                    }

                    for (Session s : new HashSet<>(sessions)) {
                        JSONObject msg = new JSONObject();
                        msg.put("type", "undo_result");
                        msg.put("result", "ok");

                        if (undone != null) {
                            msg.put("x", undone.getX());
                            msg.put("y", undone.getY());
                            msg.put("userId", senderId);
                        }

                        try {
                            s.getBasicRemote().sendText(msg.toString());
                            roomTurnMap.put(roomId, myColor.equals("B") ? "W" : "B");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    // 무르기 거절 시
                    for (Session s : new HashSet<>(sessions)) {
                        JSONObject msg = new JSONObject();
                        msg.put("type", "undo_result");
                        msg.put("result", "deny");
                        msg.put("userId", senderId);

                        try {
                            s.getBasicRemote().sendText(msg.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } 
        else if ("quit".equals(type)) {
            String senderId = sessionUserMap.get(session);
            System.out.println("quit > senderId : " + senderId);

            Set<Session> sessions = roomSessions.get(roomId);
            if (sessions != null) {
                for (Session s : new HashSet<>(sessions)) {
                    JSONObject out = new JSONObject();
                    out.put("type", "quit");

                    if (sessionUserMap.get(s).equals(senderId)) {
                        out.put("result", "lose");
                    } else {
                        out.put("result", "win");
                    }

                    try {
                        s.getBasicRemote().sendText(out.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // 여기선 Map에서 직접 제거하지 않음 — onClose에서만 관리
            try {
                session.close(); // @OnClose가 호출되며 정리됨 -> 이게 진짜 똑똑한 듯
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                roomSessionsOrder.remove(roomId);
                boardStackMap.remove(roomId);
                roomTurnMap.remove(roomId);
                roomPlayersMap.remove(roomId); // roomPlayersMap도 정리
                System.out.println("Room removed: " + roomId);
            }
        }

        List<Session> sessionOrder = roomSessionsOrder.get(roomId);
        if (sessionOrder != null) {
            sessionOrder.remove(session);
        }
    }

    /* 헬퍼 메소드 */
    private static void broadcast(String roomId, String message) {
        Set<Session> sessions = roomSessions.get(roomId);

        if (sessions != null) {
            // 복사본을 만들어 순회 시 예외 방지 -> 복사본 없다면 roomSessions이 제거될 경우 동시성 문제 발생
            for (Session s : new HashSet<>(sessions)) {
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

    private static JSONArray boardToJsonArray(int[][] board) {
        JSONArray result = new JSONArray();
        for (int[] row : board) {
            JSONArray jsonRow = new JSONArray();
            for (int cell : row) {
                jsonRow.put(cell);
            }
            result.put(jsonRow);
        }
        return result;
    }

    private static int[][] getCurrentBoard(String roomId) {
        int[][] board = new int[BOARD_ROWS][BOARD_COLS];

        Stack<Move> stack = boardStackMap.get(roomId);

        if (stack == null) {
            // System.out.println("boardStackMap: 해당 방에 대한 스택 없음");
            return board;
        }

        // System.out.println("boardStackMap size: " + stack.size());
        for (Move move : stack) {
            // System.out.printf("Move: (%d, %d) color=%d\n", move.getX(), move.getY(),
            // move.getColor());
            board[move.getY()][move.getX()] = move.getColor(); // y, x 순서 주의
        }

        return board;
    }

    private static int[][] getBoardFromStack(Stack<Move> stack, int rows, int cols) {
        int[][] board = new int[rows][cols];

        for (Move move : stack) {
            board[move.y][move.x] = move.color;
        }

        return board;
    }

    private String getColorForSession(Session session, String roomId) {
        List<Session> order = roomSessionsOrder.get(roomId);
        int index = order.indexOf(session);
        return (index == 0) ? "W" : (index == 1) ? "B" : "E";
    }

    private int gameMapCheck(int[][] board, int x, int y) {
        int color = board[y][x];

        // 보드 출력
        /*
         * System.out.println("gameMapCheck.currentBoard:");
         * for (int i = 0; i < BOARD_ROWS; i++) {
         * for (int j = 0; j < BOARD_COLS; j++) {
         * System.out.print(board[i][j] + " ");
         * }
         * System.out.println();
         * }
         * System.out.println("gameMapCheck.color: " + color + ", x:" + x + ", y:" + y);
         */

        if (color == 0)
            return 0;

        int[][] directions = {
                { 0, 1 }, // →
                { 1, 0 }, // ↓
                { 1, 1 }, // ↘
                { 1, -1 } // ↙
        };

        for (int[] dir : directions) {
            int count = 1;
            count += countStones(board, x, y, dir[0], dir[1], color);
            count += countStones(board, x, y, -dir[0], -dir[1], color);

            if (count >= 5)
                return color;
        }

        return 0;
    }

    private int countStones(int[][] board, int x, int y, int dx, int dy, int color) {
        int count = 0;
        int nx = x + dx;
        int ny = y + dy;

        while (nx >= 0 && nx < BOARD_COLS && ny >= 0 && ny < BOARD_ROWS && board[ny][nx] == color) {
            count++;
            nx += dx;
            ny += dy;
        }

        return count;
    }

    private void handleGameEnd(String roomId, String winnerColor) {
        for (Session s : roomSessions.get(roomId)) {
            String userId = sessionUserMap.get(s);
            String playerColor = getColorForSession(s, roomId);

            JSONObject out = new JSONObject();
            out.put("type", "quit");

            if (playerColor.equals(winnerColor)) {
                out.put("result", "win");
                out.put("message", "게임에서 승리했습니다.");
            } else {
                out.put("result", "lose");
                out.put("message", "게임에서 패배하였습니다.");
            }

            try {
                if (s.isOpen()) {
                    s.getBasicRemote().sendText(out.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 게임 상태 초기화 (스택 삭제)
        boardStackMap.remove(roomId);
        roomTurnMap.remove(roomId);
        roomSessions.remove(roomId);
        roomSessionsOrder.remove(roomId);
    }

}