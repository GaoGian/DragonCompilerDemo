# Demo_Compiler_front
《编译原理》（机械工业出版社）--编译器前端

#示例代码
{
	int i; int j; float v; float x; float[100] a;
	while(true){
		do i=i+1; while(a[i]<v);
		do j=j-1; while(a[j]>v);
		if(i>=j) break;
		x=a[i]; a[i]=a[j]; a[j]=x;
	}
}

#实例词法
##查看类：LexExpression

#示例文法
##如果终结符使用“◀▶”包裹，则说明是正则表达式词法单元，需要根据词法单元类型匹配
stmt → { declared stmts }
declared → type factor ; declared | ε
type → int | float
stmts → whilecycle | docycle | assign ; stmts | ε
whilecycle → while ( bexpr ) { stmts }
docycle → do stmts ; while ( bexpr ) ;
bexpr → factor > factor | factor < factor | factor >= factor | factor <= factor | factor == factor | factor != factor | true | false
assign → factor = expression
expression → expression + term | expression - term | term
term → term * factor | term / factor | factor | ◀number▶
factor → ◀id▶ | factor [ ◀digit▶ ]


// TODO 下一步计划
1、输出词法分析结果 Token
2、编写文法分析
    解析文法
    消除左递归
    抽取左公因式
    计算对应的FIRST、FOLLOW集


#完整文法
funcdef → type id ( parastate ) { funcblock }
type → int | float | char | void
factor → ( exp ) | id | number | ch
exp → divi item
divi → factor faccycle
faccycle → * factor faccycle | / factor faccycle | ε
item → + divi item | - divi item | ε
parastate → state stateclo | ε
state → type id init | id init
init → = rvalue | ε
rvalue → exp
stateclo → , stateclo | ε
funcblock → staclo funcbloclo
staclo → statement staclo | ε
statement → state ;
funcbloclo → opera funcbloclo | whilecycle funcbloclo | condistate funcbloclo | funcend funcbloclo | coutstate funcbloclo | cinstate funcbloclo | ε
opera → id callstate
callstate → = rvalue ; | ( paralist ) ;
paralist → para paraclo
paraclo → , para paraclo | ε
para → id | number | string
whilecycle → while ( logicexp ) do { funcblock } we
logicexp → exp logicopera exp
logicopera → > | < | == | >= | <=
condistate → if ( logicexp ) { funcblock } nor ie
nor → else { funcblock } | ε
funcend → return factor ;
coutstate → cout < < id ;
cinstate → cin > > id ;
do → ε
we → ε
ie → ε
