import java.util.HashSet;
import java.util.Set;

public class Parser {
    protected Lexer lexer;
    protected Token curToken;
    protected Token peekToken;
    protected Set<Object> symbols;
    protected Set<Object> labelsDeclared;
    protected Set<Object> labelsGOTOed;

    public Parser(Lexer lexer){
        this.lexer = lexer;

        symbols = new HashSet<>();
        labelsDeclared = new HashSet<>();
        labelsGOTOed = new HashSet<>();

        this.curToken = null;
        this.peekToken = null;
        nextToken();
        nextToken();
    }

    private boolean checkToken(TokenType tokenType){
        return tokenType == this.curToken.tokenType;
    }

    private boolean checkPeek(TokenType tokenType){
        return tokenType == this.peekToken.tokenType;
    }

    private void match(TokenType tokenType){
        if(!checkToken(tokenType)){
            abort("Expected " + tokenType.name() + ", got " + ((TokenType)curToken.tokenType).name());
        }
        nextToken();
    }

    private void nextToken(){
        this.curToken = this.peekToken;
        this.peekToken = lexer.getToken();
    }

    private void abort(String message){
        System.out.println("Parser Error: " + message);
        System.exit(1);
    }

    protected void program(){
        System.out.println("PROGRAM");
        while(checkToken(TokenType.NEWLINE)){
            nextToken();
        }
        while(!checkToken(TokenType.EOF)){
            statement();
        }

        for (Object label: this.labelsGOTOed){
            if(!this.labelsDeclared.contains(label))
                abort("Attempting to GOTO to undeclared label: " + label);
        }
    }

    public void statement(){
        switch ((TokenType) this.curToken.tokenType){
            case PRINT -> {
                System.out.println("STATEMENT-PRINT");
                nextToken();
                if(checkToken(TokenType.STRING)){
                    nextToken();
                }
                else {
                    expression();
                }
            }
            case IF -> {
                System.out.println("STATEMENT-IF");
                nextToken();
                comparison();
                match(TokenType.THEN);
                nl();
                while(!checkToken(TokenType.ENDIF)){
                    statement();
                }
                match(TokenType.ENDIF);
            }
            case WHILE -> {
                System.out.println("STATEMENT-WHILE");
                nextToken();
                comparison();

                match(TokenType.REPEAT);
                nl();
                while(!checkToken(TokenType.ENDWHILE)){
                    statement();
                }
                match(TokenType.ENDWHILE);
            }
            case LABEL -> {
                System.out.println("STATEMENT-LABEL");
                nextToken();

                if (this.labelsDeclared.contains(this.curToken.tokenText))
                    abort("Label already exists: " + this.curToken.tokenText);
                this.labelsDeclared.add(this.curToken.tokenText);

                match(TokenType.IDENT);
            }
            case GOTO -> {
                System.out.println("STATEMENT-GOTO");
                nextToken();

                this.labelsGOTOed.add(this.curToken.tokenText);

                match(TokenType.IDENT);
            }
            case LET -> {
                System.out.println("STATEMENT-LET");
                nextToken();

                this.symbols.add(this.curToken.tokenText);

                match(TokenType.IDENT);
                match(TokenType.EQ);
                expression();
            }
            case INPUT -> {
                System.out.println("STATEMENT-INPUT");
                nextToken();

                this.symbols.add(this.curToken.tokenText);

                match(TokenType.IDENT);
            }
            default -> abort("Invalid statement at : " + this.curToken.tokenText +" (" + this.curToken.tokenType +")");
        }
        nl();
    }

    private void nl(){
        System.out.println("NEWLINE");
        match(TokenType.NEWLINE);
        while (checkToken(TokenType.NEWLINE))
            nextToken();
    }

    private void comparison(){
        System.out.println("COMPARISON");
        expression();
        if(isComparisonOperator()){
            nextToken();
            expression();
        }
        else
            abort("Expected comparison operator at: " + this.curToken.tokenText);
        while (isComparisonOperator()){
            nextToken();
            expression();
        }
    }

    private boolean isComparisonOperator(){
        return checkToken(TokenType.EQEQ) || checkToken(TokenType.NOTEQ) ||
                checkToken(TokenType.LT) || checkToken(TokenType.LTEQ) ||
                checkToken(TokenType.GT) || checkToken(TokenType.GTEQ);
    }

    private void expression(){
        System.out.println("EXPRESSION");
        term();
        while (checkToken(TokenType.PLUS) || checkToken(TokenType.MINUS)){
            nextToken();
            term();
        }
    }

    private void term(){
        System.out.println("TERM");
        unary();
        while(checkToken(TokenType.ASTERISK) || checkToken(TokenType.SLASH)){
            nextToken();
            unary();
        }
    }

    private void unary(){
        System.out.println("UNARY");
        if (checkToken(TokenType.PLUS) || checkToken(TokenType.MINUS))
            nextToken();
        primary();
    }

    private void primary(){
        System.out.println("PRIMARY (" + this.curToken.tokenText + ")");
        if(checkToken(TokenType.NUMBER))
            nextToken();
        else if (checkToken(TokenType.IDENT)) {
            if(!this.symbols.contains(this.curToken.tokenText))
                abort("Referencing variable before assignment: " + this.curToken.tokenText);
            nextToken();
        }
        else
            abort("Unexpected token at " + this.curToken.tokenText);
    }
}
