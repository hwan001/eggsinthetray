import styled, { keyframes } from 'styled-components';

// 통통 튀는 애니메이션 정의
const bounce = keyframes`
  0%, 20%, 50%, 75%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-20px);
  }
  60% {
    transform: translateY(-5px);
  }
`;