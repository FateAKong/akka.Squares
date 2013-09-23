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

5. --- BONUS ---
   For the bonus part, to be honest I didn't make it done. I am continuing debugging it. And right now when I deploy
   my remote actors to more than 5 boxes or so, it will crash on some of them. For now it costs like 5-6s to run
   "100000000 4" on 5 boxes.

   To view my bonus part code, please switch to 'bonus' branch using "git checkout bonus".

   Basically there are no much changes in the Squares.scala comparing to the local version. Almost all the remote part
   comes from the configuration file. Here I apply a router to deploy on remote nodes, which contains a bunch of routees
   whose hostname, port and daemon actor system are read from configuration files. Most of other configurations like
   listening port are set as default value.

   Also, I include a Daemon class to implement the daemon actor system as mentioned above. It is simple enough - only
   open up a port (like a server) and wait for Master actor (which is like a client) assigning Worker actors to run on it.

   Note that I use SBT for my bonus part to simplify the project dependency settings. So to run the program,
   - Daemon: "sbt run" -> choose the number corresponding to Daemon class
   - Squares: "sbt "run 100000000 4"" -> choose the number corresponding to Squares class

   That's it. I know this is not desired result for the bonus part, but I am keeping on debugging it...



And thank you for your great job!

