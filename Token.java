public class Token {

  private Symbol symbol;	// current token
  private String lexeme;	// lexeme

  public Token () { }

  public Token (Symbol symbol) {
    this (symbol, null);
  }

  public Token (Symbol symbol, String lexeme) {
    this . symbol = symbol;
    this . lexeme  = lexeme;
  }

  public Symbol symbol () { return symbol; }

  public String lexeme () { return lexeme; }

  public String toString () {
    switch (symbol) {
      case PERIOD :    return "(punctuation, .) ";
      case COMMA :     return "(punctuation, ,) ";
      case SEMICOLON : return "(punctuation, ;) ";
      case COLON :     return "(punctuation, :) ";
      case ARROW :     return "(punctuation, ->) ";

      case ASSIGN :    return "(operator, =) ";
      case EQ :        return "(operator, ==) ";
      case NE :        return "(operator, !=) ";
      case LT :        return "(operator, <) ";
      case GT :        return "(operator, >) ";
      case LE :        return "(operator, <=) ";
      case GE :        return "(operator, >=) ";
      case PLUS :      return "(operator, +) ";
      case MINUS :     return "(operator, -) ";
      case TIMES :     return "(operator, *) ";
      case SLASH :     return "(operator, //) ";
      case LPAREN :    return "(operator, () ";
      case RPAREN :    return "(operator, )) ";

      case FROM :      return "(keyword, from) ";
      case CLASS :     return "(keyword, MicroPythonListClass) ";
      case IMPORT :    return "(keyword, import) ";
      case LIST :      return "(keyword, MicroPythonList) ";
      case DEF :       return "(keyword, def) ";
      case RETURN :    return "(keyword, return) ";
      case INT :       return "(keyword, int) ";
      case IF :        return "(keyword, if) ";
      case ELSE :      return "(keyword, else) ";
      case WHILE :     return "(keyword, while) ";
      case PRINT :     return "(keyword, print) ";
      case OR :        return "(keyword, or) ";
      case AND :       return "(keyword, and) ";
      case NOT :       return "(keyword, not) ";
      case INPUT :     return "(keyword, input) ";
      case CONSTANT :  return "(keyword, cons) ";
      case HEAD :      return "(keyword, head) ";
      case TAIL :      return "(keyword, tail) ";
      case NULL :      return "(keyword, null) ";

      case ID :        return "(identifier, " + lexeme + ") ";
      case INTEGER :   return "(integer, " + lexeme + ") ";
      default : 
	ErrorMessage . print (0, "Unrecognized token");
        return null;
    }
  }

}