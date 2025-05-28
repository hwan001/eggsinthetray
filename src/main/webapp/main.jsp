<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Document</title>
    <link rel="stylesheet" href="./assets/css/main.css">
    <script src="./assets/js/main.js"></script>
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/galmuri/dist/galmuri.css"
    />
  </head>
  <body>
    <div id="wrapper">
      <!-- 검색(검색 시 특정 item만 나옴 & 옆에 리셋 버튼 누르면 방 목록 새로고침) -->
      <div class="search_container">
        <div class="section_search">
          <input type="text" class="searchInput" placeholder="방 이름을 입력해주세요" />
          <button class="searchBtn" id="searchBtn">
            <img src="./assets/images/mainSearch.png">
          </button>
        </div>
        <button class="refreshBtn" id="refreshBtn"></button>
      </div>

      <!-- 방 목록(추후 item 동적으로 추가) 더미 데이터 15개 이상 있어야 드래그바 예쁨-->
      <div class="section_room">
        <div class="content">
          <div class="item">
            <div class="item_roomState"><img src="./assets/images/mainLock.png"></div>
            <div class="item_title">여기는 방 이름</div>
            <button class="item_joinBtn" id="joinBtn1"></button>
          </div>

          <div class="item">
            <div class="item_roomState"><img src="./assets/images/mainUnlock.png"></div>
            <div class="item_title">여기는 방 이름</div>
            <button class="item_joinBtn" id="joinBtn2"></button>
          </div>
        </div>
      </div>

      <button class=""></button>
      <!-- 오른쪽(프로필 정보는 더미) -->
      <div class="section_right">
        <div class="content_profile">
          <div class="item" id="item_profile"></div>
          <div class="item" id="item_memberInfo">
            <div class="item" id="item_nickname">이름 : AVGMAX</div>
            <div class="item" id="item_exp">
              <!--경험치 바(추후 애니메이션 추가)-->
              <div class="exp-bar-fill" style="width:70%;"></div>
            </div>
            <div class="item" id="item_play">경기 수 : 20</div>
            <div class="item" id="item_win">승 수 : 10</div>
            <div class="item" id="item_winRate">승률 : 50%</div>
          </div>
        </div>
        <div class="content_piyo">
          <img src="./assets/images/mainPiyo.png">
        </div>
      </div> 

      <!-- 하단 버튼 -->
      <div class="section_bottom">
        <button class="soundBtn" id="soundBtn">
          <img src="./assets/images/mainSpeaker.png">
        </button>

        <button class="createBtn" id="createRoomBtn"></button>
      </div>

    </div>

    
    <div id="createModal" class="modal-container" style="display: none;"></div>


  </body>
</html>