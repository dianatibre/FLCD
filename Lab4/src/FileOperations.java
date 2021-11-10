import java.io.*;
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
}