package gian.compiler.language.simplejava.action;

import gian.compiler.language.simplejava.JavaConstants;
import gian.compiler.language.simplejava.bean.ClazzField;
import gian.compiler.language.simplejava.bean.VariableInitInfo;
import gian.compiler.language.simplejava.bean.VariableType;
import gian.compiler.language.simplejava.env.JavaEnvironment;
import gian.compiler.language.simplejava.exception.ClazzTransformException;
import gian.compiler.front.lexical.transform.LexConstants;
import gian.compiler.front.syntactic.element.SyntaxTree;
import gian.compiler.front.syntaxDirected.SyntaxDirectedContext;
import gian.compiler.front.syntaxDirected.SyntaxDirectedListener;

import java.util.List;

/**
 * Created by gaojian on 2019/3/28.
 */
public class FieldDeclarationAction {

    public static String product = "fieldDeclaration → modifierDeclaration typeDeclaration Identifier variableInitializer ;";

    public static class ModifierDeclarationListener extends SyntaxDirectedListener{

        public ModifierDeclarationListener(){
            this.matchProductTag = product;
            this.matchSymbol = "modifierDeclaration";
            this.matchIndex = 0;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }
    }

    public static class TypeDeclarationListener extends SyntaxDirectedListener{

        public TypeDeclarationListener(){
            this.matchProductTag = product;
            this.matchSymbol = "typeDeclaration";
            this.matchIndex = 1;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }
    }

    public static class IdentifierListener extends SyntaxDirectedListener{

        public IdentifierListener(){
            this.matchProductTag = product;
            this.matchSymbol = "Identifier";
            this.matchIndex = 2;
            this.isLeaf = true;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }
    }

    public static class VariableInitializer extends SyntaxDirectedListener{

        public VariableInitializer(){
            this.matchProductTag = product;
            this.matchSymbol = "variableInitializer";
            this.matchIndex = 3;
            this.isLeaf = false;
        }

        @Override
        public String enterSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {
            return null;
        }

        @Override
        public String exitSyntaxSymbol(SyntaxDirectedContext context, SyntaxTree.SyntaxTreeNode currentTreeNode, Integer currentIndex) {

            String modifier = (String) context.getBrotherNodeList().get(currentIndex - 3).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.MODIFIER);
            VariableType variableType = (VariableType) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.VARIABLE_TYPE);
            String variableId = currentTreeNode.getIdToken().getToken();

            // TODO 设置变量的初始化信息，包括地址、变量数据等
            VariableInitInfo variableInitInfo = (VariableInitInfo) context.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.VARIABLE_INIT_INFO);
            VariableType initVariableType = variableInitInfo.getInitVariableType();

            // TODO 获取初始化信息的数据类型，方便类型校验
            VariableType targetVariabletype = (VariableType) context.getBrotherNodeList().get(currentIndex - 2).getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.VARIABLE_TYPE);
            if(!initVariableType.equals(targetVariabletype)){
                throw new ClazzTransformException("初始化类型不匹配");
            }

            ClazzField clazzField = new ClazzField();
            clazzField.setPermission(modifier);
            clazzField.setVariableType(variableType);
            clazzField.setFieldName(variableId);
            clazzField.setVariableInitInfo(variableInitInfo);

            // TODO 将声明的变量存入到类实例作用域内
            JavaEnvironment environment = (JavaEnvironment) context.getGlobalPropertyMap().get(JavaConstants.CURRENT_ENV);
            environment.getPropertyMap().put(clazzField.getFieldName(), clazzField);

            List<ClazzField> fieldList = (List<ClazzField>) currentTreeNode.getPropertyMap().get(LexConstants.SYNTAX_DIRECT_PROPERTY_SYN).get(JavaConstants.FIELD_LIST);
            fieldList.add(clazzField);

            return null;
        }
    }

}