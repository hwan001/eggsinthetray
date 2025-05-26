// 전역 상태 변수
const roomId = "<%= roomId %>";

let chatSocket;

function init() {
    const roomId = getRoomIdFromURL();
    connectChatSocket(roomId);
}

/*  채팅 웹소켓 서버에 연결 */
function connectChatSocket(roomId) {
    chatSocket = new WebSocket("ws://" + location.host + "/eggsinthetray/chat/" + roomId);
    chatSocket.onopen = () => console.log("채팅 소켓 연결됨");
    chatSocket.onmessage = handleChatMessage;
}

function handleChatMessage(event) {
    appendChatMessage(event.data);
}

// 서버로 부터 수신받은 메시지를 그려줌
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

// 입력 받은 메시지를 웹소켓 서버로 보내줌
function sendChatMessage() {
    const input = document.getElementById("chatInput");
    const msg = input.value.trim();
    if (msg !== "") {
        chatSocket.send(msg);
        console.log("sendChatMessage : " + msg);
        input.value = ""; // 전송 후 입력창 비우기
    }
}

function getRoomIdFromURL() {
    const params = new URLSearchParams(window.location.search);
    return params.get("roomId");
}


// 실행
window.onload = init;