#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/time.h>

int thread_count;
int m, n;
double* A_1D;
double A_2D[1024][1024]; //ok
double* x;
double* y;

double get_time();
void Read_matrix(char* prompt, double A[], double A_2D[][1024], int m, int n);
void Read_vector(char* prompt, double x[], int n);
void Print_matrix(char* title, double A[], int m, int n);
void Print_vector(char* title, double y[], double m);
void* Pth_mat_vect_1D(void* rank);
void* Pth_mat_vect_2D(void* rank);

int main(int argc, char* argv[])
{
    thread_count = atoi(argv[1]);
    pthread_t* thread_handles = malloc(thread_count*sizeof(pthread_t));
    
    printf("Enter m and n\n");
    scanf("%d%d", &m, &n);
    
    A_1D = malloc(m*n*sizeof(double));
    x = malloc(n*sizeof(double));
    y = malloc(m*sizeof(double));
    
    Read_matrix("Enter the matrix", A_1D, A_2D, m, n);
    Print_matrix("We read", A_1D, m, n);
    
    Read_vector("Enter the vector", x, n);
    Print_vector("We read", x, n);
    
    long thread;
    double start_1D = get_time();
    for (thread = 0; thread < thread_count; thread++)
        pthread_create(&thread_handles[thread], NULL,
                       Pth_mat_vect_1D, (void*) thread);
    
    for (thread = 0; thread < thread_count; thread++)
        pthread_join(thread_handles[thread], NULL);
    double stop_1D = get_time();
    double time_1D = stop_1D - start_1D;
    
    Print_vector("The product with a 1D array is:", y, m);
    
    double start_2D = get_time();
    for (thread = 0; thread < thread_count; thread++)
        pthread_create(&thread_handles[thread], NULL,
                       Pth_mat_vect_2D, (void*) thread);
    
    for (thread = 0; thread < thread_count; thread++)
        pthread_join(thread_handles[thread], NULL);
    double stop_2D = get_time();
    double time_2D = stop_2D - start_2D;
    
    Print_vector("The product with a 2D array is:", y, m);
    printf("Runtime with a 1D array: %e.\nRuntime with a 2D array: %e.\n", time_1D, time_2D);
    
    free(A_1D);
    free(x);
    free(y);
    
    return 0;
}  /* main */

void* Pth_mat_vect_1D(void* rank)
{
    long my_rank = (long) rank;
    int i;
    int local_m = m / thread_count;
    int my_first_row = my_rank * local_m;
    int my_last_row = (my_rank+1) * local_m - 1;
    for (i = my_first_row; i <= my_last_row; i++)
    {
        y[i] = 0.0;
        int j;
        for (j = 0; j < n; j++)
        {
            y[i] += A_1D[i*n+j] * x[j];
        }
    }
    return NULL;
} /* 1D array */

void* Pth_mat_vect_2D(void* rank)
{
    long my_rank = (long) rank;
    int i, j;
    int local_m = m/thread_count;
    int my_first_row = my_rank*local_m;
    int my_last_row = (my_rank+1)*local_m - 1;
    
    for (i = my_first_row; i <= my_last_row; i++)
    {
        y[i] = 0.0;
        for (j = 0; j < n; j++)
        {
            y[i] += A_2D[i][j] * x[j];
        }
    }
    return NULL;
 } /* 2D array */

//--------- Utilities (mostly) copied from pth_mat_vect.c: ---------

/*------------------------------------------------------------------
 * Function:    Read_matrix
 * Purpose:     Read in the matrix
 * In args:     prompt, m, n
 * Out arg:     A
 */
void Read_matrix(char* prompt, double A_1D[], double A_2D[][1024], int m, int n)
{
    printf("%s\n", prompt);
    
    int i, j;
    for (i = 0; i < m; i++)
    {
        for (j = 0; j < n; j++)
        {
            scanf("%lf", &A_1D[i*n+j]);
            A_2D[i][j] = A_1D[i*n+j];
        }
    }
    
}  /* Read_matrix */

/*------------------------------------------------------------------
 * Function:        Read_vector
 * Purpose:         Read in the vector x
 * In arg:          prompt, n
 * Out arg:         x
 */
void Read_vector(char* prompt, double x[], int n) {
    int   i;
    
    printf("%s\n", prompt);
    for (i = 0; i < n; i++)
        scanf("%lf", &x[i]);
}  /* Read_vector */


/*------------------------------------------------------------------
 * Function:    Print_matrix
 * Purpose:     Print the matrix
 * In args:     title, A, m, n
 */
void Print_matrix( char* title, double A[], int m, int n) {
    int   i, j;
    
    printf("%s\n", title);
    for (i = 0; i < m; i++) {
        for (j = 0; j < n; j++)
            printf("%4.1f ", A[i*n + j]);
        printf("\n");
    }
}  /* Print_matrix */


/*------------------------------------------------------------------
 * Function:    Print_vector
 * Purpose:     Print a vector
 * In args:     title, y, m
 */
void Print_vector(char* title, double y[], double m) {
    int   i;
    
    printf("%s\n", title);
    for (i = 0; i < m; i++)
        printf("%4.1f ", y[i]);
    printf("\n");
}  /* Print_vector */

double get_time() //modified from timer.h
{
    struct timeval t;
    gettimeofday(&t, NULL);
    return t.tv_sec + t.tv_usec/1000000.0;
}
