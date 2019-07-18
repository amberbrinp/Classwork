1) Why is no mutex needed in this assignment's barrier implementation?

The different message types and if-else which handles the message at each process effectively replaces the
mutex. The behavior of a process is dependent on the type of message it receives, instead of relying on the 
status of the mutex.


2) Describe why the MPI implementation is more scalable than this assignment's implementation.

The MPI_Barrier implementation sends less messages than this one. Instead of sending the barrier count in a reply to each new process that enters the barrier and letting that process decide whether or not to reset, the master waits until everyone is in the barrier, and then sends everyone the command to reset. The MPI_Barrier has no setup cost and less overhead than our implementation.
