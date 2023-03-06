//ParserDS.java
//Andrew Pamer
//ajp0317


/*********/


import java.io.*;
import java.util.*;

public class ParserInt {
	
	protected MicroPythonLexer lexer;	//Lexical Analyzer
	protected Token token;
	
	public SymbolTableInt SymTable;
	protected FuncEntry FunctionTable;
	
	protected Store storeTable;
	
	public ParserInt() throws IOException {
		lexer = new MicroPythonLexer (new InputStreamReader (System.in));
		getToken();		//Get first token
	}
	
	private void getToken() throws IOException {
		token = lexer.nextToken();
	}

	/************/
	
	//<program> ::= from MicroPythonListClass import MicroPythonList { funcdef }
	
	public void program(SymbolTableInt Table) throws IOException {
		storeTable = new Store();
		SymTable = Table;
		Program program = null;
		Statement funcStmt = null, funcStmt1 = null;
		if(token.symbol() != Symbol.FROM)
			ErrorMessage.print(lexer.position(), "from EXPECTED");	//Program exits here
		
		getToken();	//Else token must be 'from', so it is correct so far...
		if(token.symbol() != Symbol.CLASS)
			ErrorMessage.print(lexer.position(), "MicroPythonListClass EXPECTED");
		
		getToken();
		if(token.symbol() != Symbol.IMPORT)
			ErrorMessage.print(lexer.position(), "import EXPECTED");
		
		getToken();
		if(token.symbol() != Symbol.LIST)
			ErrorMessage.print(lexer.position(), "MicroPythonList EXPECTED");
		
		getToken();
		while(token.symbol() != Symbol.EOF)
			funcdef();
		
		if(SymTable.isThere("main") == null)
			ErrorMessage.print(lexer.position(), "main function missing");
		
		//storeTable.showStore();
		
	}
	
	/************/
	
	//<funcdef> ::= def <function-identifier> ( [ <formal-parameter> { , <formal-parameter> } ] ) -> <type> : <suite> return <add-expr>
	
	public void funcdef() throws IOException {
		Statement funcBody, retStmt;
		Expression returnExp;
		FunctionTable = new FuncEntry();
		SymTable.addTableEntry(FunctionTable);
		
		ArrayList <String> ParamList = new ArrayList<String>();
		
		String ParameterList = "", retType = null;
		if(token.symbol() != Symbol.DEF)
			ErrorMessage.print(lexer.position(), "def EXPECTED");
		
		getToken();
		if(token.symbol() != Symbol.ID)
			ErrorMessage.print(lexer.position(), "Identifier EXPECTED");
		
		String funcName = token.lexeme();

		if(SymTable.getSize() > 1 )
			if(SymTable.isThere(funcName) != null)
				ErrorMessage.print(lexer.position(), "Cannot have two functions of the same name");
		
		getToken();
		if(token.symbol() != Symbol.LPAREN)
			ErrorMessage.print(lexer.position(), "( EXPECTED");
		
		getToken();
		if(token.symbol() != Symbol.RPAREN) {	//If the token is not a ')' then it should be a formal parameter
			//ParameterList = formal_parameter();
			ParamList.add(formal_parameter());
			
			while(token.symbol() == Symbol.COMMA) {
				getToken();
				ParamList.add(formal_parameter());
				//ParameterList = ParameterList + ", " + formal_parameter();
			}
		}
		
		getToken();
		if(token.symbol() != Symbol.ARROW)
			ErrorMessage.print(lexer.position(), "-> EXPECTED");
		
		getToken();
		retType = type();
		//ParameterList = "[" + ParameterList + "] -> " + retType;
		
		//FunctionTable.setTable(funcName, ParameterList, retType);
		FunctionTable.setTable(funcName, ParamList, retType);
		
		
		if(token.symbol() != Symbol.COLON)
			ErrorMessage.print(lexer.position(), ": EXPECTED");
		
		getToken();
		funcBody = suite();
		
		if(token.symbol() != Symbol.RETURN)
			ErrorMessage.print(lexer.position(), "return EXPECTED");
		
		
		getToken();
		returnExp = add_expr();

		
		if(!(returnExp.getType().equals(retType)))
			ErrorMessage.print(lexer.position(), "function must return correct type");
		
		funcBody = new Statement(funcBody, new ReturnStatement(returnExp));
		
		FunctionTable.setTableAST(funcBody);
		
		
	}
	
