import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CompilerUtils {
    public static String readFile(String filename){
        if(!filename.endsWith(".ty")){
            System.out.println("Invalid file type: " + filename);
            System.exit(1);
        }

        File file = new File(filename);
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
            System.out.println("Error: An error occurred when reading file: " + filename);
            return null;
        }
    }
}
