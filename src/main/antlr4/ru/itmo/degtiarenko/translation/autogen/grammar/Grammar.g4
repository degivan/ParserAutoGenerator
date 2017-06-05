grammar Grammar;

@header {
    package ru.itmo.degtiarenko.translation.autogen.grammer;
}

gramm
    : header? members? main? nonTerminalRule* terminalRule*
    ;

header
    : '@header' LFBR CODE LFBR
    ;

members
    : '@members' LFBR CODE LFBR
    ;

main
    : '@main' LFBR CODE LFBR
    ;

nonTerminalRule
    : NON_TERMINAL ':' nonTermProduction ('|' nonTermProduction)* ';'
    ;

nonTermProduction : (TERMINAL | NON_TERMINAL)+ ;

terminalRule
    : TERMINAL ':' termProduction ('|' termProduction)* ';'
    ;

termProduction
    : STRING+
    ;
