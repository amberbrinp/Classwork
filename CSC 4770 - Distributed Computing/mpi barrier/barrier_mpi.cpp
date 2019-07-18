#include <unistd.h>
#include <iostream>
#include <fstream>
#include <string>
#include <mpi.h>
using namespace std;

struct barrier
{
	int count; //who has been stopped
	int flag; //what phase is the barrier in (its 0)
	int everyone; //how many processes are there
	int me; //who am i
};

void my_barrier_init(barrier* barrier);
void my_barrier(barrier* barrier);


void my_barrier_init(barrier* barrier)
{
	//initialize barrier
	barrier->count = 0;
	barrier->flag = 0;
	MPI_Comm_size(MPI_COMM_WORLD, &barrier->everyone);
	MPI_Comm_rank(MPI_COMM_WORLD, &barrier->me);
}

void my_barrier(barrier* barrier)
{
	MPI_Comm comm = MPI_COMM_WORLD;
	int local_sense = 1 - barrier->flag;
	int total_barred = 1;

	//ok i've decided:
	// -1 = INCREMENT
	// -2 = RESET
	// total_barred = INCREMENT_REPLY

	if (barrier->me != 0)
	{
		int increment = -1;
        	MPI_Send(&increment, 1, MPI_INT, 0, local_sense, comm);
	}
	
	while(barrier->flag != local_sense)
	{
		int val;
		MPI_Status stat; //will contain info about sender
		MPI_Recv(&val, 1, MPI_INT, MPI_ANY_SOURCE, local_sense, comm, &stat);

		if (val == -1) //INCREMENT, only master should get this
		{
			//cout << "received INCREMENT on process " << barrier->me << endl;
			total_barred++;
			MPI_Send(&total_barred, 1, MPI_INT, stat.MPI_SOURCE, local_sense, comm);
		}
		else if (val == -2)//RESET
		{
			//cout << "received RESET on process " << barrier->me << endl;
			total_barred = 0;
			barrier->flag = local_sense;
		}
		else if (val > 0)//INCREMENT_REPLY, should only come from master
		{
			//cout << "received INCREMENT_REPLY on process " << barrier->me << ". val: " << val << endl;
			if (val == barrier->everyone)
			{
				int reset = -2;
				for(int i = 0; i < barrier->everyone; i++)
				{
					if(i != barrier->me)
					{
						MPI_Send(&reset, 1, MPI_INT, i, local_sense, comm);
					}
				}
				total_barred = 0;
				barrier->flag = local_sense;
			}
		}
		else //something has gone wrong
			cout << "Something has gone wrong.\n";
	}
	//stop at this function call
	//once everyone is stopped
		//then everyone continues
}

int main()
{
	MPI_Init(NULL, NULL);

	barrier barr;
	my_barrier_init(&barr);

	for (int i = 0; i < 10; i++)
	{
		if (barr.me == 0)
		{
			sleep(1);
			remove("tstfile.txt");
			ofstream test_file;
			test_file.open("tstfile.txt");
			test_file << "test " << i << endl;
			test_file.close();
		}

		my_barrier(&barr);

		//do the other stuff
		ifstream test_file;
		test_file.open("tstfile.txt");
		//read & print contents of file
		string line;
		getline(test_file, line);
		cout << line << " on process " << barr.me << endl;
		test_file.close();
	}

	MPI_Finalize();
	return 0;
}
