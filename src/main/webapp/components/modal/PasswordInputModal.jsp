<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>비밀번호 입력 모달</title>
    <link
        rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/galmuri/dist/galmuri.css"
    />
     <link rel="stylesheet" href="/eggsinthetray/assets/css/clickEffect.css" />
     <script src="/eggsinthetray/assets/js/clickEffect.js"></script>
    <style>
        #wrapper {
            background-color: rgba(0, 0, 0, 0.5);
        }
        
        #section_modal_container {
            background: #FFF4E8;
            border-radius: 20px;
            width: 611px;
            height: 267px;
            box-shadow: 4px 4px 10px rgba(0, 0, 0, 0.3);
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 2rem;
            z-index: 9999;
        }

        #password_input_form {
            font-family: "Galmuri11";
            font-weight: 400;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }

        #content_password {
            width: 493px;
            height: 73px;
            border-radius: 20px;
            background: #FFFBF1;
            border: 3.5px solid #5C3411;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        #password_input {
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

        #password_input::placeholder {
            color: #A9A9A9;
        }
        
        #password_input_button {
            background-image: url('/eggsinthetray/assets/images/roomCreateBtn.png');
            background-size: 100% 100%;
            background-repeat: no-repeat;
            background-position: center;
            background-color: transparent;
            border: none;
            cursor: pointer;
            width: 189px;
            height: 52px;
            margin-top: 2rem;
            display: block;
            font-family: "Galmuri11";
            font-weight: 700;
            font-size: 18px;
            border-radius: 15px;
            box-shadow: 0 4px 4px rgba(0, 0, 0, 0.25);
            color: #522B09;
        }

        #password_input_button:hover {
            opacity: 0.8;
        }

        #password_input_button:active {
            transform: scale(0.95);
        }
    </style>
</head>
<body>
 <!-- audio  -->
      <audio
        id="click_sound"
        src="${pageContext.request.contextPath}/assets/sound/click_effect.mp3"
        preload="auto"
      ></audio>
    <div id="wrapper">
        <div id="section_modal_container">
            <form id="password_input_form">
                <div id="content_password">
                    <input type="password" 
                            id="password_input"
                            name="password"
                            placeholder="방 비밀번호를 입력해주세요"
                            maxlength="4"
                            required>
                </div>
                <button type="submit" id="password_input_button" class="clickEffect" form="password_input_form">
                    확인
                </button>
            </form>
        </div>
    </div>
</body>
</html>