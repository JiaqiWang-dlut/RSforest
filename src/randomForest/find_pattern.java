package randomForest;

import java.util.ArrayList;
import java.util.Arrays;

public class find_pattern {
    public int Sunday_has_pattern(String [] haystack, String [] needle) { //quickly search pattern, when gap == 1
        int hayLen = haystack.length;
        int nLen = needle.length;
        int i = 0;
        int j = 0;
        while (i <= hayLen - nLen) { 
            while (j < nLen && haystack[i + j].equals(needle[j]) ) {
                j++;
            }
            if (j == nLen) { 
                return 1;
            }
            if (i < hayLen - nLen) {
                i += (nLen - lastIndex(needle, haystack[i + nLen]));
            } else {
                return -1;
            }
            j=0;
        }
        return 0;
    }
    
    public int has_pattern(String [] seq, String [] pattern) { 
    	int seq_length = seq.length;
     	for(int i=0; i<seq_length ; i++) {
      		 if(seq[i].equals(pattern[0])) {
      			return 1;
      		  }
      	   }
		return 0;
    }
    
    public int lastIndex(String []str, String ch) {
        // ´ÓºóÍùÇ°¼ìË÷
        for (int j = str.length - 1; j >= 0; j--) {
            if (str[j].equals(ch)) {
                return j;
            }
        }
        return -1;
    } 
    
	public int maxSubseq(String[]x,String y[]) {  // when gap != 1
        if(x == null) {
     	   return 0;
        }
       if(y == null) {
     	  return 0;
       }
         int m = x.length;
         int n = y.length;
         int[][] c = new int[m+1][n+1];
         for (int i = 0; i < m+1; i++) {
             c[i][0] = 0;
         }
         for (int i = 0; i < n+1; i++) {
             c[0][i] = 0;
         }
         int[][] path = new int[m+1][n+1];
         for (int i = 1; i < m+1; i++) {
             for (int j = 1; j < n+1; j++) {
                 if(x[i-1] .equals(y[j-1])){
                     c[i][j] = c[i-1][j-1] + 1;
                 }else if(c[i-1][j] >= c[i][j-1]){
                     c[i][j] = c[i-1][j];
                     path[i][j] = 1;
                 }else{
                     c[i][j] = c[i][j-1];
                     path[i][j] = -1;
                 }
             }
         }
         ArrayList<String> s = new ArrayList<String>();
         ArrayList<String> s1 = new ArrayList<String>();
         s = PrintLCS(s1,path,x,m,n);
     	int size = s.size();     	
     	String[] array = (String[])s.toArray(new String[size]);
         return array.length;
     }

     public static ArrayList<String> PrintLCS(ArrayList<String> s,int[][]b,String[] x,int i,int j){      	
         if(i == 0 || j == 0){         	
             return s;
         }
         if(b[i][j] == 0){
             PrintLCS(s,b,x,i-1,j-1);
             s.add(x[i-1]);
         }else if(b[i][j] == 1){
             PrintLCS(s,b,x,i-1,j);
         }else{
             PrintLCS(s,b,x,i,j-1);
         }
         return s;
     }

	public int has_pattern(String[] seq, String[] pat, int gap) {
		int start = 0;
		int p_start = 0;
		int first = 0;// location of first item
		int flag = 0;//search first item
		int count = 0;
		if(pat.length>seq.length)
			return 0;
		for(int i = p_start; i<pat.length; i++) {
			for(int j=start; j<seq.length; j++) {
				if(seq[j].equals(pat[i])) {
					if(flag == 0) {
						start = j+1;
						flag = 1;
						first = j;
						count ++;
						break;
					}
					else if(flag == 1 && j-start <= gap) { //j-(start-1)-1
						start = j+1;
						count ++;
						break;
					}
					else {
						i = -1;
						count = 0;
						flag = 0;
						start = first+1;
						break;
					}												
				}	
				if(j==seq.length-1 && count != pat.length)
					return 0;
				if(count == pat.length)
					return 1;
			}			
		}
		if(count == pat.length)
			return 1;
		else
			return 0;
	}
	public static void main(String args[]) {
		find_pattern fp = new find_pattern();
		String[]seq = new String[] {"1","2","3","7","8","6","8","4"};
		String[]pat = new String[] {"6","4"};
		int p = fp.has_pattern(seq,pat,2);
		System.out.println("test"+p);
	}
}
