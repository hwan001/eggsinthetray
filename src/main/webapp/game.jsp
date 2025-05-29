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

        <% String roomId=request.getParameter("roomId"); String userId=request.getParameter("userId"); int rows=15; int
            cols=15; String rowsParam=request.getParameter("rows"); String colsParam=request.getParameter("cols"); if
            (rowsParam !=null) rows=Integer.parseInt(rowsParam); if (colsParam !=null) cols=Integer.parseInt(colsParam);
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

            <!-- 방제목 UI -->
            <div id="section_room_title">
                <div class="content_room_title">[공개] 방이름이름이름</div>
            </div>

            <!-- 프로필 UI -->
            <div id="section_profile">
                <!-- 백돌 프로필 -->
                <div class="content_profile_frame white">
                    <div class="profile_level"></div>
                    <div class="profile_img" id="profile_img_white"></div>
                    <!-- 유저 정보 -->
                    <div class="profile_user_info">
                        <div class="profile_name"></div>
                        <div class="profile_record"></div>
                        <div class="profile_win_rate"></div>
                    </div>
                    <!-- 흑백바둑돌 -->
                    <div class="profile_egg_box">
                        <div class="profile_egg_image"></div>
                        <div class="profile_egg_text"></div>
                    </div>
                </div>
                <!-- 흑돌 프로필 -->
                <div class="content_profile_frame black">
                    <div class="profile_level"></div>
                    <div class="profile_img" id="profile_img_black"></div>
                    <div class="profile_user_info">
                        <div class="profile_name"></div>
                        <div class="profile_record"></div>
                        <div class="profile_win_rate"></div>
                    </div>
                    <div class="profile_egg_box">
                        <div class="profile_egg_image"></div>
                        <div class="profile_egg_text"></div>
                    </div>
                </div>
            </div>

            <!-- 타이머 프로그래스바 -->
            <div id="section_timer">
                <div class="content_timer_text" id="timerText">20</div>
                <div class="content_timer_wrap">
                    <div class="timer_fill" id="fillBar"></div>
                </div>
            </div>


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