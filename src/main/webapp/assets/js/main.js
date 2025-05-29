console.log('main.js loaded');

// 함수 정의는 바깥에 두고, 초기화 코드는 하나의 DOMContentLoaded 핸들러에만 넣음

function setupRoomModalEvents() {
  const publicRoom = document.getElementById('public_room');
  const privateRoom = document.getElementById('private_room');
  const passwordInput = document.getElementById('item_room_password');
  const roomCreateForm = document.getElementById('room_create_form');

  if (!publicRoom || !privateRoom || !passwordInput || !roomCreateForm) return;

  publicRoom.addEventListener('change', function () {
    if (this.checked) {
      passwordInput.disabled = true;
      passwordInput.value = '';
    }
  });

  privateRoom.addEventListener('change', function () {
    if (this.checked) {
      passwordInput.disabled = false;
    }
  });

  passwordInput.addEventListener('input', function (e) {
    let value = e.target.value.replace(/[^0-9]/g, '');
    if (value.length > 4) value = value.slice(0, 4);
    e.target.value = value;
  });

  // 초기 상태 설정
  if (publicRoom.checked) {
    passwordInput.disabled = true;
  } else if (privateRoom.checked) {
    passwordInput.disabled = false;
  }

  // 방 생성 폼 제출 이벤트
  roomCreateForm.addEventListener('submit', async function(e) {
    e.preventDefault();
    const title = document.getElementById('room_title_input').value;
    const isPublic = document.querySelector('input[name="isPublic"]:checked').value;
    const password = document.getElementById('item_room_password').value;
    if (isPublic === 'N' && !password) {
      alert('비밀방은 비밀번호를 입력해주세요.');
      return;
    }
    try {
      const response = await fetch('/eggsinthetray/api/rooms', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json; charset=UTF-8',
        },
        body: JSON.stringify({
          title: title,
          isPublic: isPublic,
          password: password
        })
      });
      if (response.ok) {
        const result = await response.json();
        window.location.href = `/eggsinthetray/game.jsp?roomId=${result.roomId}`;
      } else {
        const error = await response.text();
        alert('방 생성에 실패했습니다: ' + error);
      }
    } catch (error) {
      console.error('방 생성 요청 실패:', error);
      alert('방 생성 요청 중 오류가 발생했습니다.');
    }
  });
}

function renderRoomList(roomList) {
  const container = document.getElementById("roomListContainer");
  container.innerHTML = "";
  if (!roomList || roomList.length === 0) {
    container.innerHTML = "<div style='text-align: center; font-family: 'galmuri11'; font-size: 24px; font-weight: 700; color: #F66E89;'>방이 없습니다.</div>";
    return;
  }
  roomList.forEach((room, idx) => {
    const imgSrc = room.isPublic === "Y"
      ? "./assets/images/mainUnlock.png"
      : "./assets/images/mainLock.png";
    const html = `
      <div class="item">
        <div class="item_roomState">
          <img src="${imgSrc}">
        </div>
        <div class="item_title">${room.title}</div>
        <button class="item_joinBtn" id="joinBtn${idx}" data-public="${room.isPublic}" data-room-id="${room.roomId}"></button>
      </div>
    `;
    container.innerHTML += html;
  });
  attachJoinEvents();
}

function attachJoinEvents() {
  const joinButtons = document.querySelectorAll(".item_joinBtn");
  joinButtons.forEach(btn => {
    btn.addEventListener("click", () => {
      const isPublic = btn.dataset.public;
      const roomId = btn.dataset.roomId;
      if (isPublic === 'N') {
        showPasswordModal(roomId);
      } else {
        enterPublicRoom(roomId);
      }
    });
  });
}

function enterPublicRoom(roomId) {
  location.href = `/eggsinthetray/game?roomId=${roomId}`;
}

function showPasswordModal(roomId) {
  showModal("passwordModal", "/eggsinthetray/components/modal/PasswordInputModal.jsp", () => {
    const input = document.getElementById("password_input");
    if (input) {
      input.focus();
      input.addEventListener("input", (e) => {
        let value = e.target.value.replace(/[^0-9]/g, '');
        if (value.length > 4) value = value.slice(0, 4);
        e.target.value = value;
      });
    }
  });
}

function showModal(modalId, jspPath, onLoadCallback) {
  const modalContainer = document.getElementById(modalId);
  fetch(jspPath)
    .then(res => res.text())
    .then(html => {
      modalContainer.innerHTML = html;
      modalContainer.style.display = "flex";
      if (!document.getElementById("modalBackdrop")) {
        const backdrop = document.createElement("div");
        backdrop.id = "modalBackdrop";
        backdrop.className = "modal-backdrop";
        document.body.appendChild(backdrop);
      }
      if (typeof onLoadCallback === "function") {
        onLoadCallback();
      }
    })
    .catch(err => {
      console.error("모달 로딩 실패:", err);
    });
  document.addEventListener("click", (e) => {
    if (e.target.id === "modalBackdrop" || e.target.id === modalId) {
      closeModal(modalId);
    }
  });
}

