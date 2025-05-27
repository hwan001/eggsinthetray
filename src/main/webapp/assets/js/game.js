let chatSocket = null;
let mySessionId = null;

// 메시지를 DOM에 추가
function appendChatMessage(text, direction) {
    const chatDisplay = document.getElementById("chat_display_el");
    const row = document.createElement("div");
    const msg = document.createElement("div");

    if (direction === "system") {
        row.className = "chat_row system";
        msg.className = "chat_message system";
        msg.innerHTML = text;
        row.appendChild(msg);
    } else {
        row.className = `chat_row ${direction}`;
        msg.className = `chat_message ${direction}`;
        msg.innerHTML = text;

        const icon = document.createElement("div");
        icon.className = "profile_icon";

        if (direction === "right") {
            row.appendChild(msg);
            row.appendChild(icon);
        } else {
            row.appendChild(icon);
            row.appendChild(msg);
        }
    }

    chatDisplay.appendChild(row);
    chatDisplay.scrollTop = chatDisplay.scrollHeight;
}

// 메시지 전송
function sendChatMessage() {
    const input = document.getElementById("chat_input");
    const msg = input.value.trim();

    if (msg !== "") {
        if (chatSocket.readyState === WebSocket.OPEN) {
            chatSocket.send(msg);
        } else {
            alert("서버와 연결되어 있지 않습니다.");
        }
        input.value = "";
    }
}

// 웹소켓 초기화 및 이벤트 바인딩
function init() {
    const roomId = new URLSearchParams(window.location.search).get("roomId");
    chatSocket = new WebSocket("ws://" + location.host + "/eggsinthetray/chat/" + roomId);

    chatSocket.onmessage = function (event) {
        const data = JSON.parse(event.data);

        if (data.type === "init") {
            mySessionId = data.sessionId;
            return;
        }

        if (data.type === "system") {
            appendChatMessage(data.message, "system");
            return;
        }

        const direction = data.senderId === mySessionId ? "right" : "left";
        appendChatMessage(data.message, direction);
    };

    document.querySelector(".send_btn").addEventListener("click", sendChatMessage);
    document.getElementById("chat_input").addEventListener("keydown", function (event) {
        if (event.key === "Enter" && !event.shiftKey) {
            event.preventDefault();
            sendChatMessage();
        }
    });

    // 시스템 초기 메시지 표시
    appendChatMessage("[시스템] 게임이 시작되었습니다.", "system");
}

window.onload = init;
