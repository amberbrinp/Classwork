import java.util.Comparator;

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
    
    public String toString()
    {
        return "( " + start.getKey() + ", " + end.getKey() + ", " + weight + " )";
    }
    
    public static final Comparator<Edge> comparator = new Comparator<Edge>()
    {
        public int compare(Edge e1, Edge e2)
        {
            return (int) (e1.getWeight() - e2.getWeight());
        }
    };
}