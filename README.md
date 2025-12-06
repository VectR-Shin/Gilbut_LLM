JAR 실행 통합 테스트 할 때 (서버 요청 uri: ws://localhost:9090 고정)
1. Java 17 Amazon Corretto 17.0.11 설치
2. Gemini api key 환경변수 추가 (환경변수명, 키 값 은 공지 확인 - 정확히 지키기)
3. java -jar [JAR 파일명 (ex: llmService-0.0.1.jar)] 명령으로 실행

IntelliJ 으로 실행 - 통합 테스트 할 때
1. Java 17 Amazon Corretto 17.0.11 설치
2. Gemini api key 환경변수 추가 (환경변수명, 키 값은 공지 확인 - 정확히 지키기)
3. IntelliJ Ultimate 설치
4. IntelliJ Ultimate 에 Educated Licence 적용하기
5. IntelliJ 설정
 5_1. File - Project Structure - Project - SDK 를 Amazon Corretto 17.0.11 로 변경
 5_2. Settings - Build, Execution, Deployment - Build Tools - Gradle - 중간의 Build and run using 을 IntelliJ IDEA 로 변경
 5_3. Settings - Build, Execution, Deployment - Build Tools - Gradle - 하단의 Gradle JVM 을 Amazon Corretto 17.0.11 로 변경
 5_4. Settings - Build, Execution, Deployment - Compiler - Annotation Processors - 상단의 Enable annotation processing 체크
6. application.yaml 의 ros.websocket.uri 확인
 6_1. 실제 ROS 서버와 통신할 경우, 'ws://localhost:9090' 선택
 6_2. SimpleWebSocket 을 이용한 테스트의 경우, 'ws://localhost:9090/ros' 선택
7. SimpleWebSocket 을 IntelliJ 로 '새 창' 에서 열고, 5번의 설정 진행
8. SimpleWebSocket 먼저 실행
9. llmService 실행
10. 'https://hoppscotch.io/realtime/websocket' 에서 테스트 진행
 10_1. 'ws://localhost:8080/whisper' 와 연결
 10_2. 메시지 타입을 JSON 변경
 10_3. 정해진 형식의 JSON 메시지를 작성
 10_4. 보내기 버튼 클릭
 10_5. 조금 기다린 뒤, SimpleWebSocket 에 발생하는 로그 확인
11. 주의점
 11_1. 현재는 NAVIGATION 기능만이 제공된다. 따라서, SimpleWebSocket 에서 로그를 보고 싶다면 NAVIGATION 이 되도록 user prompt 제공
 11_2. CHAT, ERROR 가 발생하면 llmService 내부에서 그냥 먹어버리도록 설계했다. (이후에 TTS 연계할 수 있도록 임시 조치) - SttMessageHandler 참조
 11_3. /whisper 의 요청 status 가 ERROR 인 경우도, 일단은 내부에서 먹도록 설계했다. - SttMessageHandler 참조


+ Spring boot 의 STT Handler url(ws://localhost:8080/whisper)
+ ROS 의 url(ws://localhost:9090)
