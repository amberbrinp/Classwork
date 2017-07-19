import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Iterator;
import java.util.Stack;

public class Graph<T>
{
    private final int max_num_vertices;
    private int size;
    
    private Edge<T>[][] adjacencyMatrix;
    private LinkedList<LinkedList<Vertex<T>>> adjacencyList;
    
    private LinkedList<Vertex<T>> vertices;
    private Vertex<T> top;
    private boolean dag = true;
    
    
    public Graph(int max_num_vertices) //constructor should accept the maximum number of vertices allowed in the graph
    {
        size = 0;
        top = null;
        
        this.max_num_vertices = max_num_vertices;
        
        adjacencyList = new LinkedList<LinkedList<Vertex<T>>>();
        adjacencyMatrix = new Edge[max_num_vertices][max_num_vertices];
        vertices = new LinkedList<Vertex<T>>();
    }
    
    public void addVertex(T item) //allow items (items are identified by String search keys) to be stored in the Graph vertices
    {
        if (size > max_num_vertices) return;
        
        
        Vertex<T> v = new Vertex<T>(item, size);
        if (size == 0) top = v;
        
        String key = v.getKey();
        vertices.add(v);
        LinkedList<Vertex<T>> edges = new LinkedList<Vertex<T>>();
        edges.add(v);
        adjacencyList.add(edges);
        size++;
    }
    
    public void addEdge(String start_vertex_key, String end_vertex_key, double edge_weight) //add a directed edge between two vertices
    {
        //iterate through vertices till you find the right ones, quit once you do
        Iterator<Vertex<T>> iter = vertices.iterator();
        Vertex<T> start = null;
        Vertex<T> end = null;
        while (iter.hasNext())
        {
            Vertex<T> v = iter.next();
            if (v.getKey().equals(start_vertex_key))
            {
                start = v;
                if (end != null) break;
            }
            else if (v.getKey().equals(end_vertex_key))
            {
                end = v;
                if (start != null) break;
            }
        }
        
        //iterate through adjacency lists till you find the right one, add the endpoint & quit once you do
        if (start != null && end != null)
        {
            Iterator <LinkedList<Vertex<T>>> lists = adjacencyList.iterator();
            int i = 0;
            while (lists.hasNext())
            {
                LinkedList<Vertex<T>> v = lists.next();
                if (v.get(0).getKey().equals(start.getKey()))
                {
                    adjacencyList.get(i).add(end);
                    break;
                }
                i++;
            }
            
            Edge<T> edge = new Edge<T>(start, end, edge_weight);
            adjacencyMatrix[start.getIndex()][end.getIndex()] = edge;
        }
    }
    
   public List dfs() //perform a depth first search, adding items to a linked list
    {
        LinkedList<T> dfs = new LinkedList<T>();
        dfs(top, dfs);
        
        for (Vertex<T> v : vertices)
        {
            if (!v.wasVisited())
            {
                dfs(v, dfs);
            }
            v.setAsVisited(false);
        }
        
        return dfs;
    }
    
    private void dfs(Vertex<T> vertex, LinkedList<T> dfs)
    {
        dfs.add(vertex.getItem());
        vertex.setAsVisited(true);
        
        for (int i = 0; i < size; i++)
        {
            Edge<T> edge = adjacencyMatrix[vertex.getIndex()][i];
            if (edge != null && !edge.getEndPoint().wasVisited()) dfs(edge.getEndPoint(), dfs);
        }
    }
    
    public List bfs() //perform a breadth first search, adding items to a linked list
    {
        LinkedList<T> bfs = new LinkedList<T>();
        bfs(top, bfs);
        
        for (Vertex<T> v : vertices)
        {
            if (!v.wasVisited())
            {
                bfs(v, bfs);
            }
            v.setAsVisited(false);
        }
        
        return bfs;
    }
    
    private void bfs(Vertex<T> top, LinkedList<T> bfs)
    {
        Queue<Vertex<T>> q = new LinkedList<Vertex<T>>();
        q.add(top);
        bfs.add(top.getItem());
        top.setAsVisited(true);
        
        while (!q.isEmpty())
        {
            //iterate through the adjacency lists to get the right one
            Iterator<LinkedList<Vertex<T>>> lists = adjacencyList.iterator();
            int i = 0;
            Vertex<T> peek = q.peek();
            LinkedList<Vertex<T>> edges = null;
            while (lists.hasNext())
            {
                LinkedList<Vertex<T>> v = lists.next();
                if (v.get(0).getKey().equals(peek.getKey()))
                {
                    edges = adjacencyList.get(i);
                    break;
                }
                i++;
            }

            //iterate through the one list & perform the search on it
            Iterator<Vertex<T>> iter = edges.iterator();
            while (iter.hasNext())
            {
                Vertex<T> end = iter.next();
                if (!end.wasVisited())
                {
                    end.setAsVisited(true);
                    bfs.add(end.getItem());
                    q.add(end);
                }
            }
            q.poll();
        }
    }
    
    public List topological_sort() //compute a topological sort using depth first search, return null if not a dag
    {
        LinkedList<T> ts = new LinkedList<T>();
        Stack<Vertex<T>> stack = new Stack<Vertex<T>>();
        dag = true; //in case it had been left as false before
        
        for (Vertex<T> v : vertices)
        {
            if (!v.wasVisited())
            {
                topological_sort(v, stack, ts);
            }
            v.setAsVisited(false);
        }
        
        if(!dag) return null;
        
        return ts;
    }
    
    private void topological_sort(Vertex<T> vertex, Stack<Vertex<T>> stack, LinkedList<T> ts)
    {
        for (Vertex<T> v : stack)
        {
            if (v.wasVisited())
            {
                if (adjacencyMatrix[vertex.getIndex()][v.getIndex()] != null)
                {
                    dag = false;
                    return;
                }
            }
        }
        
        stack.push(vertex);
        vertex.setAsVisited(true);
        
        for (int i = 0; i < size; i++)
        {
            Edge<T> edge = adjacencyMatrix[vertex.getIndex()][i];
            if (edge != null)
            {
                if (!edge.getEndPoint().wasVisited()) topological_sort(edge.getEndPoint(), stack, ts);
            }
        }
        ts.add(0, stack.pop().getItem());
    }
}