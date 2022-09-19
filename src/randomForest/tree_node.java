package randomForest;

import java.util.ArrayList;

public class tree_node {

	public tree_node leftChild;
	public tree_node rightChild;
	public String[] feature;
	public String label;
	public int is_leaf = 0;
	public ArrayList<tree_node> children = new ArrayList<tree_node>();
	
	
	public tree_node() {
		this.feature=null;
		this.leftChild = null;
		this.rightChild = null;
		this.label=null;
	}
	
    @Override
	public tree_node clone() throws CloneNotSupportedException {
        return (tree_node) super.clone();
    }

	
	public tree_node(String l,int flag) {
		this.feature=null;
		this.leftChild = null;
		this.rightChild = null;
		this.label=l;
		this.is_leaf = flag;
	}
}
