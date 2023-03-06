//AbstractSyntaxTreeInt.java
//Andrew Pamer
//ajp0317

import java.util.*;


abstract class Program { //abstract int semantics(Store s); 
}

class Statement extends Program {

   protected Statement stmt1, stmt2;
   

   public Statement () { }

   public Statement (Statement stmt1, Statement stmt2) {
     this . stmt1 = stmt1;
     this . stmt2 = stmt2;
   }
   

   public String toString () {
     if (stmt1 == null)
       return "()";
     else if (stmt2 == null)
       return "(" + stmt2 + ")";
     else
       return "(: " + stmt1 + " " + stmt2 + ")";
   }
   
   public ArrayList<Integer> semantics(Store s) {
	   ArrayList<Integer> a = new ArrayList<Integer>();
	   stmt1.semantics(s);
	   return stmt2.semantics(s);
   }
   
   
}

/************************************/

class Assignment extends Statement {

   protected Identifier lhs;
   protected Expression rhs;

   public Assignment () { }

   public Assignment (Identifier lhs, Expression rhs) {
     this . lhs = lhs;
     this . rhs = rhs;
   }

   public String toString () {
     return "(= " + lhs + " " + rhs + ")";
   }
   
   public ArrayList<Integer> semantics(Store s) {
	   return s.update(rhs.semantics(s), lhs.getID());
   }
}
   
class IfStatement extends Statement {

  protected Expression test;
  protected Statement ifStmt, elseStmt = null;

  public IfStatement () { }

  public IfStatement (Expression test, Statement ifStmt, Statement elseStmt) {
    this . test = test;
	this . ifStmt = ifStmt;
    this . elseStmt = elseStmt;
  }
  
    public IfStatement (Expression test, Statement ifStmt) {
    this . test = test;
	this . ifStmt = ifStmt;
  }

  public String toString () {
	if(elseStmt != null)
		return "(if " + test +" " + ifStmt + elseStmt + ")";
	else
		return "(if " + test +" " + ifStmt + ")";
   }
   
   public ArrayList<Integer> semantics(Store s) {
	   ArrayList<Integer> x = new ArrayList<Integer>(Arrays.asList(0));
	   if(test.semantics(s).get(0) == 1)
		   x = ifStmt.semantics(s);
	   else if(elseStmt != null)
		   x = elseStmt.semantics(s);
	   
	   return x;
   }   

}

class WhileStatement extends Statement {

  protected Expression test;
  protected Statement body;

  public WhileStatement () { }

  public WhileStatement (Expression test, Statement body) {
    this . test = test;
    this . body = body;
  }

  public String toString () {
     return "(while " + test + " " + body + ")";
   }
  
   public ArrayList<Integer> semantics(Store s) {
	   ArrayList<Integer> x = new ArrayList<Integer>(Arrays.asList(0));
	   while(test.semantics(s).get(0) == 1) {
		   x = body.semantics(s);
	   }
	   
	   return x;
   }  

}

class PrintStatement extends Statement {
	
	protected Expression body;
	
	public PrintStatement() { }
	
	public PrintStatement( Expression body) {
		this.body = body;
	}
	
  public String toString () {
     return "(print " + body + ")";
   }
   
   public ArrayList<Integer> semantics(Store s) {
	   ArrayList<Integer> x = new ArrayList<Integer>();
	   x = body.semantics(s);
	   if(x.size() == 1)
		   System.out.println(x.get(0));
	   else
		   System.out.println(x);
	   return x;
   }  
}

class ReturnStatement extends Statement {
	protected Expression returnVal;
	
	public ReturnStatement() { }
	
	public ReturnStatement(Expression returnVal) {
		this.returnVal = returnVal;
	}
	
	public String toString() {
		return "(return " + returnVal + ")";
	}
	
   public ArrayList<Integer> semantics(Store s) {
	   ArrayList<Integer> x = new ArrayList<Integer>(Arrays.asList(0));
	   x = returnVal.semantics(s);
	   return x;
   }	
}



