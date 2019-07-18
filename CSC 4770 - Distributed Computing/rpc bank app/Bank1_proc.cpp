#include <rpc/rpc.h>
#include <sqlite3.h>
#include <stdio.h>
#include <string>
#include "Bank1.h"

using namespace std;

static string queryDB(string query);
static int callback(void* data, int numCols, char** colData, char** colName);

int *b1_credit_1_svc(B1_req* request, struct svc_req *svc)
{
	CLIENT *client;
  	return(b1_credit_1(request, client));
}


int* b1_debit_1_svc(B1_req* request, struct svc_req *svc)
{
	CLIENT *client;
  	return(b1_debit_1(request, client));
}


int* b1_credit_1(B1_req* request, CLIENT* client)
{
	char* balanceQuery = new char[500];
	sprintf(balanceQuery, "SELECT balance FROM bank2Accounts  WHERE account LIKE '%s';", request->account);
	string balanceStr = queryDB(string(balanceQuery));
	string::size_type sz;
	int balance = stoi(balanceStr, &sz);
	int newBalance = balance + request->amount;
	char* query = new char[500];
	sprintf(query, "UPDATE bank2Accounts SET balance=%d WHERE account LIKE '%s';", newBalance, request->account);
	string bank = queryDB(string(query));
	int* val = new int(1);
	return val;
}


int* b1_debit_1(B1_req* request, CLIENT* client)
{
	//in your main make sure to make this table
	char* balanceQuery = new char[500];
	sprintf(balanceQuery, "SELECT balance FROM bank1Accounts  WHERE account LIKE '%s';", request->account);
	string balanceStr = queryDB(string(balanceQuery));
	string::size_type sz;
	int balance = stoi(balanceStr, &sz);
	if(balance > request->amount)
	{
		int newBalance = balance - request->amount;
		char* query = new char[500];
		sprintf(query, "UPDATE bank1Accounts SET balance=%d WHERE account LIKE '%s';", newBalance, request->account);
		string bank = queryDB(string(query));

		int* val = new int(1);
		delete[] balanceQuery;
		delete[] query;
		return val;
	}
	delete[] balanceQuery;
	return NULL;
}

static int callback(void* data, int numCols, char** colData, char** colName)
{
	int i;
	for(i = 0; i < numCols; i++)
	{
		printf("%s = %s\n", colName[i], colData[i] ? colData[i] : "NULL");
	}
	data = colData[0]; //there should only be one row returned in this case
	printf("\n");
	return 1;
}

static string queryDB(string query)
{
	sqlite3* db;
	char* zErrMsg = 0;
	char* result = new char[200];
	sqlite3_open("bank1.db", &db);
	sqlite3_exec(db, query.c_str(), callback, result, &zErrMsg);
	sqlite3_close(db);
	return string(result);
}
