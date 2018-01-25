/* File:     parallel_mat_vect.c 
 *
 * Purpose:  Computes a parallel matrix-vector product.  Matrix
 *           is distributed by block rows.  Vectors are distributed 
 *           by blocks.
 *
 * Input:
 *     m, n: order of matrix
 *     A, x: the matrix and the vector to be multiplied
 *
 * Output:
 *     y:    the product vector
 *
 * Compile:  mpicc -g -Wall -o parallel_mat_vect parallel_mat_vect.c
 * Run:      mpiexec -n <number of processes> parallel_mat_vect <seed> <mod>
 *
 * Notes:  
 *     1.  Local storage for A, x, and y is dynamically allocated.
 *     2.  Number of processes (p) should evenly divide both m and n.
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <mpi.h>
#include <sys/time.h>

void Generate_matrix(int seed, int mod, float local_A[], int local_m, int n, int my_rank, int p, MPI_Comm comm);
void Generate_vector(int seed, int mod, float local_x[], int local_n, int my_rank, int p, MPI_Comm comm);

void Print_matrix(char* title, float local_A[], int local_m, int n, int my_rank, int p, MPI_Comm comm);
void Print_vector(char* title, float local_y[], int local_m, int my_rank, int p, MPI_Comm comm);

void Parallel_matrix_vector_prod(float local_A[], int m, int n, float local_x[], float global_x[], float local_y[], int local_m, int local_n, MPI_Comm comm);
double get_time();
void parallel_vector_sum(float local_y[], int local_m, int p, int my_rank, MPI_Comm comm);

int main(int argc, char **argv)
{
    int             my_rank;
    int             p;
    float*          local_A; 
    float*          global_x;
    float*          local_x;
    float*          local_y;
    int             m, n;
    int             local_m, local_n;
    int seed = atoi(argv[1]);
    int mod = atoi(argv[2]);
    MPI_Comm        comm;

    MPI_Init(NULL, NULL);
    comm = MPI_COMM_WORLD;
    MPI_Comm_size(comm, &p);
    MPI_Comm_rank(comm, &my_rank);

    if (my_rank == 0)
    {
        printf("Enter the order of the matrix (n x n)\n");
        scanf("%d", &n);
        if (n % p != 0)
        {
            printf("Error. Matrix dimension n (%d provided) has to be divisible by number of processes p (%d provided).\n", n, p);
            return 1;
        }
        m = n;
    }
    double start = get_time();
    MPI_Bcast(&m, 1, MPI_INT, 0, comm);
    MPI_Bcast(&n, 1, MPI_INT, 0, comm);
    double stop = get_time();
    double local_runtime = stop - start;

    local_m = m/p;
    local_n = n/p;

    local_A = malloc(local_m*n*sizeof(float));
    local_x = malloc(local_n*sizeof(float));
    local_y = malloc(local_m*sizeof(float));
    global_x = malloc(n*sizeof(float));
    
    start = get_time();
    Generate_matrix(seed, mod, local_A, local_m, n, my_rank, p, comm);
    stop = get_time();
    local_runtime += stop - start;
    Print_matrix("We read", local_A, local_m, n, my_rank, p, comm);

    start = get_time();
    Generate_vector(seed, mod, local_x, local_n, my_rank, p, comm);
    stop = get_time();
    local_runtime += stop - start;
    Print_vector("We read", local_x, local_n, my_rank, p, comm);

    start = get_time();
    Parallel_matrix_vector_prod(local_A, m, n, local_x, global_x,  local_y, local_m, local_n, comm);
    stop = get_time();
    local_runtime += stop - start;
    
    Print_vector("The product is:", local_y, local_m, my_rank, p, comm);
    parallel_vector_sum(local_y, local_m, p, my_rank, comm);
    
    if (my_rank != 0)
    {
        MPI_Send(&local_runtime, 1, MPI_DOUBLE, 0, 0, comm);
    }
    else
    {
        double total_runtime = local_runtime;
        local_runtime = 0;
        int source;
        for (source = 1; source < p; source++)
        {
            MPI_Recv(&local_runtime, 1, MPI_DOUBLE, source, 0, comm, MPI_STATUS_IGNORE);
            total_runtime += local_runtime;
        }
        printf("The runtime for the calculations and MPI calls was %f.\n", total_runtime);
    }

    free(local_A);
    free(local_x);
    free(local_y);
    free(global_x);

    MPI_Finalize();

    return 0;
}  /* main */


