public class Compiler{
    public static void main(String[] args){
        String source = "IF+-123 foo*THEN/";
        Lexer lexer = new Lexer(source);

        Token token = lexer.getToken();

        while (token.tokenType != TokenType.EOF){
            System.out.println("TokenType." + token.tokenType);
            token = lexer.getToken();
        }
    };
}