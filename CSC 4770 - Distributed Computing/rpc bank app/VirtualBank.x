struct VB_req
{
	string account<>;
	int amount;
};

struct VB_transfer_req
{
	string account1<>;
	string account2<>;
	int amount;
};

program VIRTUALBANK
{
	version VER1
	{
		int vb_credit(VB_req) = 1;
		int vb_debit(VB_req) = 2;
		int vb_transfer(VB_transfer_req) = 3;
	} = 1;
} = 99;

