import java.util.Arrays;

public class Compiler{
    public static void main(String[] args){

        if(args.length != 1){
            System.out.println("Error: Compiler needs a single source file as argument.");
            System.exit(1);
        }

        String source = CompilerUtils.readFile(args[0]);

        if (source == null)
            System.exit(1);

        Lexer lexer = new Lexer(source);
        Parser parser = new Parser(lexer);
        parser.program();
    };
}