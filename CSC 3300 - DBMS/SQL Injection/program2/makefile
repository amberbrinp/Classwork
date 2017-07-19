.PHONY: run
HOST=localhost
DATABASE=TTU
USER=root
PASSWORD=coursework

all: run

run: program2
	java -cp ./:/usr/share/java/mysql.jar program2 $(HOST) $(DATABASE) $(USER) $(PASSWORD)

program2: program2.java
	javac -cp ./:/usr/share/java/mysql.jar program2.java
