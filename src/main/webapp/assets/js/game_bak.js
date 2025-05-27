
/*
document.querySelectorAll('.boardBlock').forEach((block, index) => {
  const isDisabled = index === 0; // 예: 첫 칸만 비활성화
  if (isDisabled) block.classList.add('disabled');
});
*/

const roomId = "<%= roomId %>";
const userId = "<%= session.getAttribute("userId") %>";      // 예: 세션에 저장된 userId
const userName = "<%= session.getAttribute("userName") %>";  // 예: 닉네임

// 전역 상태 변수
let gameWebSocket, chatSocket;
let myColor = null;
let isMyTurn = false;

function init() {
    const roomId = getRoomIdFromURL();
    connectGameWebSocket(roomId);
    connectChatSocket(roomId);
    setupBoardEventHandlers();
}



// Game
function connectGameWebSocket(roomId) {
    gameWebSocket = new WebSocket("ws://" + location.host + "/eggsinthetray/game/" + roomId);
    gameWebSocket.onopen = () => {
        console.log("게임 소켓 연결됨");
        gameWebSocket.send(JSON.stringify({
                    type: "init",
                    rows: 15,
                    cols: 15,
                    userId: userId,
                    userName: userName
                }));
    };
    gameWebSocket.onmessage = handleGameMessage;
}

/* 서버에서 받은 JSON 양식 데이터를 기반으로 행동을 핸들링함*/
function handleGameMessage(event) {
    const data = JSON.parse(event.data);

    if (data.type === "start") {
        myColor = data.color;
        isMyTurn = (myColor === "B");
        updateStatus("게임 시작. 당신은 " + (myColor === "B" ? "흑" : "백"));
    } else if (data.type === "move") {
        drawStone(data.x, data.y, data.color);
        isMyTurn = (data.color !== myColor);
    } else if (data.type === "gameover") {
        updateStatus("게임 종료. 승자: " + data.winner);
    } else if (data.type === "board") {
        drawBoard(data.map);
    }
}

function drawBoard(map) {
    const blocks = document.getElementsByClassName("boardBlock");

    for (let i = 0; i < map.length; i++) {
        for (let j = 0; j < map[i].length; j++) {
            const index = i * map[i].length + j;
            const block = blocks[index];

            // 기존 색상 제거
            block.classList.remove("black", "white");

            // 새로운 상태 추가
            if (map[i][j] === "B") {
                block.classList.add("black");
            } else if (map[i][j] === "W") {
                block.classList.add("white");
            }
        }
    }
}


// 3. 돌 놓기
function sendMove(x, y) {
    if (!isMyTurn) return;
    gameWebSocket.send(JSON.stringify({
        type: "move",
        x: x,
        y: y,
        userId: userId
    }));
}


function drawStone(x, y, color) {
    const index = x * 15 + y;
    const blocks = document.getElementsByClassName("boardBlock");
    blocks[index].classList.add(color === "B" ? "black" : "white");
}


// Chatting
function connectChatSocket(roomId) {
    chatSocket = new WebSocket("ws://" + location.host + "/eggsinthetray/chat/" + roomId);
    chatSocket.onopen = () => console.log("채팅 소켓 연결됨");
    chatSocket.onmessage = handleChatMessage;
}

function handleChatMessage(event) {
    appendChatMessage(event.data);
}

function appendChatMessage(html) {
    console.log("appendChatMessage : " + html);
    const chatArea = document.getElementById("chatDisplay");
    if (!chatArea) {
        console.warn("chatDisplay 요소가 없습니다");
        return;
    }
    const wrapper = document.createElement("div");
    wrapper.innerHTML = html;
    chatArea.appendChild(wrapper);
}

function sendChatMessage() {
    const input = document.getElementById("chatInput");
    const msg = input.value.trim();
    if (msg !== "") {
        chatSocket.send(msg);
        console.log("sendChatMessage : " + msg);
        input.value = ""; // 전송 후 입력창 비우기
    }
}


// 5. 유틸리티
function getRoomIdFromURL() {
    const params = new URLSearchParams(window.location.search);
    return params.get("roomId");
}

function updateStatus(text) {
    document.getElementById("status").textContent = text;
}

function setupBoardEventHandlers() {
    const blocks = document.getElementsByClassName("boardBlock");
    Array.from(blocks).forEach((block, index) => {
        block.addEventListener("click", () => {
            const x = Math.floor(index / 15);
            const y = index % 15;
            sendMove(x, y);
        });
    });
}

// 실행
window.onload = init;