.NOLIST      ; turn off listing
.386

EXTRN interpolate_proc : Near32

interpolate_macro	MACRO	pointArray, xCoord, degree

			lea ebx, pointArray
			push ebx
			push xCoord
			push degree
			call interpolate_proc
		
ENDM


.NOLISTMACRO ; suppress macro expansion listings
.LIST        ; begin listing