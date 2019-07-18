#include <stdio.h>
#include <rpc/rpc.h>
#include "sumit.h"

int main(int argc, char *argv[]) {
	CLIENT *remote_client;
	struct sum_in input;
	int i;
	for (i = 0; i < 20; i++) {
		input.nums[i] = i;
	}
	input.count = 20;
	remote_client = clnt_create(argv[1], VB, VER1, "tcp");			
	if (remote_client == NULL) {
		clnt_pcreateerror(argv[1]);
		exit(1);
	}
	// while loop (ready)
	// print out ready
	// cin string
	// parse string
	// switch transfer,debit,credit
		// call vb_transfer,vb_debit, vb_credit
		// or if quit set ready to false;
	int *result = sum_it_1(&input, remote_client);
	if (result == NULL) {
		clnt_perror(remote_client, argv[1]);
		exit(1);
	}
	printf("Result is %d\n", *result);
	return 0;
}
