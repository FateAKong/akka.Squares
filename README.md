To compile and/or run the program, input "./compile.sh" and/or "./run.sh N k" under bash

1. Size of work unit
   Here is an average performance comparison among different work unit sizes:
   (Test is based on the average time command output on input "1000000 4")
   
   Number of Unit 	Size of Unit	Real Time 		CPU/Real Ratio
   2 				500000			0:03.45 		152.6%
   4				250000			0:03.30 		165.1%
   8				125000			0:03.52 		161.3%
   16 				62500			0:03.80 		145.3%
   32				31250			0:03.71 		160.7%
   64				15625			0:03.57 		151.0%
   
   Based on the data above, I believe that 4 work units work best here

2. There is no output for this input combination

3. Here is the output of executing "time ./run.sh 1000000 4"
   5.328u 0.204s 0:03.38 163.3%    0+0k 0+64io 0pf+0w

4. I can solve an input size like "100000000 4" or "5000000 24" 