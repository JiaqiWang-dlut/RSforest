package randomForest;

import java.util.ArrayList;
import java.util.Random;

public class five_fold_test {
	public ArrayList<String> sample(ArrayList<String> dataset, int m){
		int leng = dataset.size();
		ArrayList<String> list = new ArrayList<String>();
		for(int i=0;i<m;i++) {
			 Random rand = new Random();
			 int choose = rand.nextInt(leng);
			 String str = dataset.get(choose);			
			 list.add(str);
		}
		return list;
	}
	
	public nFold divideSet(ArrayList<String> dataset, int fold) {
		ArrayList<String> newList = new ArrayList<>();		
		for (int i=0;i<dataset.size();i++)
		     newList.add(dataset.get(i));
	    nFold nf =  new nFold(fold);   
		Random rand = new Random();
		int len = dataset.size();
		int train_len = len/fold;
		int p = fold - 1;
	 for(int i=0; i<p; i++) {
		for(int j=0;j<train_len;j++) {
			len = newList.size();
			int temp = rand.nextInt(len);
			nf.foldList.get(i).add(newList.get(temp));
			newList.remove(temp);
		 }
	 }
	int final_len = newList.size();
	for(int k=0; k<final_len; k++) {
		nf.foldList.get(p).add(newList.get(k));
	}		
		return nf;
}

}
