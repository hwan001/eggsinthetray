<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    // 매개변수로 게임 결과를 받음 (win 또는 lose)
    String gameResult = request.getParameter("result");
    if (gameResult == null) {
        gameResult = "win"; // 기본값
    }
    
    String title = gameResult.equals("win") ? "이겼닭" : "졌닭";
    String imagePath = gameResult.equals("win") ? "../../assets/images/resultWin.GIF" : "../../assets/images/resultLose.GIF";

    String soundPath = gameResult.equals("win") ? "../../assets/sound/win.mp3" : "../../assets/sound/lose.mp3";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게임 결과 모달</title>
    <link
        rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/galmuri/dist/galmuri.css"
    />
    <link rel="stylesheet" href="/eggsinthetray/assets/css/clickEffect.css" />
    <script src="/eggsinthetray/assets/js/clickEffect.js"></script>
    <style>
        #wrapper {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.3);
            display: flex;
            justify-content: center;
            align-items: center;
        }

        #section_modal_container {
            width: 700px;
            height: 700px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
        }

        #content_modal_title {
            font-family: "Galmuri11";
            font-weight: 700;
            font-size: 96px;
            color: #341A02;
            -webkit-text-stroke: 5px #FFFFFF;
        }

        #content_modal_image {
            height: 500px;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        
        #content_modal_image img {
            width: 100%;
            height: 100%;
            object-fit: contain;
        }

        #to_main_button {
            width: 254px;
            height: 85px;
            display: flex;
            justify-content: center;
            align-items: center;
            cursor: pointer;
            position: fixed;
            bottom: 20px;
            right: 20px;
        }

        #to_main_button button {
            background-image: url("../../assets/images/toMainBtn.png");
            border: none;
            border-radius: 20px;
            background-color: transparent;
            width: 100%;
            height: 100%;
            font-family: "Galmuri11";
            font-weight: 700;
            font-size: 36px;
            color: #522B09;
            transition: all 0.2s ease;
        }

        #to_main_button:hover {
            transform: scale(1.05);
        }

        #to_main_button button:hover {
            color: #341A02;
            filter: brightness(1.1);
        }
    </style>
</head>
<body>
 <!-- audio  -->
      <audio
        id="click_sound"
        src="${pageContext.request.contextPath}/assets/sound/click_effect.mp3"
        preload="auto"
      ></audio>

       <embed src="../../assets/sound/win.mp3" autostart="true" loop="infinite" width="0" height="0"></embed>
 <!-- audio  -->
    <div id="wrapper">
        <div id="section_modal_container">
            <div id="content_modal_title"><%= title %></div>
            <div id="content_modal_image">
                <img src="<%= imagePath %>" alt="<%= title %>">
            </div>
        </div>
        <div id="to_main_button">
            <button class = "clickEffect" onclick="goToMain()">메인으로</button>
        </div>
    </div>
    
    <script>
        //jsp상단에서 soundPath 찾기
        const resultSoundPath = "<%= soundPath %>";

        function goToMain() {
            window.location.href = '../../main.jsp';
        }

         //bgm 우회 마이크 허용해서 사용자와 인터렉션 있다고 하게 만들기
         window.onload = function () {
           const bgSound = new Audio(resultSoundPath);
           navigator.mediaDevices
             .getUserMedia({ audio: true })
             .then(() => {
               let AudioContext = window.AudioContext;
               let audioContext = new AudioContext();
               playSound(bgSound);
             })
             .catch((e) => {
               console.error(`Audio permissions denied: ${e}`);
             });
         };


         function playSound(sound) {
           const bgsong = sound.play();
           sound.loop = true;
           if (bgsong !== undefined) {
             bgsong;
           }
         }
    </script>
</body>
</html> 