	/************/
	
	//<formal-parameter> ::= <variable-identifier> : <type>
	
	public String formal_parameter() throws IOException {
		
		if(token.symbol() != Symbol.ID)
			ErrorMessage.print(lexer.position(), "Identifier EXPECTED");
		String paramId = token.lexeme();
		
		getToken();
		if(token.symbol() != Symbol.COLON)
			ErrorMessage.print(lexer.position(), ": EXPECTED");
		
		getToken();
		
		String type = type();
		VarEntry paramVar = new VarEntry(paramId, type);
		if(FunctionTable.getSize() > 0)
			if(FunctionTable.isThere(paramId) != null)
				ErrorMessage.print(lexer.position(), "formal-parameters cannot be duplicated");
		FunctionTable.addVarTable(paramVar);
		return type;
	}
	
	/************/
	
	//<type> ::= int | MicroPythonList
	
	public String type() throws IOException {
		String type = null;
		
		if(token.symbol() != Symbol.INT) {
			if(token.symbol() != Symbol.LIST)
				ErrorMessage.print(lexer.position(), "int or MicroPythonList EXPECTED");				
		}
		if(token.symbol() == Symbol.INT)
			type = "int";
		else if(token.symbol() == Symbol.LIST)
			type = "MicroPythonList";
		
		getToken();
		return type;
	}
	
	/************/
	
	//<suite> ::= <statement> { <statement> }
	
	public Statement suite() throws IOException {
		Statement stmt, stmt1 = null;
		stmt = statement();
		
		while(token.symbol() != Symbol.RETURN && token.symbol() != Symbol.SEMICOLON && token.symbol() != Symbol.EOF) {
			stmt1 = statement();
			stmt = new Statement(stmt, stmt1);
		}

		return stmt;

	}
	
	/************/
	
/*	statement ::= variable-identifier = add-expr
		| if or-test : suite ; [else : suite ;]
		| while or-test : suite ;
		| print ( add-expr )
*/

	public Statement statement() throws IOException {
		Expression exp = null;
		Identifier id = null;
		Statement stmt = null, stmt1, stmt2 = null;
		
		VarEntry Variable;
		String id_Table = null;
		
		switch(token.symbol()) {
			case ID:
				id_Table = token.lexeme();
				getToken();
				if(token.symbol() != Symbol.ASSIGN)
					ErrorMessage.print(lexer.position(), "= EXPECTED");
				
				getToken();
				exp = add_expr();
				id = new Identifier (id_Table, exp.getType());
				
				if(FunctionTable.isThere(id_Table) != null) {
					if(!(FunctionTable.isThere(id_Table).type.equals(exp.getType())))
						ErrorMessage.print(lexer.position(), "Type Error");
				}
				
				else if(FunctionTable.isThere(id_Table) == null) {
					Variable = new VarEntry(id_Table, exp.getType());
					FunctionTable.addVarTable(Variable);
				}				
				//id = new Identifier (id_Table, exp.getType());
				
				
				stmt = new Assignment(id, exp);
				
				break;
			
			case IF:			//if
				getToken();
				exp = or_test();	//<or-test>
				
				if(!(exp.getType().equals("boolean")))
					ErrorMessage.print(lexer.position(), "Type error");
				
				if(token.symbol() != Symbol.COLON)	//:
					ErrorMessage.print(lexer.position(), ": EXPECTED");
				
				getToken();
				stmt1 = suite();	//<suite>
				
				if(token.symbol() != Symbol.SEMICOLON)	//;
					ErrorMessage.print(lexer.position(), "; EXPECTED");
				
				getToken();
				if(token.symbol() == Symbol.ELSE) {	//[else
					getToken();
					if(token.symbol() != Symbol.COLON)	//:
						ErrorMessage.print(lexer.position(), ": EXPECTED");
					
					getToken();
					stmt2 = suite();	//<suite>
					
					if(token.symbol() != Symbol.SEMICOLON)	//;]
						ErrorMessage.print(lexer.position(), "; EXPECTED");
					getToken();
					
				}
				stmt = new IfStatement(exp, stmt1, stmt2);
				
				break;
				
			case WHILE:				//while
				getToken();
				exp = or_test();	//<or-test>
				
				
				if(!(exp.getType().equals("boolean")))
					ErrorMessage.print(lexer.position(), "Type Error");
				
				if(token.symbol() != Symbol.COLON)	//:
					ErrorMessage.print(lexer.position(), ": EXPECTED");
				
				getToken();
				stmt1 = suite();	//<suite>
				if(token.symbol() != Symbol.SEMICOLON)	//;
					ErrorMessage.print(lexer.position(), "; EXPECTED");
				getToken();
				
				stmt = new WhileStatement(exp, stmt1);
				break;
				
			case PRINT:				//print
				getToken();
				if(token.symbol() != Symbol.LPAREN)	//(
					ErrorMessage.print(lexer.position(), "( EXPECTED");
				
				getToken();
				exp = add_expr();	//<add-expr>
				
				
				//if(!(exp.getType().equals("int")))
				//	ErrorMessage.print(lexer.position(), "Type error");
				
				if(token.symbol() != Symbol.RPAREN)	//)
					ErrorMessage.print(lexer.position(), ") EXPECTED");
				
				getToken();
				stmt = new PrintStatement(exp);
				break;
				
			default: 
				ErrorMessage.print(lexer.position(), "identifier, if, while, or print EXPECTED");
				stmt = null;
			break;
		}
		return stmt;
	}
	
