.386
.MODEL FLAT

ExitProcess PROTO NEAR32 stdcall, dwExitCode:DWORD

include debug.h
include io.h
include fallout_hack.h 

LEN 	EQU 	13
MAX		EQU     20
CR     	EQU     0dh     ; carriage return character
LF	   	EQU     0ah     ; line feed

.STACK  4096            ; reserve 4096-byte stack

.DATA 
inputString		    BYTE LEN DUP(?)
list				BYTE MAX * LEN DUP(?)

stringPrompt		BYTE "Enter a string: ",  0
numStrings		BYTE "The number of strings entered is  ", 0 
indexPrompt 		BYTE "Enter the index for the test password (1-based): ", 0 
matchesPrompt		BYTE "Enter the number of exact character matches: " , 0 
newline				BYTE CR, LF, 0

print_list MACRO list, numToPrint
local print_loop, done	
	lea ebx, list
	mov ax, 0
print_loop:
	cmp ax, numToPrint
	je done
	output BYTE PTR [ebx]
	output newline
	add ebx, LEN
	inc ax 
	jmp print_loop
done:
ENDM

ioW MACRO prompt, destination
	output prompt
	input inputString, LEN
	atoi inputString
	mov destination, ax 
	outputW destination
ENDM

.CODE
_start:
	
mov cx, 0 
lea ebx, list

get_list:
	output stringPrompt
	input BYTE PTR [ebx], LEN
	output BYTE PTR [ebx]
	
	cmp BYTE PTR[ebx], 'x'
	je got_list
	cmp cx, MAX
	jge got_list
	
	output newline
	add ebx, LEN
	inc cx
	jmp get_list

got_list: ; cx now contains the # of passwords
	
	output newline
	output newline 
	output numStrings
	outputW cx
	output newline
	
	print_list list, cx
	ioW indexPrompt, dx ; dx contains the guess password index
	ioW matchesPrompt, ax ; ax contains the # of matches
	output newline

get_password:
	fallout_hack list, LEN, cx, dx, ax 
	; guess what. cx STILL contains the # of passwords
	cmp cx, 1 
	jle got_password
	
	print_list list, cx
	ioW indexPrompt, dx
	ioW matchesPrompt, ax 
	
	jmp get_password

got_password:
	output newline 
	print_list list, cx
	
INVOKE ExitProcess, 0
PUBLIC _start
END