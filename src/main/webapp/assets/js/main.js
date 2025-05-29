// 모달 관련 함수들
function showModal(modalId, jspPath, onLoadCallback) {
  const modalContainer = document.getElementById(modalId);
  fetch(jspPath)
    .then(res => res.text())
    .then(html => {
      modalContainer.innerHTML = html;
      modalContainer.style.display = "flex";
      addModalBackdrop();
      if (typeof onLoadCallback === "function") {
        onLoadCallback();
      }
    })
    .catch(err => {
      console.error("모달 로딩 실패:", err);
    });
  document.addEventListener("click", (e) => {
    if (e.target.id === "modalBackdrop" || e.target.id === modalId || e.target.id === "wrapper") {
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
  removeModalBackdrop();
}

function addModalBackdrop() {
  if (!document.getElementById("modalBackdrop")) {
    const backdrop = document.createElement("div");
    backdrop.id = "modalBackdrop";
    backdrop.className = "modal-backdrop";
    document.body.appendChild(backdrop);
  }
}

function removeModalBackdrop() {
  const backdrop = document.getElementById("modalBackdrop");
  if (backdrop) backdrop.remove();
}

// 방 관련 함수들
function setupRoomModalEvents() {
  const publicRoom = document.getElementById('public_room');
  const privateRoom = document.getElementById('private_room');
  const passwordInput = document.getElementById('item_room_password');
  const roomCreateForm = document.getElementById('room_create_form');

  if (!publicRoom || !privateRoom || !passwordInput || !roomCreateForm) return;

  // 공개방/비밀방 선택에 따른 비밀번호 입력 활성화/비활성화
  publicRoom.addEventListener('change', () => handleRoomTypeChange(true, passwordInput));
  privateRoom.addEventListener('change', () => handleRoomTypeChange(false, passwordInput));

  // 비밀번호 입력 제한
  passwordInput.addEventListener('input', handlePasswordInput);

  // 초기 상태 설정
  handleRoomTypeChange(publicRoom.checked, passwordInput);

  // 방 생성 폼 제출
  roomCreateForm.addEventListener('submit', handleRoomCreate);
}

function handleRoomTypeChange(isPublic, passwordInput) {
  passwordInput.disabled = isPublic;
  if (isPublic) passwordInput.value = '';
}

function handlePasswordInput(e) {
  let value = e.target.value.replace(/[^0-9]/g, '');
  if (value.length > 4) value = value.slice(0, 4);
  e.target.value = value;
}

async function handleRoomCreate(e) {
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
      body: JSON.stringify({ title, isPublic, password })
    });

    if (response.ok) {
      const result = await response.json();
      window.location.href = `/eggsinthetray/game.jsp?roomId=${result.roomId}&userId=${memberId}`;
    } else {
      const error = await response.text();
      alert('방 생성에 실패했습니다: ' + error);
    }
  } catch (error) {
    console.error('방 생성 요청 실패:', error);
    alert('방 생성 요청 중 오류가 발생했습니다.');
  }
}

// 방 목록 관련 함수들
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
    container.innerHTML += createRoomElement(room, imgSrc, idx);
  });
  
  attachJoinEvents();
}

function createRoomElement(room, imgSrc, idx) {
  return `
    <div class="item">
      <div class="item_roomState">
        <img src="${imgSrc}">
      </div>
      <div class="item_title">${room.title}</div>
      <button class="item_joinBtn" id="joinBtn${idx}" data-public="${room.isPublic}" data-room-id="${room.roomId}"></button>
    </div>
  `;
}

function attachJoinEvents() {
  const joinButtons = document.querySelectorAll(".item_joinBtn");
  joinButtons.forEach(btn => {
    btn.addEventListener("click", () => {
      const isPublic = btn.dataset.public;
      const roomId = btn.dataset.roomId;
      isPublic === 'N' ? showPasswordModal(roomId) : enterPublicRoom(roomId);
    });
  });
}

function showPasswordModal(roomId) {
  showModal("passwordModal", "/eggsinthetray/components/modal/PasswordInputModal.jsp", () => {
    const input = document.getElementById("password_input");
    if (input) {
      input.focus();
      input.addEventListener("input", handlePasswordInput);
    }
  });
}

function enterPublicRoom(roomId) {
  location.href = `/eggsinthetray/game.jsp?roomId=${roomId}&userId=${memberId}`;
}

// 프로필 관련 함수들
async function renderMemberProfile() {
  try {
    const memberData = await fetchMemberInfo(memberId);
    updateProfileUI(memberData);
  } catch (error) {
    alert('프로필 정보를 불러오는데 실패했습니다.');
  }
}

async function fetchMemberInfo(memberId) {
  const response = await fetch(`/eggsinthetray/api/members/${memberId}`);
  if (!response.ok) throw new Error('멤버 정보를 가져오는데 실패했습니다.');
  return response.json();
}

function updateProfileUI(memberData) {
  document.getElementById('item_profile').innerHTML = 
    `<img src="${memberData.imageUrl}" style="width:130px; height:130px; border-radius:25px; object-fit:cover;">`;
  document.querySelector('.member-nickname').textContent = memberData.nickname;
  document.querySelector('.exp-bar-fill').style.width = `${(memberData.memberExp / memberData.totalExp) * 100}%`;
  document.querySelector('.exp-text').textContent = `Lv.${memberData.memberLevel}`;
  document.getElementById('play-count').textContent = memberData.playCnt;
  document.getElementById('win-count').textContent = memberData.winCnt;
  document.getElementById('win-rate').textContent = `${Math.round(memberData.winRate)}%`;
}

// 초기화 및 이벤트 설정
document.addEventListener("DOMContentLoaded", function() {
  let allRooms = [];
  
  // 방 생성 버튼 이벤트
  const createBtn = document.getElementById("createRoomBtn");
  if (createBtn) {
    createBtn.addEventListener("click", () => {
      showModal("createModal", "/eggsinthetray/components/modal/RoomCreateModal.jsp", setupRoomModalEvents);
    });
  }

  // 방 목록 초기 로드
  fetch("/eggsinthetray/api/rooms")
    .then(response => response.json())
    .then(result => {
      allRooms = result.data ? result.data : result;
      renderRoomList(allRooms);
    });

  // 검색 기능
  const searchBtn = document.getElementById("searchBtn");
  const searchInput = document.querySelector(".searchInput");
  if (searchBtn && searchInput) {
    const handleSearch = () => {
      const keyword = searchInput.value.trim().toLowerCase();
      const filteredRooms = allRooms.filter(room =>
        room.title && room.title.toLowerCase().includes(keyword)
      );
      renderRoomList(filteredRooms);
    };

    searchBtn.addEventListener("click", handleSearch);
    searchInput.addEventListener("keydown", (e) => {
      if (e.key === "Enter") handleSearch();
    });
  }

  // 새로고침 버튼
  const refreshBtn = document.getElementById("refreshBtn");
  if (refreshBtn) {
    refreshBtn.addEventListener("click", () => {
      fetch("/eggsinthetray/api/rooms")
        .then(response => response.json())
        .then(result => {
          allRooms = result.data ? result.data : result;
          renderRoomList(allRooms);
          console.log("방 목록 새로고침 완료");
        });
    });
  }

  // 프로필 정보 렌더링
  renderMemberProfile();
});