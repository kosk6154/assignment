# 과제

## 개발 구성
* java 21
* gradle 8.6
* Spring boot 3.3.4

## 시작 방법
* intellij인 경우
  1. settings.gradle을 열고 `open as project` 로 엽니다.
  2. setting에서 `enable annotation processing`을 켜주세요.
  3. coordination-api 모듈의 java 폴더에 CoordinationApiApplication을 spring boot application으로 실행하면 됩니다. 
* gradle인 경우
  1. `./gradlew :coordination-api:clean :coordination-api:build` 실행
  2. `java -jar coordination-api/build/libs/coordination-api.jar` 실행
* docker인 경우
  1. `./gradlew :coordination-api:clean :coordination-api:build` 실행
  2. `docker build --build-arg MODULE_NAME=coordination-api -t assignment .` 실행
  3. `docker run -d --name assignment -p 8080:8080 assignment` 실행
  4. 종료시에는 `docker rm -f assignment` 하시면 됩니다.
  
## 개요
본래 redis로 구성하려 하였으나 DB 사용은 H2만 사용해야하는 조건이 있어, H2 DB(memory)와 메모리에 java의 HashMap, TreeMap등을 이용하여 캐싱하는 방식으로 구현 하였습니다.  
캐시에 데이터가 존재하면 캐시에서 내려주고, 그렇지 않다면 DB에서 조회하게끔 작성되었습니다.  
프로젝트는 2개의 module로 구성됩니다.
* coordination-common
* coordination-api

common의 경우 추후 확장을 위해 공유 가능하게끔 entity와 enum으로 구성되어 있고, api에 대부분의 기능이 구현되어 있습니다.  

주어진 구현1~4는 구현하였으나, 시간상 test 및 front 페이지는 구현하지 못하였습니다.  
다만 front 페이지 대신 테스트가 용이하도록 swagger를 적용해 놓았습니다.  

## 프로젝트 구성
api 모듈의 configuration 폴더 내에 `AssignmentData` 라는 java 파일에 초기 데이터가 선언되어 있습니다.  
이후 동일 폴더 내에 `AssignmentInitializer` 파일에서 `ApplicationReadyEvent` 이벤트가 발생할 경우 최초 1번만 초기화하게끔 설정되어 있습니다.  

jdbc는 jpa 및 querydsl을 사용하였으며 H2의 memory DB를 사용하였고, h2:mem:test 으로 구성하였습니다.  
join을 사용할 경우 fetch join을 사용하였습니다.  
모든 설정은 application.yml에 정의되어 있습니다.  

h2의 접근 url은 다음과 같습니다.  
http://localhost:8080/h2-console  
user는 `sa` 이며 비밀번호는 없습니다.  

swagger의 경우 javadoc의 설명이 description으로 지정되게끔 되어 있고, url은 다음과 같습니다.  
http://localhost:8080/assignment/swagger-ui.html  

api 모듈의 application 폴더 내에 MVC 패턴으로 구성되어 있고 controller, service, repo, model 등의 패키지가 해당합니다.  
오류의 처리는 application내 exception.handler 패키지에 ControllerAdvice를 통해 오류를 제어합니다.  
오류는 제가 임의로 생성한 오류와 spring boot에서 사용되는 오류, Exception 오류를 모두 포함하였습니다.  

응답 객체는 response 구성을 통일하고자 `ResponseBase` 라는 객체에 generic으로 담아 반환합니다.

브랜드는 브랜드명만 수정이 가능합니다.  
상품의 경우 가격, 상품명 수정이 가능합니다.  
브랜드 삭제 시에 판매중이 상품이 있다면 삭제가 불가능합니다.  

entity의 경우 equals and hashcode를 지정해야 안전하므로 `JPA buddy`라는 플러그인을 이용하여 자동 생성 하였습니다.  
java collection을 이용하여 캐싱되는 객체인 `ClothInfo` 객체는 intellij에서 지원하는 equals and hashcode 생성 기능을 이용하여 생성 되었습니다.  

request의 validation은 `@Valid` 이용하여 각 객체의 field에 정의된 annotation 조건에 따라 제어되며 조건을 만족하지 못하면 ControllerAdvice 통해 정의된 오류가 반환 됩니다.  


## 개인 해석
category는 총 8종이었으나, 대분류, 중분류를 통해 추후 좀 더 다양하게 필터링하여 내려줄 수 있게 category group 개념을 만들고 하위 분류로 들어갈 수 있게끔 구성 하였습니다.  
다만 이를 DB로 관리하기에는 다소 유연하지 못하다고 생각되어서 sud_code를 key로 잡고 group은 column으로 두었습니다.  
group은 1부터 시작하고, sub_code는 group + 10000번으로 지정하여 한개의 group 하위에 10000개를 포함할 수 있게끔 생각해보았습니다.    

상품정보 수정 시 상품 가격만 수정하기에는 구현4에서 상품을 추가할 수 있고, 특정 브랜드가 동일 카테고리에서 1개의 상품만 출시한다는 보장은 없으므로 상품명을 넣어 브랜드/카테고리/상품명으로 상품을 구성할 수 있게끔 하였습니다.  
초기 상품명은 카테고리와 동일하게 하였습니다.  

초기 상품이 정해져서 그대로 테스트가 가능해야 하므로, 여러대의 서버가 구동되어 초기 구동 시 데이터등이 최신으로 싱크되거나, 실시간으로 상품정보가 변경되어 전파되어야 하는 pubsub 구성이나, event driven등의 방식은 제외 하였습니다.  

구현1~4에 명시된 response에 대한 스펙이 있거나, 스펙은 없되 화면에 표와 같이 보여야 한다는 예시들이 있거나, 한글로 된 응답등이 있어 영어 + camelCase 방식으로 통일 하였습니다.  
카테고리 이름 또한 영어로 맞추었습니다.  

가격의 경우 원화는 소수점을 거의 사용하지 않지만 다른 통화와의 확장 가능성을 위해 `BigDecimal`로 작성하였습니다.  

entity(table)와 java에서 다루는 객체의 이름 중복을 막고자 비슷한 의미를 갖는 `ClothCategory`, `ClothType` 등으로 구분해서 사용했습니다.  

카테고리별 최저 가격의 합을 통해 가장 저렴한 브랜드를 찾는 구현에 대해, 요구하는 카테고리를 브랜드에서 하나라도 판매하지 않는 경우 해당 브랜드는 제외하게끔 구성 되었습니다.  
(admin api 등으로 삭제하는 경우)

endpoint는 외부/내부에서 각각 사용될것으로 예상되며, 접근제어가 필요할것이라고 판단되어 사용 목적에 따라 구현 1~3을 만족하는 `/v1/coordination` 과 구현 4를 만족하는 `/i1/admin` 으로 나누었습니다.  
(h2 콘솔 및 swagger는 편의상 그냥 두었습니다.)

카테고리의 경우 추가될 수 있으나 거의 변동이 없다고 생각되어 DB에 유지하고 코드에서 가독성을 위해 enum으로 받아 파싱하는 방식으로 사용 했습니다.  
