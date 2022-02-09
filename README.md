# 캠핑예약 어플리케이션
**전국의 캠핑 장소들을 사용자가 원하는 조건에 따라 검색하고 예약을 할 수 있게 도와주는 어플리케이션**
## Preview
<p align="left">
<img src="https://user-images.githubusercontent.com/92709137/153226314-bba165f2-0124-4a60-b814-38b8fdec4884.png" width="22%"/>
<img src="https://user-images.githubusercontent.com/92709137/153226808-20ab60ee-28db-4c51-a67f-8dcd4d7ec88c.png" width="22%"/>
<img src="https://user-images.githubusercontent.com/92709137/153226935-e404707b-313e-46a3-ab98-6c9404fe0b18.png" width="22%"/>
<img src="https://user-images.githubusercontent.com/92709137/153227831-54a3ce5c-6603-4f5e-95a1-e8ed2c779d36.png" width="22%"/>
</p>

## 개발 기간
**2021.12.11 - 2022.02.08**

### [OPEN API를 이용한 캠핑 예약 어플](https://changeable-cap-1e9.notion.site/OPEN-API-bdd376f3ca9b4c7ca10b41ba43520b1d)
: 이 프로젝트의 라이브러리 구현방식

## 개발 환경 및 라이브러리
### Android Studio - Kotlin

|MVVM|Rest API|Async|View|DataBase|Permission|
|:---:|:---:|:---:|:---:|:---:|:---:|
|Livedata|Retrofit2|RxJava2|Android Navigation|Room|TedPermission|
|ViewModel|OkHttp3|Coroutine|Glide||
|DataBinding|Gson||ViewPager2|
|BindingAdapter|||TabLayout|
## 어플 소개
✔ **이미지 후 설명하는 방식입니다!**

<p align="left">
<img src="https://user-images.githubusercontent.com/92709137/153226314-bba165f2-0124-4a60-b814-38b8fdec4884.png" width="45%"/>
<img src="https://user-images.githubusercontent.com/92709137/153226718-ee3c1e5b-61df-4fe8-807b-999f3ab03392.png" width="45%"/>
</p>

* 상단의 오토캠핑, 글램핑, 카라반 아이콘과 우측 하단의 반려견과 함께하는 캠핑의 더보기 버튼을 클릭 시 지역 선택화면으로 전환되어집니다.

<p align="left">
<img src="https://user-images.githubusercontent.com/92709137/153226314-bba165f2-0124-4a60-b814-38b8fdec4884.png" width="30%"/>
<img src="https://user-images.githubusercontent.com/92709137/153273966-a76e27bc-d8ec-4309-a339-fdde2eaea82f.png" width="30%"/>
<img src="https://user-images.githubusercontent.com/92709137/153228334-d4a3a82d-f955-4dc9-8e76-e7bd5a49ce42.png" width="30%"/>
</p>

* 추천 캠핑장은 3초마다 자동으로 스크롤되고 이미지 클릭 시 해당 캠핑장 세부 정보화면으로, 그 밑 테마별 추천 여행지를 클릭 시 테마에 해당되는 리스트 화면으로 전환되어집니다.

<p align="left">
<img src="https://user-images.githubusercontent.com/92709137/153226935-e404707b-313e-46a3-ab98-6c9404fe0b18.png" width="30%"/>
<img src="https://user-images.githubusercontent.com/92709137/153227154-eadf4082-12d3-4624-a3db-001e92f78d38.png" width="30%"/> 
<img src="https://user-images.githubusercontent.com/92709137/153275788-bed249d9-37b7-4758-8a36-a69eae617ca4.png" width="30%"/>  
</p>

* 검색화면은 숙소명과 키워드로 나눠 필요한 검색어를 입력할 수 있고 최근 검색어 목록과 두 글자 미만 입력 시 알림 다이얼로그가 발생하도록 구성하였습니다.

<p align="left">
<img src="https://user-images.githubusercontent.com/92709137/153273013-b4464cb7-0df3-4035-b6b7-eb81a307cd85.png" width="50%"/>
</p>

* 내주변화면은 현재 사용자의 주소를 상단바에 표시하고 현재위치에서 20km이내의 캠핑장을 검색하고 떨어진 거리를 계산하여 보여주도록 구성하였습니다.

<p align="left">
<img src="https://user-images.githubusercontent.com/92709137/153227409-d67937bb-cb0c-43ac-a72e-97b1cea2afc1.png" width="30%"/>
<img src="https://user-images.githubusercontent.com/92709137/153227438-002c767f-d2c1-4a1b-a442-1b0028fde899.png" width="30%"/>
<img src="https://user-images.githubusercontent.com/92709137/153227993-364148f1-e142-42c8-bba7-01b5585a38a3.png" width="30%"/>  
</p>

* 찜 화면은 Room을 사용하여 내부 DB의 용량을 제한하도록 최대 10개까지 설정했고 삭제 시 재확인 다이얼로그가 발생하도록 구성하였습니다.

<p align="left">
<img src="https://user-images.githubusercontent.com/92709137/153227831-54a3ce5c-6603-4f5e-95a1-e8ed2c779d36.png" width="45%"/>
<img src="https://user-images.githubusercontent.com/92709137/153227861-e73904a5-76a6-43cb-9502-7fd2b3483d6b.png" width="45%"/>
</p>

* 세부화면은 해당 캠핑장의 여러가지 사진들과 야영에 도움 될 정보들이 나와있고 예약을 원할 시 링크되어있는 사이트나 전화를 걸 수 있도록 구성하였습니다.
