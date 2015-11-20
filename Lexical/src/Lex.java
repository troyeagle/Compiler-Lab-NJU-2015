import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * Created by Cee on 11/5/14.
 */
public class Lex {

    private Stack rawInputLines;
    private StringTokenizer tokenedInputLines[];
    private Object processedInput[];
    private String [] tokenImage =
            {"DOUBLE", "ELSE", "IF", "INT", "RETURN",
            "VOID", "WHILE",
            "plus", "minus", "multiplication", "division",
            "less", "lessOrEqual", "greater",
            "greaterOfEqual", "equal", "notEqual",
            "assignOp",
            "semicolon", "comma", "period", "leftparen",
            "rightparen", "leftbracket", "rightbracket",
            "leftbrace","rightbrace",
            "id", "num"};
    private boolean lineCommentFlag = false;

    public Lex(File file) throws IOException{
        Scanner in = new Scanner(file);
        rawInputLines = new Stack();
        while(in.hasNext()){
            rawInputLines.push(new StringTokenizer(in.nextLine()));
        }
        tokenedInputLines = new StringTokenizer [rawInputLines.size()];
        processedInput = new Object[tokenedInputLines.length];
        for(int i = rawInputLines.size()-1; i >= 0; i--){
            tokenedInputLines[i] = (StringTokenizer)rawInputLines.pop();
        }
    }

