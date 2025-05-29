//clickEffect class 명이 clickEffect인 버튼들에 해당

document.addEventListener("DOMContentLoaded", () => {
  const sound = document.getElementById("click_sound"); //오디오 가져오기

  document.querySelectorAll(".clickEffect").forEach((btn) => {
    btn.addEventListener("click", (e) => {
      console.log("click", e.target);
      //소리 재생
       sound.currentTime = 0;
       sound.play();
      // //애니메이션 재적용
      btn.classList.remove("scale-up-center");
      void btn.offsetWidth;
      btn.classList.add("scale-up-center");
    });
  });
});