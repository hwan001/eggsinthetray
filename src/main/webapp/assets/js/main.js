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

  document.addEventListener("click", (e) => {
    if (e.target.id === "modalBackdrop" || e.target.id === "createModal") {
      closeModal();
    }
  });

  function closeModal() {
    modalContainer.style.display = "none";
    modalContainer.innerHTML = "";

    const backdrop = document.getElementById("modalBackdrop");
    if (backdrop) backdrop.remove();
  }
});


// modal 기능 함수화 script -> main.js
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

// Exp(추후 수정)
// function setExp(percent) {
//   const fill = document.querySelector('.exp-bar-fill');
//   const label = document.querySelector('.exp-label');
//   fill.style.width = percent + '%';
//   label.textContent = `EXP: ${percent}%`;
// }


