grammar FunArrayLang;

WHILE: 'WHILE';
IF: 'IF';
ELSE: 'ELSE';
ADD: 'ADD';
SUBTRACT: 'SUBTRACT';
VAR: 'VAR';
ARR: 'ARR';
ASSIGN: 'ASSIGN';
BRACEL: '{';
BRACER: '}';
PARANL: '(';
PARANR: ')';
BRACKETL: '[';
BRACKETR: ']';
SEMI: ';';
COMMA: ',';
MINUS: '-';
PLUS: '+';
LESSEQUAL: '<=';
LESS: '<';
GREATEREQUAL: '>=';
GREATER: '>';
NOTEQUAL: '!=';
EQUAL: '=';
INT : [0-9]+;
ARRAYID: [A-Z]+;
VARID: [a-z]+;
WS: [ \t\n\r\f]+ -> skip ;

funArrayProgram: variableDefinition arrayDefinition statement EOF;
statement: (atomicStatement | ifThenElse | loop | block)+;

variableDefinition: VAR (VARID (COMMA VARID)*)? SEMI;
arrayDefinition: ARR (ARRAYID (COMMA ARRAYID)*)? SEMI;

normalExpression: VARID (PLUS|MINUS) INT
                | VARID
                | MINUS? INT
                ;

arrayAccess: ARRAYID BRACKETL normalExpression BRACKETR;

// Conditions
comparator: LESSEQUAL | LESS | GREATEREQUAL | GREATER | NOTEQUAL | EQUAL;
comparand: normalExpression | arrayAccess;
comparison: comparand comparator comparand;
condition: PARANL comparison PARANR;

// Atomics
addition: (arrayAccess|VARID) ADD INT;
subtraction: (arrayAccess|VARID) SUBTRACT INT;
assignee: VARID | arrayAccess;
assignmentValue: normalExpression | arrayAccess;
assignment: assignee ASSIGN assignmentValue;
atomicStatement: (addition | subtraction | assignment) SEMI;

// Control structures
block: BRACEL statement BRACER;
loop: WHILE condition block SEMI;
ifThenElse: IF condition block ELSE block SEMI;