    public void process(){
        for(int i = 0; i < tokenedInputLines.length; i++){
            Stack tempInputLineValues = new Stack();
            while(tokenedInputLines[i].hasMoreTokens()) {
                convertToCharacters(tempInputLineValues,
                        new StringReader(tokenedInputLines[i].nextToken()));
            }
            Integer inputLineValues [] = new Integer [tempInputLineValues.size()];
            for(int j = tempInputLineValues.size() - 1; j >= 0; j--){
                inputLineValues[j] = (Integer)tempInputLineValues.pop();
            }
            processedInput[i] = inputLineValues;
            lineCommentFlag = false;
        }
    }
    private void convertToCharacters(Stack imageTokenIntegers, StringReader sr)
    {
        Stack s = new Stack();
        int in = -1;
        do{
            try{
                in = sr.read();
            }
            catch(IOException e){
                break;
            }
            s.push(in);
        } while(in != -1);
        char characters [] = new char [s.size()];
        for(int i = characters.length-1; i >= 0; i--){
            int a = (Integer)s.pop();
            if(i < characters.length-1)
            {
                Character b = (char)a;
                characters[i] = b;
            }
        }
        processCharacters(imageTokenIntegers, characters);
    }
    private void processCharacters(Stack imageTokenIntegers, char chars [])
    {
        boolean possibleIdentifier = false;
        boolean possibleKeyword = false;
        String record = new String();
        for(int i = 0; i < chars.length-1; i++)
        {
            if(lineCommentFlag)
                break;
            char input = chars[i];
            record += input;
            switch(input)
            {
                case 'a':
                case 'A':
                case 'b':
                case 'B':
                case 'c':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'g':
                case 'G':
                case 'H':
                case 'I':
                case 'j':
                case 'J':
                case 'k':
                case 'L':
                case 'm':
                case 'M':
                case 'N':
                case 'O':
                case 'p':
                case 'P':
                case 'q':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'x':
                case 'X':
                case 'y':
                case 'Y':
                case 'z':
                case 'Z':
                case 'h':
                case 'i':
                case 'l':
                case 'u':
                case 'v':
                case 'w':
                case 'o':
                case 'r':
                case 's':
                case '_':
                    if(possibleKeyword)
                    {
                        imageTokenIntegers.pop();
                        imageTokenIntegers.push(27);
                        possibleKeyword = false;
                        break;
                    }
                    try
                    {
                        if(imageTokenIntegers.peek() == (Integer)27)
                        {
                            possibleIdentifier = true;
                            possibleKeyword = false;
                            break;
                        }
                        else
                        {
                            imageTokenIntegers.push(27);
                            possibleIdentifier = true;
                            possibleKeyword = false;
                            break;
                        }
                    }
                    catch(EmptyStackException e)
                    {
                        imageTokenIntegers.push(27);
                        possibleIdentifier = true;
                        possibleKeyword = false;
                        break;
                    }

                case 'd':
                    if(record.compareTo("void") == 0)
                    {
                        possibleKeyword = true;
                        if(possibleIdentifier)
                            imageTokenIntegers.pop();
                        imageTokenIntegers.push(5);
                        possibleIdentifier = false;
                    }
                    possibleIdentifier = true;
                    try
                    {
                        if(imageTokenIntegers.peek() == (Integer)27)
                        {
                            break;
                        }
                        imageTokenIntegers.push(27);
                    }
                    catch(EmptyStackException e)
                    {
                        imageTokenIntegers.push(27);
                        break;
                    }
                    break;
                case 'e':
                    if(record.compareTo("else") == 0)
                    {
                        possibleKeyword = true;
                        if(possibleIdentifier)
                            imageTokenIntegers.pop();
                        imageTokenIntegers.push(1);
                        possibleIdentifier = false;
                    }
                    if(record.compareTo("double") == 0)
                    {
                        possibleKeyword = true;
                        if(possibleIdentifier)
                            imageTokenIntegers.pop();
                        imageTokenIntegers.push(0);
                        possibleIdentifier = false;
                    }
                    if(record.compareTo("while") == 0)
                    {
                        possibleKeyword = true;
                        if(possibleIdentifier)
                            imageTokenIntegers.pop();
                        imageTokenIntegers.push(6);
                        possibleIdentifier = false;
                    }
                    possibleIdentifier = true;
                    try
                    {
                        if(imageTokenIntegers.peek() == (Integer)27)
                        {
                            break;
                        }
                        imageTokenIntegers.push(27);
                    }
                    catch(EmptyStackException e)
                    {
                        imageTokenIntegers.push(27);
                        break;
                    }
                    break;
                case 'f':
                    if(record.compareTo("if") == 0)
                    {
                        possibleKeyword = true;
                        if(possibleIdentifier)
                            imageTokenIntegers.pop();
                        imageTokenIntegers.push(2);
                        possibleIdentifier = false;
                    }
                    possibleIdentifier = true;
                    try
                    {
                        if(imageTokenIntegers.peek() == (Integer)27)
                        {
                            break;
                        }
                        imageTokenIntegers.push(27);
                    }
                    catch(EmptyStackException e)
                    {
                        imageTokenIntegers.push(27);
                        break;
                    }
                    break;
                case 'n':
                    if(record.compareTo("return") == 0)
                    {
                        possibleKeyword = true;
                        if(possibleIdentifier)
                            imageTokenIntegers.pop();
                        imageTokenIntegers.push(4);
                        possibleIdentifier = false;
                    }
                    possibleIdentifier = true;
                    try
                    {
                        if(imageTokenIntegers.peek() == (Integer)27)
                        {
                            break;
                        }
                        imageTokenIntegers.push(27);
                    }
                    catch(EmptyStackException e)
                    {
                        imageTokenIntegers.push(27);
                        break;
                    }
                    break;
                case 't':
                    if(record.compareTo("int") == 0)
                    {
                        possibleKeyword = true;
                        if(possibleIdentifier)
                            imageTokenIntegers.pop();
                        imageTokenIntegers.push(3);
                        possibleIdentifier = false;
                    }
                    possibleIdentifier = true;
                    try
                    {
                        if(imageTokenIntegers.peek() == (Integer)27)
                        {
                            break;
                        }
                        imageTokenIntegers.push(27);
                    }
                    catch(EmptyStackException e)
                    {
                        imageTokenIntegers.push(27);
                        break;
                    }
                    break;
                //number cases
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    if(!possibleIdentifier)
                    {
                        if(possibleKeyword)
                        {
                            possibleIdentifier = true;
                            imageTokenIntegers.pop();
                            imageTokenIntegers.push(27);
                            break;
                        }
                        try
                        {
                            if(!(imageTokenIntegers.peek() == (Integer)28))
                            {
                                imageTokenIntegers.push(28);
                                break;
                            }
                            break;
                        }
                        catch(EmptyStackException e)
                        {
                            imageTokenIntegers.push(28);
                            break;
                        }
                    }
                    break;
                case '+':
                    imageTokenIntegers.push(7);
                    possibleIdentifier = false;
                    possibleKeyword = false;
                    record = "";
                    break;
                case '-':
                    imageTokenIntegers.push(8);
                    possibleIdentifier = false;
                    possibleKeyword = false;
                    record = "";
                    break;
                case '*':
                    imageTokenIntegers.push(9);
                    possibleIdentifier = false;
                    possibleKeyword = false;
                    record = "";
                    break;
                case '/':
                    try
                    {
                        if(imageTokenIntegers.peek() == (Integer)10)
                        {
                            imageTokenIntegers.pop();
                            lineCommentFlag = true;
                            break;
                        }
                    }
                    catch(EmptyStackException e)
                    {
                        imageTokenIntegers.push(10);
                        possibleIdentifier = false;
                        possibleKeyword = false;
                        record = "";
                        break;
                    }
                case '<':
                    imageTokenIntegers.push(11);
                    possibleIdentifier = false;
                    possibleKeyword = false;
                    record = "";
                    break;
                case '>':
                    imageTokenIntegers.push(13);
                    possibleIdentifier = false;
                    possibleKeyword = false;
                    record = "";
                    break;
                case '=':
                    try
                    {
                        if(imageTokenIntegers.peek() == (Integer)11)
                        {
                            imageTokenIntegers.pop();
                            imageTokenIntegers.push(12);
                            possibleIdentifier = false;
                            possibleKeyword = false;
                            break;
                        }
                    }
                    catch(EmptyStackException e)
                    {
                        imageTokenIntegers.push(17);
                        possibleIdentifier = false;
                        possibleKeyword = false;
                        record = "";
                        break;
                    }
                    try
                    {
                        if(imageTokenIntegers.peek() == (Integer)13)
                        {
                            imageTokenIntegers.pop();
                            imageTokenIntegers.push(14);
                            possibleIdentifier = false;
                            possibleKeyword = false;
                            break;
                        }
                    }
                    catch(EmptyStackException e)
                    {
                        imageTokenIntegers.push(17);
                        possibleIdentifier = false;
                        possibleKeyword = false;
                        record = "";
                        break;
                    }
                    try
                    {
                        if(imageTokenIntegers.peek() == (Integer)17)
                        {
                            imageTokenIntegers.pop();
                            imageTokenIntegers.push(15);
                            possibleIdentifier = false;
                            possibleKeyword = false;
                            break;
                        }
                    }
                    catch(EmptyStackException e)
                    {
                        imageTokenIntegers.push(17);
                        possibleIdentifier = false;
                        possibleKeyword = false;
                        record = "";
                        break;
                    }
                    imageTokenIntegers.push(17);
                    possibleIdentifier = false;
                    possibleKeyword = false;
                    record = "";
                    break;
                case '!':
                    imageTokenIntegers.push(16);
                    possibleIdentifier = false;
                    possibleKeyword = false;
                    record = "";
                    i++;
                    break;
                case ';':
                    imageTokenIntegers.push(18);
                    break;
                case ',':
                    imageTokenIntegers.push(19);
                    possibleIdentifier = false;
                    possibleKeyword = false;
                    record = "";
                    break;
                case '.':
                    try
                    {
                        if(imageTokenIntegers.peek() == (Integer)28)
                            break;
                    }
                    catch(EmptyStackException e)
                    {
                        imageTokenIntegers.push(20);
                        break;
                    }
                    imageTokenIntegers.push(20);
                    break;
                case '(':
                    imageTokenIntegers.push(21);
                    possibleIdentifier = false;
                    possibleKeyword = false;
                    record = "";
                    break;
                case ')':
                    imageTokenIntegers.push(22);
                    possibleIdentifier = false;
                    possibleKeyword = false;
                    record = "";
                    break;
                case '[':
                    imageTokenIntegers.push(23);
                    possibleIdentifier = false;
                    possibleKeyword = false;
                    record = "";
                    break;
                case ']':
                    imageTokenIntegers.push(24);
                    possibleIdentifier = false;
                    possibleKeyword = false;
                    record = "";
                    break;
                case '{':
                    imageTokenIntegers.push(25);
                    possibleIdentifier = false;
                    possibleKeyword = false;
                    record = "";
                    break;
                case '}':
                    imageTokenIntegers.push(26);
                    possibleIdentifier = false;
                    possibleKeyword = false;
                    record = "";
                    break;
                default:
                    System.out.println("unrecognized character");
            }
        }
    }
    public void output(){
        for(int i = 0; i < processedInput.length; i++){
            Integer[] in = (Integer[])processedInput[i];
            for (int j = 0; j < in.length; j++){
                System.out.print(tokenImage[in[j]] + " ");
            }
            System.out.println();
        }
    }
    public static void main(String[] args) throws IOException {
        File file = new File("good.java");
        Lex lex = new Lex(file);
        lex.process();
        lex.output();
    }
}
