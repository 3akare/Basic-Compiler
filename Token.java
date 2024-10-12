public class Token{
    protected Object tokenText;
    protected Object tokenType;

    public Token(Object tokenText, Object tokenType){
        this.tokenText = tokenText;
        this.tokenType = tokenType;
    }

    protected static TokenType checkIfKeyword(String tokenText){
        for(TokenType tokenType :TokenType.values()){
            if (tokenType.name().equals(tokenText) && tokenType.ordinal() >= 5 && tokenType.ordinal() <= 15)
                return tokenType;
        }
        return null;
    }
}
