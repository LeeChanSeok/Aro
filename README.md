# README

## 🌌 프로젝트 진행 기간

---

2023.02.20 ~ 2023.04.07

SSAFY 8기 2학기 특화 프로젝트 - ARO

## 🌌 ARO - 배경 및 개요

---

바쁜 일상을 살아가는 와중 밤하늘을 관찰하고 싶었던 적 있으신가요? 오로라, 유성우를 구경하고 싶지만, 언제 어디로 가야 할지 모르겠었던 적 있으신가요?

전문적으로 오로라와 유성우를 관찰하는 사람들, 여행 온 김에 오로라 혹은 유성우를 관찰하고 싶은 사람들, 오로라 혹은 유성우를 구경하는게 버킷리스트인 사람들… 오로라를 관찰하고 싶지만 언제 어디로 가야 할지 모르는 모든 사람들을 위해서 ARO에서는 오로라 예측 데이터를 제공합니다.

 ARO에서는 미래 3일 간의 오로라 예측 데이터를 제공하며,  예측 데이터에 더불어 날씨 정보를 추가적으로 분석하여 해당 날짜, 시간, 해당 지역에서 오로라를 관찰할 수 있는지 확률 정보를 제공합니다.

## ✅ 데이터 분석

---

### 오로라 예측

- SWPC(Space Weather Prediction Center)의 데이터를 활용하여 KP 지수를 예측합니다.
- 단기 예측은 랜덤포레스트 모델을 이용하여 KP 지수를 제공합니다.
- 3일 간의 예측은 LSTM 모델을 이용하여 KP 지수를 제공합니다.

### 유성우 예측

- 해당 국가의 위도를 확인합니다.
- 위도를 이용하여 해당 국가에서 표시되는 별자리를 확인하고 이에 해당하는 유성우를 찾습니다.
- 유성우가 나타나는 날짜의 범위 중 달에서 반사되는 빛의 양이 가장 적은 날짜를 찾아서
  유성우를 관측하기 최적의 날짜를 제공합니다.

## ✅ 주요 기능

---

### 오로라 예측 서비스

- 미래 3일치 오로라 예측 데이터를 제공합니다.
- 특정 날짜, 특정 시간에 대한 KP지수를 제공합니다.
- KP지수와 날씨 등 여러 요소들을 고려하여, 오로라를 볼 수 있는 확률 정보를 제공합니다.
- 사용자가 편의에 따라 정보 표현 방식을 선택할 수 있도록, 지도와 그래프의 두가지 방식으로 정보를 제공합니다.

### 오로라 관심 지역 설정 및 알림 서비스

- 오로라를 관측할 수 있는 다양한 명소 정보를 제공하고, 관심 명소를  선택할 수 있습니다.
- 나의 관심 지역에서 오로라를 볼 수 있는 확률이 일정 확률을 넘으면 FCM 알림을 받을 수 있습니다.
- 확률 수치와 알림 수신 여부를 선택할 수 있습니다.

### 일지 서비스

- 각 명소 방문시 일지를 작성할 수 있습니다.
- 일지를 작성하기 위해서는 해당 명소에서 찍은 오로라 사진을 통해서 인증을 받아야 합니다. (AI를 통한 오로라 판별, 메타 데이터를 통한 위치 판별)
- 인증에 성공하면, 사진과 글을 작성하여 해당 명소에 방문했던 추억을 남길 수 있습니다.

### 유성우 정보 제공 서비스

- 각 나라별 관찰 가능한 유성우 정보 제공합니다.
- 유성우를 관측할 수 있는 나라들 중 관심 있는 나라를 선택할 수 있습니다.
- 추후 예측 서비스 업데이트 예정입니다.

## ✅ 주요 기술

---

### Backend

- IntelliJ
- Spring boot
- Spring batch
- Spring Data Jpa
- Spring Security
- Firebase
- MariaDB
- Redis
- Rserve

### Frontend

- Android Studio : Electric eel
- Kotlin : 1.8.10
- compileSdk : 33 / minSdk : 26
- Android Jetpack
  - Viewmodel
  - Compose
- Dagger-Hilt

### CI/CD

- AWS EC2
- Docker
- Jenkins
- Nginx

