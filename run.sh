echo "Running Compiler"
javac Compiler.java
java Compiler $1 "Output.java"

javac "Output.java"
java "Output"

sleep 10