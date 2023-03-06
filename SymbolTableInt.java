//Andrew Pamer
//ajp0317

//SymbolTableInt.java

import java.util.*;
import java.io.*;







class VarEntry {
	protected String id;
	protected String category = "variable";
	protected String type;
	protected int offset;
	
	public VarEntry(String id, String type){
		this.id = id;
		this.type = type;
	}
	
	public void setOffest(int offset) {
		this.offset = offset;
	}
	
	public String toString (String format) {
		return String.format(format, this.id, this.category, this.type + "(" + this.offset + ")");
   }
   
   public String getVarName() {
	   return id;
   }
	
	
	
	
}

class FuncEntry {
	
	protected String id;
	protected String category = "function";
	protected String type;
	protected ArrayList<String> paramList;
	protected ArrayList<VarEntry> Table;
	
	protected int offset = 0;
	
	protected Statement AST;
	
	public FuncEntry(String id, String type, ArrayList<String> paramList, Statement AST){
		this.id = id;
		this.type = type;
		this.paramList = paramList;
		this.AST = AST;
	}
	
	public FuncEntry() {
		Table = new ArrayList<VarEntry>();
		paramList = new ArrayList<String>();
	}

	public void setTable(String id, ArrayList<String> paramList, String type) {
		this.id = id;
		this.paramList = paramList;
		this.type = type;
	}
	
	public void setTableAST(Statement AST) {
		this.AST = AST;
	}
	
	public ArrayList<Integer> run(Store s) {
		return this.AST.semantics(s);
	}
	
	
	
	public void addVarTable(VarEntry variable){
		this.Table.add(variable);
		this.Table.get(offset).setOffest(offset);
		this.offset++;
	}
	
	public String toString (String format) {
		return String.format(format, this.id, this.category,this.paramList + "-> " + this.type);
   }
   
   public void funcPrint() {
	   
		int largest = 0;
		for(int i = 0; i < Table.size(); i++)
			if(Table.get(i).id.length() > Table.get(largest).id.length())
				largest = i;
			
	    String format = this.Table.size() > 0 ? "%-" + (this.Table.get(largest).id.length() + 3) + "s" + "%-" + (this.Table.get(largest).category.length() + 3) + "s" + "%-" + this.Table.get(largest).type.length() +"s\n" : "%-4s%-10s%-5s\n";
		
		System.out.println();
		System.out.println();
		System.out.println("Identifier Table for "+ this.id);
		System.out.print("---------------------");
		
		for(int i = 0; i < this.id.length(); i++)
			System.out.print("-");
		System.out.println();
		
		System.out.println();
		System.out.printf(format, "Id","Category","Type");

		System.out.printf(format, "--","--------","----");
		for(int i = 0; i < this.Table.size(); i++)
			System.out.print(this.Table.get(i).toString(format));
		
		
		System.out.println();
		System.out.println();
		System.out.println("Abstract Syntax Tree for " + this.id);
		System.out.print  ("-------------------------");
		for(int i = 0; i < this.id.length(); i++)
			System.out.print("-");
		System.out.println();
		System.out.println(this.AST);
	}
   
   public VarEntry isThere(String findId) {
	   VarEntry Var = null;
	   for(int i = 0; i < Table.size(); i++) {
		   if(Table.get(i).id.equals(findId)) {
			   Var = Table.get(i);
			   break;
		   }
	   }
	   return Var;
   }
   
   public int getSize() {
		return Table.size();
   }
   
   public String getVar(int index) {
	   return Table.get(index).getVarName();
   }
	
	
}

class SymbolTableInt {
	
	private ArrayList<FuncEntry> Table;
	
	private String name;
	

	
	public SymbolTableInt(String name) {
		Table = new ArrayList<FuncEntry>();
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addTableEntry(FuncEntry func) {
		this.Table.add(func);
	}
	
   public void print () {
		String format = "%-" + (Table.get(0).id.length() + 3) + "s" + "%-" + (Table.get(0).category.length() + 3) + "s" + "%-" + Table.get(0).type.length() +"s\n";
	   	System.out.println();
		System.out.println();
		System.out.println("Identifier Table for " + name);
		System.out.print("---------------------");
		for(int i = 0; i < name.length(); i++)
			System.out.print("-");
		System.out.println();
		System.out.println();
		System.out.printf(format, "Id","Category","Type");

		System.out.printf(format, "--","--------","----");
		for(int i = 0; i < Table.size(); i++)
			System.out.print(Table.get(i).toString(format));
		
		for(int j = 0; j < Table.size(); j++)
			Table.get(j).funcPrint();
		

   }
   
   public FuncEntry isThere(String findId) {
	   FuncEntry Function = null;
	   for(int i = 0; i < Table.size(); i++) {
		   if(Table.get(i).id == null) break;
		   if(Table.get(i).id.equals(findId)) {
			   Function = Table.get(i);
			   break;
		   }
	   }
	   
	   return Function;
   }
   
   public int getSize() {
	   return Table.size();
   }
   
   public void Run() {
	   isThere("main").run(new Store());
   }
   
   public ArrayList<Integer> Run(Store s, String name) {
	   return isThere(name).run(s);
   }

}