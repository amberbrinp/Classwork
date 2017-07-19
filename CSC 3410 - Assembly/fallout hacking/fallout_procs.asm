.386
.MODEL FLAT
PUBLIC hack

call_swap MACRO
	pushD passwordLength
	push esi
	push edi 
	call swap_passwords
ENDM

.CODE

passwordLength 	EQU [ebp + 14]
numPasswords  	EQU [ebp + 12]
guessIndex	  	EQU [ebp + 10]
numMatches 		EQU [ebp + 8]

hack PROC Near32

start:
	push ebp
	mov ebp, esp 
	
	pushD passwordLength
	push ebx 
	pushW numPasswords
	pushW guessIndex
	call get_addresses
	
	call_swap
	; put the guess password at the end of the list
	
	mov dx, 1 
	mov cx, 0 
continue:
	cmp dx, numPasswords
	jge exit
		
	pushD passwordLength
	push ebx 
	pushW numPasswords ; this is the guess password
	push dx 
	call get_addresses
	
	pushD passwordLength
	push esi
	push edi 
	call get_matches
	
	cmp ax, numMatches
	je do_the_swap
	jmp dont_do_the_swap
		
	do_the_swap: ; can use ax again- don't need to tho
		inc cx 
		
		pushD passwordLength
		push cx ; cx will never be the guess password bc it will always be smaller than dx
		push dx ; dx will never be the guess password
		call get_addresses
		
		call_swap
		
	dont_do_the_swap:
		inc dx ; dx has the smallest index for a string that doesn't match
		jmp continue
		
exit:
	mov esp, ebp 
	pop ebp 
	ret 14
hack ENDP

passwordLengthD	EQU [ebp + 16] ; it's literally just passwordLength but as a DWord
new_addr		EQU [ebp + 10]	
old_addr		EQU [ebp + 8]

get_addresses PROC Near32
start:
	push ebp
	mov ebp, esp 
	push eax 
	push esi 
	push edi 
	
	mov ax, 1
	mov esi, ebx	
	get_old_addr:
		cmp ax, old_addr
		je got_old_addr
		add esi, passwordLengthD
		inc ax 
		jmp get_old_addr

got_old_addr: ; esi is the address of the place you wanna swap from

	mov ax, 1 
	mov edi, ebx 
	get_new_addr:
		cmp ax, new_addr
		je exit
		add edi, passwordLengthD 
		inc ax 
		jmp get_new_addr
		
exit: ; edi is the address of the place you wanna swap to
	;don't pop esi or edi
	pop eax 
	mov esp, ebp 
	pop ebp 
	ret 12
get_addresses ENDP

swap_passwords PROC Near32
start:
	push ebp                 
    mov	ebp, esp
	push eax
	push ecx 
	push esi
	push edi
	
	mov ecx, passwordLengthD
	swap: ; give em the ol switcheroo 
		cmp ecx, 2 
		je exit
		
		dec ecx 
		cld 
		
		lodsb 
		
		dec esi 
		xchg esi, edi
		
		movsb 
		
		dec esi
		xchg esi, edi 
		
		stosb 
		
		jmp swap
exit:
	pop edi 
	pop esi
	pop ecx 
	pop eax 
	mov esp, ebp
	pop ebp
	ret 8
swap_passwords ENDP

get_matches PROC Near32
start:
	push ebp                 
    mov	ebp, esp
	push ecx 
	push esi
	push edi
	
	mov ax, 0 
	mov ecx, passwordLengthD
	cld
	count:
		repne cmpsb
		cmp ecx, 2
		jl exit
		
		inc ax 	
		jmp count
exit:
	pop edi 
	pop esi
	pop ecx
	; eax needs to contain #
 
	mov esp, ebp
	pop ebp
	ret 12 
get_matches ENDP

END 