	/************/
	
	//<or-test> ::= <and-test> { or <and-test> }
	
	public Expression or_test() throws IOException {
		Expression exp, exp1 = null;
		exp = and_test();
		while(token.symbol() == Symbol.OR) {
			getToken();
			exp1 = and_test();
			if(!(exp.getType().equals(exp1.getType())))
				ErrorMessage.print(lexer.position(), "Type Error");
			exp = new Binary("or", exp, exp1, "boolean");

		}
		return exp;
	}
	
	/************/
	
	//<and-test> ::= <not-test> { and <not-test> }
	
	public Expression and_test() throws IOException {
		Expression exp, exp1 = null;
		exp = not_test();
		while(token.symbol() == Symbol.AND) {
			getToken();
			exp1 = not_test();
			if(!(exp.getType().equals(exp1.getType())))
				ErrorMessage.print(lexer.position(), "Type Error");
			exp = new Binary("and", exp, exp1, "boolean");
		}
		return exp;
	}
	
	/************/
	
	//<not-test> ::= <comparison> | not <not-test>
	
	public Expression not_test() throws IOException {
		Expression exp;
		if(token.symbol() == Symbol.NOT) {
			getToken();
			exp = not_test();
			exp = new Unary("not", exp, "boolean");
		}
		else
			exp = comparison();
		
		return exp;
	}
	
	/************/
	
	//<comparison> ::= <add-expr> [ <comp-operator> <add-expr> ]
	
	public Expression comparison() throws IOException {
		Expression exp, exp1, exp2 = null;
		String relOP = null;
		
		
		exp = add_expr();
		exp1 = exp;
		if(token.symbol() == Symbol.GT || token.symbol() == Symbol.LT || token.symbol() == Symbol.EQ || token.symbol() == Symbol.GE || token.symbol() == Symbol.LE || token.symbol() == Symbol.NE) {
			relOP = token.lexeme();
			getToken();
			exp2 = add_expr();
			if(!(exp2.getType().equals("int")))
				ErrorMessage.print(lexer.position(), "Type error");
			exp = new Binary(relOP, exp1, exp2, "boolean");
		}
		return exp;
	}
	
	/************/
	
	//<add-expr> ::= <mult-expr> { <add-operator> <mult-expr> }
	
	public Expression add_expr() throws IOException {
		
		Expression exp, exp1, exp2 = null;
		String OP = null;
		
		
		exp = mult_expr();
		exp1 = exp;
		while(token.symbol() == Symbol.PLUS || token.symbol() == Symbol.MINUS) {
			if(token.symbol() == Symbol.PLUS)
				OP = "+";
			else
				OP = "-";
			getToken();
			exp2 = mult_expr();
			if(!(exp1.getType().equals("int"))) {
				if(!(exp2.getType().equals("int")))
					ErrorMessage.print(lexer.position(), "Type error");
			}

			exp = new Binary(OP, exp, exp2, "int");
			
		}
		return exp;
	}
	
