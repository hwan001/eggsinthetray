<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>바둑방 참가</title>
</head>
<body>
    <h2>방 참가</h2>
    <form action="game.jsp" method="get">
        <label>방 ID: <input type="text" name="roomId" required></label>
        <button type="submit">참가</button>
    </form>
</body>
</html>