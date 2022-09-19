package randomForest;

import java.util.ArrayList;
import java.util.HashMap;

public class classifyTest {
	public String classify_by_one_tree(String []sequence, tree_node root) {
		find_pattern fp = new find_pattern();
		tree_node node = root;
		String label = null;
		while(node!=null) {
			if(node.is_leaf==1) {
				return node.label;
			}
			int k;
			if(node.feature.length ==1)
				k = fp.has_pattern(sequence,node.feature);
			else
				k = fp.Sunday_has_pattern(sequence,node.feature);
			if(k==1) {
				String temp = node.label;
				if(node.leftChild == null) {
					return node.label;
				}
				else {
					node = node.leftChild;
					if(node.label == null)
						label = temp;
					else
					 label = node.label;
				}
			}		
			else {
				String temp = node.label;
				node = node.rightChild;
				if(node == null)
					label = temp;
				else
				 label = node.label;
			}	
		}
		return label;
	}
	
	public String classify(String sequence,tree_node[] rf) {
		String[] s = sequence.split("\t");
		String[] dataInstanceForTesting=s[1].split(" ");
		HashMap<String, Integer> labelMap = new HashMap<String, Integer>();
		String final_label = null;
		int max = 0;
		for(int i = 0; i < rf.length; i++) {
			String label = classify_by_one_tree(dataInstanceForTesting,rf[i]);
			boolean flag = labelMap.containsKey(label);
   		    if(flag==false) {
   			   labelMap.put(label,1);
   		    }
   		    else {
   			  int num = labelMap.get(label);
   			  labelMap.put(label,num+1);
   		    }
		}
     for (String key : labelMap.keySet()) {
	      int temp = labelMap.get(key);
	      if(temp > max) {
	    	  max = temp;
	    	  final_label = key;
	      }   	  
      }
		return final_label;
	}
	
	public float Test(ArrayList<String> test, tree_node[] rf){
		int right = 0;
		int num = test.size();
		for(int i=0;i<num;i++) {
			String sequence = test.get(i);
			String[] s = sequence.split("\t");
			String true_label = s[0];
			String label = classify(sequence,rf);
			if(label.equals(true_label)) {
				right++;
			}
		}
		float acc = ((float) right/num);	
		return acc;
	}
	
	
	public double TestbyTree(ArrayList<String> test, tree_node[] rf){
		double acc = 0;//acc of one tree
		double aver_acc = 0;// average acc of 50 trees
		int num = test.size();
		int leng = rf.length;
		for(int i=0;i<leng;i++) {
			int right = 0;
			for(int j=0;j<num;j++) {			
				String sequence = test.get(j);
				String[] s = sequence.split("\t");
				String true_label=s[0];
				String[] dataInstanceForTesting=s[1].split(" ");
				String label = classify_by_one_tree(dataInstanceForTesting,rf[i]);
				if(label.equals(true_label)) {
					right++;
				}		
			}
			acc = ((double) right/num);
			aver_acc += acc;
			System.out.println("classify by one tree");
			System.out.println(acc);	   	
		}	
		return aver_acc/rf.length;
	}	
	
}
