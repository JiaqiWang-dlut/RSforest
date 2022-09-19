package randomForest;

import java.util.ArrayList;
import java.util.Random;
import suffixTree.*;

public class generatePattern {
	public pattern ramdom_find_pattern(ArrayList<String> train, int pattern_length, int maxL) {	
		int leng = train.size();
		int p_len = maxL-1; 
	    Random rand = new Random(); 
	    int choose = rand.nextInt(leng);
		String choose_pattern = train.get(choose);
		String[] s = choose_pattern.split("\t");
		String label=s[0];		
		String[] new_pattern=s[1].split(" ");
		int sequence_length = new_pattern.length;
		int start = 0;
		int end = 0;
		int patternLen;		
		if(pattern_length < maxL)
			patternLen =  rand.nextInt(p_len)+1;
		else
			patternLen = pattern_length;		
		if(sequence_length > patternLen) {
			start = rand.nextInt(sequence_length-patternLen+1);
			end = start + patternLen -1;
		}
		else {
			start = 0;
			end = sequence_length-1;
		}	
		int length = end-start+1;
		String[] s2 = new String[length];
		System.arraycopy(new_pattern,start,s2,0,length);
		pattern p = new pattern(s2,label);
		return p;
	}
}
