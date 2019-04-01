# CompilerDemo
《编译原理》（机械工业出版社）--编译器

# 编译器前端
## 主要功能
1、正则表达式简单解析
2、正则表达式 --> NFA --> DFA
3、词法分析
4、LL文法消除左递归、提取公因式
5、LL文法解析(试验) --> SyntacticLLParser.syntaxParseByLL()
6、LR(1)文法解析(可用) --> SyntacticLRParser.syntaxParseLR()
7、语义动作监听器
8、中间码生成
9、SimplerJava翻译
    @词法文件 --> src/resources/SimpleJavaLexical.txt
    @文法文件 --> src/resources/SimpleJavaSyntax.txt

## 查看功能 --> ParseChartUtils
1、NFA、DFA关系转换图
2、LR文法项集状态图
3、LR分析表
4、语法分析树、注释语法树、抽象语法树结构图

