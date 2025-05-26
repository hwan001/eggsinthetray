<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>카카오로그</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css" />
  </head>
  <body>
    <div id="wrapper">
      <main class="center">
        <div id="content_title">
          Eggs<br />
          <span>in the</span> Tray
        </div>
        <div id="content_btn" class="center">
          <a href="${pageContext.request.contextPath}/kakaooauth">
            <img src="${pageContext.request.contextPath}/assets/images/loginKakaoLogo.png" alt="카카오로고" />
            카카오로 시작하기
          </a>
        </div>
      </main>
    </div>
  </body>
</html>
