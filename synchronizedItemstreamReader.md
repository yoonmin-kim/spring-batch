Thread safe 하지 않은 itemReader 를 thread safe 
하게 처리한다

스프링배치 4.0 부터 지원한다

synchronizedItemStramReader에 기본적인 itemReader를
위임 해주면 synchronize 블록처리에 의해 동시성 문제를 해결
할 수 있다


synchronizedItemStramReader 의 read 메소드에는 synchronized
처리가 되어있다
