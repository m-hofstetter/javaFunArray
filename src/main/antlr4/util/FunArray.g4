grammar FunArray;

funArray: bound NON_EMPTY ' '? (interval ' '? bound emptiness ' '?)* EOF;

POS_INF : '∞';
NEG_INF : '-∞';

infinity : POS_INF | NEG_INF;

DIGIT: [0-9];

LETTER: [a-zA-Z0-9]| '.';

integer: infinity | finiteInteger;
finiteInteger: DIGIT+ | MINUS DIGIT+;

interval: INTERVAL_START integer INTERVAL_DIVIDER integer INTERVAL_END | UNREACHABLE;

expression: variableName PLUS finiteInteger | variableName MINUS finiteInteger | variableName | finiteInteger;

variableName: LETTER+;

bound: '{' expression (' ' expression)*;

EMPTY: '}?';
NON_EMPTY: '}';

emptiness: EMPTY | NON_EMPTY;

INTERVAL_START: '[';
INTERVAL_DIVIDER: ', ';
INTERVAL_END: ']';
UNREACHABLE: '⊥';
PLUS: '+';
MINUS: '-';