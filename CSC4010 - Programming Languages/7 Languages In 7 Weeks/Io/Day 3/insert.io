squareBrackets := method( call message arguments )
List squareBrackets := method( index, value, if( value == nil, at(index), atPut(index, value) ) )

myList := [4, 8, 12, 16, 20]
write(myList ..("\n") ..(myList[1]) ..("\n") ..(myList[1, 24]) ..("\n"))


