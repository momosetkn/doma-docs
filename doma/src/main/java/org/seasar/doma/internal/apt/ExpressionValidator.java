package org.seasar.doma.internal.apt;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.declaration.ConstructorDeclaration;
import org.seasar.doma.internal.apt.declaration.FieldDeclaration;
import org.seasar.doma.internal.apt.declaration.MethodDeclaration;
import org.seasar.doma.internal.apt.declaration.TypeDeclaration;
import org.seasar.doma.internal.expr.node.AddOperatorNode;
import org.seasar.doma.internal.expr.node.AndOperatorNode;
import org.seasar.doma.internal.expr.node.ArithmeticOperatorNode;
import org.seasar.doma.internal.expr.node.CommaOperatorNode;
import org.seasar.doma.internal.expr.node.ComparisonOperatorNode;
import org.seasar.doma.internal.expr.node.DivideOperatorNode;
import org.seasar.doma.internal.expr.node.EmptyNode;
import org.seasar.doma.internal.expr.node.EqOperatorNode;
import org.seasar.doma.internal.expr.node.ExpressionLocation;
import org.seasar.doma.internal.expr.node.ExpressionNode;
import org.seasar.doma.internal.expr.node.ExpressionNodeVisitor;
import org.seasar.doma.internal.expr.node.FieldOperatorNode;
import org.seasar.doma.internal.expr.node.GeOperatorNode;
import org.seasar.doma.internal.expr.node.GtOperatorNode;
import org.seasar.doma.internal.expr.node.LeOperatorNode;
import org.seasar.doma.internal.expr.node.LiteralNode;
import org.seasar.doma.internal.expr.node.LogicalBinaryOperatorNode;
import org.seasar.doma.internal.expr.node.LtOperatorNode;
import org.seasar.doma.internal.expr.node.MethodOperatorNode;
import org.seasar.doma.internal.expr.node.MultiplyOperatorNode;
import org.seasar.doma.internal.expr.node.NeOperatorNode;
import org.seasar.doma.internal.expr.node.NewOperatorNode;
import org.seasar.doma.internal.expr.node.NotOperatorNode;
import org.seasar.doma.internal.expr.node.OrOperatorNode;
import org.seasar.doma.internal.expr.node.ParensNode;
import org.seasar.doma.internal.expr.node.SubtractOperatorNode;
import org.seasar.doma.internal.expr.node.VariableNode;
import org.seasar.doma.internal.util.ClassUtil;
import org.seasar.doma.message.DomaMessageCode;

