import java.util.HashSet;
import java.util.Set;

public class Parser {
    protected Lexer lexer;
    protected Token curToken;
    protected Token peekToken;
    protected Set<Object> symbols;
    protected Set<Object> labelsDeclared;
    protected Set<Object> labelsGOTOed;
    protected Emitter emitter;

    public Parser(Lexer lexer, Emitter emitter){
        this.lexer = lexer;
        this.emitter = emitter;

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
        // tab at the beginning of a new statement
        switch ((TokenType) this.curToken.tokenType){
            case PRINT -> {
                nextToken();
                if(checkToken(TokenType.STRING)){
                    emitter.emitLine("console.log(\""+this.curToken.tokenText+"\");");
                    nextToken();
                }
                else {
                    emitter.emit("console.log(");
                    expression();
                    emitter.emitLine(");");
                }
            }
            case IF -> {
                nextToken();
                emitter.emit("if (");
                comparison();

                match(TokenType.THEN);
                nl();
                emitter.emitLine("){");
                while(!checkToken(TokenType.ENDIF)){
                    statement();
                }
                match(TokenType.ENDIF);
                emitter.emitLine("}");
            }
            case WHILE -> {
                nextToken();
                emitter.emit("while(");
                comparison();

                emitter.emitLine("){");
                match(TokenType.REPEAT);
                nl();
                while(!checkToken(TokenType.ENDWHILE)){
                    statement();
                }
                match(TokenType.ENDWHILE);
                emitter.emitLine("}");
            }
            case LABEL -> {
                abort("GOTO is no longer supported");
                nextToken();
                if (this.labelsDeclared.contains(this.curToken.tokenText))
                    abort("Label already exists: " + this.curToken.tokenText);
                this.labelsDeclared.add(this.curToken.tokenText);
                match(TokenType.IDENT);
            }
            case GOTO -> {
                abort("GOTO is no longer supported");
                nextToken();
                this.labelsGOTOed.add(this.curToken.tokenText);
                match(TokenType.IDENT);
            }
            case LET -> {
                nextToken();
                if (!this.symbols.contains(this.curToken.tokenText)){
                    this.symbols.add(this.curToken.tokenText);
                    emitter.headerLine("let " + this.curToken.tokenText + ";");
                }

                emitter.emit(this.curToken.tokenText + " = ");
                match(TokenType.IDENT);
                match(TokenType.EQ);
                expression();
                emitter.emitLine(";");
            }
            case INPUT -> {
                abort("INPUT is no longer supported");
                nextToken();

                if (!this.symbols.contains(this.curToken.tokenText)){
                    this.symbols.add(this.curToken.tokenText);
                    emitter.headerLine("let " + this.curToken.tokenText + ";");
                }
                emitter.emitLine(this.curToken.tokenText + " = input();");
                match(TokenType.IDENT);
            }
            default -> abort("Invalid statement at : " + this.curToken.tokenText +" (" + this.curToken.tokenType +")");
        }
        nl();
    }

    private void nl(){
        match(TokenType.NEWLINE);
        while (checkToken(TokenType.NEWLINE))
            nextToken();
    }

    private void comparison(){
        expression();
        if(isComparisonOperator()){
            emitter.emit(this.curToken.tokenText + "");
            nextToken();
            expression();
        }
        else
            abort("Expected comparison operator at: " + this.curToken.tokenText);
        while (isComparisonOperator()){
            emitter.emit(this.curToken.tokenText + "");
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
        term();
        while (checkToken(TokenType.PLUS) || checkToken(TokenType.MINUS)){
            emitter.emit(this.curToken.tokenText + "");
            nextToken();
            term();
        }
    }

    private void term(){
        unary();
        while(checkToken(TokenType.ASTERISK) || checkToken(TokenType.SLASH)){
            emitter.emit(this.curToken.tokenText + "");
            nextToken();
            unary();
        }
    }

    private void unary(){
        if (checkToken(TokenType.PLUS) || checkToken(TokenType.MINUS)){
            emitter.emit(this.curToken.tokenText + "");
            nextToken();
        }
        primary();
    }

    private void primary(){
        if(checkToken(TokenType.NUMBER)){
            emitter.emit(this.curToken.tokenText + "");
            nextToken();
        }
        else if (checkToken(TokenType.IDENT)) {
            if(!this.symbols.contains(this.curToken.tokenText))
                abort("Referencing variable before assignment: " + this.curToken.tokenText);
            emitter.emit(this.curToken.tokenText + "");
            nextToken();
        }
        else if (checkToken(TokenType.STRING)) {
            emitter.emit('\"'+"");
            emitter.emit(this.curToken.tokenText + "");
            emitter.emit('\"'+"");
            nextToken();
        }
        else
            abort("Unexpected token at " + this.curToken.tokenText);
    }
}
