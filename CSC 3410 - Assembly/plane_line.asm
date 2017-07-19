.386
.MODEL FLAT

;will need to link to a windows library to exit the program
ExitProcess PROTO NEAR32 stdcall, dwExitCode:DWORD

; simple text replacement
CR      	EQU     0Dh     ; carriage return ASCII code
LF      	EQU     0Ah    	; line feed

.STACK  4096

.DATA

plane1x		WORD	?	; the x-value for the first point on the plane
plane1y		WORD	?
plane1z		WORD	?

plane2x		WORD	?
plane2y		WORD	?	; the y-value for the second point on the plane
plane2z		WORD	?

plane3x		WORD	?
plane3y		WORD	?
plane3z		WORD	?	; the z-value for the third point on the plane

line1x		WORD	?	; the x-value for the first point on the line
line1y		WORD	?
line1z		WORD	?

line2x		WORD	?
line2y		WORD	?	; the y-value for the second point on the line
line2z		WORD	?

xplaneprompt	BYTE	"Enter the x-coordinate of the point on the plane:  ", 0
yplaneprompt	BYTE	"Enter the y-coordinate of the point on the plane:  ", 0
zplaneprompt	BYTE	"Enter the z-coordinate of the point on the plane:  ", 0

xlineprompt	BYTE	"Enter the x-coordinate of the point on the line:  ", 0
ylineprompt	BYTE	"Enter the y-coordinate of the point on the line:  ", 0
zlineprompt	BYTE	"Enter the z-coordinate of the point on the line:  ", 0

crossProduct1 	WORD	?
crossProduct2 	WORD	?
crossProduct3 	WORD	?

dotProduct 	WORD	?	; holds the denominator of alpha for most of the program

ppSubx		WORD	?
ppSuby		WORD	?
ppSubz		WORD	?

midnormx	WORD	?
midnormy	WORD	?
midnormz	WORD	?

scalarNumerator WORD	?	; holds the numerator of alpha for most of the program

scalarx		WORD	?
scalary		WORD	?
scalarz		WORD	?

paramEqResult	WORD	? 	

coordinates		BYTE	40 DUP (0)

xQ		WORD	?	; quotient for x-value of the point of intersection
xR		WORD	?
yQ		WORD	?
yR		WORD	?	; remainder for y-value of the point of intersection
zQ		WORD	?
zR		WORD	?

INCLUDE io.h           		; header file for input/output
INCLUDE debug.h

getAllPoints MACRO		; called first in code

	getPoint plane1x, plane1y, plane1z, xplaneprompt, yplaneprompt, zplaneprompt
	outputCoordinates plane1x, plane1y, plane1z
	getPoint plane2x, plane2y, plane2z, xplaneprompt, yplaneprompt, zplaneprompt
	outputCoordinates plane2x, plane2y, plane2z
	getPoint plane3x, plane3y, plane3z, xplaneprompt, yplaneprompt, zplaneprompt
	outputCoordinates plane3x, plane3y, plane3z

	getPoint line1x, line1y, line1z, xlineprompt, ylineprompt, zlineprompt
	outputCoordinates line1x, line1y, line1z
	getPoint line2x, line2y, line2z, xlineprompt, ylineprompt, zlineprompt
	outputCoordinates line2x, line2y, line2z

ENDM

getPoint MACRO xCoord, yCoord, zCoord, xPrompt, yPrompt, zPrompt	; called by getAllPoints

	inputW xPrompt, xCoord
	outputW xCoord
	inputW yPrompt, yCoord
	outputW yCoord
	inputW zPrompt, zCoord
	outputW zCoord

	output carriage

ENDM

outputCoordinates MACRO xCoord, yCoord, zCoord		; called by getAllPoints

	mov coordinates, "("
	itoa coordinates+1, xCoord
	mov coordinates+7, ","
	itoa coordinates+8, yCoord
	mov coordinates+14, ","
	itoa coordinates+15, zCoord
	mov coordinates+21, ")"

	output coordinates
	output carriage

ENDM

