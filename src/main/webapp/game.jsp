<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>Eggs in the Tray</title>
        <!-- 폰트 -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/galmuri/dist/galmuri.css" />

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/game.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/chat.css">
        <!-- 애니 -->
        <link rel="stylesheet" href="/eggsinthetray/assets/css/clickEffect.css" />
        <script src="/eggsinthetray/assets/js/clickEffect.js"></script>

        <script src="${pageContext.request.contextPath}/assets/js/game.js" defer></script>
        <script src="${pageContext.request.contextPath}/assets/js/chat.js"></script>

        <%
            String roomId=request.getParameter("roomId");
            String userId=request.getParameter("userId");

            int rows = 15;
            int cols = 15;

            String rowsParam = request.getParameter("rows");
            String colsParam = request.getParameter("cols");

            if (rowsParam != null) rows = Integer.parseInt(rowsParam);
            if (colsParam != null) cols = Integer.parseInt(colsParam);
        %>
    </head>

    <body>
        <!-- 오디오추가 -->
        <audio
                id="click_sound"
                src="${pageContext.request.contextPath}/assets/sound/click_effect.mp3"
                preload="auto">
        </audio>


        <!-- 배경 UI -->
        <div id="wrapper">
            <!-- 게임 UI -->
            <div class="chickHead" style="top: 4%; left: 5%;"></div>
            <div class="chickHead" style="top: 4%; right: 47%;"></div>

            <div id="boardWrapper"></div>

            <!-- 프로필 UI -->
            <!-- 타이머 프로그래스바 -->

            <!-- 채팅 UI -->
            <div id="section_chat">
                <!-- 배경 이미지  -->
                <div class="chat_bg_img"></div>
                <!-- 채팅 인터페이스 전체 -->
                <div class="content_chat">
                    <!-- 채팅 메시지 표시 영역 -->
                    <div class="chat_display" id="chat_display_el"></div>
                    <!-- 채팅 입력창과 전송 버튼 -->
                    <div class="chat_input_box">
                        <textarea id="chat_input" placeholder="메시지를 입력하세요..."></textarea>
                        <button class="send_btn">
                            <img src="${pageContext.request.contextPath}/assets/images/gameSendIcon.png" alt="채팅 전송 버튼">
                        </button>
                    </div>
                </div>
            </div>

            <!-- 무르기 버튼 -->
            <button class="moveback_btn clickEffect">
                <img src="${pageContext.request.contextPath}/assets/images/gameMoveBackButton.png" alt="무르기 버튼">
            </button>

            <!-- 기권하기 버튼 -->
            <button class="quit_btn clickEffect">
                <img src="${pageContext.request.contextPath}/assets/images/gameQuitButton.png" alt="기권하기 버튼">
            </button>
        </div>
    </body>

    </html>