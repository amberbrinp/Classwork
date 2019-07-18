

#include <sqlite3.h>

static int callback(void *data, int numCols, char **colData, char **colName);

int main (int argc, char **argv)
{

	sqlite3* db;
	char *zErrMsg = 0;
	sqlite3_open("bank1.db", &db);
	sqlite3_exec(db, "CREATE TABLE bank1Accounts(account varchar(250), balance int);", callback, 0, &zErrMsg);
	sqlite3_exec(db, "INSERT INTO bank1Accounts VALUES ('A12345', 0);", callback, 0, &zErrMsg);
	sqlite3_close(db);

	register SVCXPRT *transp;
	pmap_unset (BANK1, VER1);

	transp = svcudp_create(RPC_ANYSOCK);
	if (transp == NULL)
	{
		fprintf (stderr, "%s", "cannot create udp service.");
		exit(1);
	}
	if (!svc_register(transp, BANK1, VER1, bank1_1, IPPROTO_UDP))
	{
		fprintf (stderr, "%s", "unable to register (BANK1, VER1, udp).");
		exit(1);
	}

	transp = svctcp_create(RPC_ANYSOCK, 0, 0);
	if (transp == NULL) {
		fprintf (stderr, "%s", "cannot create tcp service.");
		exit(1);
	}
	if (!svc_register(transp, BANK1, VER1, bank1_1, IPPROTO_TCP))
	{
		fprintf (stderr, "%s", "unable to register (BANK1, VER1, tcp).");
		exit(1);
	}

	svc_run ();
	fprintf (stderr, "%s", "svc_run returned");
	exit (1);
	/* NOTREACHED */
}

static int callback(void *data, int numCols, char **colData, char **colName)
{
	return 0;
}
