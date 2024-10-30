public class Compiler{
    public static void main(String[] args){

        if(args.length != 2){
            System.out.print("Error: Compiler needs two files as argument ");
            System.out.println("input file (.ty) output file (.js)");
            System.exit(1);
        }

        System.out.println("Starting compiler");
        String source = CompilerUtils.readFile(args[0]);

        if (source == null)
            System.exit(1);

        Lexer lexer = new Lexer(source);
        Emitter emitter = new Emitter(args[1]);
        Parser parser = new Parser(lexer, emitter);

        parser.program();
        emitter.writeFile();
        System.out.println("Compiling completed\n\n");
    };
}