public class ExpressionValidator implements
        ExpressionNodeVisitor<TypeDeclaration, Void> {

    protected final ProcessingEnvironment env;

    protected final ExecutableElement methodElement;

    protected final Map<String, TypeMirror> parameterTypeMap;

    protected final TypeDeclaration unknownTypeDeclaration;

    public ExpressionValidator(ProcessingEnvironment env,
            ExecutableElement methodElement,
            Map<String, TypeMirror> parameterTypeMap) {
        assertNotNull(env, methodElement, parameterTypeMap);
        this.env = env;
        this.methodElement = methodElement;
        this.parameterTypeMap = new HashMap<String, TypeMirror>(
                parameterTypeMap);
        this.unknownTypeDeclaration = TypeDeclaration.newUnknownInstance(env);
    }

    public TypeDeclaration validate(ExpressionNode node) {
        TypeDeclaration result = validateInternal(node);
        return result;
    }

    protected TypeDeclaration validateInternal(ExpressionNode node) {
        return node.accept(this, null);
    }

    @Override
    public TypeDeclaration visitEqOperatorNode(EqOperatorNode node, Void p) {
        return handleComparisonOperation(node, p);
    }

    @Override
    public TypeDeclaration visitNeOperatorNode(NeOperatorNode node, Void p) {
        return handleComparisonOperation(node, p);
    }

    @Override
    public TypeDeclaration visitGeOperatorNode(GeOperatorNode node, Void p) {
        return handleComparisonOperation(node, p);
    }

    @Override
    public TypeDeclaration visitGtOperatorNode(GtOperatorNode node, Void p) {
        return handleComparisonOperation(node, p);
    }

    @Override
    public TypeDeclaration visitLeOperatorNode(LeOperatorNode node, Void p) {
        return handleComparisonOperation(node, p);
    }

    @Override
    public TypeDeclaration visitLtOperatorNode(LtOperatorNode node, Void p) {
        return handleComparisonOperation(node, p);
    }

    protected TypeDeclaration handleComparisonOperation(
            ComparisonOperatorNode node, Void p) {
        TypeDeclaration left = node.getLeftNode().accept(this, p);
        TypeDeclaration right = node.getRightNode().accept(this, p);
        if (left.isNullType() || right.isNullType() || left.equals(right)) {
            return TypeDeclaration.newBooleanInstance(env);
        }
        ExpressionLocation location = node.getLocation();
        throw new AptException(DomaMessageCode.DOMA4116, env, methodElement,
                location.getExpression(), location.getPosition(), node
                        .getExpression(), node.getLeftNode().toString(), node
                        .getRightNode().toString());
    }

    @Override
    public TypeDeclaration visitAndOperatorNode(AndOperatorNode node, Void p) {
        return handleLogicalBinaryOperatorNode(node, p);
    }

    @Override
    public TypeDeclaration visitOrOperatorNode(OrOperatorNode node, Void p) {
        return handleLogicalBinaryOperatorNode(node, p);
    }

    protected TypeDeclaration handleLogicalBinaryOperatorNode(
            LogicalBinaryOperatorNode node, Void p) {
        TypeDeclaration left = node.getLeftNode().accept(this, p);
        TypeDeclaration right = node.getRightNode().accept(this, p);
        if (!left.isBooleanType()) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(DomaMessageCode.DOMA4117, env,
                    methodElement, location.getExpression(), location
                            .getPosition(), node.getExpression(), node
                            .getLeftNode().toString());
        }
        if (!right.isBooleanType()) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(DomaMessageCode.DOMA4118, env,
                    methodElement, location.getExpression(), location
                            .getPosition(), node.getExpression(), node
                            .getRightNode().toString());
        }
        return TypeDeclaration.newBooleanInstance(env);
    }

    @Override
    public TypeDeclaration visitNotOperatorNode(NotOperatorNode node, Void p) {
        TypeDeclaration result = node.getNode().accept(this, p);
        if (result.isBooleanType()) {
            return TypeDeclaration.newBooleanInstance(env);
        }
        ExpressionLocation location = node.getLocation();
        throw new AptException(DomaMessageCode.DOMA4119, env, methodElement,
                location.getExpression(), location.getPosition(), node
                        .getExpression(), node.getNode().toString());
    }

    @Override
    public TypeDeclaration visitAddOperatorNode(AddOperatorNode node, Void p) {
        return handleArithmeticOperatorNode(node, p);
    }

    @Override
    public TypeDeclaration visitSubtractOperatorNode(SubtractOperatorNode node,
            Void p) {
        return handleArithmeticOperatorNode(node, p);
    }

    @Override
    public TypeDeclaration visitMultiplyOperatorNode(MultiplyOperatorNode node,
            Void p) {
        return handleArithmeticOperatorNode(node, p);
    }

    @Override
    public TypeDeclaration visitDivideOperatorNode(DivideOperatorNode node,
            Void p) {
        return handleArithmeticOperatorNode(node, p);
    }

    protected TypeDeclaration handleArithmeticOperatorNode(
            ArithmeticOperatorNode node, Void p) {
        TypeDeclaration left = node.getLeftNode().accept(this, p);
        TypeDeclaration right = node.getRightNode().accept(this, p);
        if (!left.isNumberType()) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(DomaMessageCode.DOMA4120, env,
                    methodElement, location.getExpression(), location
                            .getPosition(), node.getExpression(), node
                            .getLeftNode().toString());
        }
        if (!right.isNumberType()) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(DomaMessageCode.DOMA4121, env,
                    methodElement, location.getExpression(), location
                            .getPosition(), node.getExpression(), node
                            .getRightNode().toString());
        }
        return TypeDeclaration.newNumberInstance(left, right, env);
    }

    @Override
    public TypeDeclaration visitLiteralNode(LiteralNode node, Void p) {
        Class<?> clazz = ClassUtil.getWrapperClassIfPrimitive(node
                .getValueClass());
        TypeElement typeElement = ElementUtil.getTypeElement(clazz, env);
        if (typeElement == null) {
            throw new AptIllegalStateException();
        }
        return TypeDeclaration.newInstance(typeElement, env);
    }

    @Override
    public TypeDeclaration visitParensNode(ParensNode node, Void p) {
        return node.getNode().accept(this, p);
    }

    @Override
    public TypeDeclaration visitNewOperatorNode(NewOperatorNode node, Void p) {
        node.getParametersNode().accept(this, p);

        List<TypeDeclaration> parameterTypeDeclarations = new ParameterCollector()
                .collect(node.getParametersNode());

        String className = node.getClassName();
        TypeElement typeElement = ElementUtil.getTypeElement(className, env);
        TypeDeclaration typeDeclaration = TypeDeclaration.newInstance(
                typeElement, env);

        ConstructorDeclaration constructorDeclaration = typeDeclaration
                .getConstructorDeclarations(parameterTypeDeclarations);
        if (constructorDeclaration != null) {
            return typeDeclaration;
        }
        ExpressionLocation location = node.getLocation();
        throw new AptException(DomaMessageCode.DOMA4115, env, methodElement,
                location.getExpression(), location.getPosition(), className);
    }

    @Override
    public TypeDeclaration visitCommaOperatorNode(CommaOperatorNode node, Void p) {
        return unknownTypeDeclaration;
    }

    @Override
    public TypeDeclaration visitEmptyNode(EmptyNode node, Void p) {
        return unknownTypeDeclaration;
    }

    @Override
    public TypeDeclaration visitMethodOperatorNode(MethodOperatorNode node,
            Void p) {
        TypeDeclaration typeDeclaration = node.getTargetObjectNode().accept(
                this, p);
        List<TypeDeclaration> parameterTypeDeclarations = new ParameterCollector()
                .collect(node.getParametersNode());
        String methodName = node.getMethodName();
        List<MethodDeclaration> methodDeclarations = typeDeclaration
                .getMethodDeclarations(methodName, parameterTypeDeclarations);
        if (methodDeclarations.size() == 0) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(DomaMessageCode.DOMA4071, env,
                    methodElement, location.getExpression(), location
                            .getPosition(), node.getTargetObjectNode()
                            .getExpression(), typeDeclaration
                            .getQualifiedName(), parameterTypeDeclarations
                            .size(), methodName);
        }
        if (methodDeclarations.size() == 1) {
            MethodDeclaration methodDeclaration = methodDeclarations.get(0);
            TypeDeclaration returnTypeDeclaration = methodDeclaration
                    .getReturnTypeDeclaration();
            if (returnTypeDeclaration != null) {
                return returnTypeDeclaration;
            }
        }
        throw new AptIllegalStateException();
    }

    @Override
    public TypeDeclaration visitFieldOperatorNode(FieldOperatorNode node, Void p) {
        TypeDeclaration typeDeclaration = node.getTargetObjectNode().accept(
                this, p);
        String fieldName = node.getFieldName();
        FieldDeclaration fieldDeclarations = typeDeclaration
                .getFieldDeclaration(fieldName);
        if (fieldDeclarations != null) {
            TypeDeclaration fieldTypeDeclaration = fieldDeclarations
                    .getTypeDeclaration();
            if (fieldTypeDeclaration != null) {
                return fieldTypeDeclaration;
            }
        }
        ExpressionLocation location = node.getLocation();
        throw new AptException(DomaMessageCode.DOMA4114, env, methodElement,
                location.getExpression(), location.getPosition(), node
                        .getTargetObjectNode().getExpression(), typeDeclaration
                        .getTypeElement().getQualifiedName(), fieldName);
    }

    @Override
    public TypeDeclaration visitVariableNode(VariableNode node, Void p) {
        String variableName = node.getExpression();
        TypeMirror typeMirror = parameterTypeMap.get(variableName);
        if (typeMirror == null) {
            ExpressionLocation location = node.getLocation();
            throw new AptException(DomaMessageCode.DOMA4067, env,
                    methodElement, location.getExpression(), location
                            .getPosition(), variableName);
        }
        TypeElement typeElement = TypeUtil.toTypeElement(typeMirror, env);
        if (typeElement == null) {
            return unknownTypeDeclaration;
        }
        return TypeDeclaration.newInstance(typeElement, env);
    }

    protected class ParameterCollector implements
            ExpressionNodeVisitor<Void, List<TypeDeclaration>> {

        public List<TypeDeclaration> collect(ExpressionNode node) {
            List<TypeDeclaration> results = new ArrayList<TypeDeclaration>();
            node.accept(this, results);
            return results;
        }

        @Override
        public Void visitEqOperatorNode(EqOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitNeOperatorNode(NeOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitGeOperatorNode(GeOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitGtOperatorNode(GtOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitLeOperatorNode(LeOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitLtOperatorNode(LtOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitCommaOperatorNode(CommaOperatorNode node,
                List<TypeDeclaration> p) {
            for (ExpressionNode expressionNode : node.getNodes()) {
                expressionNode.accept(this, p);
            }
            return null;
        }

        @Override
        public Void visitLiteralNode(LiteralNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitVariableNode(VariableNode node, List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitOrOperatorNode(OrOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitAndOperatorNode(AndOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitNotOperatorNode(NotOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitAddOperatorNode(AddOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitSubtractOperatorNode(SubtractOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitMultiplyOperatorNode(MultiplyOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitDivideOperatorNode(DivideOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitNewOperatorNode(NewOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitMethodOperatorNode(MethodOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitFieldOperatorNode(FieldOperatorNode node,
                List<TypeDeclaration> p) {
            validate(node, p);
            return null;
        }

        @Override
        public Void visitParensNode(ParensNode node, List<TypeDeclaration> p) {
            node.getNode().accept(this, p);
            return null;
        }

        @Override
        public Void visitEmptyNode(EmptyNode node, List<TypeDeclaration> p) {
            return null;
        }

        protected void validate(ExpressionNode node, List<TypeDeclaration> p) {
            TypeDeclaration result = ExpressionValidator.this
                    .validateInternal(node);
            p.add(result);
        }

    }

}