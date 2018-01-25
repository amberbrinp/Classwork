numTries := 10
guess := nil
numToGuess := (Random value * 100) floor
stdio := File standardInput

while(numTries > 0 and guess != numToGuess,
write("You can guess " ..(numTries asNumber) ..(" more times. Enter your next guess: "))
guess := stdio readLine asNumber
numTries = numTries - 1
)

if( guess == numToGuess, write("Correct!\n"), write("You failed. The number was: " ..(numToGuess) ..("\n")) )
