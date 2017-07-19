struct ConnectionInfo
{
    int sid;
};

int sendMessage( ConnectionInfo *con, char *msg);
char * recieveMessage( ConnectionInfo* con);
void dealocate_message( char * mem );
int connect_to_server( char * who, int port, ConnectionInfo* con);
int run_server( int port );
