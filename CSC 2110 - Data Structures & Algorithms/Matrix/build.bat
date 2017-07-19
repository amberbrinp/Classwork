@echo off
cls

set DRIVE_LETTER=%1:
set PATH=%DRIVE_LETTER%\MinGW\bin;%DRIVE_LETTER%\MinGW\msys\1.0\bin;c:\Windows;c:\Windows\system32

g++ -c Matrix.cpp
g++ -c Keyboard.cpp
g++ -c Random.cpp
g++ -c String.cpp
g++ -c Tokens.cpp
g++ -c ReadFile.cpp
g++ -c WriteFile.cpp
g++ -c Lab02Driver.cpp

g++ -o Lab02.exe Lab02Driver.o ReadFile.o WriteFile.o String.o Tokens.o Keyboard.o Random.o Matrix.o
