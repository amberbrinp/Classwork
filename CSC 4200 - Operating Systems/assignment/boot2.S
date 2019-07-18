.intel_syntax noprefix

.global show_eax
.global writeScr

.global kbd_enter
.global lidtr
.global outportb

.global go
.global schedule
.global init_timer_dev

.global pushf_cli_fun
.global popf_fun
.global schedule_fun

.extern convert_num
.extern clearScr

.extern kbd_handler
.extern enqueue
.extern dequeue
.extern currentPCB

eax_msg: .ascii "The value in the EAX register is: "
eax_val: .ascii "          "

pushf_cli_fun:
	pushf
	cli
	pop eax
	ret

popf_fun:
	push ebp
	mov ebp, esp
	push [ebp+8]
	popf
	pop ebp
	ret

go:
	call dequeue
	mov esp, [eax]
	mov currentPCB, eax
	pop gs
	pop fs
	pop es
	pop ds
	popad
	iret

schedule_fun:
	pushfd
	push cs
	push offset afterSwitch

	pushad
	push ds
	push es
	push fs
	push gs
	
	mov eax, [currentPCB]
	mov [eax], esp
	push currentPCB
	call enqueue_VTq

	add esp, 4
	call dequeue
	mov esp, [eax]
	mov currentPCB, eax

	pop gs
	pop fs
	pop es
	pop ds
	popad

	iret
	
afterSwitch:
	ret

schedule:
	pushad
	push ds
	push es
	push fs
	push gs
	
	mov eax, [currentPCB]
	mov [eax], esp
	push currentPCB
	call enqueue

	call dequeue
	mov esp, [eax]
	mov currentPCB, eax

	pop gs
	pop fs
	pop es
	pop ds
	popad

	push eax
	mov al, 0x20
	out 0x20, al
	pop eax
	iret

init_timer_dev:
	
	push ebp
	mov ebp, esp
	push eax
	push edx

	mov edx, [ebp+8]
	mov ax, 1193
	mul dx
	mov dx, ax

	mov al, 0b00110110
	out 0x43, al

	mov ax, dx
	out 0x40, al
	xchg ah, al
	out 0x40, al

	pop edx
	pop eax
	mov esp, ebp
	pop ebp
	
	ret

kbd_enter:
	pushf
	cli
	pusha

	xor eax, eax /* post-feedback garbage disposal */

	in al, 0x64
	and al, 0x01 /* see if the keyboard has something available */
	jz _kbd_skip /* if not, skip to the end */
	in al, 0x60 /* read the keyboard scan code */
	
	push eax /* push on the scan code before calling */
	call kbd_handler
	add esp, 4	

	_kbd_skip:
	mov al, 0x20
	out 0x20, al

	popa
	sti
	popf
	iret

lidtr:
	lidt [esp + 4]
	ret

outportb:
	push ebp
	mov ebp, esp
	push eax
	push edx

	mov eax, [ebp + 12]
	mov edx, [ebp + 8]
	out dx, al

	pop edx
	pop eax
	mov esp, ebp
	pop ebp
	ret
/*--------------------------------------------------------------------------------*/

show_eax:
	push ebp
	mov ebp, esp
	push eax

	call clearScr
	push offset eax_val
	push eax
	call convert_num
	add esp, 8

	pushd 18
	pushd 12
	push offset eax_msg
	call writeScr

	add esp, 12

	pop eax
	mov esp, ebp
	pop ebp
	ret

writeScr: 
	push ebp
	mov ebp, esp
	push eax
	push edi

	mov eax, [ebp+12]
	mov ebx, [ebp+16]
	imul eax, 80
	add eax, ebx
	imul eax, 2

	mov edi, 0xB8000
	add edi, eax
	mov esi, [ebp+8]

	writing_loop:
	movsb
	movb [edi], 31
	inc edi
	cmpb [esi], 0
	jne writing_loop

	pop edi
	pop eax
	mov esp, ebp
	pop ebp
	ret
