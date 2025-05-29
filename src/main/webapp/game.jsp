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
            <div id="section_left">
                <div id="section_game">
                    <!-- 게임 UI -->
                    <div class="chick_head_wrapper">
                        <div class="chickHead" style="top: 4%; left: 5%;margin-left: 50px;"></div>
                        <div class="chickHead" style="top: 4%; right: 47%;margin-right: 50px;"></div>
                    </div>
                    <div id="boardWrapper"></div>
                </div>

                <div id="section_timer">
                    <!-- 타이머 프로그래스바 -->
                    <div id="section_timer">
                        <div class="content_timer_text" id="timerText">20</div>
                        <div class="content_timer_wrap">
                            <div class="timer_fill" id="fillBar"></div>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- 프로필 UI -->
            <div id="section_right">
                <div class="content_room_title" id="section_room_title"></div>
                
                <div class="section_profile" id="section_profile">
                    <!-- 백돌 프로필 -->
                    <div class="content_profile_frame white">
                        <div class="profile_level"></div>
                        <div class="profile_img" id="profile_img_white"></div>
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

                <!-- 채팅 UI -->
                <!-- <div id="section_chatting">  -->
                <div id="section_chat">
                    <!-- <div class="chat_bg_img"></div> -->
                    <div class="content_chat">
                        <div class="chat_display" id="chat_display_el"></div>
                        <div class="chat_input_box">
                            <textarea placeholder="메시지를 입력하세요..." id="chat_input"></textarea>
                            <button class="send_btn">
                                <img src="/eggsinthetray/assets/images/gameSendIcon.png" alt="전송">
                            </button>
                        </div>
                    </div>
                </div>
                <!-- </div> -->

                <!-- 버튼 -->
                 <div id="section_button"> 
                    <button class="btn-icon moveback_btn clickEffect" aria-label="무르기 버튼"></button>
                    <button class="btn-icon quit_btn clickEffect" aria-label="기권하기 버튼"></button>
                </div>
            </div>
        </div>
    </body>

    </html>