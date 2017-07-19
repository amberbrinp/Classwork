#if !defined WRITE_FILE
#define WRITE_FILE

#include "Text.h"
#include <fstream>
using namespace std;
//Class to write to a text file
//The class accepts a const char* variable from the constructor.
//It also accepts a String* variable from writeLine().
class WriteFile
{
	private:
		ofstream output_file;
		bool closed;
	public:
		//Constructor that accepts name of the file to be written.
		WriteFile(const char* file_name);
		//Destructor is unnecessary
		//~WriteFile();
		//If closed is false and the line's length is greater than zero, text is written to the output file.
		void writeLine(String* line);
		//If closed is false, it closes output_file and sets closed to true.
		void close();	
};
#endif
