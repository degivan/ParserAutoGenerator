grammar Grammar;


gramm
    : header? nonTerminalRule* terminalRule*
    ;

header
    : '@header' CODE
    ;

nonTerminalRule
    : NON_TERMINAL inherited? synthesized? ':' nonTermProduction ('|' nonTermProduction)* ';'
    ;

nonTermProduction
    : nonTermVar* CODE?
    ;

nonTermVar
    : TERMINAL
    | NON_TERMINAL callAttrs?
    ;

inherited
    : declAttrs
    ;

declAttrs
    : '[' arg (',' arg)* ']'
    ;
arg
    : argType argName
    ;

argType
    : NON_TERMINAL
    | MIXED_CASE
    ;

argName
    : NON_TERMINAL
    | MIXED_CASE
    ;

callAttrs
    : '[' CODE (',' CODE)* ']'
    ;

terminalRule
    : TERMINAL ':' termProduction ('|' termProduction)* ('->' SKP)? ';'
    ;

termProduction
    : STRING
    ;

synthesized
    : '->' (NON_TERMINAL | MIXED_CASE)
    ;

SKP: 'skip';
NON_TERMINAL : [a-z][a-zA-Z_0-9]* ;
TERMINAL : [A-Z][A-Z_0-9]* ;
MIXED_CASE : [A-Za-z][a-z_A-Z0-9]* ;
CODE : '{' (~[{}]+ CODE?)* '}' ;
STRING: '"' (~('\n'| '|'))* '"';
WS : [ \t\r\n]+ -> skip ;