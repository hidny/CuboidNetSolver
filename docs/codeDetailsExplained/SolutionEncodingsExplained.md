
## Solution Encodings

How to read the solution encoding in this Cuboid repo: (the one in weirdMathStuff repo is slightly different)
It's a binary encoding of this form:
(start bits)
(8 bits for height)
(8 bits for width)
(width * height 0s and 1s that represent if the cell in the grid is used.


- Note that the encoding in weirdMathStuff has the width go first... I switched it at the last minute.

Example from the output folder:
Unique solution found:
```
###.|
.##.|
..##|
...#|
..##|
.##.|
##..|
#...|
##..|
.##.|
.###|
(...)
Solution code: 3476976291191831580
```

solution code in binary: (from wolframalpha)
11000001000000101100111000001111011000111100011011110000011100


Dividing the binary into the different pieces:
11 (first 2 1s are always there)

00000100 (height is 4)
00001011 (width is 11)

00111000001 (make a 4 x 11 grid out of the rest of the bits)
11101100011
11000110111
10000011100

Convert the ones to cells and the zeros to blank to get the answer:
Final answer:
```
..###.....#
###.##...##
##...##.###
#.....###..
```

Note that it's the same answer, but rotated.


How to write the encoding:

Set it up the way it's described and remember to do two extra things:
1) Make sure that the dimensions are as small as possible (no padding allowed)
2) Make sure that the solution is rotated and reflected in a way that minimizes the solution code. (This takes care of symmetric solutions) (I think the optimized code in weirdmathstuff gets the max value instead. I'm sorry about the inconsistency.)

