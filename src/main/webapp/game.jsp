<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
        <meta charset="UTF-8" />
        <title>Eggs in the Tray</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/game.css">
        <script src="${pageContext.request.contextPath}/assets/js/game.js"> </script>

        <%
            String roomId = request.getParameter("roomId");
        %>
	</head>
	<body>
        <div id="wrapper">

            <div id="chatWrapper" >
                <div class="chatInner">
                    <div class="chatDisplay" id="chatDisplay"></div>
                    <div class="chatInputWrapper">
                        <input type="text" id="chatInput" placeholder="메시지를 입력하세요" />
                        <button onclick="sendChatMessage()">send</button>
                    </div>
                </div>
            </div>

        </div>
	</body>
</html>
