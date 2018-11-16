
# 최저가 검색

환경
<front>
nodejs
electronjs
vuejs
axios
pace
bootstrap

<backend>
java
gradle
embedded tomcat
selenium
json
lombok
multi thread pool

<기능 구현>
1. 검색어 입력 
2. 서버에서 키워드로 네이버 쇼핑 검색
	- 리스트는 초기 검색에서 최적가 80개를 가져오고,
	- 거기서 1000원씩 내리면서 80개씩 검색 하여 목록에 저장 (10개의 스레드)
	- 각 상품의 이미지를 버퍼로 저장.
	- 상위 상품들과 이미지 비교하여 비슷한 이미지가 없으면 상위에 추가하고 비슷한게 있으면 그상품의 하위로 추가
3. 비동기로 셀레니움 restful server 에서 상품 목록 list 를 전달 받음.
4. json 을 tree 형태로 상위상품(비슷한 이미지의 상품중에 제일 싼 상품) + 하위상품(비슷한 이미지의 상품) 으로 출력
5. 

<사용법>
1. 검색어 입력
2. grid 로 된 이미지 중에서 원하는 상품을 찾음. 
3. 원하지 않는 상품은 상품명중 uniq 한 keyword 를 찾아서 예외검색어에 입력. 

<이슈>
1. 셀레니움으로 web searching 을 하는데 시간이 너무 오래 걸림. 
2. 