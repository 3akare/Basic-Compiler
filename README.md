# Tiny Basic Compiler

## Purpose
The world doesn't need another BASIC compiler; there are millions out there that outperform and support more keywords compared to mine. However, this project is a significant step in my journey toward becoming a better software engineer. I built this compiler following a guide by Austin Henley (https://austinhenley.com/blog/teenytinycompiler1.html). To make it more interesting and to avoid downloading a C compiler just for this project, I decided to build my compiler in Java, with the compiler code outputting in JavaScript, as opposed to the author's Python compiler with C output. I chose JavaScript for its dynamic typing.

## Compiler Overview
The compiler transpiles code written in Tiny BASIC into Javascript, then runs the Javascript code to produce the expected output. It is broken down into three main components:

- **Lexer:** Responsible for tokenization and classification of the input code.
- **Parser:** Verifies the grammatical rules of the language.
- **Emitter:** Translates the tokens into the desired C code.

## Usage

### Compilation
#### Build Tools
- **Java:** 17.0.12
- **Node.js:** 20.18.0

### Instructions

#### MacOS and Linux
To compile and run the program, execute the following command in the terminal:
```bash
./run.sh
```

#### Windows
For Windows users, I currently don't have a `.bat` file set up. Instead, you can run the commands manually as follows:
```bash
javac Compiler.java
java Compiler [input_file].ty [output_file].js
node [output_file].js
```

## Example
Here is an example of a Tiny BASIC program and its corresponding JavaScript output.

### Tiny BASIC Program
```basic
# This program prints numbers from 1 to 10
LET N = 1

WHILE N < 10 REPEAT
    PRINT "COUNTING: "
    PRINT N
    LET N = N + 1
ENDWHILE

PRINT "DONE!"
```

### Compiled JavaScript Output
```javascript
let N;
N = 1;
while (N < 10) {
    console.log("COUNTING: ");
    console.log(N);
    N = N + 1;
}
console.log("DONE!");
```

## Conclusion
The Basic Compiler serves as a practical exercise in understanding compiler design and the translation of programming languages. Through this project, I've gained valuable insights into parsing, syntax analysis, and code generation. This experience has not only improved my programming skills but also deepened my understanding of how compilers work.
