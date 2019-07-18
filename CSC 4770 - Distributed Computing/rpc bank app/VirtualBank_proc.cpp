#include <rpc/rpc.h>
#include <sqlite3.h>
#include <stdio.h>
#include <string>
#include "VirtualBank.h"
#include "Bank2.h"
#include "Bank1.h"
using namespace std;

static string queryDB(string query);
static int callback(void *data, int numCols, char **colData, char **colName);

int* vb_credit_1_svc(VB_req* request, struct svc_req *svc)
{
	CLIENT *client;
  	return(vb_credit_1(request, client));
}


int* vb_debit_1_svc(VB_req* request, struct svc_req *svc)
{
	CLIENT *client;
  	return(vb_debit_1(request, client));
}


int* vb_transfer_1_svc(VB_transfer_req* request, struct svc_req *svc)
{
	CLIENT *client;
  	return(vb_transfer_1(request, client));
}


int* vb_credit_1(VB_req* request, CLIENT* client)
{
	//in your main make sure to make this table 
	char* query = new char[500];
	sprintf(query, "SELECT bank FROM bankAccounts WHERE account LIKE '%s';", request->account);
	string bank = queryDB(query);
	int* result;
	if(!bank.compare("bank1"))
	{
		B1_req creditRequest;
		creditRequest.account = request->account;
		creditRequest.amount = request->amount;
		//connect to bank1
		client = clnt_create("localhost", BANK1, VER1, "tcp");
		result = b1_credit_1(&creditRequest, client);
	}
	else if(!bank.compare("bank2"))
	{
		B2_req creditRequest;
		creditRequest.account = request->account;
		creditRequest.amount = request->amount;
		//connect to bank2
		client = clnt_create("localhost", BANK2, VER1, "tcp");
		result = b2_credit_1(&creditRequest, client);
	}
	else
	{
		delete[] query;
		return NULL;
	}

	delete[] query;
	return result;
}


int* vb_debit_1(VB_req* request, CLIENT* client)
{
	//in your main make sure to make this table 
	string query = "SELECT bank FROM bankAccounts WHERE account LIKE '" + string(request->account) + "';";
	string bank = queryDB(query);

	int* result;
	if(!bank.compare("bank1"))
	{
		B1_req debitRequest;
		debitRequest.account = request->account;
		debitRequest.amount = request->amount;
		//connect to bank1
		client = clnt_create("localhost", BANK1, VER1, "tcp");
		result = b1_debit_1(&debitRequest, client);
	}
	else if(!bank.compare("bank2"))
	{
		B2_req debitRequest;
		debitRequest.account = request->account;
		debitRequest.amount = request->amount;
		//connect to bank2
		client = clnt_create("localhost", BANK2, VER1, "tcp");
		result = b2_debit_1(&debitRequest, client);
	}
	else
		return NULL;

	return result;
}


int* vb_transfer_1(VB_transfer_req* request, CLIENT* client)
{
	//in your main make sure to make this table 
	string query1 = "SELECT bank FROM bankAccounts WHERE account LIKE '" + string(request->account1) + "';";
	string bankFrom = queryDB(query1);
	string query2 = "SELECT bank FROM bankAccounts WHERE account LIKE '" + string(request->account2) + "';";
	string bankTo = queryDB(query2);

	int* result;
	if(!bankFrom.compare("bank1"))
	{
		B1_req fromRequest;
		fromRequest.account = request->account1;
		fromRequest.amount = request->amount;
		client = clnt_create("localhost", BANK1, VER1, "tcp");
		result = b1_debit_1(&fromRequest, client);
	}
	else if(!bankFrom.compare("bank2"))
	{
		B2_req fromRequest;
		fromRequest.account = request->account1;
		fromRequest.amount = request->amount;
		client = clnt_create("localhost", BANK2, VER1, "tcp");
		result = b2_debit_1(&fromRequest, client);
	}
	else
		return NULL;

	if(result)//no longer allowed to return 0 for success
	{
		if(!bankTo.compare("bank1"))
		{
			B1_req toRequest;
			toRequest.account = request->account2;
			toRequest.amount = request->amount;
			client = clnt_create("localhost", BANK1, VER1, "tcp");
			result = b1_credit_1(&toRequest, client);
		}
		else if(!bankTo.compare("bank2"))
		{
			B2_req toRequest;
			toRequest.account = request->account2;
			toRequest.amount = request->amount;
			client = clnt_create("localhost", BANK2, VER1, "tcp");
			result = b2_credit_1(&toRequest, client);
		}
		else
			return NULL;

		return result;//this has to be a non-null non-zero int*
	}
	return NULL;

}

static int callback(void *data, int numCols, char **colData, char **colName)
{
	data = colData[0]; //there should only be one row returned in this case

	return 0;
}

static string queryDB(string query)
{
	sqlite3* db;
	char *zErrMsg = 0;
	char* result = new char[200];
	sqlite3_open("accounts.db", &db);
	sqlite3_exec(db, query.c_str(), callback, result, &zErrMsg);
	sqlite3_close(db);
	return string(result);
}
