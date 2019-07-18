struct B1_req
{
	string account<>;
	int amount;
};

program BANK1
{
	version VER1
	{
		int b1_credit(B1_req request) = 1;
		int b1_debit(B1_req request) = 2;
	} = 1;
} = 97;

