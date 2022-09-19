package randomForest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import suffixTree.*;

public class createForest extends Thread{
	int p = 5;//number of trees
	int l = 2;//lower length of pattern
    int u = 100;//upper length of pattern
    int r = 6;//number of patterns
 	double pena = 2; // penalty parameter
    int g = 1; // gap constraint
 	int maxL = 4; // maximum length = 4

	public createForest(){
	}
	
    public int getclass(ArrayList<String> dataset) {
    	HashSet<String> hs = new HashSet<String>();
    	for(int i=0;i<dataset.size();i++) {
    		String seq = dataset.get(i);
    		String[] s = seq.split("\t");
    		hs.add(s[0]);
    	}
    	return hs.size();
    }
        
    public static String getType(Object o){ 
    	return o.getClass().toString(); 
    	}   
    
    public gini_with_split calculateGini(pattern p, ArrayList<String> train, int gap) { 
    	int len = train.size(); 
    	double gini = 0;
    	find_pattern fp = new find_pattern();
    	gini_with_split g = new gini_with_split();
        HashMap<  String, Integer> hMap1 = new HashMap<  String, Integer>();
        HashMap<  String, Integer> hMap2 = new HashMap<  String, Integer>();
        ArrayList<String> gini_left = new ArrayList<String> ();
        ArrayList<String> gini_right = new ArrayList<String> ();
         int have =0;
         int not_have = 0;
   	     double giniD1 = 0;
   	     double giniD2 = 0;   	     
    		  if(p.pat == null) {	  
    			   return null;
    		  } 
    		  else {			  
    			for(int i=0;i<len;i++) {
	    	    	String sequence = train.get(i);
	    	  		String[] s = sequence.split("\t");
	    			String label=s[0];
	    			String[] seq=s[1].split(" ");
	    			int k_flag;
    		        if(p.pat.length<=1)
    		        	k_flag = fp.has_pattern(seq,p.pat);
    		        else if (p.pat.length>1 && gap == 0)
    		        	k_flag = fp.Sunday_has_pattern(seq,p.pat);
    		        else {
    		        	k_flag = fp.has_pattern(seq, p.pat, gap);
    		        }   		        	   		        	
    	    	  if(k_flag == 1) {  
    	    		 gini_left.add(sequence);
    	    		 have++;
    	    		 boolean flag=hMap1.containsKey(s[0]);
    	    		 if(flag==false) {
    	    			 hMap1.put(s[0],1);
    	    		 }
    	    		 else {
    	    			 int num = hMap1.get(s[0]);
    	    			 hMap1.put(s[0],num+1);
    	    		 }
    	    	  }
    	    	  else {
    	    		 gini_right.add(sequence);
    	    		 not_have++;
     	    		 boolean flag=hMap2.containsKey(s[0]);
     	    		 if(flag==false) {    	    			
     	    			 hMap2.put(s[0],1);
     	    		 }
     	    		 else {
     	    			 int num = hMap2.get(s[0]);
     	    			 hMap2.put(s[0],num+1);
     	    		 }
    	    	  }
    	      }   
    	     for (Entry<String, Integer> entry : hMap1.entrySet()) {    
    	            double temp = entry.getValue();  
    				giniD1 += Math.pow(temp/have,2);
    	        }
    	     for (Entry<String, Integer> entry : hMap2.entrySet()) {    
 	            double temp = entry.getValue();
 				giniD2 += Math.pow(temp/not_have,2);
 	        }  	     
    	     giniD1 = 1-giniD1;
    	     giniD2 = 1-giniD2;
             double temp1 = have;
             double temp2 = not_have;
    	     gini =((temp1/len)*giniD1)+((temp2/len)*giniD2);
    	  }
    	g.gini_left = gini_left;
    	g.gini_right = gini_right;
    	g.gini_index = gini;
    	g.gini_index_left = giniD1;
    	g.gini_index_right = giniD2;
		return g;
    }
    
