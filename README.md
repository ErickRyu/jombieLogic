# jombieLogic
* 2016_03_26
  * http://stackoverflow.com/questions/17009368/how-to-use-readobject-twice-in-socket-of-java
  * server - client hashmap주고받도록 변경
  * bug : 다른 컴퓨터로 접속할 경우 user정보들이 제각각으로 저장됨. server에서 userMap을 관리하도록 변경해야 함.
* 2016_02_08
  * isThereAttack -> isAttackable로 이름 변경
  * Login User가 좀비 일 경우, 공격 가능한 위치에 있는 사람은 맵에 표시

* 2016_02_06
  * SinglePlayMode 생성, 로그인하는 유저 위주 진행화면으로 변경
  * 좀비는 좀비만, 사람은 사람만 맵에서 볼 수 있음.
  * 추가 구현 계획
    * map에 공격가능 범위에 들어온 경우는 사람, 좀비 모두 띄우도록 변경하기
    * network socket만들어서 통신시키기
    * exp나 level은 제외하고 일단 지금까지의 정보들을 그대로 저장하고 불러오기
    * thread단위로 움직이게 변경시키기
    * GPS location파악, 수정

* 2016_02_05
  * isThereAttack_8D() 추가
  * 역할
    * 8방위 공격가능 (유저 수를 10명정도로 제한한다고 했을 때, 유저를 nested loop 돌려봤자 시간비용은 크지 않다고 판단)
  * 문제점
    * 나중에 thread로 돌릴 경우에도 이런 알고리즘이 충분히 빠르게 작동할 것인가
    * 일단 GPS로 Location을 받아올경우 좌표표현이 floating number인데 어떻게 처리해야 할 것인가?

* 2016_02_02
  * 중복되는 유저 좌표 제거(현실에서 좌표가 같을 일이 거의 없다고 판단) <- 같다고 나올 경우도 있는 것 같음. 다시 한 번 고려해야할 필요성 있음!

* 2016_02_01
  * gameTest 클래스에서 moveRandom() 호출 후 drawMap을 하기 전 sorting 추가


