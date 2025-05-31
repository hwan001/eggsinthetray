let gameWebSocket;
let myColor = null;
let serverTurn = null;
let userId = null;

function getWebSocketProtocol() {
    const metaElement = document.querySelector('meta[name="websocket-protocol"]');
    return metaElement ? metaElement.getAttribute('content') : 'ws';
}

function gameInit() {
    const roomId = getRoomIdFromURL();
    userId = getUserIdFromURL();
    setRoomTitle(roomId);
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
    const protocol = getWebSocketProtocol();
    gameWebSocket = new WebSocket(protocol + "://" + location.host + "/eggsinthetray/game/" + roomId + "?userId=" + userId);
    gameWebSocket.onopen = () => {
        const startMessage = {
            type: "start"
        };
        gameWebSocket.send(JSON.stringify(startMessage));
    };
    gameWebSocket.onmessage = handleGameMessage;
}

/* 서버에서 받은 JSON 양식 데이터를 기반으로 행동을 핸들링함*/
async function handleGameMessage(event) {
    const data = JSON.parse(event.data);
    const roomId = getRoomIdFromURL();

    if (data.type === "start") {
        myColor = data.color;
        
        try {
            // 자신의 정보 가져오기
            const response = await fetch(`/eggsinthetray/api/members/${userId}`);
            if (!response.ok) throw new Error('프로필 정보를 가져오는데 실패했습니다.');
            const memberData = await response.json();
            
            // 내 정보 업데이트
            updateGameProfileUI(memberData, myColor);

            // 기존 플레이어 정보가 있다면 가져오기
            if (data.existingPlayerId) {
                const existingPlayerResponse = await fetch(`/eggsinthetray/api/members/${data.existingPlayerId}`);
                if (existingPlayerResponse.ok) {
                    const existingPlayerData = await existingPlayerResponse.json();
                    updateGameProfileUI(existingPlayerData, data.existingPlayerColor);
                }
            }
        
        } catch (error) {
            console.error('프로필 정보 로딩 실패:', error);
        }
    } else if (data.type === "player_joined") {
        try {
            // 새로 입장한 플레이어의 정보 가져오기
            const response = await fetch(`/eggsinthetray/api/members/${data.userId}`);
            if (!response.ok) throw new Error('상대방 프로필 정보를 가져오는데 실패했습니다.');
            const memberData = await response.json();
            
            // 상대방 정보 업데이트
            updateGameProfileUI(memberData, data.color);
        } catch (error) {
            console.error('상대방 프로필 정보 로딩 실패:', error);
        }
        // console.log(myColor);
        // startTimer(30);
    } else if (data.type === "quit") {
        const result = data.result || "lose";
        updateResult(userId, result);
        deleteRoom(roomId);
        showQuitModal(result);
    } else if (data.type === "board") {
        renderMap(data.map);
        serverTurn = data.turn
        timerStatus = (myColor === serverTurn)? true : false;
        console.log("serverTurn :" + serverTurn + ", myColor :" + myColor + ", timer init :" +  timerStatus);
        if (!timerStatus) startTimer(30);

    } else if (data.type === "undo") {
        const msg = data.message;
        console.log(msg);
        
        const confirmUndo = confirm("상대가 무르기를 요청했습니다. 수락하시겠습니까?");
        const moveBackMessage = {
            type: "undo_result",
            result: confirmUndo ? "ok" : "deny",
            sender: userId  // 요청자 ID 전달 (필요 시)
        };

        if (gameWebSocket && gameWebSocket.readyState === WebSocket.OPEN) {
            gameWebSocket.send(JSON.stringify(moveBackMessage));
        }
    } else if (data.type === "undo_result") {
        if (data.result === "ok") {
            alert("무르기 요청이 수락되었습니다.");
            resetTimer();
            startTimer(30);
        } else {
            alert("무르기 요청이 거절되었습니다.");
        }
    }
}

// 게임 프로필 렌더링 함수
async function renderGameProfile(userId, myColor) {
    try {
        console.log("userId : " + userId + " myColor : " + myColor);
        const response = await fetch(`/eggsinthetray/api/members/${userId}`);
        if (!response.ok) throw new Error('프로필 정보를 가져오는데 실패했습니다.');
        
        const memberData = await response.json();
        updateGameProfileUI(memberData, myColor);
        console.log("memberData : " + memberData);
        console.log("myColor : " + myColor);
    } catch (error) {
        console.error('게임 프로필 로딩 실패:', error);
    }
}

