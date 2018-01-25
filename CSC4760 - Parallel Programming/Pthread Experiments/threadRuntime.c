/*
The average runtime is inconsistent for multiple runs of the program with the same number of threads,
but overall it doesn't seem like the time to create a thread varies significantly
with the number of threads created.
*/

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <sys/time.h>

int thread_count;
void* foo (void* rank);
double get_time();

int main(int argc, char* argv[])
{
    int iterations = 4000; //the amount of times to run the experiment to get the average
    thread_count = strtol(argv[1], NULL, 10);
    pthread_t* thread_handles = malloc(thread_count * sizeof(pthread_t));
    
    double start = get_time();
    int i;
    for(i = 0; i < iterations; i++) //create and join thread_counts threads a bunch of times
    {
        long thread;
        for(thread = 0; thread < thread_count; thread++)
        {
            pthread_create(&thread_handles[thread], NULL, foo, (void*) thread);
        }
        
        for(thread = 0; thread < thread_count; thread++)
        {
            pthread_join(thread_handles[thread], NULL);
        }
    }
    double stop = get_time();
    double time = ((stop - start) / iterations); //get the average time to create all the the threads
    double time_single = time / thread_count; //get the average time to create a single thread
    
    printf("The average runtime to create and terminate 1 of %d threads is %e seconds.\nThe average time to create %d threads is %e seconds.\nAverage taken from  %d iterations.\n", thread_count, time_single, thread_count, time, iterations);
    
    free(thread_handles);
    return 0;
}

void* foo(void* rank)
{
    return rank;
}

double get_time() //modified from timer.h, basically so i didn't have to keep track of it
{
    struct timeval t;
    gettimeofday(&t, NULL);
    return t.tv_sec + t.tv_usec/1000000.0;
}
