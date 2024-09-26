# 가져올 이미지를 정의
FROM node:14
# 경로 설정하기
WORKDIR /app
# package.json 워킹 디렉토리에 복사 (.은 설정한 워킹 디렉토리를 뜻함)
COPY package.json .
# 명령어 실행 (의존성 설치)
RUN npm install
# 현재 디렉토리의 모든 파일을 도커 컨테이너의 워킹 디렉토리에 복사한다.
COPY . .
# 3000번 포트 노출
EXPOSE 3000
# 빌드 스크립트 실행
RUN npm run build
# serve 패키지를 글로벌로 설치
RUN npm install -g serve
# 정적 파일을 서빙하기 위해 serve 명령어를 사용 (CMD 대신 ENTRYPOINT 사용을 고려해 볼 수 있음)
CMD ["serve", "-s", "build"]

# 그리고 Dockerfile로 docker 이미지를 빌드해야한다.
# $ docker build -t <your-image-name> .
