let gameWebSocket;
let myColor = null;

function gameInit() {
    const roomId = getRoomIdFromURL();
    const userId = getUserIdFromURL();
    connectGameWebSocket(roomId, userId);
}

function getRoomIdFromURL() {
    const params = new URLSearchParams(window.location.search);
    return params.get("roomId");
}

function getUserIdFromURL() {
    const params = new URLSearchParams(window.location.search);
    return params.get("userId");
}

function connectGameWebSocket(roomId, userId) {
    gameWebSocket = new WebSocket("wss://" + location.host + "/eggsinthetray/game/" + roomId + "?userId=" + userId);
    gameWebSocket.onopen = () => {
        const startMessage = {
            type: "start"
        };
        gameWebSocket.send(JSON.stringify(startMessage));
    };
    gameWebSocket.onmessage = handleGameMessage;
}

/* 서버에서 받은 JSON 양식 데이터를 기반으로 행동을 핸들링함*/
function handleGameMessage(event) {
    const data = JSON.parse(event.data);

    if (data.type === "start") {
        myColor = data.color;
        console.log(myColor);
    } else if (data.type === "quit") {
        const result = data.result || "lose";
        showQuitModal(result);
        disableGameUI();
        updateResult(userId, result);
        deleteRoom(roomId);
    } else if (data.type === "board") {
        renderMap(data.map);
        console.log(data.turn); // 현재 턴을 서버가 뿌려줌
        
    }
}


/* function */
function showQuitModal(result) {
    window.location.href = `/eggsinthetray/components/modal/GameResultModal.jsp?result=${result}`;
}

function updateResult(userId, result) {
    fetch(`/eggsinthetray/api/members/${userId}/result`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            result: result
        })
    });
}

function deleteRoom(roomId) {
    fetch(`/eggsinthetray/api/rooms/${roomId}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json"
        }
    });
}

function sendBoardClick(x, y) {
  const moveMessage = {
    type: "move",
    x: x,
    y: y
  };

  if (gameWebSocket && gameWebSocket.readyState === WebSocket.OPEN) {
    gameWebSocket.send(JSON.stringify(moveMessage));
  }
}

function disableGameUI() {
    document.getElementById("boardWrapper").classList.add("disabled");
    document.getElementById("chat_input").disabled = true;
    document.querySelector(".send_btn").disabled = true;
    document.querySelector(".quit_btn").disabled = true;
}

function gameQuit() {
    const quitMessage = {
        type: "quit",
        message: "상대방이 게임 종료 요청을 보냈습니다."
    };

    if (gameWebSocket && gameWebSocket.readyState === WebSocket.OPEN) {
        gameWebSocket.send(JSON.stringify(quitMessage));
    }
}

function gameMoveBack() {
    const moveBackMessage = {
        type: "undo",
        message: "상대방이 무르기 요청을 보냈습니다."
    };

    if (gameWebSocket && gameWebSocket.readyState === WebSocket.OPEN) {
        gameWebSocket.send(JSON.stringify(moveBackMessage));
    }
}

function renderMap(mapData) {
  const container = document.getElementById('boardWrapper');
  container.innerHTML = '';

  const rows = mapData.length;
  const cols = mapData[0].length;

  container.style.display = 'grid';
  container.style.gridTemplateColumns = `repeat(${cols}, 40px)`;
  container.style.gridTemplateRows = `repeat(${rows}, 40px)`;

  const tiles = [];

  for (let y = 0; y < rows; y++) {
    for (let x = 0; x < cols; x++) {
      const cell = mapData[y][x];

      const block = document.createElement('div');
      block.classList.add('boardBlock');

      block.dataset.x = x;
      block.dataset.y = y;

      const stoneLayer = document.createElement('div');
      stoneLayer.classList.add('stoneLayer');

      if (cell === 1) {
        stoneLayer.classList.add('black');
        stoneLayer.classList.add('disabled');
      } else if (cell === 2) {
        stoneLayer.classList.add('white');
        stoneLayer.classList.add('disabled');
      }

      block.appendChild(stoneLayer);

      if (cell === 0) {
        block.addEventListener('click', () => sendBoardClick(x, y));
      }

      tiles.push(block);
    }
  }

  container.append(...tiles);
}

document.querySelector(".quit_btn").addEventListener("click", gameQuit);
document.querySelector(".moveback_btn").addEventListener("click", gameMoveBack);

window.addEventListener("load", gameInit);

