# test5.py

# This program tests recursive functions.

from MicroPythonListClass import MicroPythonList

def facto (x : int) -> int :
  if x == 1 :
    s = 1;
  else :
    s = x * facto (x - 1);
  return s

def main () -> int :
  print (facto (int (input ())))
  return 0
