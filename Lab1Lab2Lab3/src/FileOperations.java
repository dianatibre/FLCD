import java.io.*;
import java.util.List;
import java.util.Scanner;

public class FileOperations {

    public static Scanner readFile(String file) {
        try {
            File myObj = new File(file);
            Scanner myReader = new Scanner(myObj);
            return myReader;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    public static void writeFile(String file, Object what) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(what.toString());
        writer.newLine();
        writer.close();
    }

    /*
    public static String writePIF(PIF pif, Codification c) {
        List<List<Integer>> tokens = pif.getTokens();
        String str = "\tPIF \n\t--------\n";
        for (List<Integer> token : tokens)
            if (token.get(0) < 10) {
                str += c.getCodificationTable().get(token.get(0)) + "     |     " + token.get(1) + "\n";
            } else {
                str += c.getCodificationTable().get(token.get(0)) + "    |     " + token.get(1) + "\n";
            }
        str += "\t--------\n";
        return str;
    }

     */
}
