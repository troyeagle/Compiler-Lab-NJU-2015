import java.util.ArrayList;


public class InputStrip {
	public ArrayList<Character> inputs;
	public String str;
	public int index;
	public static int reserved_bits=10;
	public InputStrip(String s){
		inputs= new ArrayList<Character>();
		str = new String();
		index=0;
		int i = 0;
		for(i = 0;i<s.length();i++){
			this.inputs.add(s.charAt(i));
		}
		this.inputs.add('$');
		this.str=s;
		this.index=0;
	}
	public String getCurrentString(){
		return this.str.substring(this.index, this.inputs.size()-1);
	}
	public char getCurrentChar(){
		int size = this.inputs.size();
		if(!(this.index<0||this.index>=size)){
			return this.inputs.get(this.index);
		}else{
			return '\0';
		}
	}

}

