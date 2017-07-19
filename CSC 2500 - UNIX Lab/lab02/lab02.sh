#!/bin/bash
echo "1) Show the current date and time"
echo "2) Show a calendar for the current month"
echo "3) Show a calendar for a specified month (e.g. to show the calendar for December: 2.sh 12)"
echo "4) Show the file system disk space usage for all devices"
echo "5) Show the file system disk space usage for the device that hold the root file system"
echo "6) Show the current working directory"
echo "7) Move up one directory"
echo "8) List the files in the current directory"
echo "9) List the files in the current directory sorted by modification time (newest first)"
echo "10) List all files in the current directory, even the hidden ones"
echo "11) Show a detailed long listing of all the files in the current directory including the hidden ones"
echo "12) Show a detailed long listing of all the files in the parent directory including the hidden ones"
echo "13) Print the full pathe of your home directory"
echo "0) Exit"

read users_choice
exec ./$users_choice.sh
