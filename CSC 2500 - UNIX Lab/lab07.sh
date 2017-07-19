#!/bin/bash

if [ ! $# -eq 2 ]; then
	echo "usage: $0 file user";
	echo "Where file is the file to search and user is the user to find";
	exit 0;
fi

if [ ! -e $1  ]; then
	echo "error: $1 does not exist";
	exit 0;
fi

count=0;
for line in `cat $1`; do
	if [ "$line" = "$2" ]; then
		echo "$2 found on line: $count";
		exit 0;
	fi
	count=`expr $count + 1`;
done

echo "$2 doesn't exist. write $2 to $1? (Y/N)";
read answer;
answer=`echo $answer | tr [a-z] [A-Z]`;
if [ "$answer" = "Y"  ]; then
	echo "$2" >> ./$1;
fi
