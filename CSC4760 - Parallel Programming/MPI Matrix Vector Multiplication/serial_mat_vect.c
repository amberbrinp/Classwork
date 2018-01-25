/* File:    serial_mat_vect.c 
 *
 * Purpose: Computes a matrix-vector product on a single processor.
 *
 * Compile: gcc -g -Wall -o serial_mat_vect serial_mat_vect.c
 * Run:     ./serial_mat_vect <seed>
 * Warning: I'm not checking to make sure you input a seed!
 *
 * Input:   m, n: order of matrix
 *          A, x:  the matrix and the vector to be multiplied
 *
 * Output:  y: the product vector
 *
 * Note:    A is stored as a 1-dimensional array.
 */
#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>

void Generate_matrix(float A[], int m, int n, int seed);
void Generate_vector(float A[], float  v[], int n);

void Serial_matrix_vector_prod(float A[], int m, int n, float x[], float y[]);
float serial_vector_sum(float y[], int n);
double get_time();

void Print_matrix(char* title, float A[], int m, int n);
void Print_vector(char* title, float y[], int n);
    
int main(int argc, char **argv)
{
    float*    A; 
    float*    x;
    float*    y;
    int       n;
    int seed = atoi(argv[1]);

    printf("Enter the order of the matrix (n x n)\n");
    scanf("%d", &n);
    
    A = malloc(n*n*sizeof(float));
    x = malloc(n*sizeof(float));
    y = malloc(n*sizeof(float));
    
    double start = get_time();
    Generate_matrix(A, n, n, seed);
    Generate_vector(A, x, n);
    double stop = get_time();
    double runtime = stop - start;
    
    Print_matrix("We read", A, n, n);
    Print_vector("We read", x, n);

    start = get_time();
    Serial_matrix_vector_prod(A, n, n, x, y);
    float R = serial_vector_sum(y, n);
    stop = get_time();
    runtime += stop - start;
    
    Print_vector("Resulting vector is:", y, n);
    printf("Resulting vector sum is %f. \n", R);
    printf("Runtime for calculation was %f. \n", runtime);

    free(A);
    free(x);
    free(y);
    return 0;
}  /* main */

/*---------------------------------------------------------------
 * Function:  Generate_matrix
 * Purpose:   Read a matrix from stdin
 * In args:
 *            m:  number of rows
 *            n:  number of cols
 *            seed: the seed by which to generate the matrix
 * Out arg:   A:  the matrix
 */
void Generate_matrix(float A[], int m, int n, int seed)
{
    int i, j;
    for (i = 0; i < m; i++)
        for (j = 0; j < n; j++)
            A[i*n + j] = (seed + (i*n) + (j+1)) % 10; //what's the point of doing [0-9] mod 10??
}  /* Generate_matrix */


/*---------------------------------------------------------------
 * Function: Generate_vector
 * Purpose:  Read a vector from stdin
 * In args:  A:  matrix to blatantly read the first column of
 *           n:  number of components in vector
 * Out arg:  v:  vector
 */
void Generate_vector(float A[], float  v[], int n)
{
    int i;
    for (i = 0; i < n; i++)
        v[i] = A[i]; //is that cheating? i'm tired
}  /* Generate_vector */


/*---------------------------------------------------------------
 * Function:  Serial_matrix_vector_prod
 * Purpose:   Find a matrix-vector product
 * In args:   A:  the matrix
 *            m:  the number of rows in A (and entries in y)
 *            n:  the number of cols in A (and entries in x)
 *            x:  the vector being multiplied by A
 * Out args:  y:  the product vector Ax
 */
void Serial_matrix_vector_prod(float A[], int m, int n, float x[], float y[])
{
    int i, j;
    for (i = 0; i < m; i++)
    {
        y[i] = 0.0;
        for (j = 0; j < n; j++)
            y[i] += A[i*n + j]*x[j];
    }
}  /* Serial_matrix_vector_prod */

float serial_vector_sum(float y[], int n)
{
    int i;
    float R = 0;
    for (i = 0; i < n; i++)
    {
        R += y[i];
    }
    return R;
}

double get_time() //modified from timer.h
{
    struct timeval t;
    gettimeofday(&t, NULL);
    return t.tv_sec + t.tv_usec/1000000.0;
}

/*---------------------------------------------------------------
 * Function:  Print_matrix
 * Purpose:   Print a matrix
 * In args:   title:  what we're printing
 *            A:  the matrix
 *            m:  the number of rows
 *            n:  the number of cols
 */
void Print_matrix(
         char*     title   /* in  */,
         float     A[]     /* out */,
         int       m       /* in  */,
         int       n       /* in  */) {
    int i, j;

    printf("%s \n", title);
    for (i = 0; i < m; i++) {
        for (j = 0; j < n; j++)
            printf("%4.1f ", A[i*n + j]);
        printf("\n");
    }
}  /* Print_matrix */


/*---------------------------------------------------------------
 * Function:  Print_vector
 * Purpose:   Print a vector
 * In args:   title:  what we're printing
 *            y:  the vector
 *            n:  the number of components in y
 */
void Print_vector(
         char*  title  /* in */,
         float  y[]    /* in */,
         int    n      /* in */) {
    int i;

    printf("%s\n", title);
    for (i = 0; i < n; i++)
        printf("%4.1f ", y[i]);
    printf("\n");
}  /* Print_vector */

