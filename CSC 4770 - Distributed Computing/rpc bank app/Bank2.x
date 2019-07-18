struct B2_req
{
	string account<>;
	int amount;
};

program BANK2
{
	version VER1
	{
		int b2_credit(B2_req request) = 1;
		int b2_debit(B2_req request) = 2;
	} = 1;
} = 98;

