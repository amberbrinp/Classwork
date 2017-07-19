import java.util.Comparator;

public class Node<T>
{
    private Node<T> parent;
    private T item;
    private Tree<T> parentTree;
    
    private T previous;
    private double distance;
    
    public Node(T item)
    {
        this.item = item;
        
        parent = null;
        parentTree = null;
    }
    
    public Node<T> getParent()
    {
        return parent;
    }
    
    public Node<T> getRoot()
    {
        if (parent == null) return this;
        else return parent.getRoot();
    }
    
    public T getItem()
    {
        return item;
    }
    
    public Tree<T> getParentTree()
    {
        return parentTree;
    }
    
    public void setParentTree(Tree<T> tree)
    {
        parentTree = tree;
    }
    
    public boolean setParentNode(Node<T> parent)
    {
        if (!parent.equals(this))
        {
            this.parent = parent;
            return true;
        }
        else
            return false;
    }
    
    public void setPrevious(T previous)
    {
        this.previous = previous;
    }
    
    public T getPrevious()
    {
        return previous;
    }
    
    public void setDistance(double distance)
    {
        this.distance = distance;
    }
    
    public double getDistance()
    {
        return distance;
    }
    
    public static final Comparator<Node> comparator = new Comparator<Node>()
    {
        public int compare(Node n1, Node n2)
        {
            return (int) (n1.getDistance() - n2.getDistance());
        }
    };

}