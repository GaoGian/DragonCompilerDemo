package gian.compiler.language.simplejava.action;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.bean.Clazz;
import gian.compiler.language.simplejava.bean.Variable;
import gian.compiler.language.simplejava.exception.SuperClazzNotFoundException;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gaojian on 2019/3/31.
 */
public class ExplicitConstructorInvocationAction {

    public static String product = "explicitConstructorInvocation → super ( expressionList ) ;";

    public static class ExpressionListListener extends SyntaxDirectedListener{

        public ExpressionListListener(){
            this.matchProductTag = product;
            this.matchSymbol = "expressionList";
            this.matchIndex = 2;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            Clazz clazz = (Clazz) context.getGlobalPropertyMap().get(JavaConstants.CURRENT_CLAZZ_NAME);
            Map<String, String> extendInfo = clazz.getExtendInfo();

            if(extendInfo != null){
                // code信息
                List<String> refCode = new ArrayList<>();

                // TODO 获取参数列表
                List<Variable> paramAddrList = (List<Variable>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.CALL_PARAM_LIST);
                // TODO 加上参数
                for(Variable variable : paramAddrList) {
                    String paramCode = "param " + variable.getFieldName();
                    refCode.add(paramCode);
                }

                // TODO 根据继承类的信息查找匹配的父类构造方法，如果没有则抛出异常
                // TODO 自定义方法引用
                Map<String, String> importMap = (Map<String, String>) context.getGlobalPropertyMap().get(JavaConstants.IMPORT_MAP);
                String extendClazzName = extendInfo.get(JavaConstants.CLAZZ_NAME);
                String extendClazzAllName = importMap.get(extendClazzName);
                String callCode = "call " + extendClazzAllName;
                refCode.add(callCode);

                // FIXME 调用方法校验，判断是否有匹配的方法


                // TODO 将code属性加入到方法节点
                context.getParentNode().getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).put(JavaConstants.REF_CODE_INDEX, refCode);

            }else{
                // TODO 根据继承类的信息查找匹配的父类构造方法，如果没有则抛出异常
                throw new SuperClazzNotFoundException("找不到匹配的父类构造方法");
            }

            return null;
        }
    }

}