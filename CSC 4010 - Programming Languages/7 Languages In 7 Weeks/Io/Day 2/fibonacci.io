fib := method( num, if( num < 3, 1, fib(num - 1) + fib(num - 2) ) )

write("Fibonacci of 10: " ..(fib(10)) ..("\nFibonacci of 4: ") ..(fib(4)) ..("\nFibonacci of 2: ") ..(fib(2)) ..("\nFibonacci of 1: ") ..(fib(1)) ..("\n"))
