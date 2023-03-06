public enum Symbol {
  EOF, 
  // punctuation
  PERIOD, COMMA, SEMICOLON, COLON, ARROW,
  // operators
  ASSIGN, EQ, NE, LT, GT, LE, GE, PLUS, MINUS, TIMES, SLASH, LPAREN, RPAREN, 
  // keywords
  FROM, CLASS, IMPORT, LIST, DEF, RETURN, INT, IF, ELSE, WHILE, PRINT, OR, AND, NOT, INPUT, CONSTANT, HEAD, TAIL, NULL,
  // ids and integers
  ID, INTEGER
}