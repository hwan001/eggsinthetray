<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>방 생성 모달</title>
    <link
        rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/galmuri/dist/galmuri.css"
    />
    <style>
        #wrapper {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.3);
            display: flex;
            justify-content: center;
            align-items: center;
        }

        #section_modal_container {
            background: #FFF4E8;
            border-radius: 20px;
            width: 611px;
            height: 358px;
            box-shadow: 4px 4px 10px rgba(0, 0, 0, 0.3);
            position: relative;
            display: flex;
            justify-content: center;
            padding: 2rem;
        }

        .header_background {
            background: #B8988F;
            width: 260px;
            height: 55px;
            border-radius: 10px;
            box-shadow: 4px 4px 10px rgba(0, 0, 0, 0.3);
            position: absolute;
            top: 0;
            left: 50%;
            transform: translate(-50%, -50%);
        }

        .header {
            background: #C6ABA3;
            width: 255px;
            height: 55px;
            border-radius: 10px;
        }

        .header_title {
            font-family: "Galmuri11";
            font-weight: 700;
            color: #522B09;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100%;
            font-size: 32px;
        }

        #room_create_form {
            font-family: "Galmuri11";
            font-weight: 400;
            margin: 2rem auto;
        }

        #content_room_title {
            width: 493px;
            height: 73px;
            border-radius: 20px;
            background: #FFFBF1;
            border: 3.5px solid #5C3411;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        #room_title_input {
            color: #000000;
            width: 100%;
            height: 100%;
            font-size: 24px;
            text-align: center;
            font-family: "Galmuri11";
            font-weight: 400;
            background: transparent;
            border: none;
            outline: none;
        }

        #room_title_input::placeholder {
            color: #A9A9A9;
        }

        #content_room_type {
            display: flex;
            justify-content: center;
            gap: 2rem;
            margin: 2rem auto;
        }

        .room_type_radio_group {
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .item_room_type {
            appearance: none;
            width: 24px;
            height: 24px;
            background-image: url('../../assets/images/roomTypeGray.png');
            background-size: contain;
            background-repeat: no-repeat;
            background-position: center;
            cursor: pointer;
        }

        .item_room_type:checked {
            background-image: url('../../assets/images/roomTypeGreen.png');
        }

        .content_room_password {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 0.5rem;
        }

        .password_label {
            font-size: 16px;
            color: #8D8B8B;
        }

        #item_room_password {
            background: #FFF9EC;
            border-radius: 10px;
            border: 1px solid #5C3411;
            width: 180px;
            height: 38px;
            font-size: 16px;
            text-align: center;
            color: #000000;
            font-family: "Galmuri11";
            font-weight: 400;
            outline: none;
        }

        #item_room_password::placeholder {
            color: #A9A9A9;
        }

        #item_room_password:disabled {
            background: #FCFCFC;
            border-color: #A9A9A9;
            cursor: not-allowed;
        }

        #room_create_button {
            background-image: url('../../assets/images/roomCreateBtn.png');
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-position: center;
            background-color: transparent;
            border: none;
            cursor: pointer;
            width: 189px;
            height: 52px;
            margin: 2rem auto;
            display: block;
            font-family: "Galmuri11";
            font-weight: 700;
            font-size: 18px;
            border-radius: 15px;
            box-shadow: 0 4px 4px rgba(0, 0, 0, 0.25);
            color: #522B09;
        }

        #room_create_button:hover {
            opacity: 0.8;
        }

        #room_create_button:active {
            transform: scale(0.95);
        }

            
    </style>
</head>
<body>
    <div id="wrapper">
        <div id="section_modal_container">
            <div class="header_background">
                <div class="header">
                    <div class="header_title">
                        <p>방 생성하기</p>
                    </div>
                </div>
            </div>
            <form id="room_create_form">
                <div id="content_room_title">
                    <input type="text" 
                            id="room_title_input"
                            placeholder="방 이름을 입력해주세요"
                            maxlength="20"
                            required>
                </div>
                <div id="content_room_type">
                    <div class="room_type_radio_group">
                        <input type="radio" 
                                id="public_room" 
                                name="roomType" 
                                value="public" 
                                class="item_room_type"
                                checked>
                        <label for="public_room" class="room_type_label">공개방</label>
                    </div>
                    <div class="room_type_radio_group">
                        <input type="radio" 
                                id="private_room" 
                                name="roomType" 
                                value="private" 
                                class="item_room_type">
                        <label for="private_room" class="room_type_label">비밀방</label>
                    </div>
                </div>
                <div class="content_room_password">
                    <label class="password_label">비밀번호</label>
                    <input type="password"
                            id="item_room_password" 
                            placeholder="****"
                            maxlength="4"
                            disabled>
                </div>
                <button type="submit" id="room_create_button" form="room_create_form">
                    방 생성
                </button>
            </form>
            
        </div>
    </div>

    <script>
        // 라디오 버튼 변경 이벤트 리스너
        const publicRoom = document.getElementById('public_room');
        const privateRoom = document.getElementById('private_room');
        const passwordInput = document.getElementById('item_room_password');

        // 공개방 선택 시
        publicRoom.addEventListener('change', function() {
            if (this.checked) {
                passwordInput.disabled = true;
                passwordInput.value = '';
            }
        });

        // 비밀방 선택 시
        privateRoom.addEventListener('change', function() {
            if (this.checked) {
                passwordInput.disabled = false;
            }
        });

        // 비밀번호 입력 제한 (숫자만 4자리)
        document.getElementById('item_room_password').addEventListener('input', function(e) {
            let value = e.target.value.replace(/[^0-9]/g, '');
            if (value.length > 4) {
                value = value.slice(0, 4);
            }
            e.target.value = value;
        });

        // 페이지 로드 시 초기 상태 설정
        document.addEventListener('DOMContentLoaded', function() {
            if (publicRoom.checked) {
                passwordInput.disabled = true;
            } else if (privateRoom.checked) {
                passwordInput.disabled = false;
            }
        });
    </script>
</body>
</html>