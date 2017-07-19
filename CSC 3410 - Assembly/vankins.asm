.386
.MODEL FLAT

;will need to link to a windows library to exit the program
ExitProcess PROTO NEAR32 stdcall, dwExitCode:DWORD

; simple text replacement
CR      	EQU     0Dh     ; carriage return ASCII code
LF      	EQU     0Ah    	; line feed

.STACK 4096

.DATA

txt		BYTE	13 DUP(?)
line	 	BYTE	LF, CR, 0
emptyString 	BYTE 	13 DUP(0)
solutionString	BYTE	13 DUP(?)

n		WORD	?
m		WORD	?
row		WORD	?
col		WORD	?

element		WORD	?
index		WORD	?

num1		WORD	?
num2		WORD	?

right		WORD	?
down		WORD	?

originalMatrix	WORD	100 DUP(?)

totalElements	WORD	?
matrixIndex	WORD	?

INCLUDE io.h           		; header file for input/output
INCLUDE debug.h

getElement MACRO index	

	lea ebx, originalMatrix
	mov ax, index
	mov dx, 2
	mul dx
	movzx edx, ax
	add ebx, edx
	mov ax, [ebx]
ENDM

setElement MACRO row, col, index

	getIndex row, col, index
	getElement index

ENDM

getIndex MACRO row, col, index
	mov index, 0
	sub row, 1
	sub col, 1
				
	mov ax, row
	mul m
				
	add ax, col
	add index, ax	; index now contains the correct index
				
	add row, 1
	add col, 1						
ENDM

vankins MACRO
local colZero, getRight, rightMath, notRight, downMath, notDown, gotElement, done
	mov ax, n
	mov row, ax
	mov ax, m
	mov col, ax
	mov ax, totalElements
	mov matrixIndex, ax
	jmp getRight

colZero:
	mov ax, m
	mov col, ax
	dec row
	cmp row, 0
	jle done

getRight:			
	mov cx, col
	inc cx
	mov num1, 0
	cmp cx, m
	jg notRight

rightMath:
	setElement row, cx, index
	mov num1, ax

notRight:
	mov cx, row
	inc cx
	cmp cx, n
	mov num2, 0
	jg notDown

downMath:
	setElement cx, col, index
	mov num2, ax

notDown:
	setElement row, col, index
	mov element, ax

	mov ax, num1
	cmp ax, num2
	jge gotElement
	mov ax, num2

gotElement:
	add element, ax
	mov ax, element
	mov [ebx], ax

	dec col
	cmp col, 0
	je colZero

	dec matrixIndex
	cmp matrixIndex, 0
	jle done
	jmp getRight

done:	
ENDM

solution MACRO
local while, notRight, compare, lesser, greaterEqual, skip, slideToTheRight, getDown, done
	mov col, 1
	mov row, 1
	mov matrixIndex, 0

while:
	mov cx, col 
	inc cx 
	mov num1, 0
	mov right, 0
	cmp cx, m 
	jg notRight
	
	add right, cx
	setElement row, cx, index
	mov num1, ax

notRight:			
	mov cx, row 
	inc cx 
	cmp cx, n 
	mov num2, 0
	mov down, 0
	jg compare
			
	add down, cx 
	setElement cx, col, index
	mov num2, ax 

compare:
	mov ax, num1
	cmp ax, num2
	jge greaterEqual

lesser:
	mov ax, down
	mov row, ax
	mov solutionString, 'd'
	output solutionString
	jmp skip

greaterEqual:
	mov ax, right
	mov col, ax
	mov solutionString, 'r'
	output solutionString

skip:				
	mov ax, n
	cmp row, ax
	jge getDown
	mov ax, m
	cmp col, ax
	jge slideToTheRight

	inc matrixIndex
	mov ax, totalElements
	dec ax
	cmp matrixIndex, ax
	jle while
	jmp done

slideToTheRight:		
	mov solutionString, 'r'
	output solutionString
	jmp done

getDown:			
	mov solutionString, 'd'
	output solutionString

done:
ENDM

inputMatrix MACRO
local while
	mov cx, 0
	inputW emptyString, n
	inputW emptyString, m
	mov ax, n
	mul m			; no such thing as negative rows or columns
	mov totalElements, ax
	
	while:
		inputW emptyString, element
		mov ax, element
		mov [ebx], ax
		add ebx, 2
		inc cx
		cmp cx, totalElements
		jl while
ENDM

outputW_inline MACRO var	; same as normal outputW but without the carriage return

	itoa txt, var
	output txt
ENDM

outputMatrix MACRO 
local new_row, while_in_row, end_matrix
	mov dx, 0
	new_row:
		mov cx, 0
		output line
	while_in_row:
		mov ax, [ebx]
		outputW_inline ax
		
		add ebx, 2
		inc cx
		inc dx		 ; i need two counters for end of row and end of matrix
		
		cmp dx, totalElements
		je end_matrix 	 ; end of the matrix
		cmp cx, m
		je new_row	 ; printed out m cols, reset
		jmp while_in_row ; continue with rest of the columns for this row
	end_matrix: 		 ; done

ENDM



.CODE                   	; start of main program code
_start: 

	lea ebx, originalMatrix
	inputMatrix
	lea ebx, originalMatrix
	outputMatrix
	lea ebx, originalMatrix
	vankins
	lea ebx, originalMatrix
	output line
	outputMatrix
	output line
	solution
	

        INVOKE  ExitProcess, 0  ; exit with return code 0

PUBLIC _start

END