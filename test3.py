# test3.py

# This program tests list comparison operations.

from MicroPythonListClass import MicroPythonList

def main () -> int :
  my_list0 = MicroPythonList ()
  my_list1 = my_list0 . cons (46)
  my_list2 = my_list1 . cons (578)
  my_list3 = my_list2 . cons (405)
  my_list4 = my_list3 . cons (918)
  my_list5 = my_list4 . cons (47)
  my_list6 = my_list5 . cons (198)
  my_list  = my_list6 . cons (14)
  print (my_list)
  target = 405
  position = 0
  while not my_list . null () and my_list . head () != target :
    my_list = my_list . tail ()
    position = position + 1;
  if my_list . null () :
    print (-1);
  else :
    print (position);
  return 0

