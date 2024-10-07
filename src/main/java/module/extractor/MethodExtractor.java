package module.extractor;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MethodExtractor {

    public static void main(String[] args) throws IOException {

        FileInputStream in = new FileInputStream(new File("src/main/java/module/extractor/MethodExtractor.java"));

        CompilationUnit cu = StaticJavaParser.parse(in);

        cu.accept(new MethodVisitor(), null);
    }

    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodDeclaration md, Void arg) {
            super.visit(md, arg);

            // メソッドの再現
            StringBuilder recreatedMethod = new StringBuilder();

            md.getModifiers().forEach(modifier -> recreatedMethod.append(modifier).append(" "));

            recreatedMethod.append(md.getType()).append(" ");

            recreatedMethod.append(md.getName()).append("(");

            md.getParameters().forEach(parameter -> {
                recreatedMethod.append(parameter.getType()).append(" ").append(parameter.getName()).append(", ");
            });

            if (!md.getParameters().isEmpty()) {
                recreatedMethod.setLength(recreatedMethod.length() - 2);
            }

            recreatedMethod.append(")");

            if (!md.getThrownExceptions().isEmpty()) {
                recreatedMethod.append(" throws ");
                md.getThrownExceptions().forEach(exception -> recreatedMethod.append(exception).append(", "));
                recreatedMethod.setLength(recreatedMethod.length() - 2); // 最後のカンマを削除
            }

            if (md.getBody().isPresent()) {
                recreatedMethod.append(" {\n");
                recreatedMethod.append(md.getBody().get().toString());
                recreatedMethod.append("}");
            } else {
                recreatedMethod.append(";");
            }

            System.out.println(recreatedMethod.toString());
        }
    }
}

