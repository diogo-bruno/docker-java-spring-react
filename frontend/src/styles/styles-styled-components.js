import styled from 'styled-components';

export const ContainerCenterFixed = styled.div`
  width: 100%;
  top: 0;
  left: 0;
  position: fixed;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  background-color: #ffffffe6;
`;

export const StaticBackdrop = styled.div`
  height: 100%;
  width: 100%;
  position: absolute;
  top: 0;
  left: 0;
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
`;

export const TextStyle = styled.p`
  flex-basis: 100%;
  text-align: center;
  font-weight: 400;
  color: #6b6b6b;
`;

export const Flex = styled.div`
  width: ${(props) => (props.w ? props.w : 'auto')};
  display: flex;
  align-items: ${(props) => (props.alignItems ? props.alignItems : 'normal')};
  justify-content: ${(props) =>
    props.justifyContent ? props.justifyContent : 'normal'};
  flex-wrap: wrap;
`;

export const Box = styled.div`
  float: left;
  clear: both;
  margin: 10px;
`;

export const ButtonLogoff = styled.button`
  margin: 0;
  border-radius: 8px;
  background-color: #1f8c38;
  padding: 5px;
  width: auto;
  height: 35px;
  line-height: 0;
`;

export const Heading = styled.p`
  flex-basis: 100%;
  text-align: center;
  font-weight: 400;
  color: #6b6b6b;
  font-size: x-large;
  margin-bottom: 40px;
`;
