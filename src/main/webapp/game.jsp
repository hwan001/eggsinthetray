<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>Eggs in the Tray</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/galmuri/dist/galmuri.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/game.css">
        <script src="${pageContext.request.contextPath}/assets/js/game.js"></script>
        <% String roomId=request.getParameter("roomId"); %>
    </head>

    <body>
        <div id="wrapper">
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
        </div>
    </body>

    </html>