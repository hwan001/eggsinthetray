

document.addEventListener("DOMContentLoaded", () => {
  const elements = document.querySelectorAll(".flipEffect");

  elements.forEach((el) => {
    el.classList.remove("flip-scale"); // 애니메이션 초기화
    void el.offsetWidth; // 리플로우 발생으로 애니메이션 리셋
  });

  elements.forEach((el, index) => {
    setTimeout(() => {
      el.classList.add("flip-scale");
    }, index * 1000); // index * 1000ms = 각 요소마다 1초 지연
  });
});