### 데이터 분석

- R
- Python
- Tensorflow
- Keras

### AI

- flask
- YOLOv5

## ✅ 프로젝트 파일 구조

---

### Back

```bash
├─api-module
│  └─src
│      ├─main
│      │  ├─java
│      │  │  └─com
│      │  │      └─nassafy
│      │  │          └─api
│      │  │              ├─config
│      │  │              │  ├─s3
│      │  │              │  └─security
│      │  │              ├─controller
│      │  │              ├─dto
│      │  │              │  ├─jwt
│      │  │              │  ├─notification
│      │  │              │  ├─req
│      │  │              │  └─res
│      │  │              ├─exception
│      │  │              ├─handler
│      │  │              ├─jwt
│      │  │              ├─service
│      │  │              └─util
│      │  └─resources
│      └─test
│          └─Java
│              ├─com
│              │  └─nassafy
│              │      └─api
│              │          └─redis
│              └─com.nassafy.api
│                  └─stamp
├─batch-module
│  └─src
│      ├─main
│      │  ├─java
│      │  │  └─com
│      │  │      └─nassafy
│      │  │          └─batch
│      │  │              ├─config
│      │  │              ├─controller
│      │  │              ├─dto
│      │  │              │  ├─notificcation
│      │  │              │  ├─r
│      │  │              │  └─response
│      │  │              ├─job
│      │  │              ├─scheduler
│      │  │              └─service
│      │  └─resources
│      │      ├─firebase
│      │      └─templates
│      └─test
│          └─java
│              └─com
│                  └─nassafy
│                      └─batch
│                          └─r
├─core-module
│  └─src
│      └─main
│          └─java
│              ├─com
│              │  └─nassafy
│              │      └─core
│              │          ├─config
│              │          ├─DTO
│              │          ├─entity
│              │          ├─exception
│              │          ├─respository
│              │          └─util
│              └─resources
├─gradle
│  └─wrapper
└─src
    └─main
        └─resources
```

### Front

```bash
├─main
│  ├─java
│  │  └─com
│  │      └─nassafy
│  │          └─aro
│  │              ├─data
│  │              │  └─dto
│  │              │      └─kp
│  │              ├─domain
│  │              │  ├─api
│  │              │  └─repository
│  │              ├─service
│  │              ├─ui
│  │              │  ├─adapter
│  │              │  └─view
│  │              │      ├─aurora
│  │              │      ├─custom
│  │              │      ├─dialog
│  │              │      ├─login
│  │              │      │  ├─splash
│  │              │      │  └─viewmodel
│  │              │      ├─main
│  │              │      │  ├─mypage
│  │              │      │  │  └─viewmodel
│  │              │      │  ├─setting
│  │              │      │  └─stamp
│  │              │      └─meteorshower
│  │              └─util
│  │                  └─di
│  └─res
│      ├─anim
│      ├─drawable
│      │  ├─drawable-hdpi
│      │  ├─drawable-mdpi
│      │  ├─drawable-xhdpi
│      │  ├─drawable-xxhdpi
│      │  └─drawable-xxxhdpi
│      ├─font
│      ├─layout
│      ├─menu
│      ├─mipmap-anydpi-v26
│      ├─mipmap-hdpi
│      ├─mipmap-mdpi
│      ├─mipmap-xhdpi
│      ├─mipmap-xxhdpi
│      ├─mipmap-xxxhdpi
│      ├─navigation
│      ├─raw
│      ├─values
│      ├─values-night
│      └─xml
```

## ✅ 협업 툴

---

- Git
- Notion
- Jira
- MatterMost
- Discord
- Figma

## ✅ 협업 환경

---

- GItlab
  - 코드 버전 관리
  - MR를 통한 코드 리뷰
  - git flow 전략
- JIRA
  - 매주 목표 설정
  - 스프린트를 통해서 팀원이 현재 하고 있는 작업 확인
  - 번다운 차트를 통해서 한주의 작업 분석
- Notion
  - 프로젝트 일정 관리
  - 회의록 정리
  - 컨벤션 정리
  - 기능 명세서 정리
  - API 및 ERD 문서 정리
  - 각종 자료 공유
