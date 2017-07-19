public class Vertex<T>
{
    private T item;
    private int index;
    private boolean visited;
    
    public Vertex(T item, int index)
    {
        this.item = item;
        this.index = index;
        
        visited = false;
    }
    
    public void setAsVisited(boolean tf)
    {
        visited = tf;
    }
    
    public T getItem()
    {
        return item;
    }
    
    public int getIndex()
    {
        return index;
    }
    
   public boolean wasVisited()
    {
        return visited;
    }
    
    public String getKey()
    {
        return item.toString();
    }
    
}