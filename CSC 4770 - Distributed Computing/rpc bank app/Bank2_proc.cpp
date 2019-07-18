#include <rpc/rpc.h>
#include <sqlite3.h>
#include <stdio.h>
#include <string>
#include "Bank2.h"

using namespace std;

static string queryDB(string query);
static int callback(void* data, int numCols, char** colData, char** colName);

int* b2_credit_1_svc(B2_req* request, struct svc_req *svc)
{
	CLIENT *client;
  	return(b2_credit_1(request, client));
}


int* b2_debit_1_svc(B2_req* request, struct svc_req *svc)
{
	CLIENT *client;
  	return(b2_debit_1(request, client));
}


int* b2_credit_1(B2_req* request, CLIENT* client)
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


int* b2_debit_1(B2_req* request, CLIENT* client)
{
	//in your main make sure to make this table
	char* balanceQuery = new char[500];
	sprintf(balanceQuery, "SELECT balance FROM bank2Accounts  WHERE account LIKE '%s';", request->account);
	string balanceStr = queryDB(string(balanceQuery));
	string::size_type sz;
	int balance = stoi(balanceStr, &sz);
	if(balance > request->amount)
	{
		int newBalance = balance - request->amount;
		char* query = new char[500];
		sprintf(query, "UPDATE bank2Accounts SET balance=%d WHERE account LIKE '%s';", newBalance, request->account);
		string bank = queryDB(string(query));
		int* val = new int(1);
		return val;
	}
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
	return 0;
}

static string queryDB(string query)
{
	sqlite3* db;
	char* zErrMsg = 0;
	char* result = new char[200];
	sqlite3_open("bank2.db", &db);
	sqlite3_exec(db, query.c_str(), callback, result, &zErrMsg);
	sqlite3_close(db);
	return string(result);
}
