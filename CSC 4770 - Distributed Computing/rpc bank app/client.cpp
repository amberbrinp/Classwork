#include <stdio.h>
#include <rpc/rpc.h>
#include "VirtualBank.h"
#include <string>
#include <cstring>
#include <iostream>
using namespace std;

int main(int argc, char *argv[]) {
	CLIENT *client;
	client = clnt_create("localhost", VIRTUALBANK, VER1, "tcp");			
	if (client == NULL) {
		clnt_pcreateerror("localhost");
		exit(1);
	}
	string::size_type sz;
	bool ready = true;
	while(ready)
	{
		cout << "Ready> ";
		string input = "";
		getline(cin, input);
		char* request[4];
		char* temp;
		char* cstr = new char[input.length() + 1];

		strcpy(cstr, input.c_str());

		request[0] = strtok_r(cstr, " ", &temp);
		for (int i = 1; i < 3; i++)
		{
			request[i] = strtok_r(NULL, " ", &temp);
			if (!request[i])
			{
				cout << "Goodbye.\n";
				delete[] cstr;
				return 0;
			}
		}

		int* result;
		if(!string(request[0]).compare("credit"))
		{
			VB_req creditRequest;
			creditRequest.account = request[1];
			string strAmount = request[2];
			creditRequest.amount = stoi(strAmount, &sz);
			result = vb_credit_1(&creditRequest, client);
			cout << "result: " << result << endl;
			if(result)
			{
				cout << "OK: added " << request[2] << " dollars to account " << request[1] << ".\n";
			}
			else
			{
				cout << "Error: couldn't add " << request[2] << " dollars to account " << request[1] << ".\n";
			}
		}
		else if(!string(request[0]).compare("debit"))
		{
			VB_req debitRequest;
			debitRequest.account = request[1];
			string strAmount = request[2];
			debitRequest.amount = stoi(strAmount, &sz);
			result = vb_debit_1(&debitRequest, client);
			cout << "result: " << result << endl;
			if(result)
			{
				cout << "OK: removed " << request[2] << " dollars from account " << request[1] << ".\n";
			}
			else
			{
				cout << "Error: couldn't remove " << request[2] << " dollars from account " << request[1] << ".\n";
			}
		}
		else if (!string(request[0]).compare("transfer"))
		{
			request[3] = strtok_r(NULL, " ", &temp);
			VB_transfer_req transferRequest;
			transferRequest.account1 = request[1];
			transferRequest.account1 = request[2];
			string strAmount = request[3];
	
			transferRequest.amount = stoi(strAmount.c_str(), &sz);
			result = vb_transfer_1(&transferRequest, client);
			cout << "result: " << result << endl;
			if(result)
			{
				cout << "OK: transferred " << request[3] << " dollars from account " << request[1] << " to account " << request[2] << ".\n";
			}
			else
			{
				cout << "Error: couldn't transfer " << request[3] << " dollars from account " << request[1] << " to account " << request[2] << ".\n";
			}
		}
		else if (!string(request[0]).compare("quit"))
		{//this will never actually happen because of the parsing above
				ready = false;
				cout << "Goodbye.\n";
		}
		else
			cout << "That's not a valid command.\n";

		delete[] cstr;

	}
	return 0;
}
