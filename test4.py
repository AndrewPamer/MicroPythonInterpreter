# test4.py

# This program tests non-recursive functions.

from MicroPythonListClass import MicroPythonList

def area (h : int, x : int, y : int) -> int :
  z = 2 * (x * y + (x * h) + y * h)
  return z

def main () -> int :
  a = int (input ())
  b = int (input ())
  h = int (input ())
  s = area (h, a, b)
  print (s)
  return 0
