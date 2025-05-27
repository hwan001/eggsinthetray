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
          <button class="searchBtn">
            <img src="./assets/images/mainSearch.png">
          </button>
        </div>
        <button class="refreshBtn"></button>
      </div>

      <!-- 방 목록(추후 item 동적으로 추가) 더미 데이터 15개 이상 있어야 드래그바 예쁨-->
      <div class="section_room">
        <div class="content">
          <div class="item">
            <div class="item_roomState"><img src="./assets/images/mainLock.png"></div>
            <div class="item_title">여기는 방 이름</div>
            <button class="item_joinBtn"></button>
          </div>

          <div class="item">
            <div class="item_roomState"><img src="./assets/images/mainUnlock.png"></div>
            <div class="item_title">여기는 방 이름</div>
            <button class="item_joinBtn"></button>
          </div>


        </div>
      </div>

      <!-- 오른쪽 -->
      <div class="section_right">
        <div class="content_profile">
          <div class="item" id="item_profile"></div>
          <div class="item" id="item_nickname"></div>
          <div class="item" id="item_exp"></div>
          <div class="item" id="item_play"></div>
          <div class="item" id="item_win"></div>
          <div class="item" id="item_winRate"></div>
          <img src="./assets/images/mainProfile.png">
        </div>
        <div class="content_piyo">
          <img src="./assets/images/mainPiyo.png">
        </div>
      </div> 

      <!-- 하단 버튼 -->
      <div class="section_bottom">
        <button class="soundBtn">
          <img src="./assets/images/mainSpeaker.png">
        </button>

        <button class="createBtn"></div>
      </div>
    </div>

  </body>
</html>