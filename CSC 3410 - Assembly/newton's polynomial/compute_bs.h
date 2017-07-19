.NOLIST      ; turn off listing
.386

EXTRN compute_bs: Near32

compute_bs_macro MACRO n, pointArray

	push n
	push pointArray
	call compute_bs
	
ENDM

.NOLISTMACRO ; suppress macro expansion listings
.LIST        ; begin listing