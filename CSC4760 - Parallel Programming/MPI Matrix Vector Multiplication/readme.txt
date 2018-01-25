Both the serial and parallel versions of this program were taken online and modified from http://people.sc.fsu.edu/~jburkardt/c_src/mpi/mpi.html

For serial_mat_vect.c:
* Compile: gcc -g -Wall -o serial_mat_vect serial_mat_vect.c
* Run:     ./serial_mat_vect <seed>
* Warning: I'm not checking to make sure you input a seed!

For parallel_mat_vect.c
* Compile:  mpicc -g -Wall -o parallel_mat_vect parallel_mat_vect.c
* Run:      mpiexec -n <number of processes> parallel_mat_vect <seed> <mod>
* Warning:  I'm not checking to make sure you input a seed or mod,
*           but I am checking to make sure that N is a multiple of the number of processes.
