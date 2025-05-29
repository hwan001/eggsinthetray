<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Document</title>
    <link rel="stylesheet" href="/eggsinthetray/assets/css/main.css" />
    <!-- 애니 -->
    <link rel="stylesheet" href="/eggsinthetray/assets/css/clickEffect.css" />
    <% String memberId = (String) session.getAttribute("memberId"); %>
    <script>
        var memberId = "<%= memberId %>";
    </script>
    <script src="/eggsinthetray/assets/js/clickEffect.js"></script>
    <script src="/eggsinthetray/assets/js/main.js"></script>
    <!-- 이벤트 넣기 -->
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/galmuri/dist/galmuri.css"
    />
  </head>
  <body>
    <div id="wrapper">
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
        <!-- 오른쪽(프로필 정보는 더미) -->
        <div class="section_right">
          <div class="content_profile">
            <div class="item" id="item_profile"></div>
            <div class="item" id="item_memberInfo">
              <div class="item" id="item_nickname">AVGMAX 님</div>
              <div class="item" id="item_exp">
                <!--경험치 바(추후 애니메이션 추가)-->
                <div class="exp-bar-fill" style="width: 70%"></div>
              </div>
              <div class="play_box">
                <div class="item" id="item_play">경기수 &nbsp&nbsp20</div>
                <div class="item" id="item_win">
                  승수&nbsp&nbsp&nbsp&nbsp&nbsp 10
                </div>
                <div class="item" id="item_winRate">승률 50%</div>
              </div>
            </div>
          </div>
          <div class="content_piyo">
            <img src="./assets/images/mainPiyo.GIF" />
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
