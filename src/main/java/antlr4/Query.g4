grammar Query;


//@header{
//    package antlr4;
//}

//options {
//    visitor=false;
//    listener=false;
//}

/*
Parser rules
*/


query : expression ;
expression
    :   mainOperatorationExpression
    |   notOperationExpression
    |   logicalOperatonExpression

;

mainOperatorationExpression
    :   main_operator OPEN_PAR property DELIMITER value CLOSE_PAR
;

logicalOperatonExpression
    :   logical_operator OPEN_PAR mainOperatorationExpression DELIMITER mainOperatorationExpression CLOSE_PAR
;

notOperationExpression:
    not_operator OPEN_PAR mainOperatorationExpression CLOSE_PAR
;

main_operator: 'EQUAL' | 'GREATER_THAN' | 'LESS_THAN';
logical_operator: 'AND' | 'OR';
not_operator: 'NOT';

property
    :  WORD
  ;

value
    : DQSTR
    | NUMBER
;


/*
Lexer rules
*/


DQSTR : '"'  (~['"] )* '"';
OPEN_PAR: '(';
CLOSE_PAR: ')';
DELIMITER: ',';
WORD: ('a'..'z' | 'A'..'Z')+;
HYPHEN: '-';
NUMBER: ('0' .. '9') + (('0' .. '9') +)?;
WS : [ \t\r\n]+ -> skip ;

