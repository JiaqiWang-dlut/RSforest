package randomForest;


import java.util.ArrayList;

import suffixTree.*;

public class five_fold_experiment {
	
public static void main(String args[])throws Exception{   
		int g = 0; // gap constraint
		int maxL = 4; // maximum length
		double threshold=0.1; //maximum value of Gini index in one node
		int minNum = 2; // minimum number of sequence in one node
	    double c = 0.5;// percentage of sampled sequences 
	    int q = 50; // number of trees
	    int p = 10; // number of sequences
	    int flag = 1; // whether use count-suffix tree or not (1 = yes, 0 = no)
		double sum = 0;
	    double aver_time = 0;
	    int fold = 5;
 		ReadFile r=new ReadFile();
		ArrayList<String> Tdata=new ArrayList<String>();
		Tdata=r.Read("src/data/question.txt");    
		createForest m1 = new createForest();
		classifyTest ct = new classifyTest();
		five_fold_test ft = new five_fold_test();
		nFold n_fold_sets = new nFold(fold); //5-fold test
		n_fold_sets = ft.divideSet(Tdata,fold);
		double average_acc = 0;
		train_patten trainP = new train_patten();	
        for(int z = 0;z<5;z++) {
        	long startTime=System.currentTimeMillis(); 
        	for(int i=0; i<fold; i++) {  //5-fold test
	        	long five_fold_startTime=System.currentTimeMillis();  
				ArrayList<String> nfTrain = new ArrayList<String>();
				ArrayList<String> nfTest = new ArrayList<String>();	
			    for(int j=0; j<fold; j++) {
				  if(j!=i)
					 nfTrain.addAll(n_fold_sets.foldList.get(j));
			     }
				nfTest.addAll(n_fold_sets.foldList.get(i));		
	            trainP.train = nfTrain;  
				tree_node rf[];  
				rf = new tree_node[q];
			    countSuffixTree cstTree = new countSuffixTree();
			    labelTree2 lt2 = cstTree.buildTree(nfTrain, maxL, c);
				for(int k = 0; k < rf.length; k++ )
				{	
				  rf[k] = m1.buildTree(lt2.root, lt2.labelmap, trainP, flag, maxL, threshold, minNum, g, c, p);					
				}		    
				float acc = ct.Test(nfTest, rf);		
				average_acc += acc;
				System.out.println("acc: " + acc);
				long five_fold_endTime = System.currentTimeMillis(); 
				System.out.println("running time：" + (five_fold_endTime - five_fold_startTime) + "ms"); 
	     }
	        System.out.print("cv_average_acc: ");//5折的平均
			System.out.println(average_acc/fold);
			double pp = average_acc /fold;
			sum += pp;
			long endTime=System.currentTimeMillis(); //获取结束时间
			double time = endTime - startTime;//一次五折的时间
			aver_time += time;
			System.out.println("cv_average_time： "+(endTime-startTime)+"ms");
			average_acc = 0;
        }      
        System.out.println("average acc of cv_average_acc: ");
		System.out.println(sum/5);//5 means run 5-fold cross-vaildation 5 times
		System.out.println("average time of five 5-fold cross-validation： "+(aver_time/5)+"ms");//五次五折/5的时间
	}

}