- 스크럼
  - 매일 아침 약 10분 내외로 각자의 진행상황 등을 공유

## ✅ 기능 명세서

---

![Untitled](README%207bcfa11852d84f7ea91a2e63f319bf05/Untitled.png)

![Untitled](README%207bcfa11852d84f7ea91a2e63f319bf05/Untitled%201.png)

## ✅ API 문서

---

![Untitled](README%207bcfa11852d84f7ea91a2e63f319bf05/Untitled%202.png)

![Untitled](README%207bcfa11852d84f7ea91a2e63f319bf05/Untitled%203.png)

![Untitled](README%207bcfa11852d84f7ea91a2e63f319bf05/Untitled%204.png)

![Untitled](README%207bcfa11852d84f7ea91a2e63f319bf05/Untitled%205.png)

![Untitled](README%207bcfa11852d84f7ea91a2e63f319bf05/Untitled%206.png)

![Untitled](README%207bcfa11852d84f7ea91a2e63f319bf05/Untitled%207.png)

![Untitled](README%207bcfa11852d84f7ea91a2e63f319bf05/Untitled%208.png)

![Untitled](README%207bcfa11852d84f7ea91a2e63f319bf05/Untitled%209.png)

![Untitled](README%207bcfa11852d84f7ea91a2e63f319bf05/Untitled%2010.png)

## ✅ Figma

---

![Untitled](README%207bcfa11852d84f7ea91a2e63f319bf05/Untitled%2011.png)

## ✅ ERD

---

![Untitled](README%207bcfa11852d84f7ea91a2e63f319bf05/Untitled%2012.png)

## ✅ 팀원 역할 분배

---

### FrontEnd

- 박채성
  - 로그인, 회원가입 구현
  - 마이 페이지 구현
  - 서비스 및 관심지역 선택 구현
  - 세팅 페이지 구현
  - 오로라 인증 AI 학습 및 Flask 서버 구성
  - 오로라 데이터 전처리 및 분석
- 송기훈
  - 메인화면 오로라 페이지 구현
    - 구글 지도 커스텀
      - 마커, 정보창
      - 오로라 위치 표시선 오버레이
      - 구름 타일 오버레이
    - 오로라 관측 확률 표시 (바텀 시트)
      - Openweather API를 이용한 오로라 관측 확률 계산
  - 메인화면 유성우 페이지 구현
  - 오로라 데이터 전처리 및 분석
    - LSTM 예측 모델 사용
    - KP 지수 예측
  - 유성우 데이터 전처리 및 분석
- 최창영
  - 앱 디자인
  - 스플래시 페이지
  - 스탬프 기능 구현
    - 스탬프 인증 페이지 구현
      - 오로라 인증
        - 위치 기반 인증
        - 사진 전송
        - 인증 완료 애니메이션
    - 여행 일지 기록 페이지 구현
  - 알림 서비스 구현
  - 메인화면 유성우 페이지 구현
  - 오로라 데이터 전처리 및 분석
    - Random-Forest 예측 모델 사용

### BackEnd

- 이찬석
  - ERD 설계
  - JWT 기반 자체 로그인 구현
    - 이메일 인증
  - 소셜 로그인 구현
    - Naver / Github
  - 회원 관리
  - FCM알림
- 고청천
  - API 설계
  - API 개발
  - Spring batch
    - weather API 데이터베이스화
    - 오로라 확률 산정
- 이민지
  - 아키텍처 설계
  - AWS, Docker, Jenkins기반 서버 및 CI/CD 구축
  - S3를 통한 이미지 저장 및 삭제 API 개발
  - KP 및 확률 관련 API 개발
  - Spring batch - R 데이터 분석 결과 데이터베이스화

## 📱 시연 시나리오

---

<img src="README%207bcfa11852d84f7ea91a2e63f319bf05/1.jpg" title="" alt="1.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/2.jpg" title="" alt="2.jpg" width="200">

- 제일 처음 앱을 실행하면 스플래쉬 화면이 뜹니다.
- EMAIL, PW을 입력하고 SIGN IN(1번)을 누르면, 로그인이 되고 메인 페이지로 연결됩니다.
- 가입이 되어 있지 않은 상태라면 2번을 클릭하여 회원가입을 진행합니다.
- 네이버, 깃허브로 회원가입 및 로그인을 하고 싶은 사용자라면 3번 혹은 4번을 클릭합니다.

