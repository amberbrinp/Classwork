all:
	mpic++ -o barrier_mpi barrier_mpi.cpp

.PHONY: run clean

run:
	mpirun -n 4 --hostfile hostfile.txt `pwd`/barrier_mpi

clean: 
	rm -f barrier_mpi tstfile.txt
