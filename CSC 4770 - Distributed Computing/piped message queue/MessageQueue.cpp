#include <iostream>
#include <zmq.h>
using namespace std;

int main()
{
	void* context = zmq_ctx_new();
	void* sock_s = zmq_socket(context, ZMQ_XSUB);
	void* sock_p = zmq_socket(context, ZMQ_XPUB);
	zmq_bind(sock_s, "tcp://127.0.0.1:5555");
	zmq_bind(sock_p, "tcp://127.0.0.1:5556");
	zmq_proxy(sock_s, sock_p, NULL);
	return 0;
}
