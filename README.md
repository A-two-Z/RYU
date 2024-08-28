# 제 22회 임베디드 소프트웨어 경진대회 자유부문 AtwoZ팀
<div align="right">
<a href="https://github.com/A-two-Z"><img src="https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white"/></a>
<a href="https://www.youtube.com/watch?v=NKqtxh3rhbs"><img src="https://img.shields.io/badge/YouTube-%23FF0000.svg?style=for-the-badge&logo=YouTube&logoColor=white"/></a>
</div>


<!-- ## 개발 요약 -->
## 작품명 : 류(流)
<div align="center"><img src="Home.jpg" width="500px;" alt="" /></div>


## 소개

이 프로젝트는 기존 물류 창고의 집하 과정을 자동화 및 최적화하는 것을 목표로 한다.

로봇의 자율주행과 경로탐색 기능을 활용하여 집하 작업을 효율적으로 수행하며, 이를 통해 물류 창고의 운영 효율성을 극대화한다.

<br>



## System Architecture
![SystemArchitecture](SystemArchitecture.jpg)

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

## 개발결과물의 파급력 및 기대효과
### 새로운 월패드의 모습 제안
이 작품은 한결 같이 벽에 붙어 같은 기능만 수행하는 정적인 기기로 인지되던 월패드를 모든 세대가 가정에 하나씩 가지고 있는 주거 환경 기반 it 기기로 재해석했다. 앱 스토어를 통해 개발자가 자유롭게 webOS 월패드 앱을 창작해 배포하고 사용자가 자신의 생활 양식에 따라 앱을 선택해 사용하는 webOS 생태계를 구축할 수 있을 것이다. 이것은 주거환경 친화적인 다양한 월패드 앱들이 개발될 환경을 조성하고, 새로운 시장을 개척할 수 있을 것이다. 그 예시로 이 작품은 영상 처리를 활용해 기존 주거 환경에서 불편했던 점을 배달 물품 도난 방지, 차량 스케쥴링을 통해 성공적으로 해결했다. 또한 사용자의 수요에 맞춰 홈 트레이닝 도우미 앱을 만들었다. 이런 기존 월패드에 없던 다양한 서비스를 제공하는 앱들은 월패드가 사용자에게 더 친숙하게 인식될 계기가 될 것이다. 사용자의 기호에 따라 유연하게 바뀔 수 있는 새로운 사용법을 제안하는 **방파제의 Home++ 월패드** 는 시장에서 충분히 활용될 수 있을 것이라 생각한다.

<br>

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

## 팀 명단
| Profile | Role | Part | Tech Stack |
| ------- | ---- | ---- | ---------- |
| <div align="center"><a href="https://github.com/ymw0407"><img src="https://avatars.githubusercontent.com/u/117324719?v=4" width="70px;" alt=""/><br/><sub><b>윤지욱</b><sub></a></div> | 팀장 | PM, HW | - |
| <div align="center"><a href="https://github.com/seiyoon"><img src="https://avatars.githubusercontent.com/u/86597542?v=4" width="70px;" alt=""/><br/><sub><b>강형남</b><sub></a></div> | 팀원 | HW | - |
| <div align="center"><a href="https://github.com/judyzero"><img src="https://avatars.githubusercontent.com/u/99954264?v=4" width="70px;" alt=""/><br/><sub><b>김재현</b></sub></a></div> | 팀원 | Server, DB | RabbitMQ, mqtt |
| <div align="center"><a href="https://github.com/jjunh33"><img src="https://avatars.githubusercontent.com/u/44452761?v=4" width="70px;" alt=""/><br/><sub><b>박상빈</b></sub></a></div> | 팀원 | Digital Twin | - |
| <div align="center"><a href="https://github.com/bentshrimp"><img src="https://avatars.githubusercontent.com/u/130520505?v=4" width="70px;" alt=""/><br/><sub><b>이현주</b></sub></a></div> | 팀원 | Server, DB | RabbitMQ, Restful API |

