#include <iostream>
#include <stdio.h>
#include <unistd.h>
#include <zmq.h>
#include <time.h>
#include <string.h>
using namespace std;

int adapter_vmstat_to_csv(string line, char* converted);

int main()
{
	const int READ = 0;
	const int WRITE = 1;
	//create ZMQ_pub socket and connect to message queue
	void* context = zmq_ctx_new();
	void* sock = zmq_socket(context, ZMQ_PUB);
	zmq_connect(sock, "tcp://127.0.0.1:5555");
	//create Unix pipe
	int pipes[2];
	pipe(pipes);
	pid_t pid = fork();

	if(pid == 0)
	{//child
		close(pipes[READ]);
		dup2(pipes[WRITE], 1);
		close(pipes[WRITE]);
		char* argv[] = {(char*)"/usr/bin/vmstat", (char*)"2", (char*)"-n", 0};
		execv("/usr/bin/vmstat", argv);
		//error after this point
		cout << "error forking\n";

	}
	else if(pid > 0)
	{//parent
		close(pipes[WRITE]);
		dup2(pipes[READ], 0);
		string line;
		getline(cin, line);
		getline(cin, line);
		char converted[256];
		int len;
		while(getline(cin, line))
		{
			len = adapter_vmstat_to_csv(line, converted);
			zmq_send(sock, converted, len+1, 0);
		}
	}
	else
	{
		cout << "no\n";
		//error
	}

	return 0;

}


int adapter_vmstat_to_csv(string line, char* converted)
{
	char* free;
	char* cline = (char*) line.c_str();
	free = strtok(cline, " ");
	for(int i = 0; i < 3; i++)
	{
		free = strtok(NULL, " ");
	}

	sprintf(converted, "MSG_MEMSTAT, %d, %s\n\0", (int)time(0), free);
	string sconverted = converted;
	return sconverted.length();
}
