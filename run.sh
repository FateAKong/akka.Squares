#!/bin/bash

if [ ! -d "out" ] || [ ! -f "./out/Squares.class" ]; then
  echo "please compile first"
else
  cd out
  scala -cp .:"../lib/*" Squares $1 $2
fi
