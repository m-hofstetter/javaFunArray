grammar FunArray;

funArray: bound NON_EMPTY (interval bound emptiness)* ';' ;

POS_INF : '∞';
NEG_INF : '-∞';

infinity : POS_INF | NEG_INF;

DIGIT: [0-9];

LETTER: [a-zA-Z0-9]| '.';

integer: infinity | finiteInteger;
finiteInteger: DIGIT+ | NEGATION DIGIT+;

interval: INTERVAL_START integer INTERVAL_DIVIDER integer INTERVAL_END | UNREACHABLE;

expression: variableName '+' finiteInteger | variableName | finiteInteger;

variableName: LETTER+;

bound: '{' expression+;

EMPTY: '}?';
NON_EMPTY: '}';

emptiness: EMPTY | NON_EMPTY;

INTERVAL_START: '[';
INTERVAL_DIVIDER: ', ';
INTERVAL_END: ']';
NEGATION: '-';
UNREACHABLE: '⊥';

WS  :   (' ')+ {skip();};