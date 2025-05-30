<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%
    // 세션이 없으면 로그인 페이지로 리다이렉트
    if(session.getAttribute("memberId") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    String memberId = (String) session.getAttribute("memberId");
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Eggs in the Tray</title>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/images/favicon.ico">
    <link rel="stylesheet" href="/eggsinthetray/assets/css/main.css" />
    <!-- 애니 -->
    <link rel="stylesheet" href="/eggsinthetray/assets/css/clickEffect.css" />
    <script>
        var memberId = "<%= memberId %>";
        console.log("main.jsp memberId: " + memberId);
    </script>
    <script src="/eggsinthetray/assets/js/clickEffect.js"></script>
    <script src="/eggsinthetray/assets/js/main.js" defer></script>
    <!-- 배경음악 재생 스크립트 -->
    <script>
      document.addEventListener('DOMContentLoaded', function() {
        const backgroundMusic = document.getElementById('background_music');
        backgroundMusic.play();

        // 음소거 버튼 기능 추가
        const soundBtn = document.getElementById('soundBtn');
        soundBtn.addEventListener('click', function() {
          if (backgroundMusic.paused) {
            backgroundMusic.play();
            soundBtn.querySelector('img').src = './assets/images/mainSpeaker.png';
          } else {
            backgroundMusic.pause();
            soundBtn.querySelector('img').src = './assets/images/mainSpeakerMute.png';
          }
        });

        //이미지
          const memberLevel = Number("${memberData.memberLevel}");
          let piyoImg = '';
          if (memberLevel === 0) {
            piyoImg = './assets/images/loginPip.GIF';
          } else if (memberLevel === 1) {
            piyoImg = './assets/images/mainPiyo.GIF';
          } else if (memberLevel === 2) {
            piyoImg = './assets/images/resultWin.GIF';
          }
          const piyoImgEl = document.querySelector('.content_piyo img');
          if (piyoImgEl) {
            piyoImgEl.src = piyoImg;
          }
      });
    </script>
    <!-- 이벤트 넣기 -->
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/galmuri/dist/galmuri.css"
    />
  </head>
  <body>
    <div id="wrapper">
      <!-- 배경음악 -->
      <audio
        id="background_music"
        src="${pageContext.request.contextPath}/assets/sound/nemo.mp3"
        loop
        preload="auto"
      ></audio>
      <!-- 오디오추가 -->
      <audio
        id="click_sound"
        src="${pageContext.request.contextPath}/assets/sound/click_effect.mp3"
        preload="auto"
      ></audio>
      <!--헤더부분-->
      <div id="header">
        <form action="${pageContext.request.contextPath}/logout" method="post">
          <button type="submit"></button>
        </form>
      </div>

      <!-- 검색(검색 시 특정 item만 나옴 & 옆에 리셋 버튼 누르면 방 목록 새로고침) -->
      <main>
        <div class="section_left">
          <div class="content_header">
            <div class="inputWrapper">
              <input
                type="text"
                class="searchInput"
                placeholder="방 이름을 입력해주세요"
              />
              <button class="searchBtn" id="searchBtn">
                <img src="./assets/images/mainSearch.png" />
              </button>
            </div>
             <!--clickEffect 효과 들어감-->
            <button class="refreshBtn clickEffect" id="refreshBtn"></button>
          </div>

          <!-- 방 목록(추후 item 동적으로 추가) 더미 데이터 15개 이상 있어야 드래그바 예쁨-->
          <div class="content_main">
            <div class="content" id="roomListContainer"></div>
          </div>
          <div class="content_bottom">
          <!--clickEffect 효과 들어감-->
            <button class="createBtn clickEffect" id="createRoomBtn"></button>
          </div>
        </div>
        <!-- 오른쪽(프로필 정보) -->
        <div class="section_right">
          <div class="content_profile">
            <div id="item_profile">
              <img src="${memberData.imageUrl}" alt="${memberData.nickname}">
            </div>
            <div id="item_memberInfo">
              <div class="member-nickname">${memberData.nickname}</div>
              <div class="exp-container">
                <div id="item_exp">
                  <div class="exp-bar-fill">
                    
                  </div>
                  <div class="exp-text">Lv.${memberData.memberLevel}</div>
                </div>
              </div>
              <div class="stats-container">
                <div class="stat-item">
                  <div class="stat-label">경기수</div>
                  <div class="stat-value" id="play-count">${memberData.playCnt}</div>
                </div>
                <div class="stat-item">
                  <div class="stat-label">승리</div>
                  <div class="stat-value" id="win-count">${memberData.winCnt}</div>
                </div>
                <div class="stat-item">
                  <div class="stat-label">승률</div>
                  <div class="stat-value" id="win-rate">${memberData.winRate}%</div>
                </div>
              </div>
            </div>
          </div>
          <!-- 레벨 별 이미지 변화 -->
          <div class="content_piyo">
            <img src="./assets/images/mainPiyo.GIF" alt="병아리"/>
          </div>
        </div>
      </main>


      <!-- 하단 버튼 -->
      <div class="section_bottom">
        <button class="soundBtn" id="soundBtn">
          <img src="./assets/images/mainSpeaker.png" />
        </button>
      </div>
    </div>
    <div id="createModal" class="modal-container" style="display: none"></div>
    <div id="passwordModal" class="modal-container" style="display: none"></div>
  </body>
</html>
