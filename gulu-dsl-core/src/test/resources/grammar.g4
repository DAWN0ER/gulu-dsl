grammar QueryJudgeExpression;

// 解析器规则
// ---------- and or not 聚合规则----------
expression
    : orExpression
    ;

orExpression
    : andExpression (OR andExpression)*
    ;

andExpression
    : notExpression (AND notExpression)*
    ;

notExpression
    : NOT notExpression
    | '!' notExpression
    | baseBoolExpression
    ;
// ---------- and or not 聚合规则结束 ----------

baseBoolExpression
    : literal comparisonOperator identifier  // 简单的比较
    | identifier comparisonOperator literal  // 简单的比较
    | boolFunctionExpression                 // 见下方
    | '(' expression ')'                     // boolQuery
    | reference                              // 引用其他的表达式的结果
    ;

comparisonOperator
    : '=='
    | '!='
    | '>'
    | '<'
    | '>='
    | '<='
    ;

boolFunctionExpression
    : EXIST '(' identifier ')'                  // exist
    | identifier '[' literalList ']'            // list terms value
    | identifier IN '(' literalList ')'         // field terms valus
    | identifier '[' expression ']'             // nested query path[property == 1] 如果这里嵌套 #{} 引用，在语法树报错
    ;

literalList
    : literal (',' literal)*
    ;

identifier
    : IDENTIFIER
    ;

reference
    : REFER
    ;

literal
    : NUMBER
    | STRING
    | BOOLEAN
    | ENV_VAR
    ;

// -------------------------------------------------

// 词法规则
AND     : 'AND' ; // 不区分大小写
OR      : 'OR' ;  // 不区分大小写
NOT     : 'NOT' ;  // 不区分大小写
EXIST   : 'EXIST' ; // 不区分大小写
IN      : 'IN' ;  // 不区分大小写

BOOLEAN
    : 'TRUE'    // 不区分大小写
    | 'FALSE'   // 不区分大小写
    ;

NUMBER
    : '-'? DIGIT+ ('.' DIGIT+)? [dDlLfF]?
    ;

STRING
    : '\'' (~['\r\n])* '\''
    | '"' (~["\r\n])* '"'
    ;

IDENTIFIER
    : PATH
    ;

ENV_VAR
    : '#' PATH
    ;

REFER
    : '#' '{' PATH '}'
    ;

fragment DIGIT
    : [0-9]
    ;

fragment PATH
    : [a-zA-Z_] [a-zA-Z0-9_]* ('.' [a-zA-Z_] [a-zA-Z0-9_]*)*

// 跳过空白字符
WS
    : [ \t\r\n]+ -> skip
    ;