    public String findMostlabel(ArrayList<String> train){
    	HashMap<String, Integer> labelMap = new HashMap<String, Integer>();
    	int max = 0;
    	String final_label = null;
    	for(int i=0;i<train.size();i++) {
    		String sequence = train.get(i);
    		String[] seq = sequence.split("\t");
			boolean flag = labelMap.containsKey(seq[0]);
   		    if(flag==false) {
   			   labelMap.put(seq[0],1);
   		    }
   		    else {
   			  int num = labelMap.get(seq[0]);
   			  labelMap.put(seq[0],num+1);
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
    
	/**
	 * build a decision tree based on the training set
	 * @param train: training set
	 * @return root node of decision tree
	 * @throws Exception
	 */
	public tree_node buildTree(cstNode stRoot,HashMap<String,Integer> labelmap ,train_patten trainP, int flag, int maxL, double threshold ,int minNum,int gap, double c, int p_num) throws Exception{	
		patternCountGini SG = new patternCountGini();
		tree_node root = new tree_node();
		generatePattern gp = new generatePattern();
		int is_leaf = 1;
		int b = minNum+1;
		gini_with_split g = new gini_with_split();
	    int len = trainP.pattern_length;
	    double train_gini = trainP.train_gini;
		if(train_gini < threshold) { 
			String s_label = findMostlabel(trainP.train);
			tree_node node = new tree_node(s_label, is_leaf);
			return node;
		}		
      	int train_size = trainP.train.size();
		if(train_size <= b) {
			String s_label = findMostlabel(trainP.train);
			root = new tree_node(s_label, is_leaf);
			return root;
		}
		double gini = Integer.MAX_VALUE;	
		int icount = 0;
		pattern finalPattern = new pattern();	
		gini_with_split g_temp = new gini_with_split();
		gini_with_split g_in = new gini_with_split();
		int pattern_count = p_num; 	
		if(stRoot != null && len < maxL) {  
	      while(true) {   
			 pattern p = gp.ramdom_find_pattern(trainP.train, len, maxL); //measure by use Random choose	
			 double quick_gini;
			 if(gap == 0 && flag ==1)
				  quick_gini = SG.SuffixCalculateGini(stRoot, labelmap, p, trainP.train);
			 else {
				   g_in = calculateGini(p, trainP.train, gap);
				   quick_gini = g_in.gini_index;
			     }				  
			 if(quick_gini < gini) {	 
				 gini = quick_gini;
				 finalPattern = p;	
			  }
			 icount++;
			 if(icount > pattern_count) {	 
				 break;
			   }
		   }
			g = calculateGini(finalPattern,trainP.train,gap);
        }      
        else {	
        	gini = Integer.MAX_VALUE;
        	while(gini > threshold) {
   			 pattern p = gp.ramdom_find_pattern(trainP.train, len, maxL); //measure by use Random choose	
   		     g_temp = calculateGini(p,trainP.train,gap);
   			 double gini_temp = g_temp.gini_index;
   			 if(gini_temp < gini) {
   				 gini = gini_temp;
   				 finalPattern = p;
   				 g = g_temp;
   			  }
   			 icount++;
   			 if(icount > pattern_count)
   				 break;
   			}
        }	    
        root.label = finalPattern.label;
        root.feature = finalPattern.pat;
        String most_label = null;     
        double temp_divide = g.gini_left.size();
        double divide = temp_divide / trainP.train.size();
    	if (divide >= 0.95 ) { // didn't split
    		if(g.gini_left.size() <= minNum) {
        		most_label = findMostlabel(g.gini_left);
        	    tree_node node = new tree_node(most_label,is_leaf);
        	    return node;
    		}
    		else {
	            	if(len == 8 ) {
	            		most_label = findMostlabel(g.gini_left);
	            	    tree_node node = new tree_node(most_label,is_leaf);
	            	    return node;
	            	}
	            	len = len + 1;
	            	train_patten trainLeft = new train_patten();
	            	trainLeft.train = g.gini_left;
	            	trainLeft.pattern_length = len;
	            	trainLeft.train_gini = g.gini_index_left;
	            	int pattern_flag = 0;    	
	            	if(trainLeft.train.size()>50 && gap == 0 && flag == 1) { 
	            		countSuffixTree cstTree = new countSuffixTree();
	            		labelTree2 lt2 = cstTree.buildTree(trainLeft.train, maxL, c);
	            		root.leftChild = buildTree(lt2.root, lt2.labelmap, trainLeft, pattern_flag, maxL, threshold, minNum, gap, c, p_num);
	            	}
	            	else {
	            		root.leftChild = buildTree(null, null, trainLeft, pattern_flag, maxL, threshold, minNum, gap, c, p_num);
	            	}
    		}
        }
    	else if(g.gini_left.size()==0){
    		root.leftChild = new tree_node(root.label,is_leaf);
        }
    	else if(g.gini_left.size() != trainP.train.size() && g.gini_left.size() != 0) {
        	train_patten trainLeft = new train_patten();
        	trainLeft.train = g.gini_left;
        	trainLeft.train_gini = g.gini_index_left;
            	if(g.gini_left.size()>50 && gap == 0 && flag == 1) {
        			countSuffixTree cstTree = new countSuffixTree();
        		    labelTree2 lt2 = cstTree.buildTree(trainLeft.train, maxL, c);
            		root.leftChild = buildTree(lt2.root, lt2.labelmap, trainLeft, 1, maxL, threshold, minNum, gap, c, p_num);
            	}
            	else {
            		root.leftChild = buildTree(null, null, trainLeft, 0, maxL, threshold, minNum, gap, c, p_num);
            	}
    	}
    	if( g.gini_right.size()==0){
    		root.rightChild = new tree_node(root.label,is_leaf);
        }
    	else {
        	train_patten trainRight = new train_patten();
        	trainRight.train =  g.gini_right;
        	trainRight.train_gini = g.gini_index_right;  	
        		if(g.gini_right.size()>50 && gap == 0 && flag == 1) {
        			countSuffixTree cstTree = new countSuffixTree();
        		    labelTree2 lt2 = cstTree.buildTree(trainRight.train, maxL, c);
            		root.rightChild = buildTree(lt2.root, lt2.labelmap, trainRight, 1, maxL, threshold, minNum, gap, c, p_num);
        			}
        		else {
        			root.rightChild = buildTree(null, null, trainRight, 0, maxL, threshold, minNum, gap, c, p_num);
        		}
    	}	
        return root;
	}	
	
}
	