<img src="README%207bcfa11852d84f7ea91a2e63f319bf05/3.jpg" title="" alt="3.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/4.jpg" title="" alt="4.jpg" width="200">

- 회원가입을 진행하면 가장 먼저 이메일을 입력하는 화면으로 이동합니다. 그리고 인증하기(1번)을 클릭하면 다음과 같이 이메일 인증 번호를 입력하는 칸이 활성화됩니다.

<img src="README%207bcfa11852d84f7ea91a2e63f319bf05/5.jpg" title="" alt="5.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/5-1.jpg" title="" alt="5-1.jpg" width="200">

- 이메일로 받은 인증번호를 입력하는데, 잘못된 인증번호를 입력하면 다음과 같이 안내메세지를 제공하며, 올바른 인증번호를 입력하면 NEXT버튼(1번)이 활성화됩니다.

<img src="README%207bcfa11852d84f7ea91a2e63f319bf05/7.jpg" title="" alt="7.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/7-1.jpg" title="" alt="7-1.jpg" width="200">

- 이메일 인증을 성공하면 비밀번호 입력창으로 이동하며, 비밀번호를 올바르게 입력하면 NEXT버튼(1번)이 활성화됩니다.

<img src="README%207bcfa11852d84f7ea91a2e63f319bf05/9.jpg" title="" alt="9.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/10.jpg" title="" alt="10.jpg" width="200">

- 비밀번호 입력을 완료하면 닉네임 입력으로 이동하며, 마찬가지로 올바르게 입력하면 NEXT(1번) 버튼이 활성화됩니다.

<img src="README%207bcfa11852d84f7ea91a2e63f319bf05/11.jpg" title="" alt="11.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/12.jpg" title="" alt="12.jpg" width="200">

- 회원 정보 입력을 마치면 서비스를 선택하는 화면으로 넘어갑니다.
- 오로라(AURORA)와 유성우(METEOR SHOWER) 중 원하는 서비스를 선택하고 1번 버튼을 클릭하여 다음으로 넘어갑니다. (둘 중 하나만 선택도 가능하고, 둘 다 선택도 가능합니다.)
- 유성우를 선택하고 넘어가면 다음과 같이 관심위치를 선택할 수 있는 화면으로 넘어갑니다. (관심지역과 관련하여 여러 서비스를 제공합니다.)
- 만약 서비스를 나중에 선택하고 싶다면 2번을 클릭합니다. (바로 메인으로 이동)

<img src="README%207bcfa11852d84f7ea91a2e63f319bf05/12%201.jpg" title="" alt="12.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/13.jpg" title="" alt="13.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/14.jpg" title="" alt="14.jpg" width="200">

- 첫번째 사진에서 1번 버튼을 클릭하면 두번째 사진처럼 나라를 선택할 수 있습니다.
- 나라를 선택한 후에는 세번째 사진처럼 원하는 관심 지역을 선택할 수 있습니다. 명소를 선택후 상단에 있는 1번 버튼을 통해서 다시 선택을 취소할 수 있고, 2번 버튼을 통해서도 선택 및 취소를 할 수 있습니다.
- 선택을 완료한 후 SIGN UP(3번) 버튼을 클릭하면, 회원가입이 완료됩니다.

<img src="README%207bcfa11852d84f7ea91a2e63f319bf05/15.jpg" title="" alt="15.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/16.jpg" title="" alt="16.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/17.jpg" title="" alt="17.jpg" width="200">

- 회원가입 혹은 로그인을 완료하면 메인 페이지로 이동하게 됩니다.
- 메인페이지에 들어가면 1번 위치에 현재 시간이 표시되고, 현재 시간에 해당 하는 KP지수가 파란색 선으로 표시가 됩니다.
- 시간을 변경하고 싶다면 1번을 클릭하게 되면 다음과 같은 시간 선택 다이얼로그가 뜹니다. 시간은 현재로부터 약 3일을 1시간 단위로 선택할 수 있습니다.
- 메인 화면에서 3번을 클릭하면 다음과 같이 해당 지역의 사진과 오로라를 관측할 수 있는 확률을 확인할 수 있습니다.