printXYZ MACRO			; called at the very end in code
	
	mov coordinates, "("
	itoa coordinates+4, xR
	mov coordinates+7, "."
	itoa coordinates+1, xQ
	mov coordinates+10, ","
	itoa coordinates+14, yR
	mov coordinates+17, "."
	itoa coordinates+11, yQ
	mov coordinates+20, ","
	itoa coordinates+24, zR
	mov coordinates+27, "."
	itoa coordinates+21, zQ
	mov coordinates+30, ")"
	

	output coordinates
	output carriage

ENDM

getParamXYZ MACRO		; this is called in code

	ParamEq line1x, line2x
	mov ax, paramEqResult	
	mov bx, 100
	imul bx			; dividend in ax:dx
	idiv dotProduct		; quotient in ax, remainder in dx
	cwd			; quotient (now dividend) in ax:dx, remainder is thrown out
	idiv bx			; new quotient in ax, new remainder in dx
	mov xQ, ax
	mov xR, dx

	ParamEq line1y, line2y
	mov ax, paramEqResult	
	mov bx, 100		
	imul bx		
	idiv dotProduct	
	cwd	
	idiv bx	
	mov yQ, ax
	mov yR, dx

	ParamEq line1z, line2z  
	mov ax, paramEqResult	
	mov bx, 100		
	imul bx	
	idiv dotProduct	
	cwd	
	idiv bx	
	mov zQ, ax
	mov zR, dx

ENDM

ParamEq MACRO point1, point2	; called by getParamXYZ

	; (denom - numer)*P1 + numer*P2

	mov bx, dotProduct
	sub bx, scalarNumerator	; bx contains (denominator - numerator)
	imul bx, point1
	
	mov cx, scalarNumerator
	imul cx, point2

	add bx, cx
	mov paramEqResult, bx
ENDM

scalar MACRO	; called in code

	pointpointSubtract plane1x, plane1y, plane1z, line1x, line1y, line1z
	dot crossProduct1, crossProduct2, crossProduct3, ppSubx, ppSuby, ppSubz
	mov scalarNumerator, ax

	pointpointSubtract line2x, line2y, line2z, line1x, line1y, line1z
	dot crossProduct1, crossProduct2, crossProduct3, ppSubx, ppSuby, ppSubz
	
	; numerator in scalarNumerator, denominator in dotProduct

ENDM

normal MACRO	; called in code

	pointpointSubtract plane2x, plane2y, plane2z, plane1x, plane1y, plane1z

	mov midnormx, ax
	mov midnormy, bx
	mov midnormz, cx

	pointpointSubtract plane3x, plane3y, plane3z, plane1x, plane1y, plane1z

	cross midnormx, midnormy, midnormz, ppSubx, ppSuby, ppSubz
	
ENDM

pointpointSubtract MACRO a_x, a_y, a_z, b_x, b_y, b_z	; called by scalar and normal

	mov ax, a_x
	sub ax, b_x

	mov bx, a_y
	sub bx, b_y

	mov cx, a_z
	sub cx, b_z

	mov ppSubx, ax
	mov ppSuby, bx
	mov ppSubz, cx

ENDM

cross MACRO a_x, a_y, a_z, b_x, b_y, b_z	; called by normal

	mov ax, a_y
	imul ax, b_z

	mov bx, a_z
	imul bx, b_y

	sub ax, bx	; at this point ax contains the first component

	mov bx, a_z
	imul bx, b_x

	mov cx, a_x
	imul cx, b_z

	sub bx, cx	; at this point bx contains the second component

	mov cx, a_x
	imul cx, b_y

	mov dx, a_y
	imul dx, b_x
	
	sub cx, dx	; at this point cx contains the third component

	mov crossProduct1, ax
	mov crossProduct2, bx
	mov crossProduct3, cx

ENDM

dot MACRO a_x, a_y, a_z, b_x, b_y, b_z	; called by scalar

	mov ax, a_x
	imul ax, b_x

	mov bx, a_y
	imul bx, b_y

	add ax, bx

	mov bx, a_z
	imul bx, b_z

	add ax, bx

	mov dotProduct, ax

ENDM

.CODE                   	; start of main program code
_start: 

	getAllPoints
	normal
	scalar
	getParamXYZ
	output carriage
	printXYZ

        INVOKE  ExitProcess, 0  ; exit with return code 0

PUBLIC _start

END