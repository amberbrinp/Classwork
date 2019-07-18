#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include "timer.h"
#include <string.h>
#include <errno.h>
#include <fcntl.h>

int thread_count;
int m, n;
double* x;
pthread_mutex_t input_lock;
pthread_mutex_t runtime_lock;
pthread_cond_t barrier;
int barrier_count = 0;
double runtime = 0;
sem_t* input_scheduler;
sem_t* output_scheduler;


double get_time();
void *Pth_mat_vect(void* rank);
void Read_vector(char* prompt, double x[], int n);
void Print_matrix(char* title, double A[], int m, int n);
void Print_vector(char* title, double y[], double m);

int main(int argc, char* argv[])
{
    thread_count = strtol(argv[1], NULL, 10);
    pthread_t* thread_handles = malloc(thread_count*sizeof(pthread_t));
    
    pthread_mutex_init(&input_lock, NULL); //initialize mutexes
    pthread_mutex_init(&runtime_lock, NULL);
    pthread_cond_init(&barrier, NULL);

    input_scheduler = malloc(thread_count*sizeof(sem_t)); //initialize & open semaphores
    output_scheduler = malloc(thread_count*sizeof(sem_t));
    errno = 0;
    input_scheduler = sem_open("/sem_input", O_CREAT | O_EXCL, S_IRUSR | S_IWUSR | S_IROTH | S_IWOTH, 1);
    output_scheduler = sem_open("/sem_output", O_CREAT | O_EXCL, S_IRUSR | S_IWUSR | S_IROTH | S_IWOTH, thread_count);
    if(output_scheduler == SEM_FAILED || input_scheduler == SEM_FAILED)//error checking
    {
        printf("Sem open failed\n");
        printf("Could not open semaphore! %d (%s)\n", errno, strerror(errno));
        sem_close(input_scheduler);
        sem_close(output_scheduler);
        sem_unlink("/sem_input");
        sem_unlink("/sem_output");
        return 0;
    }
    
    printf("Enter m and n\n");
    scanf("%d%d", &m, &n);

    x = malloc(n*sizeof(double));

    Read_vector("Enter the vector x", x, n);
    Print_vector("We read the vector:\n", x, n);
    printf("\nEnter the matrix\n");

    long thread;
    for (thread = 0; thread < thread_count; thread++)
        pthread_create(&thread_handles[thread], NULL, Pth_mat_vect, (void*) thread);
    
    for (thread = 0; thread < thread_count; thread++)
        pthread_join(thread_handles[thread], NULL);
    
    printf("Total runtime with A and y distributed = %e\n", runtime);

    sem_close(input_scheduler); //close and unlink semaphores
    sem_close(output_scheduler);
    sem_unlink("/sem_input");
    sem_unlink("/sem_output");

    pthread_mutex_destroy(&input_lock); //destroy mutexes
    pthread_mutex_destroy(&runtime_lock);
    pthread_cond_destroy(&barrier);
    
    free(thread_handles);
    free(x);
    runtime = 0;

    return 0;
}

void *Pth_mat_vect(void* rank)
{
    long my_rank = (long) rank;
    int i, j;
    int local_m = m/thread_count;
    double* local_A = malloc(local_m*n*sizeof(double));
    double* local_y = malloc(local_m*sizeof(double));

    //use a semaphore to schedule input of A
    if(my_rank-1 >= 0)
        sem_wait(input_scheduler);
    for (i = 0; i < local_m; i++)
        for (j = 0; j < n; j++)
            scanf("%lf", &local_A[i*n+j]);
    if(my_rank+1 < thread_count)
        sem_post(input_scheduler);
    
    //mutex barrier on input
    pthread_mutex_lock(&input_lock);
    barrier_count++;
    if (barrier_count == thread_count)
    {
        barrier_count = 0;
        pthread_cond_broadcast(&barrier);
    }
    else
    {
        while (pthread_cond_wait(&barrier, &input_lock) != 0);
    }
    pthread_mutex_unlock(&input_lock);
    
    //the actual multiplication
    double start = get_time();
    for (i = 0; i < local_m; i++)
    {
        local_y[i] = 0.0;
        for (j = 0; j < n; j++)
            local_y[i] += local_A[i*n + j]*x[j];
    }
    double stop = get_time();
    double local_runtime = stop - start;

    //mutex lock to protect global runtime
    pthread_mutex_lock(&runtime_lock);
    if (local_runtime > runtime)
    {
        runtime = local_runtime;
    }
    pthread_mutex_unlock(&runtime_lock);
        
    //schedule output of y using a semaphore
    if (my_rank + 1 == thread_count) sem_post(output_scheduler);
    sem_wait(output_scheduler);
    if (my_rank == 0) printf("The product is: \n");
    Print_vector("", local_y, local_m);
    fflush(stdout);//!!!!!!!!!!!!!!!!!!!!!!!! what does this do?
    if (my_rank+1 < thread_count)
        sem_post(output_scheduler);
    else 
        printf("\n");

    free(local_A);
    free(local_y);
    return NULL;
}

//----------------------- Utilities -------------------

void Read_vector(char* prompt, double x[], int n)
{
    int i;
    printf("%s\n", prompt);
    for (i = 0; i < n; i++)
        scanf("%lf", &x[i]);
}

void Print_matrix( char* title, double A[], int m, int n)
{
   int i, j;
   printf("%s\n", title);
   for (i = 0; i < m; i++)
   {
      for (j = 0; j < n; j++)
         printf("%4.1f ", A[i*n + j]);
      printf("\n");
   }
}

void Print_vector(char* title, double y[], double m)
{
   int i;
   printf("%s", title);
   for (i = 0; i < m; i++)
      printf("%4.1f ", y[i]);
}

double get_time() //modified from timer.h
{
    struct timeval t;
    gettimeofday(&t, NULL);
    return t.tv_sec + t.tv_usec/1000000.0;
}
