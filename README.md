# R-Zipp - Server

## 1. 🏠서비스 소개
<br/>
간단하게 도면을 업로드 하여 <strong>3D 그래픽으로 구현</strong>해 원하는 가구를 원하는 위치에 가상으로 배치할 수 있는 서비스를 제공합니다.


## 2. 📝주요 기능 소개

### 1) 이미지 업로드 후 3D 그래픽으로 구현
사용자는 본인이 원하는 도면의 이미지를 업로드 하거나 직접 손으로 그린 도면을 업로드 할 수 있습니다. 
도면 업로드를 하면 해당 도면 사진을 3D로 구현합니다. 

### 2) 오브젝트 배치 밎 저장
3D 그래픽으로 구현된 공간에 인테리어 및 조명 디자인을 원하는 공간에 미리 적용해보고 라이팅 확인도 가능합니다.

### 3) 공간 공유
직접 꾸민 공간을 공유하여 다른 유저도 확인할 수 있고, 다른 유저가 꾸민 공간을 확인해볼 수도 있습니다.


## 3. BackEnd 구현 기능
### User
- Spring Security와 JWT 기반 회원가입/로그인 구현
- Refresh Token 저장 시 만료일 설정과 빠른 조회를 위해 Redis 사용

### Image upload
- AWS S3를 활용하여 사용자가 업로드 한 이미지 저장
- Unreal과 통신 중 MultipartFile을 사용할 수 없는 오류 발생 -> InputStream으로 수동 구현

### Space save
- 각 공간 내 오브젝트 개별 저장이 필요 -> 양방향 연관관계를 방지하기 위해 중간 객체를 활용하여 의존성 사이클을 끊음


## 4. 💻기술 스택

### Language
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 

### Framework And Libraries
![Spring](https://img.shields.io/static/v1?style=for-the-badge&message=Spring&color=6DB33F&logo=Spring&logoColor=FFFFFF&label=)
![Spring Boot](https://img.shields.io/static/v1?style=for-the-badge&message=Spring+Boot&color=6DB33F&logo=Spring+Boot&logoColor=FFFFFF&label=)
![Spring Security](https://img.shields.io/static/v1?style=for-the-badge&message=Spring+Security&color=6DB33F&logo=Spring+Security&logoColor=FFFFFF&label=)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)


### Database
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/redis-DC382D.svg?&style=for-the-badge&logo=redis&logoColor=white)


### Servers
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)




