import java.util.LinkedList;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.NumberFormatException;
import java.lang.ArrayIndexOutOfBoundsException;

public class GraphDriver
{
    public static void main(String args[])
    {
        if (args.length != 2)
        {
            System.out.println("Usage: GraphDriver vertexFileName edgeFileName");
            return;
        }
        
        Graph<String> graph = makeGraph(args);
        if (graph == null) return;
        
        LinkedList<String> bfs = (LinkedList<String>) graph.bfs();
        System.out.println("Breadth-First Search:");
        Iterator<String> iter = bfs.iterator();
        while (iter.hasNext())
        {
            System.out.println(iter.next());
        }
        
        System.out.println();
        
        LinkedList<String> dfs = (LinkedList<String>) graph.dfs();
        System.out.println("Depth-First Search:");
        iter = dfs.iterator();
        while (iter.hasNext())
        {
            System.out.println(iter.next());
        }
        
        System.out.println();
        
        LinkedList<String> ts = (LinkedList<String>) graph.topological_sort();
        System.out.println("Topological Sort:");
        if (ts != null)
        {
            iter = ts.iterator();
            while (iter.hasNext())
            {
                System.out.println(iter.next());
            }
        }
        else
        {
            System.out.println("no");
        }
    }
    
    private static Graph<String> makeGraph(String args[])
    {
        LinkedList<String> vertices = readVertices(args[0]);
        if (vertices == null) return null;
        
        Graph<String> graph = new Graph<String>(vertices.size());
        
        Iterator<String> iter = vertices.iterator();
        while (iter.hasNext())
        {
            graph.addVertex(iter.next());
        }
        
        if (!readEdges(args[1], graph)) return null;
        
        return graph;
    }
    
    private static LinkedList<String> readVertices(String fileName)
    {
        LinkedList<String> vertices = new LinkedList<String>();
        
        try
        {
            File file = new File(fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String vertex = reader.readLine();
            while (vertex != null)
            {
                vertices.add(vertex);
                vertex = reader.readLine();
            }
            reader.close();
        }
        catch(IOException ioe)
        {
            System.out.println("error reading vertices");
            return null;
        }
        
        return vertices;
    }
    
    private static boolean readEdges(String fileName, Graph<String> graph)
    {
        try
        {
            File file = new File(fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String edge = reader.readLine();
            while (edge != null)
            {
                String[] edgeValues = edge.split(" ");
                graph.addEdge(edgeValues[0], edgeValues[1], Double.parseDouble(edgeValues[2]));
                edge = reader.readLine();
            }
            reader.close();
        }
        catch(IOException | NumberFormatException | ArrayIndexOutOfBoundsException e)
        {
            System.out.println("error reading edges\n" + e);
            return false;
        }
        
        return true;
    }
}