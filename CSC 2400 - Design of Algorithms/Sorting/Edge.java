public class Edge<T>
{
    private Vertex<T> start;
    private Vertex<T> end;
    private double weight;
    
    public Edge(Vertex<T> start, Vertex<T> end, double weight)
    {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }
    
    public Vertex<T> getStartPoint()
    {
        return start;
    }
    
    public Vertex<T> getEndPoint()
    {
        return end;
    }
    
    public double getWeight()
    {
        return weight;
    }
}