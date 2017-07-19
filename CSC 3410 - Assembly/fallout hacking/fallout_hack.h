.NOLIST 
.386 
EXTRN hack : Near32 
	
fallout_hack MACRO list, passwordLength, numPasswords, guessIndex, numMatches

	IFB <list>
		.ERR <missing "list" operand in fallouthack> 
	ELSEIFB <passwordLength> 
		.ERR <missing "passwordLength" operand in fallouthack> 
	ELSEIFB <numPasswords> 
		.ERR <missing "numPasswords" operand in fallouthack>
	ELSEIFB <guessIndex> 
		.ERR <missing "guessIndex" operand in fallouthack> 
	ELSEIFB <numMatches> 
		.ERR <missing "numMatches" operand in fallouthack>
 
	ELSE
		push eax 
		push ebx
		push edx
	
		lea ebx, list
		push ebx 
		push passwordLength 
		push numPasswords 
		push guessIndex 
		push numMatches 

		call hack 
		
		pop edx
		pop ebx
		pop eax 
		
	ENDIF 
ENDM 

.NOLISTMACRO 
.LIST
