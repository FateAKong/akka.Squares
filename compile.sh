
#!/bin/bash

if [ -d "out/" ]; then
  rm -f out/*.class
else
  mkdir out
fi
echo "compiling source (may take long, please wait...)"
scalac -cp "lib/*" -d "out/"  src/Squares.scala
