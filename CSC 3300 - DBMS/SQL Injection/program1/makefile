.PHONY: run
HOST=localhost
DATABASE=TTU
USER=root
PASSWORD=coursework

all: run

run: program1
	./program1 $(HOST) $(DATABASE) $(USER) $(PASSWORD)

program1: program1.cpp
	g++ -lmysqlclient -o program1 -I/usr/include/mysql program1.cpp
