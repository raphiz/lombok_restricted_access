package li.raphael.lombock.hello;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.util.List;

import lombok.core.AST.Kind;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.javac.Javac;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.handlers.JavacHandlerUtil;
import static lombok.javac.handlers.JavacHandlerUtil.*;
import org.mangosdk.spi.ProviderFor;

@ProviderFor(JavacAnnotationHandler.class)
@HandlerPriority(value = 512)
public class HandleRestrictedAccess extends JavacAnnotationHandler<RestrictedAccess> {

    @Override
    public void handle(AnnotationValues<RestrictedAccess> annotation, JCAnnotation ast, JavacNode annotationNode) {
        // Only allow this on methods
        if (annotationNode.up().getKind() != Kind.METHOD) {
            return;
        }

        // Get Package name from the annotation value
        String packageName = annotation.getRawExpressions("value").get(0);
        packageName = packageName.substring(1, packageName.length() - 1);

        JCMethodDecl declaration;

        try {
            declaration = (JCMethodDecl) annotationNode.up().get();
        } catch (Exception e) {
            return;
        }

        JavacTreeMaker maker = annotationNode.up().getTreeMaker();

        JCExpression exType = genTypeRef(annotationNode, "java.lang.RuntimeException");

        // Create new exception instance
        JCExpression exception = maker.NewClass(null, List.<JCExpression>nil(), exType, List.<JCExpression>of(maker.Literal("No access!")), null);
        // Create throw statement
        JCStatement throwStatement = maker.Throw(exception);

        // Invoke AccessRestrictionUtils.isAllowed("..")
        JCExpression method_access_allowed_method
                = JavacHandlerUtil.chainDotsString(annotationNode, "li.raphael.lombock.hello.AccessRestrictionUtils.isAllowed");
        List<JCExpression> method_access_allowed_parameters = List.<JCExpression>of(maker.Literal(packageName));
        JCMethodInvocation method_access_allowed_invocation
                = maker.Apply(List.<JCExpression>nil(), method_access_allowed_method, method_access_allowed_parameters);

        // Make parenthesis
        JCTree.JCParens parenthesis = maker.Parens(maker.Unary(Javac.CTC_NOT, method_access_allowed_invocation));

        // Create IF statement
        JCTree.JCIf stmt = maker.If(parenthesis, throwStatement, null);

        // Prepend the new statement
        List<JCStatement> newList = List.nil();
        newList = newList.prepend(stmt);
        newList = newList.appendList(declaration.body.stats);
        declaration.body.stats = newList;

    }

}
