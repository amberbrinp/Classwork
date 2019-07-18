CC=g++
CXXFLAGS= -std=c++11 -lsqlite3
CFLAGS= -lsqlite3
LDFLAGS= -lsqlite3

PROG_CLIENT=client
VBANK_SERVER=VirtualBank_svc
BANK1_SERVER=Bank1_svc
BANK2_SERVER=Bank2_svc

all: $(PROG_CLIENT) $(BANK1_SERVER) $(BANK2_SERVER) doVBANK

$(PROG_CLIENT): $(PROG_CLIENT).o VirtualBank_clnt.o VirtualBank_xdr.o
$(PROG_CLIENT).o: VirtualBank.h $(PROG_CLIENT).cpp

VirtualBank_clnt.o: VirtualBank_clnt.c
#$(VBANK_SERVER): $(VBANK_SERVER).o VirtualBank_proc.o VirtualBank_xdr.o
doVBANK:
	g++ -std=c++11 -lsqlite3 -o VirtualBank_svc Bank1_proc.cpp Bank2_proc.cpp VirtualBank_proc.cpp VirtualBank_svc.c VirtualBank_xdr.c
#... I gave up on these
#	g++ -std=c++11 -lsqlite3 -o VirtualBank_proc.o VirtualBank_proc.cpp Bank1_proc.cpp Bank2_proc.cpp
#	g++ -std=c++11 -lsqlite3 -o VirtualBank_svc.o VirtualBank_svc.c VirtualBank_proc.cpp
#	g++ -std=c++11 -lsqlite3 -o VirtualBank_xdr.o VirtualBank_xdr.c
#	g++ -lsqlite3 VirtualBank_svc.o VirtualBank_proc.o VirtualBank_xdr.o -o VirtualBank_svc 

#for some reason these 4 rules just don't happen
$(VBANK_SERVER).o: VirtualBank.h $(VBANK_SERVER).c VirtualBank_proc.cpp
VirtualBank_proc.o: Bank1_proc.cpp Bank2_proc.cpp VirtualBank_proc.cpp
VirtualBank_svc.o: VirtualBank_proc.cpp VirtualBank_svc.c
VirtualBank_xdr.o: VirtualBank.h VirtualBank_xdr.c


VirtualBank.h: VirtualBank.x
	rpcgen VirtualBank.x
	rm -f VirtualBank_svc.c
	rpcgen -m VirtualBank.x >> VirtualBank_svc.c
	cat VirtualBank_main.c >> VirtualBank_svc.c


Bank1_clnt.o: Bank1_clnt.c
$(BANK1_SERVER): $(BANK1_SERVER).o Bank1_proc.o Bank1_xdr.o
$(BANK1_SERVER).o: Bank1.h $(BANK1_SERVER).c Bank1_proc.cpp
Bank1_proc.o: Bank1.h Bank1_proc.cpp
Bank1_svc.o: Bank1_proc.cpp Bank1_svc.c
Bank1_xdr.o: Bank1.h Bank1_xdr.c
Bank1.h: Bank1.x

Bank1_svc.c: Bank1.x	
	rpcgen Bank1.x
	rm -f Bank1_svc.c
	rpcgen -m Bank1.x >> Bank1_svc.c
	cat Bank1_main.c >> Bank1_svc.c

Bank2_clnt.o: Bank2_clnt.c
$(BANK2_SERVER): $(BANK2_SERVER).o Bank2_proc.o Bank2_xdr.o
$(BANK2_SERVER).o: Bank2.h $(BANK2_SERVER).c
Bank2_proc.o: Bank2_proc.cpp
Bank2_svc.o: Bank2_svc.c
Bank2_xdr.o: Bank2.h Bank2_xdr.c
Bank2.h: Bank2.x
Bank2_svc.c: Bank2.x
	rpcgen Bank2.x
	rm -f Bank2_svc.c
	rpcgen -m Bank2.x >> Bank2_svc.c
	cat Bank2_main.c >> Bank2_svc.c

.PHONY: runrpc test clean depend
runrpc:
	rpcgen -m VirtualBank.x
	cat VirtualBank_main.c >> VirtualBank_svc.c
	rpcgen -m Bank1.x
	cat Bank1_main.c >> Bank1_svc.c
	rpcgen -m Bank2.x
	cat Bank2_main.c >> Bank2_svc.c
test: all testCommands.txt
	./Bank1_svc & ./Bank2_svc & ./VirtualBank_svc &
	./Bank1_svc & ./Bank2_svc & ./VirtualBank_svc &
	cat testCommands.txt | ./client
	pkill -TERM Bank1_svc
	pkill -TERM Bank2_svc
	pkill -TERM VirtualBank_svc
#	echo "Bank 1 contents: "
	echo "SELECT * FROM bank1Accounts;" | sqlite3 bank1.db
#	echo "Bank 2 contents: "
	echo "SELECT * FROM bank2Accounts;" | sqlite3 bank2.db

clean: 
	rm -f $(PROG_CLIENT) $(VBANK_SERVER) $(BANK1_SERVER) $(BANK2_SERVER) *.o *_svc.c *.h *_clnt.c *_xdr.c *.db make.depend

depend: VirtualBank.h Bank1.h Bank2.h
	touch make.depend
	$(CXX) -M $(CFLAGS) -M $(PROG_CLIENT).cpp $(PROG_CLIENT).cpp > make.depend

-include make.depend
