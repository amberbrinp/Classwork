Number divide := Number getSlot("/")
Number / := method( denom, if( denom == 0, 0, divide(denom) ) )

//write("10 / 0: " ..((10 / 0)) ..("\n"))
//for some reason the above will cause an error, but the below works fine

"10 / 0 : " print
(10 / 0) println