/*--------------------------------------------------------------------
 * Function:  Generate_matrix
 * Purpose:   Read an m x n matrix from stdin and distribute it by
 *            block rows
 * In args:   prompt:  tell user to enter matrix
 *            local_m: number of local rows
 *            n:       number of columns
 *            my_rank, p, comm:  usual MPI variables
 * Out arg:   local_A: block of rows assigned to this process
 */
void Generate_matrix(int seed, int mod, float local_A[], int local_m, int n, int my_rank, int p, MPI_Comm comm)
{
    int     i, j;
    float*  temp = NULL;

    if (my_rank == 0) //construct the matrix on rank 0
    {
        temp = (float*) malloc(local_m*p*n*sizeof(float));

        for (i = 0; i < p*local_m; i++) 
            for (j = 0; j < n; j++)
                temp[i*n+j] = (seed + (i*n) + (j+1)) % mod;
        MPI_Scatter(temp, local_m*n, MPI_FLOAT, local_A, local_m*n, MPI_FLOAT, 0, comm);
        free(temp);
    }
    else
    {
        MPI_Scatter(temp, local_m*n, MPI_FLOAT, local_A, local_m*n, MPI_FLOAT, 0, comm);
    }
}  /* Generate_matrix */


/*--------------------------------------------------------------------
 * Function:  Generate_vector
 * Purpose:   Read a vector from stdin and distribute it by blocks
 * In args:   prompt:  tell the user what to enter
 *            local_n:  the number of components going to each process
 *            my_rank, p, comm:  the usual MPI variables
 * Out arg:   local_x:  the block of the vector assigned to this process
 */
void Generate_vector(int seed, int mod, float local_x[], int local_n, int my_rank, int p, MPI_Comm comm)
{

    int    i;
    float* temp = NULL;

#   ifdef DEBUG
    printf("Proc %d > local_n = %d, p = %d\n", my_rank, local_n, p);
    fflush(stdout);
#   endif

    if (my_rank == 0) //construct the vector on rank 0
    {
        temp = malloc(local_n*p*sizeof(float));

        for (i = 0; i < p*local_n; i++) 
            temp[i] = (seed + i + 1) % mod;
#       ifdef DEBUG
        printf("Proc 0 > input vector = ");
        for (i = 0; i < p*local_n; i++) 
            printf("%.1f ", temp[i]);
        printf("\n");
#       endif
        MPI_Scatter(temp, local_n, MPI_FLOAT, local_x, local_n, MPI_FLOAT, 0, comm); //do i need to replace this with broadcast?
        free(temp);
    }
    else
    {
        MPI_Scatter(temp, local_n, MPI_FLOAT, local_x, local_n, MPI_FLOAT, 0, comm); // do i need to replace this with broadcast?
    }

}  /* Generate_vector */


/*--------------------------------------------------------------------
 * Function:  Parallel_matrix_vector_prod
 * Purpose:   Multiply a matrix distributed by block rows by a vector
 *            distributed by blocks 
 * In args:   local_A:  my rows of the matrix A
 *            m:  the number of rows in the global matrix A
 *            n:  the number of columns in A
 *            local_x:  my components of the vector x
 *            local_m:  the number of rows in my block of A
 *            local_n:  the number of components in my block of x
 *            comm:  communicator for call to MPI_Allgather
 * Out arg:   local_y:  my components of the product vector Ax
 * Scratch:   global_x:  temporary storage for all of vector x
 * Note:      argument m is unused              
 */
