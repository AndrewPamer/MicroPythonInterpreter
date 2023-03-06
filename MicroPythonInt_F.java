// MicroPythonInt.java

// This program is a recursive descent parser for MicroPython.

import java.io.*;
import java.util.Scanner;

public class MicroPythonInt_F {

  public static void main (String args []) throws java.io.IOException {

    System . out . println ("Source Program");
    System . out . println ("--------------");
    String fName = args[0];
	fName = (fName.replace(".py", ""));
	
	File file = new File(args[0]);
	InputStream stream = new FileInputStream(file);
	InputStream inp = System.in;
	System.setIn(stream);
	
    ParserInt MicroPython = new ParserInt ();
	SymbolTableInt Table = new SymbolTableInt(fName);
    MicroPython . program (Table);
	System.setIn(inp);
	Table.Run();
  }

}