/************************************/

abstract class Expression {
		protected String type;
	
		public String getType() {
			return this.type;
		}
		
		abstract ArrayList<Integer> semantics(Store s);
	}
	
class Identifier extends Expression {

  protected String id;

  public Identifier () { }

  public Identifier (String id, String type) {
    this . id = id;
	this . type = type;
  }
  

  public String toString () {
    return "(id " + id + ")";
  }
  
  public String getID() {
	return id;
  }
  
  public ArrayList<Integer> semantics(Store s) {
	  ArrayList<Integer> x = new ArrayList<Integer>();
	  x = s.getValue(id);
	  return x;
  }

}

class IntValue extends Expression {

  protected int intValue;


  public IntValue () { }
  
  public IntValue (int intValue) {
    this . intValue = intValue;
	this . type = "int";
  }

  public IntValue (String intValue) {
    this . intValue = Integer.parseInt(intValue);
	this . type = "int";
  }


  public String toString () {
    return "(integer " + intValue + ")";
  }
  
  public ArrayList<Integer> semantics(Store s) {
	  
	  ArrayList<Integer> x = new ArrayList<Integer>(Arrays.asList(intValue));
	  return x;
  }

}


class Binary extends Expression {

  protected String op;
  protected Expression term1, term2;


  public Binary () { }

  public Binary (String op, Expression term1, Expression term2, String type) {
    this . op = op;
    this . term1 = term1;		//Base List
    this . term2 = term2;		//Number to add
	this . type = type;
  }


  public String toString () {
    return "(" + op + " " + term1 + " " + term2 + ")";
  }
  
  public ArrayList<Integer> semantics(Store s) {
	  //+, -, <, >, <=, >=, ==, !=, and, or
	 
	ArrayList<Integer> semVal = new ArrayList<Integer>(Arrays.asList(Integer.MAX_VALUE));
	switch(op) {
		case "+":
			semVal.set(0, term1.semantics(s).get(0) + term2.semantics(s).get(0));
			//semVal = term1.semantics(s) + term2.semantics(s);
			break;
		case "-":
			semVal.set(0, term1.semantics(s).get(0) - term2.semantics(s).get(0));
			//semVal = term1.semantics(s) - term2.semantics(s);
			break;
		case "*":
			semVal.set(0, term1.semantics(s).get(0) * term2.semantics(s).get(0));
			//semVal = term1.semantics(s) * term2.semantics(s);
			break;
		case "//":
			semVal.set(0, term1.semantics(s).get(0) / term2.semantics(s).get(0));
			//semVal = term1.semantics(s) / term2.semantics(s);
			break;
			
		case "<":
			if (term1.semantics(s).get(0) < term2.semantics(s).get(0)) semVal.set(0,1); else semVal.set(0,0);
			break;
		case ">":
			if (term1.semantics(s).get(0) > term2.semantics(s).get(0)) semVal.set(0,1); else semVal.set(0,0);
			//semVal = term1.semantics(s) > term2.semantics(s) ? 1 : 0;
			break;
		case ">=":
			if (term1.semantics(s).get(0) >= term2.semantics(s).get(0)) semVal.set(0,1); else semVal.set(0,0);
			//semVal = term1.semantics(s) >= term2.semantics(s) ? 1 : 0;
			break;
		case "<=":
			if (term1.semantics(s).get(0) <= term2.semantics(s).get(0)) semVal.set(0,1); else semVal.set(0,0);
			//semVal = term1.semantics(s) <= term2.semantics(s) ? 1 : 0;
			break;
		case "==":
			if (term1.semantics(s).get(0) == term2.semantics(s).get(0)) semVal.set(0,1); else semVal.set(0,0);
			//semVal = term1.semantics(s) == term2.semantics(s) ? 1 : 0;
			break;
		case "!=":
			int x, y;
			x = term1.semantics(s).get(0);
			y = term2.semantics(s).get(0);
			if (x  != y ) semVal.set(0,1); else semVal.set(0,0);
			//semVal = term1.semantics(s) != term2.semantics(s) ? 1 : 0;
			//System.out.println(semVal);
			break;
			
		case "and":
			if (term1.semantics(s).get(0) == 1 && term2.semantics(s).get(0) == 1) semVal.set(0,1); else semVal.set(0,0);
			//semVal = (term1.semantics(s) == 1 && term2.semantics(s) == 1) ? 1 : 0;
			break;
		case "or":
			if (term1.semantics(s).get(0) == 1 || term2.semantics(s).get(0) == 1) semVal.set(0,1); else semVal.set(0,0);
			//semVal = (term1.semantics(s) == 1 || term2.semantics(s) == 1) ? 1 : 0;
			break;
			
		case "cons":
			semVal = term1.semantics(s);
			semVal.add(0 ,term2.semantics(s).get(0));
			break;
			
	  }
	  
	  return semVal;
  }

}

