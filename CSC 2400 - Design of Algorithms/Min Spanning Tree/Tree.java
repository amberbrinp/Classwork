public class Tree<T>
{
    private Node<T> root;
    private int size;
    
    public Tree(Node<T> root)
    {
        this.root = root;
        size = 1;
    }
    
    public Node<T> getRoot()
    {
        return root;
    }
    
    public int getSize()
    {
        return size;
    }
    
    public boolean addTree(Tree<T> tree)
    {
        size += tree.getSize();
        tree.size = size;
        if(tree.getRoot().setParentNode(root))
        {
            tree.root = root;
            return true;
        }
        else
           return false;
    }
}