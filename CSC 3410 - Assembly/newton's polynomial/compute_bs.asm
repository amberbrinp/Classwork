.386
.MODEL FLAT

ExitProcess PROTO NEAR32 stdcall, dwExitCode:DWORD

INCLUDE io.h
INCLUDE debug.h
INCLUDE float.h

m    		EQU [ebp + 14]
n         	EQU [ebp + 12]
pointArray  EQU [ebp + 8]

.CODE

compute_bs		PROC 	Near32
	
	entry:
		push   ebp           
		mov    ebp, esp
		
		pushw 0
		pushw n
		pushd pointArray
		call compute_bs_recursive
	
	exit:
		mov    esp, ebp       
        pop    ebp
		ret 6

compute_bs		ENDP

compute_bs_recursive		PROC 	Near32

	entry:
		push   ebp           
        mov    ebp, esp
		push eax
		push ebx
		push edx
		pushf
		
	beginning:
		mov ax, m
		cmp ax, n
		je base_case
		
		inc ax
		push ax
		pushw n
		pushd pointArray
		call compute_bs_recursive
		mov ax, n
		dec ax
		pushw m
		push ax
		pushd pointArray
		call compute_bs_recursive
		fsub
		
		mov ebx, pointArray
		mov ax, n
		mov dx, 8
		mul dx
		movzx eax, ax
		add ebx, eax
		fld REAL4 PTR [ebx]
		
		mov ebx, pointArray
		mov ax, m
		mov dx, 8
		mul dx
		movzx eax, ax
		add ebx, eax
		fld REAL4 PTR [ebx]
		
		fsub
		fdiv
		
		jmp exit
		
	
	base_case:
		mov ebx, pointArray
		mov ax, n
		mov dx, 8
		mul dx
		add ax, 4
		movzx eax, ax
		add ebx, eax 
		fld REAL4 PTR [ebx]
	

	exit:
		popf
		pop edx
		pop ebx
		pop eax
		mov    esp, ebp       
        pop    ebp
		ret	8
		
compute_bs_recursive		ENDP



END