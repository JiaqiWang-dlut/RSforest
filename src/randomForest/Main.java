package randomForest;

import java.util.ArrayList;
import suffixTree.*;

public class Main {
	
public static void main(String args[])throws Exception{   
		int g = 0; // gap constraint
		int maxL = 4; // maximum length
		double threshold=0.1; //maximum value of Gini index in one node
		int minNum = 2; // minimum number of sequence in one node
	    double c = 0.5;// percentage of sampled sequences 
	    long start_time = System.currentTimeMillis();
	    int q = 50; // number of trees
	    int p = 10; // number of sequences
	    int flag = 0; // whether use count-suffix tree or not (1 = yes, 0 = no)
 		ReadFile r=new ReadFile();
		ArrayList<String> nfTrain = new ArrayList<String>();
		ArrayList<String> nfTest = new ArrayList<String>();	
		nfTrain=r.Read("src/data/ActivityTrain.txt"); 
		nfTest=r.Read("src/data/ActivityTest.txt"); 
		createForest m1 = new createForest();
		classifyTest ct = new classifyTest();
		train_patten trainP = new train_patten();	
        trainP.train = nfTrain;
        trainP.pattern_length = maxL;    
		tree_node rf[];  
		rf = new tree_node[q];		
	    countSuffixTree cstTree = new countSuffixTree();
	    labelTree2 lt2 = cstTree.buildTree(nfTrain, maxL, c);
		for(int k = 0; k < rf.length; k++ )
		{	  
		  rf[k] = m1.buildTree(lt2.root, lt2.labelmap, trainP, flag, maxL, threshold, minNum, g, c, p);							
		}		    
		float acc = ct.Test(nfTest, rf);	
		System.out.println("acc: " + acc);
		long end_time = System.currentTimeMillis(); 
		System.out.println("running time: " + (end_time - start_time) + "ms");
	}
}