	/************/
	
	//<mult-expr> ::= <unary-expr> { <mult-operator> <unary-expr> }
	
	public Expression mult_expr() throws IOException {
		
		Expression exp, exp1, exp2 = null;
		String OP = null;
		
		exp = unary_expr();
		exp1 = exp;
		
		while(token.symbol() == Symbol.TIMES || token.symbol() == Symbol.SLASH) {
			if(token.symbol() == Symbol.TIMES)
				OP = "*";
			else
				OP = "//";
			getToken();
			exp2 = unary_expr();
			if(!(exp2.getType().equals("int")))
				ErrorMessage.print(lexer.position(), "Type error");

			exp = new Binary(OP, exp1, exp2, exp1.getType());
		}
		return exp;
	}
	
	/************/

	//<unary-expr> ::= [<add-operator>] <primary>
	
	public Expression unary_expr() throws IOException {
		Expression exp, exp1 = null;
		String OP = null;
		
		if(token.symbol() == Symbol.PLUS || token.symbol() == Symbol.MINUS) {
			if(token.symbol() == Symbol.PLUS)
				OP = "+";
			else
				OP = "-";
			getToken();
		}
		exp = primary();
		exp1 = exp;
		
		if(OP != null) {
			if(!(exp1.getType().equals("int")))
				ErrorMessage.print(lexer.position(), "Type error");
			exp = new Unary(OP, exp1, exp1.getType());
		}
		return exp;
	}
	
	/************/
	
	//<primary> ::= <integer-primary> | <list-primary>
	
	public Expression primary() throws IOException {
		Expression exp;
		if(token.symbol() == Symbol.INTEGER || token.symbol() == Symbol.INT)
			exp = integer_primary();
		else
			exp = list_primary();
		
		return exp;
	}
	
	/************/
	
	//<integer-primary> ::= integer | int ( input ( ) )
	
	public Expression integer_primary() throws IOException {
		Expression exp = null;
		if(token.symbol() == Symbol.INTEGER)
			exp = new IntValue(token.lexeme());
		if(token.symbol() != Symbol.INTEGER) {
			if(token.symbol() != Symbol.INT)
				ErrorMessage.print(lexer.position(), "int EXPECTED");
			
			getToken();
			if(token.symbol() != Symbol.LPAREN)
				ErrorMessage.print(lexer.position(), "( EXPECTED");
			
			getToken();
			if(token.symbol() != Symbol.INPUT)
				ErrorMessage.print(lexer.position(), "input EXPECTED");
			
			getToken();
			if(token.symbol() != Symbol.LPAREN)
				ErrorMessage.print(lexer.position(), "( EXPECTED");
			
			getToken();
			if(token.symbol() != Symbol.RPAREN)
				ErrorMessage.print(lexer.position(), ") EXPECTED");
			
			getToken();
			if(token.symbol() != Symbol.RPAREN)
				ErrorMessage.print(lexer.position(), ") EXPECTED");
			exp = new SingleOperator("input", "int");
		}
		getToken();
		return exp;
	}
	
	/************/
	
	/*<list-primary> ::= <atom> | function-identifier ( [ <add-expr-list> ] )
		| <list-primary> . null ( ) | <list-primary> . cons ( add-expr )
		| <list-primary> . head ( ) | <list-primary> . tail ( )
	*/
	

