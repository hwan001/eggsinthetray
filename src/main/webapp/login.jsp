<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>🐣 Eggs in the Tray 🐣</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css" />
  </head>

  <style>
      @keyframes bounce {
        0%, 20%, 50%, 75%, 100% {
          transform: translateY(0);
        }
        40% {
          transform: translateY(-20px);
        }
        60% {
          transform: translateY(-5px);
        }
      }

      .bounce {
        animation: bounce 1.4s infinite;
      }
    </style>
  <body>
    <div id="wrapper">
      <main class="center">
        <div id="content_title">
          Eggs<br />
          <span>in the</span> Tray
        </div>
        <img src ="${pageContext.request.contextPath}/assets/images/loginPip.GIF"/>
        <div id="content_btn" class="center bounce">
          <a href="${pageContext.request.contextPath}/kakaooauth" >
            <img src="${pageContext.request.contextPath}/assets/images/loginKakaoLogo.png" alt="카카오로고" />
            카카오로 시작하기
          </a>
        </div>
      </main>
    </div>
  </body>
</html>
