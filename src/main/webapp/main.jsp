<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Document</title>
    <link rel="stylesheet" href="/eggsinthetray/assets/css/main.css">
    <% String memberId = (String) session.getAttribute("memberId"); %>
    <script>
        var memberId = "<%= memberId %>";
    </script>
    <script src="/eggsinthetray/assets/js/main.js"></script>
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/galmuri/dist/galmuri.css"
    />
  </head>
  <body>
    <div id="wrapper">
    <!--로그아웃 확인용 UI 적용 안된 버튼-->
    <form action="${pageContext.request.contextPath}/logout" method="post">
              <button type="submit">로그아웃</button>
    </form>
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
        <div class="content" id="roomListContainer"></div>
      </div>

      <button class="logoutBtn" id="logoutBtn"></button>
      <!-- 오른쪽(프로필 정보는 더미) -->
      <div class="section_right">
        <div class="content_profile">
          <div class="item" id="item_profile"></div>
          <div class="item" id="item_memberInfo">
            <div class="item" id="item_nickname">이름 : </div>
            <div class="item" id="exp"></div>
            <div class="item" id="item_play">경기 수 : </div>
            <div class="item" id="item_win">승 수 : </div>
            <div class="item" id="item_winRate">승률 : </div>
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
    <div id="passwordModal" class="modal-container" style="display: none;"></div>
    </div>

  </body>
</html>