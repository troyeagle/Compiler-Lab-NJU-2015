import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

public class SynAnalysis {
	final static int LINE_BUFFER_SIZE = 100;
	final static int MAX_STATE_SUM = 100;
	ArrayList<String> productions;
	int state_sum = 0;
	ArrayList<HashMap<Character, String>> actions;
	ArrayList<HashMap<Character, Integer>> gotos;
	Stack<Integer> state_stack;
	Stack<Character> operator_stack;
	char line_buffer[];

	public SynAnalysis() {
		this.productions = new ArrayList<String>();
		this.actions = new ArrayList<HashMap<Character, String>>();
		this.gotos = new ArrayList<HashMap<Character, Integer>>();
		this.state_stack = new Stack<Integer>();
		this.operator_stack = new Stack<Character>();
		this.line_buffer = new char[LINE_BUFFER_SIZE];

	}

	void inputGrammar(String cfg_path) {
		File file = new File(cfg_path);
		if (!file.exists()) {
			System.out.println("error");
			return;
		}
		try {
			FileReader fi = new FileReader(file);
			BufferedReader br = new BufferedReader(fi);
			String line;

			while ((line = br.readLine()) != null) {
				if (!line.startsWith("\0")) {
					productions.add(line);
				}
			}

			br.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void buildParsingTable(String ppt_path) {
		boolean isAction = true;
		try {
			BufferedReader br = new BufferedReader(new FileReader(ppt_path));
			String line;
			while ((line = br.readLine()) != null && !line.startsWith("\0")) {
				if (line.startsWith("%%")) {
					isAction = false;
					continue;
				}
				if (isAction) {
					addAction(line);
				} else {
					addGoto(line);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addAction(String line) {
		String[] split = line.split("_");
		if (split.length == 1) {
			System.out.println("error in add action:" + line);
			return;
		}
		int state_no = Integer.parseInt(split[0]);
		if (state_no + 1 > state_sum) {
			state_sum = state_no + 1;
		}
		String[] subsplit = split[1].split("%");
		HashMap<Character, String> currentAction = new HashMap<Character, String>();
		for (String s : subsplit) {
			currentAction.put(s.charAt(0), s.substring(2));// FIXME C中数组的大小？
		}
		actions.add(currentAction);

	}

	private void addGoto(String line) {
		String[] split = line.split("_");
		if (split.length == 1) {
			gotos.add(null);
			return;
		}
		int stateNo = Integer.parseInt(split[0]);
		String[] subsplit = split[1].split("%");
		HashMap<Character, Integer> currentGoto = new HashMap<Character, Integer>();

		for (String s : subsplit) {
			currentGoto.put(s.charAt(0), Integer.parseInt(s.substring(1)));
		}
		gotos.add(currentGoto);

	}

	void initParsing() {
		operator_stack.push('$');
		state_stack.push(0);

	}

	void parse(String input) {
		InputStrip inputStrip = new InputStrip(input);
		while (true) {
			if (state_stack.isEmpty() || operator_stack.isEmpty()) {
				System.out.println("Parse stack empty");
				return;
			}
			int currentStateNo = state_stack.peek();
			char currentHead = inputStrip.getCurrentChar();
			if (currentHead == '\0') {
				System.out.println("error when parse current head = 0");
				return;
			}
			HashMap<Character, String> curAction = actions.get(currentStateNo);
			HashMap<Character, Integer> curGoto = gotos.get(currentStateNo);
			if (curAction.get(currentHead) != null) {
				String opStr = curAction.get(currentHead);
				if (opStr.charAt(0) == 'S') {
					String s = opStr.substring(1, opStr.length());
					int nextStateNo = Integer.parseInt(s);
					printProcess(inputStrip.getCurrentString(), '$',
							nextStateNo);
					operator_stack.push(currentHead);
					inputStrip.index++;
					state_stack.push(nextStateNo);
				} else if (opStr.charAt(0) == 'r') {
					String s = opStr.substring(1, opStr.length());
					int productionNo = Integer.parseInt(s);
					if (productionNo == 0) {
						System.out.println("Parse Successfully End");
						return;
					}
					printProcess(inputStrip.getCurrentString(), 'r',
							productionNo);
					boolean success = reduce(inputStrip, productionNo);
					if (!success) {
						return;
					}
				} else {
					System.out.println("Error when parse");
				}
			} else if (curGoto.get(currentHead) != null) {
				System.out.println("Error with parse");
			} else {
				System.out.println("Error withh parse");
				return;
			}
		}

	}

	private boolean reduce(InputStrip inputStrip, int productionNo) {
		if (productionNo == 0)
			return true;
		String production = productions.get(productionNo);
		String right = production.substring(2, production.length());

		String s = printOperatorStack();

		int i = 0;
		for (i = right.length() - 1; i >= 0; i--) {
			if (operator_stack.peek() == right.charAt(i)) {
				operator_stack.pop();
				state_stack.pop();
			} else {
				System.out.println("error when reduce notmatch substr");
				return false;
			}
		}
		operator_stack.push(production.charAt(0));
		int currentTopState = state_stack.peek();
		HashMap<Character, Integer> tempGoto = gotos.get(currentTopState);
		if (tempGoto == null) {
			System.out.println("Error when reduce no goto");
			return false;

		}
		if (tempGoto.get(production.charAt(0)) != null) {
			int nextState = tempGoto.get(production.charAt(0));
			state_stack.push(nextState);
		}
		return true;
	}

	private void printProcess(String inputStr, char c, int num) {
		String s_stk = printStateStack();
		String o_stk = printOperatorStack();
		System.out.println(s_stk + "\t" + o_stk + "\t" + inputStr + "\t");
		if (c == 'r') {
			System.out.print("reduce by " + num);
		} else {
			System.out.print("Shift");
		}

	}

	private String printOperatorStack() {
		String s = "";

		@SuppressWarnings("unchecked")
		Stack<Character> copy = (Stack<Character>)operator_stack.clone();
		while (!copy.isEmpty()) {
			char c = copy.pop();
			s += c;
			s += " ";

		}
		return s;

	}

	private String printStateStack() {
		String s = "";
		@SuppressWarnings("unchecked")
		Stack<Integer> copy = (Stack<Integer>)state_stack.clone();
		while (!copy.isEmpty()) {
			int c = copy.pop();
			s += ('0' + c);
			s += " ";
		}
		return s;
	}

	public void printGrammar() {
		Iterator<String> it = productions.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}

	public static void main(String[] args) {
		SynAnalysis syn = new SynAnalysis();
		syn.inputGrammar("cfg.txt");
		syn.printGrammar();
		syn.buildParsingTable("ppt.txt");
		System.out.println("Input expression");
		String input;
		Scanner in = new Scanner(System.in);
		input = in.nextLine();
		System.out.println("Parsing");
		syn.initParsing();
		System.out.println("State Stack" + "\t" + "Operator Stack" + "\t"
				+ "Input" + "\t" + "Action");
		syn.parse(input);
		in.close();

	}

}
