<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
        <meta charset="UTF-8" />
        <title>Eggs in the Tray</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/game.css">
        <script src="${pageContext.request.contextPath}/assets/js/game.js"> </script>

        <%
            int rows = 15;
            int cols = 15;

            String rowsParam = request.getParameter("rows");
            String colsParam = request.getParameter("cols");

            if (rowsParam != null) rows = Integer.parseInt(rowsParam);
            if (colsParam != null) cols = Integer.parseInt(colsParam);

            String roomId = request.getParameter("roomId");
        %>
	</head>
	<body>
        <div id="wrapper">

            <div id="boardWrapper"
                 style="grid-template-columns: repeat(<%= cols %>, 40px); grid-template-rows: repeat(<%= rows %>, 40px);">

              <div class="chickHead" style="top: -52px; left: 0;"></div>
              <div class="chickHead" style="top: -52px; right: -8px;"></div>

              <% for (int i = 0; i < rows; i++) {
                   for (int j = 0; j < cols; j++) {
                       boolean isDisabled = (i == 0 && j == 0); //
                       boolean isForbidden = (i == 1 && j == 1);
              %>
                <div class="boardBlock <%= isDisabled ? "disabled " : "" %> <%= isForbidden ? " forbidden" : "" %>"></div>
              <% } } %>
            </div>

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
