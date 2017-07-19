#include <stdlib.h>
#include <iostream>
#include <mysql.h>
#include <stdio.h>

using namespace std;

int main(int argc, char* argv[])
{
    MYSQL *connect;
    connect = mysql_init(NULL);
    if (!connect)
    {
        cout << "initialization failed\n";
        return 1;
    }
    connect = mysql_real_connect(connect, argv[1], argv[3], argv[4], NULL, 0, NULL, 0);

    if (connect)
    {
        cout << "connection established\n";
        mysql_query(connect, "DROP DATABASE IF EXISTS TTU");
        mysql_query(connect, "CREATE DATABASE TTU");
        mysql_query(connect, "use TTU");
        
        mysql_query(connect, "create table students(TNumber char(8) not null primary key, FirstName varchar(20) not null, LastName varchar(20) not null, DateOfBirth date, index(LastName)) ENGINE=INNODB");
        mysql_query(connect, "insert into students(TNumber, FirstName, LastName, DateOfBirth) values('00001234','Joe','Smith','19500812')");
    }
    else
    {
        cout << "connection failed\n";
        exit(1);
    }
    
    MYSQL_RES *res;
    MYSQL_ROW row;
    mysql_query(connect, "select * from students;");
    res = mysql_store_result(connect);
    unsigned int numRows = mysql_num_rows(res);
    while ((row = mysql_fetch_row(res)) != NULL)
    {
        cout << row[0] << "\t";
        cout << row[1] << "\t";
        cout << row[2] << "\t";
        cout << row[3] << "\n";
    }
    
	mysql_close(connect);
    return 0;
}
