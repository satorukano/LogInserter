package run;

import module.extractor.MethodExtractor;

public class Main {
    public static void main(String[] args) {
        try {
            MethodExtractor.extract("example.java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}