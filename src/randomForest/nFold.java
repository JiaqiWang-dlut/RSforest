package randomForest;

import java.util.ArrayList;

public class nFold {
	ArrayList<ArrayList<String>> foldList = new ArrayList<ArrayList<String>>();
    public nFold(int num) {	
    	for(int i=0;i<num;i++) {
    		foldList.add(new ArrayList<String>());
		}			
    }
}
