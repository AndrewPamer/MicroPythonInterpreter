# test2.py 

# This program tests list manipulation operators.

from MicroPythonListClass import MicroPythonList

def main () -> int :
  myList0 = MicroPythonList ()
  myList1 = myList0 . cons (7)
  myList2 = myList1 . cons (5)
  myList3 = myList2 . cons (3)
  result = (12 * 3 + 5) + myList3 . tail () . head ()
  print (result)
  return 0
