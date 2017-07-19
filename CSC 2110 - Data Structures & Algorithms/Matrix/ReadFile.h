#if !defined READ_FILE
#define READ_FILE

#include "Text.h"
#include <fstream>
using namespace std;
//Class to read a text file 
//The class accepts a const char* variable from the constructor.

class ReadFile
{
	private:
		ifstream input_file;
		bool _eof;
		bool closed;
	public:
		//The constructor that accepts the name of the text file to be read
		ReadFile(const char* file_name);
		//The destructor was not necessary because it just did what was already done.
		//Could be made virtual
		//~ReadFile();
		//As long as _eof and closed are false, readLine creates and returns a new String* from the contents of the text file.
		String* readLine();
		//Returns the private _eof.
		bool eof();
		//Returns the private closed.
		void close();
};
#endif
