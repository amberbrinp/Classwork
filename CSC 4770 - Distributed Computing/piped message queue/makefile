CXX=g++
CXXFLAGS= -std=c++11 -lzmq
LDFLAGS= -lzmq

STAT_CON=StatisticsConnector
MON_CON=MonitorConnector
MSG_Q=MessageQueue

all: $(STAT_CON) $(MON_CON) $(MSG_Q)

$(STAT_CON): $(STAT_CON).cpp
$(MON_CON): $(MON_CON).cpp
$(MSG_Q): $(MSG_Q).cpp


.PHONY: clean run all refresh kills


clean: 
	rm -f *.o $(STAT_CON) $(MON_CON) $(MSG_Q)

run:
	./$(STAT_CON)&
	./$(MON_CON)&
	./$(MSG_Q)

kills:
	killall $(STAT_CON)
	killall $(MON_CON)
	killall $(MSG_Q)

refresh:
	make kills
	make clean
	make
	make run



