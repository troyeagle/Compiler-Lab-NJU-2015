import java.io.File;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

public class LexAnalysis {
	private String tokenCats[] = { "DOUBLE", "ELSE", "IF", "INT", "RETURN",
			"VOID", "WHILE", "plus", "minus", "multiplication", "division",
			"less", "lessOrEqual", "greater", "greaterOfEqual", "equal",
			"notEqual", "assignOp", "semicolon", "comma", "period",
			"leftparen", "rightparen", "leftbracket", "rightbracket",
			"leftbrace", "rightbrace", "id", "num" };
	private Queue<StringTokenizer> initialInput;
	private Object processedInput[];
	private StringTokenizer tokenedInputLines[];
	private boolean isComment = false;

	public LexAnalysis(File file) throws Exception {
		Scanner sc = new Scanner(file);
		initialInput = new LinkedList<StringTokenizer>();
		while (sc.hasNext()) {
			initialInput.add(new StringTokenizer(sc.nextLine()));
		}
		tokenedInputLines = new StringTokenizer[initialInput.size()];
		processedInput = new Object[tokenedInputLines.length];
		for (int i = 0; i < tokenedInputLines.length; i++) {
			tokenedInputLines[i] = initialInput.poll();
		}
		sc.close();
	}

	public void process() {

		for (int i = 0; i < tokenedInputLines.length; i++) {
			Stack tempInput = new Stack();
			try {
				while (tokenedInputLines[i].hasMoreTokens()) {
					processChars(tempInput, tokenedInputLines[i].nextToken());
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			Integer inputLineValues[] = new Integer[tempInput.size()];
			for (int j = inputLineValues.length - 1; j >= 0; j--) {
				inputLineValues[j] = (Integer) tempInput.pop();
			}

			processedInput[i] = inputLineValues;
			isComment = false;
		}

	}

	private void processChars(Stack<Object> imageTokenIntegers, String nextToken) {
		if (nextToken.length() == 0)
			return;
		nextToken+=" ";
		char chars[] = nextToken.toCharArray();

		boolean possibleIdentifier = false;
		boolean possibleKeyword = false;
		String record = new String();
		for (int i = 0; i < chars.length - 1; i++) {
			if (isComment)
				break;
			char input = chars[i];
			record += input;
			switch (input) {
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
				if (possibleKeyword) {
					imageTokenIntegers.pop();
					imageTokenIntegers.push(27);
					possibleKeyword = false;
					break;
				}
				try {
					if (imageTokenIntegers.peek() == (Integer) 27) {
						possibleIdentifier = true;
						possibleKeyword = false;
						break;
					} else {
						imageTokenIntegers.push(27);
						possibleIdentifier = true;
						possibleKeyword = false;
						break;
					}
				} catch (EmptyStackException e) {
					imageTokenIntegers.push(27);
					possibleIdentifier = true;
					possibleKeyword = false;
					break;
				}

			case 'd':
				if (record.compareTo("void") == 0) {
					possibleKeyword = true;
					if (possibleIdentifier)
						imageTokenIntegers.pop();
					imageTokenIntegers.push(5);
					possibleIdentifier = false;
				}
				possibleIdentifier = true;
				try {
					if (imageTokenIntegers.peek() == (Integer) 27) {
						break;
					}
					imageTokenIntegers.push(27);
				} catch (EmptyStackException e) {
					imageTokenIntegers.push(27);
					break;
				}
				break;
			case 'e':
				if (record.compareTo("else") == 0) {
					possibleKeyword = true;
					if (possibleIdentifier)
						imageTokenIntegers.pop();
					imageTokenIntegers.push(1);
					possibleIdentifier = false;
				}
				if (record.compareTo("double") == 0) {
					possibleKeyword = true;
					if (possibleIdentifier)
						imageTokenIntegers.pop();
					imageTokenIntegers.push(0);
					possibleIdentifier = false;
				}
				if (record.compareTo("while") == 0) {
					possibleKeyword = true;
					if (possibleIdentifier)
						imageTokenIntegers.pop();
					imageTokenIntegers.push(6);
					possibleIdentifier = false;
				}
				possibleIdentifier = true;
				try {
					if (imageTokenIntegers.peek() == (Integer) 27) {
						break;
					}
					imageTokenIntegers.push(27);
				} catch (EmptyStackException e) {
					imageTokenIntegers.push(27);
					break;
				}
				break;
			case 'f':
				if (record.compareTo("if") == 0) {
					possibleKeyword = true;
					if (possibleIdentifier)
						imageTokenIntegers.pop();
					imageTokenIntegers.push(2);
					possibleIdentifier = false;
				}
				possibleIdentifier = true;
				try {
					if (imageTokenIntegers.peek() == (Integer) 27) {
						break;
					}
					imageTokenIntegers.push(27);
				} catch (EmptyStackException e) {
					imageTokenIntegers.push(27);
					break;
				}
				break;
			case 'n':
				if (record.compareTo("return") == 0) {
					possibleKeyword = true;
					if (possibleIdentifier)
						imageTokenIntegers.pop();
					imageTokenIntegers.push(4);
					possibleIdentifier = false;
				}
				possibleIdentifier = true;
				try {
					if (imageTokenIntegers.peek() == (Integer) 27) {
						break;
					}
					imageTokenIntegers.push(27);
				} catch (EmptyStackException e) {
					imageTokenIntegers.push(27);
					break;
				}
				break;
			case 't':
				if (record.compareTo("int") == 0) {
					possibleKeyword = true;
					if (possibleIdentifier)
						imageTokenIntegers.pop();
					imageTokenIntegers.push(3);
					possibleIdentifier = false;
				}
				possibleIdentifier = true;
				try {
					if (imageTokenIntegers.peek() == (Integer) 27) {
						break;
					}
					imageTokenIntegers.push(27);
				} catch (EmptyStackException e) {
					imageTokenIntegers.push(27);
					break;
				}
				break;
			// number cases
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
				if (!possibleIdentifier) {
					if (possibleKeyword) {
						possibleIdentifier = true;
						imageTokenIntegers.pop();
						imageTokenIntegers.push(27);
						break;
					}
					try {
						if (!(imageTokenIntegers.peek() == (Integer) 28)) {
							imageTokenIntegers.push(28);
							break;
						}
						break;
					} catch (EmptyStackException e) {
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
				try {
					if (imageTokenIntegers.peek() == (Integer) 10) {
						imageTokenIntegers.pop();
						isComment = true;
						break;
					}
				} catch (EmptyStackException e) {
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
				try {
					if (imageTokenIntegers.peek() == (Integer) 11) {
						imageTokenIntegers.pop();
						imageTokenIntegers.push(12);
						possibleIdentifier = false;
						possibleKeyword = false;
						break;
					}
				} catch (EmptyStackException e) {
					imageTokenIntegers.push(17);
					possibleIdentifier = false;
					possibleKeyword = false;
					record = "";
					break;
				}
				try {
					if (imageTokenIntegers.peek() == (Integer) 13) {
						imageTokenIntegers.pop();
						imageTokenIntegers.push(14);
						possibleIdentifier = false;
						possibleKeyword = false;
						break;
					}
				} catch (EmptyStackException e) {
					imageTokenIntegers.push(17);
					possibleIdentifier = false;
					possibleKeyword = false;
					record = "";
					break;
				}
				try {
					if (imageTokenIntegers.peek() == (Integer) 17) {
						imageTokenIntegers.pop();
						imageTokenIntegers.push(15);
						possibleIdentifier = false;
						possibleKeyword = false;
						break;
					}
				} catch (EmptyStackException e) {
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
				try {
					if (imageTokenIntegers.peek() == (Integer) 28)
						break;
				} catch (EmptyStackException e) {
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

	public void output() {
		for (int i = 0; i < processedInput.length; i++) {
			Integer[] in = (Integer[]) processedInput[i];
			for (int j = 0; j < in.length; j++) {
				System.out.print(tokenCats[in[j]] + " ");
			}
			System.out.println();
		}
	}

}
