#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <stdio.h>
#include <string.h>
#include <pthread.h>
#include <regex.h>
#include <stdlib.h>
#include <unistd.h>
#include <lua.h>
#include <lualib.h>
#include <lauxlib.h>

#define PORT 5555
#define MAX_BUFFER_SIZE 1024

/* prototypes for user defined functions go here */
void handleConnect(int clisock);
void* connectHandler(void* args);
int readSock(int clisock);

typedef struct
{
	char* version;	 // HTTP version from the request
	char* path;
	int isLua;	 //yes=1
	int hasKV;	 //yes=1
	int keepAlive;	 //yes=1
	char* keys[20];
	char* values[20]; //maybe this is a low max
	
} http;

http parseHTTP(char* request);
char* getMatch(char* line, char* exp);
char* findFile(char* path);
static int HTML_OUT(lua_State *L);

static int c_print(lua_State *L);

int main(int argc, char* argv[]) //pass in port
{
	int port = atoi(argv[1]);
	
	int sockfd, newsockfd, clilen;
	struct sockaddr_in cli_addr, serv_addr;
	if ((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0) { /* error */ printf("could not create socket\n");} 
	memset((void *) &serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
	serv_addr.sin_port = htons(port);

	//bind
	if (bind(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0) { /* error */ printf("could not bind socket\n");} 
	printf("Server running on port %i\n", port);

	//listen
	listen(sockfd, 5);

	for(;;) 
	{
		clilen = sizeof(cli_addr);

		//accept
		newsockfd = accept(sockfd, (struct sockaddr *) &cli_addr, (socklen_t *) &clilen);
		if (newsockfd < 0) { /* error */ printf("could not accept connection\n");} 
		handleConnect(newsockfd);
	}
}

void handleConnect(int clisock)
{
	pthread_attr_t attribs;
	pthread_t thread;
	pthread_attr_init(&attribs);
	pthread_attr_setdetachstate(&attribs, PTHREAD_CREATE_DETACHED);
	pthread_create(&thread, &attribs, connectHandler, (void *)clisock);
}
void* connectHandler(void* args)
{
	int clisock = (int) args; 
	while (readSock(clisock));
	close(clisock);//make sure to deallocate thread resources before calling this
	pthread_exit(NULL);
}

int readSock(int clisock)
{
	char buf[MAX_BUFFER_SIZE];
	int msgSize;
	char* file;
	http request;
	memset(buf, 0, MAX_BUFFER_SIZE);
	while(1)
	{
		request.isLua = 0;
		request.hasKV = 0;
		request.keepAlive = 0;
		if ((msgSize = recv(clisock, buf, MAX_BUFFER_SIZE - 1, 0)) < 0)
		{
			/* error */
			printf("could not receive message\n");
			printf("closing connection\n");
			return 0;
		}
		/* handle the message */ 
		printf("%s\n", buf);
		request = parseHTTP(buf);

		if(request.path == NULL)
		{
			printf("could not parse request\n");
		}
		else if(request.isLua)
		{
			lua_State *L = luaL_newstate();
			luaL_openlibs(L);
			int ret;
			ret = luaL_loadfile(L, request.path);
		
			if (!ret)
			{
				lua_newtable(L);
				if(request.hasKV)
				{
					//load kv pairs into lua interpreter table
					int i;
					i = 0;
					while(request.keys[i] && request.values[i])
					{
						printf("Pushing KV pairs...\n");
						lua_pushstring(L, request.keys[i]);
						lua_pushstring(L, request.values[i]);
						i++;
						 /* lua_rawset pops the key and value off the stack and inserts them into the table       
						identified by the give index (it will be -3 at the end of every iteration). */
						lua_rawset(L, -3);      // Stores the pair in the table 
					}
					lua_setglobal(L, "Info");
				}
				// Create a userdata.  A userdata is a Lua object that can host C/C++ data..
				int *ud = (int *) lua_newuserdata(L, sizeof(int));
				*ud = clisock; // i need to send the socket id to the method
				lua_pushcclosure(L, HTML_OUT, 1);
				lua_setglobal(L, "HTML_OUT");
				int result;
				result = lua_pcall(L, 0, 1, 0);
				if (result)
				{
					printf("Failed to run script\n");
				}

				lua_pop(L, 1);  /* Take the returned value out of the stack */
				lua_close(L);
			}
			else
			{
				printf("could not load file\n");
			}
		}
		else
		{
			int found = 0;
			if((file = findFile(request.path)) > 0)//the file exists and i can grab it
			{
				printf("I have that file.\n");
				found = 1;
			}
			char* response;
			char* status;
			status = "200 OK";
			char* connection;
			if(!strcmp(request.version, "HTTP/1.0") && !request.keepAlive)
			{
				connection = "";
			}
			else
			{
				connection = "Connection: Keep-Alive";
			}
			if(found)
			{
				asprintf(&response, "%s %s\r\n%s\r\n\r\n%s", request.version, status, connection, file);
			}
			else
			{
				char* reply = "I could not find or read that file.\n";
				asprintf(&response, "%s %s\r\n%s\r\n\r\n%s", request.version, status, connection, reply);
			}
			printf("Constructed response:\n%s", response);
			if (send(clisock, response, strlen(response), 0) < 0)
			{	/* error */
				printf("could not send reply\n");
			}
			printf("Response sent\n");
			free(file);
		}
		if(!strcmp(request.version, "HTTP/1.0") && !request.keepAlive)
		{//if the thing is http1.0 and they're not using the Connection: Keep-Alive header
			printf("closing connection\n");
			return 0;
		}
	}
}

static int HTML_OUT(lua_State *L)
{
	int *clisock = (int *) lua_touserdata(L, lua_upvalueindex(1));
	// Now get the single argument passed by the call in the Lua script.
	const char *msg = (char *) lua_tostring(L, -1);
	// Now, write the argument using the file descriptor upvalue.

	if (send(&clisock, msg, strlen(msg), 0) < 0) 
	{ /* error */ 
		printf("could not send lua reply\n");
		return -1;
	}

  return 0;	
}

char* findFile(char* path)
{
	FILE* file;
	file = fopen(path, "r");

	if (file==NULL)
	{
		printf("file does not exist\n");
		return 0;
	}
	printf("Successfully opened file\n");	
	
	char* source = NULL;
	// go to the end of the file
	if (fseek(file, 0L, SEEK_END) == 0)
	{
		// get the size of the file
		long bufsize = ftell(file);
		if (bufsize == -1)
		{ 
			printf("File is empty\n");
			return 0;
		}

		// allocate buffer to that size
		source = malloc(sizeof(char) * (bufsize + 1));

		// go back to the start of the file
		if (fseek(file, 0L, SEEK_SET) != 0)
		{
			printf("Could not return to beginning of file\n");
			return 0;
		}

		// read the entire file into memory
		printf("Reading file...\n");
		size_t newLen = fread(source, sizeof(char), bufsize, file);
		if (newLen == 0)
		{
		    printf("Error reading file\n");
		    return 0;
		}
		else
		{
		    source[++newLen] = '\0'; // safe
		}
	}
	else
	{
		printf("Error seeking end of file\n");
	}
	fclose(file);
	return source;
}


/*
Structure of an HTTP request:
GET /home.html HTTP/1.1
Host: www.yoursite.com
*/
//kv pairs in a url: www.url.com?key=value&key=value&key=value
//assume no spaces or special characters (16 encoding %20 for spaces, etc)
http parseHTTP(char* buf)
{
	http request;
	char* temp;
	char* line1;
	char* line2;
	char* get;
	line1 = strtok_r(buf, "\r\n", &temp);
	line2 = strtok_r(NULL, "\r\n", &temp);
	char* header;
	header = strtok_r(NULL, "\r\n", &temp);
	while(header)
	{
		if(!strcmp(header, "Connection: keep-alive") | !strcmp(header, "Connection: Keep-Alive"))//not sure how to ignore case
		{
			request.keepAlive = 1;
			break;
			printf("found Keep Alive header\n");
		}
		else
		{
			request.keepAlive = 0; //as long as the last iteration fixes this
			header = strtok_r(NULL, "\r\n", &temp);
		}
	}
	
	temp = NULL;
	get = strtok_r(line1, " ", &temp);
	char* fullpath;
	if(!strcmp(get, "GET"))
	{
		printf("I see your GET request; parsing...\n");
		request.path = strtok_r(NULL, " /", &temp);
		request.version = strtok_r(NULL, " ", &temp);
		printf("full path: %s\n", request.path);
	}
	else
	{
		printf("poorly formatted request or not a GET request\n");
		request.path = NULL;
		return request;
	}

	if((strstr(request.path, ".lua")!=NULL)|(strstr(request.path, ".luac")!=NULL))
	{
		printf("Identified Lua file...\n");
		request.isLua = 1;
		char* kvPairs = NULL;
		char* fullpath = request.path;
		sscanf(fullpath, "%[^?]?%s", request.path, kvPairs);
		if(kvPairs)
		{
			request.hasKV = 1;
			temp = NULL;
			char* pair = strtok_r(kvPairs, "&", &temp);
			sscanf(pair, "%[^=]=%s", request.keys[0], request.values[0]);
			int i = 0;
			while((pair = strtok_r(kvPairs, "&", &temp)) != NULL)
			{
				i++;
				sscanf(pair, "%[^=]=%s", request.keys[i], request.values[i]);
			}
		}
		else
		{
			request.hasKV = 0;
		}
	}
	else
	{
		request.isLua = 0;
	}
	printf("file: %s\n", request.path);

	return request;
}