class Unary extends Expression {

  protected String op;
  protected Expression term;

  public Unary () { }

  public Unary (String op, Expression term, String type) {
    this . op = op;
    this . term = term;
	this . type = type;
  }
  


  public String toString () {
    return "(" + op + " " + term + ")";
  }
  
  public ArrayList<Integer> semantics(Store s) {
	ArrayList<Integer> semVal = new ArrayList<Integer>(Arrays.asList(Integer.MAX_VALUE));
	switch (op) {
		case "+":
			semVal = term.semantics(s);
			break;
		case "-":
			semVal.set(0, (-1 * term.semantics(s).get(0)));
			break;
			
		case "null":
			if(term.semantics(s).size() > 0) semVal.set(0, 0); else semVal.set(0,1);
			break;
			
		case "not":
			if(term.semantics(s).get(0) == 0 ) semVal.set(0, 1); else semVal.set(0,0);
			break;
			
		case "head":
			semVal.set(0, term.semantics(s).get(0));
			break;
			
		case "tail":
			semVal = term.semantics(s);
			semVal.remove(0);
			break;
	}
	return semVal;
  }

}


class SingleOperator extends Expression {	//input or MicroPythonList
	protected String op;
	static Scanner scan;
	public SingleOperator() { }
	
	public SingleOperator(String op, String type) {
		this.op = op;
		this.type = type;
	}


	
	public String toString() {
		return "(" + op + ")";
	}
	
	public ArrayList<Integer> semantics(Store s) {
		ArrayList<Integer> x = new ArrayList<Integer>();
		if(op.equals("input")) {
			scan = new Scanner(System.in);
			x.add(scan.nextInt());
		}
		return x;
	}
}


class FunctionCall extends Expression {

  protected String id;
  protected ArrayList <Expression> actualParameters;
  protected SymbolTableInt T;
  

  public FunctionCall () { }

  public FunctionCall (String id, ArrayList <Expression> actualParameters, String type, SymbolTableInt T) {
    this . id = id;
    this . actualParameters = actualParameters;
	this . type = type;
	this . T = T;
  }
  
  


  public String toString () {
    String actualParameterList = "";
    Iterator <Expression> actualParameterIterator =
      actualParameters . iterator ();
    while (actualParameterIterator . hasNext ()) {
      Expression actualParameter = actualParameterIterator . next ();
      if (actualParameterList . equals (""))
        actualParameterList = actualParameterList + actualParameter;
      else
        actualParameterList = actualParameterList + " " + actualParameter;
    }
    return "(apply " + id + " (" + actualParameterList + "))";
  }
  
  public ArrayList<Integer> semantics(Store s) {
	  Store newS = new Store();
	  for(int i = 0; i < actualParameters.size(); i++) {
		  newS.update(actualParameters.get(i).semantics(s), T.isThere(id).getVar(i));
	  }
	  ArrayList<Integer> x = new ArrayList<Integer>();
	  x = T.Run(newS, id);
	return x;
  }

} 