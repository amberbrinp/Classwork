#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <netinet/in.h>
#include <sys/ioctl.h>
#include <unistd.h>
#include "pingPong.h"

#include <string.h>
#include <cstring>
#include <iostream>
using namespace std;

int sendMessage(ConnectionInfo *con, char *msg)
{
    if (!con or !msg) return 1;
    int test = send(con->sid, msg, strlen(msg)+1, 0);
    return test;
}

char * recieveMessage(ConnectionInfo* con)
{
    int sze = 0;
    int test; //for debugging & validity checking
    while (sze == 0)
    {
        test = ioctl(con->sid, FIONREAD, &sze);
    }
    if (test!= 0) return NULL;
    
    char* buffer = new char[sze];
    memset(buffer, '\0', sze);
    test = recv(con->sid, buffer, sze, 0);
    if (test < 0) return NULL;
    return buffer;
}

void dealocate_message(char * mem )
{
    delete[] mem;
    return;
}

int connect_to_server(char * who, int port, ConnectionInfo* con)
{
    if (!who || !con) return 1;
    
    int sid = socket(AF_INET, SOCK_STREAM, 0);
    if (sid < 0) return 1;
    
    con->sid = sid;
    
    struct hostent* host = gethostbyname(who);
    
    struct sockaddr_in sai2;
    sai2.sin_family = AF_INET;
    sai2.sin_addr = *((struct in_addr *)host->h_addr);
    sai2.sin_port = htons(port);
    
    //connect
    int test = connect(sid, (struct sockaddr *) &sai2, sizeof(sai2));
    
    return test;
}


int run_server(int port)
{
    int sid = socket(AF_INET, SOCK_STREAM, 0);
    if (sid < 0) return 1;
    
    struct sockaddr_in cli_addr;
    struct sockaddr_in sai1;
    sai1.sin_family = AF_INET;
    sai1.sin_addr.s_addr = htonl(INADDR_ANY);
    sai1.sin_port = htons(port);
    
    //bind
    int test = bind(sid, (struct sockaddr *) &sai1, sizeof(sai1));
    if (test != 0) return 1;

	printf("Server running on port %i\n", port);
    
    //listen
    test = listen(sid, 10);
    if (test != 0) return 1;
    
    //accept
    socklen_t len = sizeof(cli_addr);
    test = accept(sid, (struct sockaddr *) &cli_addr, &len);
    if (test < 0) return 1;
    
    char* buffer;
    while(1)
    {
        int sze = 0;
        int test2; //for debugging & validity checking
        while (sze == 0)
        {
            test2 = ioctl(test, FIONREAD, &sze);

        }
        
        //receive
        buffer = new char[sze];
        memset(buffer, '\0', sze);
        test2 = recv(test, buffer, sze, 0);
        if (test2 < 0) return 1;
        printf("Server received the following message: %s\n",buffer);
        
        //send
        char* response = new char[sze];
        if (std::string(buffer) == "PING") sprintf(response, "PONG");
        else response = buffer;
        
        test2 = send(test, response, sze, 0);
        if (test2 < 0) return 1;
        
        dealocate_message(buffer);
    }
    
    
}