// 게임 프로필 UI 업데이트 함수
function updateGameProfileUI(memberData, color) {
    const profilePosition = color === 'B' ? 'black' : 'white';
    const profileFrame = document.querySelector(`.content_profile_frame.${profilePosition}`);
    
    if (profileFrame) {
        // 레벨 업데이트
        profileFrame.querySelector('.profile_level').textContent = `Lv.${memberData.memberLevel}`;
        
        // 프로필 이미지 업데이트
        const profileImgId = color === 'B' ? 'profile_img_black' : 'profile_img_white';
        const profileImg = document.getElementById(profileImgId);
        if (profileImg) {
            profileImg.style.backgroundImage = `url(${memberData.imageUrl})`;
        }
        
        // 유저 정보 업데이트
        profileFrame.querySelector('.profile_name').textContent = memberData.nickname;
        profileFrame.querySelector('.profile_record').textContent = `${memberData.winCnt}W ${memberData.playCnt - memberData.winCnt}L`;
        profileFrame.querySelector('.profile_win_rate').textContent = `승률 ${Math.round(memberData.winRate)}%`;
        
        // 달걀 박스 업데이트
        const eggText = profileFrame.querySelector('.profile_egg_text');
        if (eggText) {
            eggText.textContent = color === 'B' ? '흑돌' : '백돌';
        }
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
    console.log("gameQuit() called");
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

// 방 제목 설정
function setRoomTitle(roomId) {
    fetch('/eggsinthetray/api/rooms')
        .then(response => response.json())
        .then(roomList => {
            const matchedRoom = roomList.find(room => room.roomId === roomId);
            if (matchedRoom) {
                document.querySelector(".content_room_title").textContent = `[비공개] ${matchedRoom.title}`;
            } else {
                document.querySelector(".content_room_title").textContent = "[비공개] 방이름을 찾을 수 없음";
            }
        })
        .catch(error => {
            document.querySelector(".content_room_title").textContent = "[비공개] 방이름을 불러올 수 없음";
            console.error("방 목록 불러오기 실패:", error);
        });
}
document.addEventListener("DOMContentLoaded", gameInit);


let timerValue = 0;
let timerMax = 30;
let timerInterval = null;

function resetTimer() {
    const fillBar = document.getElementById("fillBar");
    const timerText = document.getElementById("timerText");
    const timerWrap = document.getElementById("timer_wrap");
    fillBar.style.width = "0%";
    fillBar.style.backgroundColor = "#FFF158";
    timerText.textContent = "30";
    timerText.style.color = "#000";
    timerWrap.style.border = "5px solid #EBAB16";
}

function startTimer(duration) {
    resetTimer();
    timerValue = 0;
    timerMax = duration;
    updateTimerDisplay();

    if (timerInterval) clearInterval(timerInterval);

    timerInterval = setInterval(() => {
        timerValue++;
        updateTimerDisplay();

        if (timerValue >= timerMax) {
            clearInterval(timerInterval);
            gameQuit();
            showQuitModal("lose");
        }
    }, 1000);
}

function updateTimerDisplay() {
    const remain = timerMax - timerValue;
    document.getElementById("timerText").textContent = remain;

    const percent = (timerValue / timerMax) * 100;
    document.getElementById("fillBar").style.width = percent + "%";

    const timerWrap = document.getElementById("timer_wrap");

    if (remain <= 10) {
        document.getElementById("fillBar").style.backgroundColor = "#F60000";
        document.getElementById("timerText").style.color = "#F60000";
        timerWrap.style.border = "1px solid #F60000";
    } else {
        document.getElementById("fillBar").style.backgroundColor = "#FFF158";
        document.getElementById("timerText").style.color = "#000";
        timerWrap.style.border = "5px solid #EBAB16";
    }
}


document.querySelector(".quit_btn").addEventListener("click", gameQuit);
document.querySelector(".moveback_btn").addEventListener("click", gameMoveBack);