<img src="README%207bcfa11852d84f7ea91a2e63f319bf05/15%201.jpg" title="" alt="15.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/18.jpg" title="" alt="18.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/19.jpg" title="" alt="19.jpg" width="200">

- 메인 화면에서 4번을 클릭하게 되면 다음과 같이 설정한 시간부터 24시간 동안의 KP지수 변화를 그래프로 확인할 수 있습니다. 또한 내가 설정해둔 관심지역의 오로라 관측 확률과 날씨 정보를 확인할 수 있습니다.
- 메인 화면에서 2번을 클릭하면 다음과 같이 사이드바가 보이면서, MY PAGE, STAMP, SETTING으로 이동할 수 있습니다.

<img src="README%207bcfa11852d84f7ea91a2e63f319bf05/20.jpg" title="" alt="20.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/22.jpg" title="" alt="22.jpg" width="200">

- 마이페이지에 들어오면 닉네임 변경(1번), 관심지역 추가 및 변경(2번), 관심지역 삭제(3번), 서비스 변경(4번)의 기능들을 사용할 수 있습니다.
- 닉네임변경(1번)을 클릭하면 다음과 같이 닉네임 부분이 활성화되어 닉네임을 변경할 수 있습니다.

<img src="README%207bcfa11852d84f7ea91a2e63f319bf05/24.jpg" title="" alt="24.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/24-1.jpg" title="" alt="24-1.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/25.jpg" title="" alt="25.jpg" width="200">

- 마이페이지에서 서비스변경 버튼을 클릭하면, 다음과 같이 서비스 선택 화면이 나오게 됩니다.
- 원하는 서비스를 클릭 후 NEXT(1번) 버튼을 클릭하면 다음과 같이 오로라 관심 지역 선택 화면이 나오게 되고, 왼쪽으로 스와이프 하게 되면 유성우 관심 위치를 선택할 수 있는 화면이 나오게 됩니다.
- 완료(2번)버튼을 누르면 다시 마이페이지로 돌아갑니다.

<img src="README%207bcfa11852d84f7ea91a2e63f319bf05/20-2.jpg" title="" alt="20-2.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/26.jpg" title="" alt="26.jpg" width="200">

- 서비스 변경 완료 후 다  시 마이페이지로 돌아오게 되면, 오로라 관심 위치를 확인할 수 있고, 해당 위치에서 왼쪽으로 스와이프하게 되면 유성우 관심 위치를 확인할 수 있습니다.

<img src="README%207bcfa11852d84f7ea91a2e63f319bf05/19%201.jpg" title="" alt="19.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/27.jpg" title="" alt="27.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/28.jpg" title="" alt="28.jpg" width="200">

- 사이드바에서 STAMP(2번)을 클릭하게 되면, stamp페이지로 이동하게 됩니다. stamp페이지는 사용자가 오로라 명소에 다녀왔음을 인증 하고, 일지를 작성할 수 있는 페이지입니다.
- 1번을 클릭해서 원하는 나라를 선택할 수 있으며, 2번을 클릭하면 선택된 나라에 해당하는 명소들의 페이지로 이동합니다.

<img src="README%207bcfa11852d84f7ea91a2e63f319bf05/29.jpg" title="" alt="29.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/30.jpg" title="" alt="30.jpg" width="200">

- 상세보기로 이동하면 선택된 나라의 명소들을 확인할 수 있습니다. 좌우로 스와이프 해서, 다른 명소들의 정보들도 확인할 수 있습니다.
- 해당 명소에 일지를 작성하기 위해서는 먼저 인증을 받아야 합니다.
- 방문인증버튼(1번)을 클릭하게 되면, 두번째 페이지로 이동하게 되며, 갤러리에서 찍은 사진을 업로드하거나 촬영하는 방식을 선택할 수 있습니다.

<img src="README%207bcfa11852d84f7ea91a2e63f319bf05/31.jpg" title="" alt="31.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/32.jpg" title="" alt="32.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/33.jpg" title="" alt="33.jpg" width="200">

