.386
.MODEL FLAT

INCLUDE compute_bs.h
PUBLIC interpolate_proc

pointArray	EQU [ebp + 14]
xCoord 		EQU [ebp + 10] 
degree		EQU [ebp + 8] 

.CODE
interpolate_proc	PROC	NEAR32

	entry:
		push ebp
		mov  ebp, esp
		push eax
		push ebx
		push ecx
		push edx
		pushf
	
	mov cx, 0
	fldz
	
	loop1:
		mov ax, degree
		add ax, 1
		cmp cx, ax
		je exit
		
		fld1 
		mov dx, 0
		loop2:
			cmp dx, cx
			je end2
			
			fld DWORD PTR xCoord 
			mov ebx, DWORD PTR pointArray
			mov eax, 8
			
			push dx
			mul dx
			add ebx, eax
			
			fld DWORD PTR [ebx] 
			fsub 
			fmul 
			pop dx
			inc dx
			jmp loop2
			
		end2:
			compute_bs_macro cx, pointArray
			fmul 
			fadd 
			inc cx
			jmp loop1
		
		exit:
			popf
			pop edx
			pop ecx
			pop ebx
			pop eax
			mov esp, ebp
			pop ebp
			ret 10
		
interpolate_proc	ENDP

END