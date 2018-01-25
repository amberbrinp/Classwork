List2d := List clone

dimension := method( x, y,
                    list := List2d clone
                    Range 1 to(y) foreach(i, list append(Range 1 to(x) map(j, 0)))
                    list)

List2d set := method(x, y, value, self at(y) atPut(x, value))
List2d get := method(x, y, self at(y) at(x))

myList := dimension(3, 4)
myList set(2, 3, 7)

write("Using 2D list: \n" ..(myList) ..("\n") ..("Element at 2, 3: ") ..(myList get(2,3)) ..("\n"))

List2d transpose := method( ySize := self size
                            xSize := self first size
                            list := dimension(ySize, xSize)
                            for (y, 0, ySize - 1,
                            for(x, 0, xSize - 1, list set(y, x, self get(x, y))))
                            list)

write("Transposed list: \n" ..(myList transpose) ..("\n"))

File with("Matrix.txt") open write(myList serialized) close

myFile := File with("Matrix.txt") open
myList := doString(myFile readToEnd)
myFile close

write("Read from file: \n" ..(myList) ..("\n"))
