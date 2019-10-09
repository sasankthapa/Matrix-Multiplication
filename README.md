# Matrix-Multiplication
Matrix Multiplication using multiple threads

Matrix A * Matrix B = Matrix C

Matrix is stored using multidimensional arrays.

Uses 3 different threads that solve 5 rows of data each time, using a barrier to synchronize the calculations.

Bugs to fix:
-Program doesn't end all the threads incase of a thread finishing earlier than 5 rows.
