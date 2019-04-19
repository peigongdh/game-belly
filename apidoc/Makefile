SRC_DIR = ./proto/game-belly
DST_DIR = ./out/game-belly

PROJECT_DIR = ../game-register/src/main/java \
    ../game-gate/src/main/java \
    ../game-inner/src/main/java

env:
	echo $(PROJECT_DIR)

gen:
	protoc -I=$(SRC_DIR) --java_out=$(DST_DIR) $(SRC_DIR)/*.proto

clean:
	rm -rf $(DST_DIR)/*

copy:
	echo $(PROJECT_DIR) | xargs -n 1 cp -r $(DST_DIR)/*
