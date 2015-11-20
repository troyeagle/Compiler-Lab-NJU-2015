import java.io.File;


public class Test {
	public static void main(String[] args)throws Exception{
		LexAnalysis lex = new LexAnalysis(new File("good.java"));
		lex.process();
		lex.output();
	}
}
