// 생성하기 버튼 클릭 시 모달
document.addEventListener("DOMContentLoaded", () => {
  const createBtn = document.getElementById("createRoomBtn");
  const modalContainer = document.getElementById("createModal");

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

      setupRoomModalEvents(); //modal 함수
    } catch (error) {
      console.error("모달 로딩 실패:", error);
    }
  });
  // 모달 닫기 이벤트 리스너
  document.addEventListener("click", (e) => {
    if (e.target.id === "modalBackdrop" || e.target.id === "createModal") {
      closeModal();
    }
  });
  // 모달 닫기 함수
  function closeModal() {
    modalContainer.style.display = "none";
    modalContainer.innerHTML = "";

    const backdrop = document.getElementById("modalBackdrop");
    if (backdrop) backdrop.remove();
  }
});

// 방 입장 함수 (전역 스코프로 이동)
function enterRoom(roomId) {
  window.location.href = `/eggsinthetray/game.jsp?roomId=${roomId}`;
}

let allRooms = [];

document.addEventListener("DOMContentLoaded", function() {
  // 전체 방 목록 불러오기
  fetch("/eggsinthetray/roomList")
    .then(response => response.json())
    .then(result => {
      const roomList = result.data ? result.data : result;
      allRooms = roomList;
      renderRoomList(roomList);
    });

  // 검색 버튼 클릭 시 필터링
  document.getElementById("searchBtn").addEventListener("click", () => {
    const keyword = document.querySelector(".searchInput").value.trim().toLowerCase();
    const filteredRooms = allRooms.filter(room =>
      room.title && room.title.toLowerCase().includes(keyword)
    );
    renderRoomList(filteredRooms);
  });

  // Enter키로도 검색 가능하게
  document.querySelector(".searchInput").addEventListener("keydown", (e) => {
    if (e.key === "Enter") {
      document.getElementById("searchBtn").click();
    }
  });

  // 방 생성 버튼
  const createBtn = document.getElementById("createRoomBtn");
  if (createBtn) {
    createBtn.addEventListener("click", () => {
      showModal("createModal", "/eggsinthetray/components/modal/RoomCreateModal.jsp", setupRoomModalEvents);
    });
  }
});

// 방 조회
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

// N이면 비번 모달, N아니면 바로 입장
function attachJoinEvents() {
  const joinButtons = document.querySelectorAll(".item_joinBtn");
  joinButtons.forEach(btn => {
    btn.addEventListener("click", () => {
      const isPublic = btn.dataset.public;
      const roomId = btn.dataset.roomId;

      if (isPublic === 'N') {
        showPasswordModal(roomId);
      } else {
        enterRoom(roomId);
      }
    });
  });
}

// 패스워드 로직
function showPasswordModal(roomId) {
  showModal("passwordModal", "/eggsinthetray/components/modal/PasswordInputModal.jsp", () => {
    const input = document.getElementById("password_input");
    const form = document.getElementById("password_input_form");
    
    if (input) {
      input.focus();
      input.addEventListener("input", (e) => {
        let value = e.target.value.replace(/[^0-9]/g, '');
        if (value.length > 4) value = value.slice(0, 4);
        e.target.value = value;
      });
    }

    if (form) {
      form.addEventListener("submit", async (e) => {
        e.preventDefault();
        const password = input.value;
        console.log(roomId);
        console.log(password);

        try {
          const response = await fetch(`/eggsinthetray/api/rooms/${roomId}/password`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json; charset=UTF-8'
            },
            body: JSON.stringify({ password })
          });

          const data = await response.json();

          if (data.success) {
            enterRoom(roomId);
          } else {
            alert('비밀번호가 일치하지 않습니다.');
            input.value = '';
          }
        } catch (error) {
          console.error('비밀번호 확인 중 오류:', error);
          alert('서버 오류가 발생했습니다. 다시 시도해주세요.');
        }
      });
    }
  });
}

// 모달 배경(백드랍)적용
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

// 방 생성 모달 기능 함수화 script -> main.js
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

    console.log(title, isPublic, password);

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
        enterRoom(result.roomId);
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

