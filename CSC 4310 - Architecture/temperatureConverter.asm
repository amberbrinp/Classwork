.MODEL small ; smallest segmented memory model (one DS, CS, ES)
.STACK 1024	; use a stack size of 1024 bytes

LF  	EQU	0Ah
TERM 	EQU 24h 

.DATA 

prompt	BYTE "Enter a number between 0 and 100 Celsius: ", TERM, 0
; buff & DISP defined in code with DB

asciiToDec MACRO
; converts ASCII number into decimal and stores the whole thing in ax (al)

	xor ax, ax
	mov cx, bx ;cx contains length of buffer
	xor bx, bx
	xor dl, dl		
	mov dh, 10
	
	nextChar:
		mov dl, buff[bx+2] ; this is fine because the buffer is created already
		inc bx 	
		
		and dl, 0Fh	; convert ascii to decimal
		imul dh						
		add al, dl ; trusting that the input is < 4 digits
		
		cmp cx, bx
		jne nextChar
		
ENDM

decToAscii MACRO
; just doing asciiToDec backwards

	mov dl, 10
	mov bx, 3 ; trusting that output is < 4 digits
	
	nextDigit: 
		xor ah, ah

		div dl		; ah::al
		or ah, 30h 		; decimal to ascii
		mov DISP[bx+1], ah 

		dec bx
		cmp al, 0
		jne nextDigit

ENDM

.CODE

my_interrupt PROC FAR
	
	push dx
	mov dx, OFFSET prompt
	mov ah, 9h
	int 21h
	pop dx
	iret

my_interrupt ENDP

start:

	cli 
	mov ax, @DATA 
	mov ds, ax ; setting up data segments bc memory modeled program
	xor ax, ax
	mov es, ax 
	sti 

	mov ax, 2523h ; put DOS function 25 in ah, int 23 in al
	mov dx, OFFSET my_interrupt ; put interrupt handler for 21 in ds:dx
	
	mov bx, ds 	
	mov es, bx ; mov es, ds 
	
	mov bx, cs ;(address for interrupt vector table ds::dx)
	mov ds, bx ; mov ds, cs
	
	int 21h	
	mov bx, es 
	mov ds, bx ; mov ds, es
	
	int 23h ;ctrl c
	
	buff db 7, 0, 7 dup(' ') ;"literally put the byte right there in the executable"
	
	mov bx, cs
	mov ds, bx ; mov ds, cs
	
	mov dx, OFFSET buff
	mov ah, 0Ah
	int 21h
	
	xor bx, bx 
	mov bl, buff[1] ;length of buffer
	
	mov buff[1], LF
	mov buff[bx+2], TERM
	mov buff[bx+3], 0 ; set up buffer
	
	mov dx, OFFSET buff+1
	mov ah, 9h
	int 21h
	
	DISP db LF, 7 dup(' '), TERM, 0 
	
	asciiToDec

	; (9/5)C + 32 = (9C + 32(5))/5
	; depends on asciiToInt putting correct whole value in ax
	mov bh, 9
	imul bh
	add ax, 160	
	mov bh, 5
	div bh ; ah::al
	
	decToAscii

	mov DISP[bx+1], LF
	lea dx, DISP[bx+1]
	mov ah, 9h
	int 21h
	
	; done
	mov ax, 4C00h
        int 21h

end start