- 사진을 입력받으면 먼저 메타데이터를 통해서 해당 위치에서 찍은 사진이 맞는지 확인을 합니다. 만약 해당 위치에서 찍은 사진이 아니라면 사진을 다시 선택하라는 안내 메세지가 나옵니다.
- 만약 위치데이터가 적합하다면, 다음으로는 해당 이미지가 오로라 사진이 맞는지 AI를 통해서 판단합니다. 모든 인증이 적합하다면, 다음과 같이 이미지가 컬러로 변경되면서 방문인증 버튼에서 기록하기(1번)버튼으로 변경됩니다.

<img src="README%207bcfa11852d84f7ea91a2e63f319bf05/34.jpg" title="" alt="34.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/35.jpg" title="" alt="35.jpg" width="200">

- 기록하기를 처음 들어오게 되면, 다음과 같이 이미지를 추가할 수 있으며, 일지를 작성할 수 있습니다.
- 기록하기를 완료한 후 다시 들어오게 되면 본인이 작성했던 내용을 확인할 수 있으며, 이미지를 삭제 및 추가할 수 있고, 일지의 내용 역시 변경할 수 있습니다.

<img src="README%207bcfa11852d84f7ea91a2e63f319bf05/19%202.jpg" title="" alt="19.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/36.jpg" title="" alt="36.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/37.jpg" title="" alt="37.jpg" width="200">

- 사이드바에서 SETTINGS(3번) 버튼을 클릭하게 되면 다음과 같이 설정 화면으로 이동합니다.
- 1번 토글을 통해서 FCM알림을 키고 끌 수 있으며, 2번 토글을 통해서 지도에 오로라 표시를 선택할 수 있고, 3번 토글을 통해서 지도의 구름을 표시할지 안할지 선택할 수 있습니다.
- 4번 버튼을 통해서 회원탈퇴를 할 수 있고, 사용자가 잘못 누른 경우를 대비하여, 다시한번 탈퇴를 진행할지 물어보는 과정을 거칩니다.

<img src="README%207bcfa11852d84f7ea91a2e63f319bf05/15%202.jpg" title="" alt="15.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/38.jpg" title="" alt="38.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/39.jpg" title="" alt="39.jpg" width="200">

- 메인 화면에서 5번 버튼을 클릭하게 되면, 본인이 관심 지역으로 설정한 지역에서 볼 수 있는 유성우의 목록이 보입니다.
- 유성우 목록을 클릭하게 되면 해당 유성우의 정보를 확인할 수 있습니다.

<img src="README%207bcfa11852d84f7ea91a2e63f319bf05/KakaoTalk_20230406_153808247.jpg" title="" alt="KakaoTalk_20230406_153808247.jpg" width="200"> <img src="README%207bcfa11852d84f7ea91a2e63f319bf05/KakaoTalk_20230406_154758783.jpg" title="" alt="KakaoTalk_20230406_154758783.jpg" width="200">

- 내가 설정해둔 오로라 관심 지역에 미래 3일 동안 오로라를 관찰할 수 있는 지역이 있다면 알림을 통해서 알려줍니다.
- 또한 내가 관심지역으로 설정해둔 곳에서 다음날 유성우를 볼 수 있다면, 알림을 통해서 어떤 유성우를 볼 수 있는지 알려줍니다.


--- 

## 회고


|이름|아쉬운 점| 좋았던 점|
|--|--|--|
|이찬석| 관련있는 기능들을 묶어 업무를 분담하여 담당자를 확실하게 정한것이 개발을 진행하는데 있어 서로의 업무를 파악하기 용이하였습니다. 프로젝트 구조나 API 설계, 코딩 컨벤션 등을 명확히하여 협업을 수월하게 할 수 있어 좋았습니다. | 구현 사항을 충분히 테스트하지 못한채 Merge하거나 MR을 통해 팀원에게 충분히 검증받지 않고 진행하여 추후에 문제가 생긴일이 있었습니다. 빠르게 개발을 진행하려다 오히려 오류를 수정하느라 더 많은 시간이 소요되었습니다.  충분히 테스트를 하고 팀원의 MR도 꼼꼼히 살펴보는 것이 프로젝트를 진행하는데 있어 중요하다는 것을 깨닫게 되었습니다. |



