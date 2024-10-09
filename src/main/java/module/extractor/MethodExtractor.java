package module.extractor;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

public class MethodExtractor {

    public static void init() throws IOException {
        FileWriter csvWriter = new FileWriter("methods.csv");

        csvWriter.append("Input,filePath,methodName,args\n");

        csvWriter.flush();
        csvWriter.close();
    }

    public static void extract(String filePath) throws IOException {

        FileInputStream in = new FileInputStream(new File(filePath));

        CompilationUnit cu = StaticJavaParser.parse(in);

        FileWriter csvWriter = new FileWriter("methods.csv");

        cu.accept(new MethodVisitor(csvWriter, filePath), null);

        csvWriter.flush();
        csvWriter.close();
    }

    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
        private FileWriter csvWriter;

        private String filePath;

        public MethodVisitor(FileWriter csvWriter, String filePath) {
            this.csvWriter = csvWriter;
            this.filePath = filePath;
        }

        @Override
        public void visit(MethodDeclaration md, Void arg) {
            super.visit(md, arg);

            try {
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

                recreatedMethod.append(") ");

                if (!md.getThrownExceptions().isEmpty()) {
                    recreatedMethod.append(" throws ");
                    md.getThrownExceptions().forEach(exception -> recreatedMethod.append(exception).append(", "));
                    recreatedMethod.setLength(recreatedMethod.length() - 2); // 最後のカンマを削除
                }

                if (md.getBody().isPresent()) {
//                    recreatedMethod.append(" {\n");
                    recreatedMethod.append(md.getBody().get().toString());
//                    recreatedMethod.append("}");
                } else {
                    recreatedMethod.append(";");
                }

                csvWriter.append("\"" + recreatedMethod.toString().replaceAll("\\s+", " ").replace("\n", "") + "\",");

                csvWriter.append("\"" + filePath + "\",");

                String methodName = md.getName().asString();

                String args = md.getParameters().stream().map(parameter -> parameter.getName().asString()).collect(Collectors.joining(", "));

                csvWriter.append("\"" + methodName + "\",");

                csvWriter.append("\"" + args + "\"\n");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

