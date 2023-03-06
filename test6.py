# test6.py

# This program tests recursive functions on lists.

from MicroPythonListClass import MicroPythonList

def cons_my_list (r : int, my_list : MicroPythonList) -> MicroPythonList :
  if r <= 10 :
    my_list = my_list . cons (r)
    my_list = cons_my_list (r + 1, my_list);
  return my_list

def main () -> int :
  print (cons_my_list (int (input ()), MicroPythonList ()))
  return 0
