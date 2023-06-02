## Solution Resolver

The solutions resolver code is just code that handles what to do after the main program in DFSIntersectFinder2.java finds a solution.

Its job is (normally) to determine if the solution is unique and keep track of the number of solutions.
I made an interface for it because I've  played around with having multiple different versions where I vary its verbosity,
 and did other things with it like make it keep track of the count of solutions while not recording the solutions.
(That's how I found that there are 407,023,305 solutions for the 7x1x1 cuboid)

In the future, I think I might have a custom resolver to help me estimate the number of nets the can fold into 2 different cuboids.