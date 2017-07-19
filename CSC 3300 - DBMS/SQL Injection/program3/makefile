.PHONY: run
HOST=localhost
DATABASE=TTU
USER=root
PASSWORD=coursework

all: run

run: program3
	java -cp ./:/usr/share/java/mysql.jar program3 $(HOST) $(DATABASE) $(USER) $(PASSWORD)

program3: program3.java
	javac -cp ./:/usr/share/java/mysql.jar program3.java
