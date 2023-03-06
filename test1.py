# test1.py

# This program tests basic arithmetic and the while loop.

from MicroPythonListClass import MicroPythonList

def main () -> int :
  x = int (input ())
  y = int (input ())
  q = 0  
  r = x
  while r >= y :
    q = q + 1
    r = r - y;
  print (q)
  print (r)
  return 0
