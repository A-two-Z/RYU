# 제 22회 임베디드 소프트웨어 경진대회 자유부문 AtwoZ팀
<div align="right">
<a href="https://github.com/A-two-Z"><img src="https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white"/></a>
<a href="https://www.youtube.com/watch?v=NKqtxh3rhbs"><img src="https://img.shields.io/badge/YouTube-%23FF0000.svg?style=for-the-badge&logo=YouTube&logoColor=white"/></a>
</div>


<!-- ## 개발 요약 -->
## 작품명 : 류(流)
![류봇](https://github.com/user-attachments/assets/a3e078a6-e5bd-4544-a32c-d9abe62e4c9b)



## 소개

많은 물류 창고에서 직원들은 '무거운 PDA'를 들고, 직접 '섹터마다' 카트를 끌며 물건을 운반합니다. 직원들은 물건을 직접 '컨베이어 벨트'로 운반하여 하역 작업을 수행합니다. 이 과정에서 여러 문제가 발생할 수 있습니다.

**비효율적인 동선**: 직원이 주문된 물건들을 운반하기 위해 각 섹터를 이동 할 시, 최적화된 동선으로 이동하기 보다는 불필요한 이동이 자주 발생할 가능성이 높습니다.
**사고 위험성 증가**: 좁은 공간에서 여러 명의 직원이 동시에 작업할 경우, 충돌이나 사고가 발생할 가능성이 높아집니다.

이러한 문제들을 해결하기 위해 본 프로젝트에서는 자율 주행 로봇을 활용한 물류 자동화 솔루션을 제안합니다. 이 로봇은 들어온 주문 요청을 분석하고 물건이 있는 위치로 효율적으로 이동한 뒤, 해당 물품을 수집하고 운송합니다. 이를 통해 직원들은 특정 섹터에서 대기하다 물건을 로봇 위에 옮겨담는 작업을 수행한 뒤, 완료 신호를 보내는 것으로 업무를 끝낼 수 있습니다.

<br>

## 동작 사진
git 추가 예정


## 작품 설명
#### 1. 하드웨어: 서보모터와 초음파 센서<br>
**서보모터**: 로봇의 움직임을 제어하기 위해 사용됩니다. 제자리 회전, 정방향 후진, 미세 전진 등을 구현하기 위해 서보모터를 활용하여 로봇의 방향과 위치를 정밀하게 조정합니다.<br>
**초음파 센서**: 물품 하역 시 사용됩니다. ESP32 마이크로컨트롤러를 통해 초음파 센서로부터 거리를 측정한 후, 이 정보를 MQTT 프로토콜을 통해 전송합니다. 로봇이 컨베이어벨트와의 거리가 일정 기준 이하로 가까워지면, 서보모터를 작동시켜 물품을 하역합니다.
#### 2. 맵 매핑과 경로 탐색: SLAM과 Nav2
**SLAM (Simultaneous Localization and Mapping)**: ‘Cartographer’를 사용하여 로봇이 주행할 환경을 스캔하고, 맵핑합니다. 이 과정에서 로봇은 자신의 위치를 파악하면서 동시에 주행 환경의 지도를 생성합니다.<br>
**Nav2**: SLAM을 통해 생성된 맵을 기반으로 경로를 설정하고, 로봇이 설정된 경로를 따라 주행할 수 있도록 합니다. Nav2는 로봇이 주행 중 장애물을 회피하거나 목적지까지 최적의 경로를 찾도록 돕습니다.
#### 3. 물품 하역: 초음파 센서와 서보모터
초음파 센서를 통해 컨베이어벨트와의 거리를 측정하고, 일정 거리 이하로 가까워지면 하역 작업을 수행합니다. 하역 작업은 서보모터를 이용하여 로봇이 정확한 위치에서 물품을 하역할 수 있도록 합니다.
#### 4. 회전 매커니즘
로봇은 U턴 형태로 회전하면서 제자리에서 방향을 전환합니다. 이를 위해, 회전, 미세전진, 정방향 후진을 반복하는 과정을 통해 로봇의 위치(X, Y 좌표)가 바뀌지 않도록 제자리 회전을 구현합니다.
#### 5. WearOS와 주문 관리
**WearOS**를 이용하여 주문 관리 시스템을 구축합니다. 하나의 주문이 완료될 때까지 다른 주문은 MQTT를 통해 블로킹되며, 예상치 못한 종료 상황에 대비해 물류 정보를 내부 데이터베이스에 저장합니다. 싱글톤 패턴을 적용하여 애플리케이션의 라이프사이클을 관리합니다.
#### 6. 디지털 트윈: Unity와 ROS2 통신
**디지털 트윈**: Unity를 활용하여 가상 환경에서 로봇의 동작을 시뮬레이션하고, 이를 실제 환경과 동기화합니다. 이를 위해 rosbridge_websocket을 통해 Unity와 ROS2 간의 통신을 구현합니다.<br>
**맵핑**: Unity에서 생성된 가상 맵과 실제 환경의 맵을 매핑합니다. 이 과정에서, 가상 세계와 현실 세계의 좌표계를 일치시키기 위해 좌표 축을 재정렬합니다. 예를 들어, 위치 좌표는 X, Y, Z에서 -Y, Z, X로 매핑되고, 회전 좌표는 X, Y, Z, W에서 Y, -Z, X, W로 매핑됩니다.<br>
**보정**: 프로토타입에서는 각 구간에 동일한 보정값을 적용하지만, 실제 구현 시에는 구간별로 다른 보정값을 적용하여 더욱 정밀한 매핑을 구현할 수 있습니다.



## 시연 영상
유튜브 영상 추가 예정


## System Architecture
![SystemArchitecture](https://github.com/user-attachments/assets/78b62013-acb7-4d3f-86c0-714aff0abd55)

<br>

## 개발환경 및 개발언어
- 운영체제 : Windows 11, Ubuntu 20.04.5 LTS, webOS 2.18.0
- 디바이스 구성 : Raspberry Pi 4B, KEYESTUDIO ESP8266
- IDE : Visual Studio Code, IntelliJ
- 개발 언어 : Java 17.0.12, C++
- 데이터베이스 : MongoDB, MariaDB
- package manager : npm
- Docker 27.1.1 - Ubuntu 20.04.6 LTS

<br>

<!--
## 파일 구성도
📦AtwoZ <br/>
 ┣ 📂client-Unity<br/>
 ┃ ┗ 📂Assets <br/>
 ┣ 📂client-WearOS <br/>
 ┃ ┗ 📂app<br/>
 ┣ 📂device-robot <br/>
 ┃ ┣ 📂monicar<br/>
 ┃ ┃ ┣ 📂monicar_control<br/>
 ┃ ┃ ┣ 📂monicar_cv <br/>
 ┃ ┃ ┗ 📂monicar_teleop <br/>
 ┣ 📂device-sector <br/>
 ┃ ┃ ┣ 📂docker <br/>
 ┃ ┃ ┃ ┗ 📂tesseract <br/>
 ┃ ┣ 📂wallpad <br/>
 ┃ ┃ ┣ 📂register-car <br/>
 ┃ ┃ ┃ ┣ 📂register-car_app <br/>
 ┃ ┃ ┃ ┗ 📂register-car_service <br/>
 ┣ 📂server-order <br/>
 ┃ ┣ 📂entrance <br/>
 ┃ ┃ ┗ 📂Detecting-packages <br/>
 ┃ ┣ 📂wallpad <br/>
 ┃ ┃ ┗ 📂delivery <br/>
 ┃ ┃ ┃ ┣ 📂delivery_app <br/>
 ┃ ┃ ┃ ┗ 📂delivery_service <br/>
 ┣ 📂server-robot <br/>
 ┃ ┗ 📂wallpad <br/>
 ┃ ┃ ┗ 📂exercise <br/>
 ┃ ┃ ┃ ┣ 📂exercise_app <br/>
 ┃ ┃ ┃ ┗ 📂exercise_service <br/>
 ┃ ┗ 📂home <br/>
 ┃ ┃ ┃ ┣ 📂led <br/>
 ┃ ┃ ┃ ┃ ┗ 📜led.ino <br/>
 ┃ ┃ ┃ ┗ 📂windowBlind <br/>
 ┃ ┃ ┃ ┃ ┗ 📜windowBlind.ino <br/>
 ┗ 📜README.md
<br><br>

-->

## 팀 명단
| Profile | Role | Part | Tech Stack |
| ------- | ---- | ---- | ---------- |
| <div align="center"><a href="https://github.com/ymw0407"><img src="https://avatars.githubusercontent.com/u/117324719?v=4" width="70px;" alt=""/><br/><sub><b>윤지욱</b><sub></a></div> | 팀장 | PM, HW | - |
| <div align="center"><a href="https://github.com/seiyoon"><img src="https://avatars.githubusercontent.com/u/86597542?v=4" width="70px;" alt=""/><br/><sub><b>강형남</b><sub></a></div> | 팀원 | HW | - |
| <div align="center"><a href="https://github.com/judyzero"><img src="https://avatars.githubusercontent.com/u/99954264?v=4" width="70px;" alt=""/><br/><sub><b>김재현</b></sub></a></div> | 팀원 | Server, DB | RabbitMQ, mqtt |
| <div align="center"><a href="https://github.com/jjunh33"><img src="https://avatars.githubusercontent.com/u/44452761?v=4" width="70px;" alt=""/><br/><sub><b>박상빈</b></sub></a></div> | 팀원 | Digital Twin | - |
| <div align="center"><a href="https://github.com/bentshrimp"><img src="https://avatars.githubusercontent.com/u/130520505?v=4" width="70px;" alt=""/><br/><sub><b>이현주</b></sub></a></div> | 팀원 | Server, DB | RabbitMQ, Restful API |