function closeModal(modalId) {
  const modalContainer = document.getElementById(modalId);
  if (modalContainer) {
    modalContainer.style.display = "none";
    modalContainer.innerHTML = "";
  }
  const backdrop = document.getElementById("modalBackdrop");
  if (backdrop) backdrop.remove();
}

let allRooms = [];

document.addEventListener("DOMContentLoaded", function() {
  // 방 생성 모달 버튼 및 모달 컨테이너
  const createBtn = document.getElementById("createRoomBtn");
  const modalContainer = document.getElementById("createModal");
  if (createBtn && modalContainer) {
    createBtn.addEventListener("click", async () => {
      try {
        const res = await fetch("/eggsinthetray/components/modal/RoomCreateModal.jsp");
        const html = await res.text();
        modalContainer.innerHTML = html;
        modalContainer.style.display = "block";
        if (!document.getElementById("modalBackdrop")) {
          const backdrop = document.createElement("div");
          backdrop.id = "modalBackdrop";
          backdrop.className = "modal-backdrop";
          document.body.appendChild(backdrop);
        }
        setupRoomModalEvents();
      } catch (error) {
        console.error("모달 로딩 실패:", error);
      }
    });
  }
  // 모달 닫기 이벤트 리스너
  document.addEventListener("click", (e) => {
    if (e.target.id === "modalBackdrop" || e.target.id === "createModal") {
      if (modalContainer) {
        modalContainer.style.display = "none";
        modalContainer.innerHTML = "";
      }
      const backdrop = document.getElementById("modalBackdrop");
      if (backdrop) backdrop.remove();
    }
  });
  // 전체 방 목록 불러오기
  fetch("/eggsinthetray/api/rooms")
    .then(response => response.json())
    .then(result => {
      const roomList = result.data ? result.data : result;
      allRooms = roomList;
      renderRoomList(roomList);
    });
  // 검색 버튼 클릭 시 필터링
  const searchBtn = document.getElementById("searchBtn");
  if (searchBtn) {
    searchBtn.addEventListener("click", () => {
      const keyword = document.querySelector(".searchInput").value.trim().toLowerCase();
      const filteredRooms = allRooms.filter(room =>
        room.title && room.title.toLowerCase().includes(keyword)
      );
      renderRoomList(filteredRooms);
    });
  }
  // 새로고침
  const refreshBtn = document.getElementById("refreshBtn");
  if (refreshBtn) {
    refreshBtn.addEventListener("click", () => {
      fetch("/eggsinthetray/api/rooms")
        .then(response => response.json())
        .then(result => {
          const roomList = result.data ? result.data : result;
          allRooms = roomList;
          renderRoomList(roomList);
          console.log("방 목록 새로고침 완료");
        });
    });
  }

  // Enter키로도 검색 가능하게
  const searchInput = document.querySelector(".searchInput");
  if (searchInput) {
    searchInput.addEventListener("keydown", (e) => {
      if (e.key === "Enter") {
        if (searchBtn) searchBtn.click();
      }
    });
  }

  // 방 생성 버튼 (showModal)
  if (createBtn) {
    console.log('createBtn:', createBtn);
    createBtn.addEventListener("click", () => {
      showModal("createModal", "/eggsinthetray/components/modal/RoomCreateModal.jsp", setupRoomModalEvents);
    });
  }
  // 멤버 정보 fetch
  const url = `/eggsinthetray/api/members/${memberId}`;
  fetch(url, {
    credentials: 'include'
  })
    .then(response => {
      console.log('fetch response:', response);
      if (!response.ok) throw new Error('서버 응답 실패');
      return response.json();
    })
    .then(data => {
      console.log('API 응답:', data);
      document.getElementById('item_profile').innerHTML = `
        <img 
          src="${data.imageUrl}" 
          style="width:150px; height:150px; border-radius:25px; object-fit:cover;"
        >
      `;
      document.getElementById('item_nickname').textContent = `이름 : ${data.nickname}`;
      document.getElementById('item_play').textContent = `경기 수 : ${data.playCnt}`;
      document.getElementById('item_win').textContent = `승 수 : ${data.winCnt}`;
      const winRate = data.playCnt > 0 ? Math.round((data.winCnt / data.playCnt) * 100) : 0;
      document.getElementById('item_winRate').textContent = `승률 : ${winRate}%`;
    })
    .catch(error => {
      console.error('에러 발생:', error);
    })
});

