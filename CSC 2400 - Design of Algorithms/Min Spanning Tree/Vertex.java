public class Vertex<T>
{
    private T item;
    private boolean visited;
    
    private int index;
    private double distance;
    
    public Vertex(T item, int index)
    {
        this.item = item;
        visited = false;
        this.index = index;
        
        distance = 0;
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
    
    public void setDistance(double distance)
    {
        this.distance = distance;
    }
    
    public double getDistance()
    {
        return distance;
    }
}