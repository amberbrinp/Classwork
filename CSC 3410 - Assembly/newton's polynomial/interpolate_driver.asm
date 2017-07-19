.386
.MODEL FLAT

ExitProcess PROTO NEAR32 stdcall, dwExitCode:DWORD

.STACK 4096

INCLUDE io.h
INCLUDE debug.h
INCLUDE float.h
INCLUDE sort_points.h
INCLUDE compute_bs.h
INCLUDE interpolate.h



CR      	EQU     0Dh     	; carriage return ASCII code
LF      	EQU     0Ah     	; line feed


.DATA

xCoord			REAL4	?
points			REAL4	40 DUP(?)
tol				REAL4	0.00001
degree			WORD	?
numPoints		WORD	?
line			BYTE	LF, CR, 0
XPrompt			BYTE	"Enter the x-coordinate of the desired interpolated y.", CR, LF, 0
DegreePrompt	BYTE	"Enter the degree of the interpolating polynomial.", CR, LF, 0
PointsPrompt	BYTE	"You may enter up to 20 points, one at at time.", CR, LF, "Input q to quit.", CR, LF, 0



.CODE
_start:

output XPrompt
input text, 8
atof text, xCoord
output text
output line

inputW DegreePrompt, degree
output text
output line

output PointsPrompt
mov cx, 0
lea ebx, points
inputPoints:

	input text, 8
	output text
	output line
	cmp text, 'q' 
	je endInputPoints
	
	atof text, REAL4 PTR [ebx]
	inc cx
	add ebx, 4
	cmp cx, 20
	je endInputPoints
	jmp inputPoints

endInputPoints:
	mov ax, cx
	mov cx, 2
	cwd
	div cx
	mov numPoints, ax
	
	output line
	sort_points points, xCoord, tol, numPoints
	print_points points, numPoints
	
	interpolate_macro points, xCoord, degree
	fst tol
	ftoa tol, 5, 11, text
	output line
	output text

INVOKE  ExitProcess, 0
PUBLIC _start
END