	public Expression list_primary() throws IOException {
		String Listid = null;
		Expression listExp, consExp = null;
		
		listExp = atom();
		
		while (token.symbol() == Symbol.PERIOD) {	//list-primary . null/cons/tail/...
			getToken();
			switch(token.symbol()) {
				case NULL:
					Listid = "null";
					getToken();
					if(token.symbol() != Symbol.LPAREN)
						ErrorMessage.print(lexer.position(), "( EXPECTED");
			
					getToken();
					if(token.symbol() != Symbol.RPAREN)
						ErrorMessage.print(lexer.position(), ") EXPECTED");
					getToken();
					
					listExp = new Unary("null", listExp, "boolean");
					
					
					
					break;
					
				case CONSTANT:
					Listid = "cons";
					getToken();
					if(token.symbol() != Symbol.LPAREN)
						ErrorMessage.print(lexer.position(), "( EXPECTED");
			
					getToken();
					consExp = add_expr();
					
					if(!(consExp.getType().equals("int")))
						ErrorMessage.print(lexer.position(), "Type Error");
					
					if(token.symbol() != Symbol.RPAREN)
						ErrorMessage.print(lexer.position(), ") EXPECTED");
					getToken();
					
					listExp = new Binary("cons", listExp, consExp, "MicroPythonList");
					
					
					break;
					
				case HEAD:
					Listid = "head";
					getToken();
					if(token.symbol() != Symbol.LPAREN)
						ErrorMessage.print(lexer.position(), "( EXPECTED");
			
					getToken();
					if(token.symbol() != Symbol.RPAREN)
						ErrorMessage.print(lexer.position(), ") EXPECTED");
					getToken();
					
					listExp = new Unary("head", listExp, "MicroPythonList");
					
					
					
					break;
					
				case TAIL:
					Listid = "tail";
					getToken();
					if(token.symbol() != Symbol.LPAREN)
						ErrorMessage.print(lexer.position(), "( EXPECTED");
			
					getToken();
					if(token.symbol() != Symbol.RPAREN)
						ErrorMessage.print(lexer.position(), ") EXPECTED");
					getToken();
					
					listExp = new Unary("tail", listExp, "MicroPythonList");
					
					
					
					break;
					
				default: 
					ErrorMessage.print(lexer.position(), "null, cons, head, or tail EXPECTED");
				break;
			}

		}
		return listExp;
	}
	
	/************/
	
	public Expression atom() throws IOException {
		ArrayList <Expression> expList = null;
		Expression atomExp = null;
		String name = null, funcName = null;
		
		if(token.symbol() == Symbol.ID) {
			name = token.lexeme();
			if(FunctionTable.isThere(name) != null)
				atomExp = new Identifier(name, FunctionTable.isThere(name).type);
			getToken();
			
			if(token.symbol() == Symbol.LPAREN) {
				getToken();
				
				if(token.symbol() != Symbol.RPAREN)
					expList = add_expr_list();
				if(token.symbol() != Symbol.RPAREN)
					ErrorMessage.print(lexer.position(), ") EXPECTED");
				getToken();
				
				funcName = SymTable.isThere(name).type;
				/*System.out.println(expList.size());
				if(expList.size() != SymTable.isThere(name).getSize())
					ErrorMessage.print(lexer.position(), "Number of parameters "*/
				
				if(expList.size() != SymTable.isThere(name).paramList.size()) 
					ErrorMessage.print(lexer.position(), "Wrong number of Parameters");
				
				for(int i = 0; i < expList.size(); i++)
					if(!(expList.get(i).getType().equals(SymTable.isThere(name).paramList.get(i))))
						ErrorMessage.print(lexer.position(), "Incorrect Parameter Type");
				
				atomExp = new FunctionCall(name, expList, funcName, SymTable);
			}
		}
		else if(token.symbol() == Symbol.LPAREN) {
			getToken();
			atomExp = add_expr();
			if(token.symbol() != Symbol.RPAREN)
				ErrorMessage.print(lexer.position(), ") EXPECTED");
			getToken();
		}
		else if (token.symbol() == Symbol.LIST) {
			getToken();
			if(token.symbol() != Symbol.LPAREN)
				ErrorMessage.print(lexer.position(), "( EXPECTED");
			
			atomExp = new SingleOperator("MicroPythonList", "MicroPythonList");
			
			getToken();
			if(token.symbol() != Symbol.RPAREN)
				ErrorMessage.print(lexer.position(), ") EXPECTED");
			
			getToken();
		}
		else {
			ErrorMessage.print(lexer.position(), "ATOM expected");
		}
		return atomExp;
	}
	
	/************/
	
	//<add-expr-list> ::= <add-expr> {, <add-expr> }
	
	public ArrayList<Expression> add_expr_list() throws IOException {
		Expression exp;
		ArrayList <Expression> expList = new ArrayList <Expression> ();
		exp = add_expr();
		expList.add(exp);
		while(token.symbol() == Symbol.COMMA) {
			getToken();
			exp = add_expr();
			expList.add(exp);
		}
		
		return expList;
	}

}
