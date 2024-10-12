public class Lexer {
    private final String source;
    private char curChar;
    private int curPos;

    public Lexer(String source){
        this.source = source + '\n';
        this.curChar = ' ';
        this.curPos = -1;
        nextChar();
    }

    private void nextChar(){
        this.curPos += 1;
        if (this.curPos >= this.source.length())
            this.curChar = '\0';
        else
            this.curChar = this.source.charAt(curPos);
    }

    private char peek(){
        if(this.curPos + 1 >= this.source.length())
                return '\0';
        return this.source.charAt(curPos + 1);
    }

    private void abort(String message){
        System.out.println("Lexing Error: " + message);
        System.exit(1);
    }

    private void skipWhitespace(){
        while (this.curChar == ' ' || this.curChar == '\t' || this.curChar == '\r')
            nextChar();
    }

    private void skipComment(){
        if (this.curChar == '#') {
                while (this.curChar != '\n')
                    nextChar();
        }
    }

    protected Token getToken(){
        skipWhitespace();
        skipComment();
        Token token = switch (this.curChar) {
            case '+' -> new Token(this.curChar, TokenType.PLUS);
            case '-' -> new Token(this.curChar, TokenType.MINUS);
            case '*' -> new Token(this.curChar, TokenType.ASTERISK);
            case '/' -> new Token(this.curChar, TokenType.SLASH);
            case '=' -> {
                if (peek() == '='){
                    char lastChar = this.curChar;
                    nextChar();
                    yield new Token(this.curChar + lastChar, TokenType.EQEQ);
                }
                else
                    yield new Token(this.curChar, TokenType.EQ);
            }
            case '>' -> {
                if (peek() == '='){
                    char lastChar = this.curChar;
                    nextChar();
                    yield new Token(this.curChar + lastChar, TokenType.GTEQ);
                }
                else
                    yield new Token(this.curChar, TokenType.GT);
            }
            case '<' -> {
                if (peek() == '='){
                    char lastChar = this.curChar;
                    nextChar();
                    yield new Token(this.curChar + lastChar, TokenType.LTEQ);
                }
                else
                    yield new Token(this.curChar, TokenType.LT);
            }
            case '!' -> {
                if (peek() == '='){
                    char lastChar = this.curChar;
                    nextChar();
                    yield new Token(this.curChar + lastChar, TokenType.NOTEQ);
                }
                else {
                    abort("Unknown token: " + this.curChar);
                    yield null;
                }

            }
            case '\n' -> new Token(this.curChar, TokenType.NEWLINE);
            case '\0' -> new Token(this.curChar, TokenType.EOF);
            case '\"' -> {
                nextChar();
                int startPos = this.curPos;

                while (this.curChar != '\"'){
                    if (this.curChar == '\r' || this.curChar == '\\' || this.curChar == '\t' || this.curChar == '%')
                        abort("Illegal character in string");
                    nextChar();
                }

                String tokenText = this.source.substring(startPos, this.curPos);
                yield new Token(tokenText, TokenType.STRING);
            }
            default -> {
                if(Character.isDigit(this.curChar)){
                    int startPos = this.curPos;
                    while (Character.isDigit(peek()))
                        nextChar();
                    if (peek() == '.'){
                        nextChar();
                        if (!Character.isDigit(peek()))
                            abort("Illegal character in number: " + peek());
                        while (Character.isDigit(peek()))
                            nextChar();
                    }

                    String tokenText = this.source.substring(startPos, this.curPos + 1);
                    yield new Token(tokenText, TokenType.NUMBER);

                } else if (Character.isAlphabetic(this.curChar)) {
                    int startPos = this.curPos;
                    while (Character.isLetterOrDigit(peek()))
                        nextChar();
                    String tokenText = this.source.substring(startPos, this.curPos + 1);
                    TokenType keyword = Token.checkIfKeyword(tokenText);
                    if (keyword == null)
                        yield new Token(tokenText, TokenType.IDENT);
                    else
                        yield new Token(tokenText, keyword);
                }
                abort("Unknown token: " + this.curChar);
                yield null;
            }
        };

        this.nextChar();
        return token;
    }
}
