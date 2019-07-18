

#include <sqlite3.h>
static int callback(void *data, int numCols, char **colData, char **colName);

int main (int argc, char **argv)
{
	sqlite3* db;
	char *zErrMsg = 0;
	sqlite3_open("accounts.db", &db);
	sqlite3_exec(db, "CREATE TABLE bankAccounts(account varchar(250), bank varchar(250));", callback, 0, &zErrMsg);
	sqlite3_exec(db, "INSERT INTO bankAccounts VALUES ('A12345', 'bank1');", callback, 0, &zErrMsg);
	sqlite3_exec(db, "INSERT INTO bankAccounts VALUES ('B12345', 'bank2');", callback, 0, &zErrMsg);
	sqlite3_close(db);

	register SVCXPRT *transp;
	pmap_unset (VIRTUALBANK, VER1);

	transp = svcudp_create(RPC_ANYSOCK);
	if (transp == NULL)
	{
		fprintf (stderr, "%s", "cannot create udp service.");
		exit(1);
	}
	if (!svc_register(transp, VIRTUALBANK, VER1, virtualbank_1, IPPROTO_UDP))
	{
		fprintf (stderr, "%s", "unable to register (VIRTUALBANK, VER1, udp).");
		exit(1);
	}

	transp = svctcp_create(RPC_ANYSOCK, 0, 0);
	if (transp == NULL)
	{
		fprintf (stderr, "%s", "cannot create tcp service.");
		exit(1);
	}
	if (!svc_register(transp, VIRTUALBANK, VER1, virtualbank_1, IPPROTO_TCP))
	{
		fprintf (stderr, "%s", "unable to register (VIRTUALBANK, VER1, tcp).");
		exit(1);
	}

	svc_run();
	fprintf (stderr, "%s", "svc_run returned");
	exit(1);
	/* NOTREACHED */
}

static int callback(void *data, int numCols, char **colData, char **colName)
{
	return 0;
}
