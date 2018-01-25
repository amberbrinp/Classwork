List arraySum := method( reduce(total, row, total + row sum, 0) )

myList := list( list( 7, 12, 19 ), list( 54, 6, 13 ))
write("Using 2D list: \n7 12 19\n54 6 13\nThe sum is: " ..(myList arraySum) ..("\n"))
