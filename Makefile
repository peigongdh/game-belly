kill:
	ps aux | grep game- | grep -v grep | awk {'print$$2'} | xargs kill

build:
	mvn clean compile package

run:
	nohup java -jar -Djava.ext.dirs=game-register/target/libs/ game-register/target/game-register-1.0-SNAPSHOT.jar >> logs/game-register.log 2>&1 &
	nohup java -jar -Djava.ext.dirs=game-gate/target/libs/ game-gate/target/game-gate-1.0-SNAPSHOT.jar >> logs/game-gate.log 2>&1 &
	nohup java -jar -Djava.ext.dirs=game-inner/target/libs/ game-inner/target/game-inner-1.0-SNAPSHOT.jar >> logs/game-inner.log 2>&1 &
	nohup java -jar -Djava.ext.dirs=game-web/target/libs/ game-web/target/game-web-1.0-SNAPSHOT.jar >> logs/game-web.log 2>&1 &

