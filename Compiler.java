import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Compiler{
    public static void main(String[] args){

        if(args.length != 1){
            System.out.println("Error: Compiler needs source file as argument.");
            System.exit(1);
        }

        String source = readFile(args[0]);
        System.out.println(source);
        Lexer lexer = new Lexer(source);

        Token token = lexer.getToken();

        while (token.tokenType != TokenType.EOF){
            System.out.println("TokenType." + token.tokenType);
            token = lexer.getToken();
        }
    };

    private static String readFile(String fileName) {
        File file = new File(fileName);
        String fileText = "";
        try{
            Scanner scanner = new Scanner(file);
            while(scanner.hasNext()){
                fileText += scanner.nextLine();
                fileText += '\n';
            }
            scanner.close();
            return fileText;
        }catch (Exception e){
            System.out.println("Error: File not found");
            return null;
        }
    }
}