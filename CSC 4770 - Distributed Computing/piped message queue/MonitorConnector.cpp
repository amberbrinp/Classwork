#include <iostream>
#include <stdio.h>
#include <unistd.h>
#include <zmq.h>
#include <time.h>
#include <string.h>
#include <queue>
#include <iomanip>
using namespace std;

int adapter_csv_to_plot(string message, char* converted);

int main()
{
	queue <string> q;
	const int READ = 0;
	const int WRITE = 1;
	//create ZMQ_pub socket and connect to message queue
	void* context = zmq_ctx_new();
	void* sock = zmq_socket(context, ZMQ_SUB);
	zmq_connect(sock, "tcp://127.0.0.1:5556");
	zmq_setsockopt(sock, ZMQ_SUBSCRIBE, "", 0);

	//create Unix pipe
	int pipes[2];
	pipe(pipes);
	pid_t pid = fork();

	if(pid == 0)
	{//child
		close(pipes[WRITE]);
		dup2(pipes[READ], 0);
		close(pipes[READ]);
		char* argv[] = {(char*) "/usr/bin/gnuplot", NULL};
		execv("/usr/bin/gnuplot", argv);
		//error after this point
		cout << "error forking\n";

	}
	else if(pid > 0)
	{//parent
		close(pipes[READ]);
		dup2(pipes[WRITE], 1); 
		cout << "set term  x11\n\0"; 
		cout << flush;
		char converted[256];
		char message[256];
		while(1)
		{
			zmq_recv(sock, message, 256, 0);
			adapter_csv_to_plot(message, converted);
			q.push(converted);
			if (q.size() > 20)
			{
				q.pop();
			}
			cout << "plot \"-\" with linespoints\n\0";
			for (int i = 0; i < q.size(); i++)
			{
				string val = q.front();
				q.pop();
				cout << val << "\n";
				q.push(val);
			}
			cout << "e\n";
			cout << flush;
		}
	}
	else
	{
		cout << "no\n";
		//error
	}

	return 0;
}

int adapter_csv_to_plot(string message, char* converted)
{
	char* cmsg = (char*) message.c_str();
	char* epoch = strtok(cmsg, ","); //header
	epoch = strtok(NULL, ","); //epoch
	char* free = strtok(NULL, ","); //free\n\0
	int nfree = atoi(free);
	
	time_t tm = time_t(atoi(epoch));
	struct tm* gmt = gmtime(&tm);
	int secs = (gmt->tm_hour * 3600) + (gmt->tm_min * 60) + gmt->tm_sec;

	sprintf(converted, "%d %d\n\0", secs, nfree);
	string sconverted = converted;
	return sconverted.length();
	
}
