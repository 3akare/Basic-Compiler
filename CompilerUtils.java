import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CompilerUtils {
    public static String readFile(String fileName) {
        File file = new File(fileName);
        String fileText = "";
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNext()){
                fileText += scanner.nextLine();
                fileText += '\n';
            }
            scanner.close();
            return fileText;
        }
        catch (FileNotFoundException e){
            System.out.println("Error: File not found");
            return null;
        }
        catch (Exception e){
            System.out.println("Error: An error occurred when reading file: " + fileName);
            return null;
        }
    }
}
