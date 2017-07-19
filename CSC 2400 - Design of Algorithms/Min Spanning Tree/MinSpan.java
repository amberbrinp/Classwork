import java.util.LinkedList;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.NumberFormatException;
import java.lang.ArrayIndexOutOfBoundsException;

public class MinSpan
{
    public static void main(String args[])
    {
        if (args.length != 4)
        {
            System.out.println("Usage: MinSpan vertexFileName edgeFileName start_vertex_key end_vertex_key");
            return;
        }
        
        Graph<String> graph = makeGraph(args);
        if (graph == null) return;
        
        LinkedList<Edge<String>> prim = (LinkedList<Edge<String>>) graph.prim();
        System.out.println("Prim:");
        Iterator<Edge<String>> iter = prim.iterator();
        while (iter.hasNext())
        {
            System.out.println(iter.next().toString());
        }
        
        System.out.println();
        
        LinkedList<Edge<String>> kruskal = (LinkedList<Edge<String>>) graph.kruskal();
        System.out.println("Kruskal:");
        iter = kruskal.iterator();
        while (iter.hasNext())
        {
            System.out.println(iter.next().toString());
        }
        
        System.out.println();
        
        LinkedList<Vertex<String>> shortestPath = (LinkedList<Vertex<String>>) graph.shortestPath(args[2],args[3]);
        System.out.println("Shortest Path from " + args[2] + " to " + args[3] + ":");
        Iterator<Vertex<String>> spIter = shortestPath.iterator();
        while (spIter.hasNext())
        {
            Vertex<String> vertex = spIter.next();
            System.out.println(vertex.getKey() + ": " + vertex.getDistance());
        }
    }
    
    private static Graph<String> makeGraph(String args[])
    {
        LinkedList<String> vertices = readVertices(args[0]);
        if (vertices == null) return null;
        
        Graph<String> graph = new Graph<String>();
        
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