void Parallel_matrix_vector_prod(float local_A[], int m, int n, float local_x[], float global_x[], float local_y[], int local_m, int local_n, MPI_Comm comm)
{
    /* local_m = m/p, local_n = n/p */
    int local_i, j;

    MPI_Allgather(local_x, local_n, MPI_FLOAT, global_x, local_n, MPI_FLOAT, comm);
    for (local_i = 0; local_i < local_m; local_i++)
    {
        local_y[local_i] = 0.0;
        for (j = 0; j < n; j++)
            local_y[local_i] += local_A[local_i*n+j]*global_x[j];
    }
}  /* Parallel_matrix_vector_prod */

double get_time() //modified from timer.h
{
    struct timeval t;
    gettimeofday(&t, NULL);
    return t.tv_sec + t.tv_usec/1000000.0;
}

void parallel_vector_sum(float local_y[], int local_m, int p, int my_rank, MPI_Comm comm)
{
    int i;
    float* temp = NULL;

    if (my_rank == 0)
    {
        temp = malloc(local_m*p*sizeof(float));
        MPI_Gather(local_y, local_m, MPI_FLOAT, temp, local_m, MPI_FLOAT, 0, MPI_COMM_WORLD);
        float R = 0;
        for (i = 0; i < p*local_m; i++)
            R += temp[i];
        free(temp);
        printf("The resulting vector sum is %f.\n", R);
    }
    else
    {
        MPI_Gather(local_y, local_m, MPI_FLOAT, temp, local_m, MPI_FLOAT, 0, MPI_COMM_WORLD);
    }
}

/*void parallel_vector_sum(float local_y[], int local_m, int p, int my_rank, MPI_Comm comm)
{
    if (my_rank != 0)
    {
        MPI_Send(&local_y, 1, MPI_DOUBLE, 0, 0, comm);
    }
    else
    {
        float* temp = NULL;
        temp[0] = local_y;
        int source;
        for (source = 1; source < p; source++)
        {
            MPI_Recv(&local_runtime, 1, MPI_DOUBLE, source, 0, comm, MPI_STATUS_IGNORE);
            total_runtime += local_runtime;
        }
        printf("The runtime for the calculations was %f.\n", total_runtime);
    }
}*/

/*--------------------------------------------------------------------*/
void Print_matrix(
         char*      title      /* in */, 
         float      local_A[]  /* in */, 
         int        local_m    /* in */, 
         int        n          /* in */,
         int        my_rank    /* in */,
         int        p          /* in */,
         MPI_Comm   comm       /* in */) {

    int   i, j;
    float* temp = NULL;

    if (my_rank == 0)
    {
        temp = malloc(local_m*p*n*sizeof(float));
        MPI_Gather(local_A, local_m*n, MPI_FLOAT, temp, local_m*n, MPI_FLOAT, 0, comm);
        printf("%s\n", title);
        for (i = 0; i < p*local_m; i++)
        {
            for (j = 0; j < n; j++)
                printf("%4.1f ", temp[i*n + j]);
            printf("\n");
        }
        free(temp);
    } else {
        MPI_Gather(local_A, local_m*n, MPI_FLOAT, temp, local_m*n, MPI_FLOAT, 0, comm);
    }
}  /* Print_matrix */


/*--------------------------------------------------------------------*/
void Print_vector(
         char*    title      /* in */, 
         float    local_y[]  /* in */, 
         int      local_m    /* in */, 
         int      my_rank    /* in */,
         int      p          /* in */,
         MPI_Comm comm       /* in */)
    {

    int    i;
    float* temp = NULL;


    if (my_rank == 0)
    {
        temp = malloc(local_m*p*sizeof(float));
        MPI_Gather(local_y, local_m, MPI_FLOAT, temp, local_m, MPI_FLOAT,
           0, MPI_COMM_WORLD);
        printf("%s\n", title);
        for (i = 0; i < p*local_m; i++)
            printf("%4.1f ", temp[i]);
        printf("\n");
        free(temp);
    }
    else
    {
        MPI_Gather(local_y, local_m, MPI_FLOAT, temp, local_m, MPI_FLOAT,
           0, MPI_COMM_WORLD);
    }
}  /* Print_vector */
