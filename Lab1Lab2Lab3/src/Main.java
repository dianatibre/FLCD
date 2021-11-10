import java.io.*;
import java.text.MessageFormat;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    public Main() {
    }

    public static void main(String[] args) throws IOException {
        /*
        // TEST - read file, populate symTable
        ST symTable = new ST(19);
        FileOperations fileOperations = new FileOperations();
        Scanner tokens = fileOperations.readFile("token.in");
        fileOperations.populateST(tokens, symTable);

        System.out.println(symTable);

         */


        /*
        // TEST - getStringToken, getToken, tokenGenerator, checkOperator
        FileOperations fileOperations = new FileOperations();
        Scanner test = fileOperations.readFile("p1.txt");
        Codification codification = new Codification();
        while (test.hasNextLine()) {
            String data = test.nextLine();
            System.out.println(codification.tokenGenerator(data));
            //System.out.println(codification.getToken(data));
            //System.out.println(codification.getStringToken(data));
        }
        //System.out.println(codification.checkOperator("++"));
        //System.out.println(codification.getOperatorToken("a<b"));


         */
        Codification codification = new Codification();
        PIF pif = new PIF();
        ST st = new ST(19);
        AtomicInteger lineNumber = new AtomicInteger();
        AtomicBoolean correct = new AtomicBoolean(true);
        AtomicReference<String> text = new AtomicReference<>("");
        try (Scanner reader = FileOperations.readFile("p3err.txt")) {
            while (reader.hasNextLine()) {
                text.set(reader.nextLine());
                //System.out.println(text);
                lineNumber.getAndIncrement();
                List<String> tokens = codification.tokenGenerator(text.get());
                //System.out.println(tokens);
                for (String s : tokens) {
                    if (codification.checkSeparator(s) || codification.checkReservedWord(s) || codification.checkOperator(s)) {
                        pif.addToken(codification.getCodificationTable().get(s), -1);
                    } else if (codification.checkIdentifier(s)) {
                        pif.addToken(0, st.add(s));
                    } else if (codification.checkConstant(s)) {
                        pif.addToken(1, st.add(s));
                    } else {
                        System.out.println(MessageFormat.format("Lexical error: {0} at location {1}",
                                s,
                                lineNumber));
                        correct.set(false);
                    }
                }
            }
        }
        if (correct.get()) {
            System.out.println("Lexically correct!");

            FileOperations.writeFile("PIF.out", pif);
            FileOperations.writeFile("ST.out", st);
        }
        //System.out.println(FileOperations.writePIF(pif, codification));
        System.out.println(pif);
        System.out.println(st);
        System.out.println(codification);

    }
}
