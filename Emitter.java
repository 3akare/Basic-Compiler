import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Emitter {
    protected final String filename;
    private String header;
    private String code;

    public Emitter(String filename){
        if(!filename.endsWith(".js"))
            abort("Invalid file type: " + filename);
        this.filename = filename;
        this.header = "";
        this.code = "";
    }

    protected void emit(String code){
        this.code += code;
    }

    protected void emitLine(String code){
        this.code += code + '\n';
    }

    protected void headerLine(String code){
        this.header += code + '\n';
    }

    private void abort(String message){
        System.out.println("Emitting Error: " + message);
        System.exit(1);
    }

    protected void writeFile(){
        File file = new File(filename);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(this.header + this.code);
            writer.close();
        } catch (IOException e) {
            abort("File not found: "+ filename);
        }
    }
}
