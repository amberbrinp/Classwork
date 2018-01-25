distributedArray.c:
    compile: gcc -g -Wall -o distributedArray distributedArray.c -lpthread
    run: ./distributedArray <thread count>

threadruntime.c:
    compile: gcc -g -Wall -o threadRuntime threadRuntime.c -lpthread
    run: ./threadRuntime <thread count>
    notes:
        Modified from pth_mat_vect.c.
        Number of threads (thread_count) should evenly divide INTO both m and n.
        The program doesn't check for this.

1D_2D_Comparison.c:
    compile: gcc -g -Wall -o 1D_2D_Comparison 1D_2D_Comparison.c -lpthread
    run: ./1D_2D_Comparison <thread count>
    notes:
        Modified from pth_mat_vect.c.
        Number of threads (thread_count) should evenly divide INTO both m and n.
        The program doesn't check for this.
