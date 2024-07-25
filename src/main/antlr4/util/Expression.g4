grammar Expression;

PARANL: '(';
PARANR: ')';
BRACKETL: '[';
BRACKETR: ']';
PLUS: '+';
MINUS: '-';
ASTERISK: '*';
SLASH: '/';
PERCENT: '%';
INT : [0-9]+;
ARRAYID: [A-Z]+;
VARID: [a-z]+;
WS: [ \t\n\r\f]+ -> skip ;

expression: aggregatedExpression
          | atomicExpression
          ;

operand: PARANL aggregatedExpression PARANR
       | atomicExpression
       ;

atomicExpression: constant
                | arrayElement
                | variable
                ;
constant: INT | PARANL MINUS INT PARANR;
variable: VARID;
arrayElement: ARRAYID BRACKETL expression BRACKETR;

aggregatedExpression: addition
                    | subtraction
                    | multiplication
                    | division
                    | modulo
                    ;
subtraction: operand? MINUS operand;
division: operand SLASH operand;
modulo: operand PERCENT operand;
addition: operand (PLUS operand)+;
multiplication: operand (ASTERISK operand)+;

test: expression EOF;







