import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Collection;
import java.util.LinkedHashMap;

public class Graph<T>
{
    private int size;//0-based
    
    private LinkedList<LinkedList<Edge<T>>> adjacencyList;
    //now holds lists of edges to hold the weights
    
    private LinkedHashMap<String, Vertex<T>> vertices;
    private Vertex<T> top;
    
    //constructor should accept the maximum number of vertices allowed in the graph
    public Graph()
    {
        size = 0;
        top = null;
        
        adjacencyList = new LinkedList<LinkedList<Edge<T>>>();
        vertices  = new LinkedHashMap<String, Vertex<T>>();
    }
    
    //adds a Vertex to the Graph with a unique String identifier stored at the Vertex
    public void addVertex(T item) 
    {
        Vertex<T> v = new Vertex<T>(item, size);
        if (size == 0) top = v;
        
        adjacencyList.add(new LinkedList<Edge<T>>());
        String key = v.getKey();
        vertices.put(key, v);
        
        size++;
    }
    
    //add a directed edge between two vertices, with the specified weight
    public void addEdge(String start_vertex_key, String end_vertex_key, double edge_weight)
    {
        Vertex<T> start = vertices.get(start_vertex_key);
        Vertex<T> end = vertices.get(end_vertex_key);
        
        Edge<T> edge = new Edge<T>(start,end,edge_weight);
        adjacencyList.get(start.getIndex()).add(edge);
    }
    
    //returns a List of the edges in the minimum spanning tree computed using Prim's algorithm (use the first vertex in the vertex list as the starting vertex)
    public List<Edge<T>> prim() 
    {
        List<Edge<T>> primEdges = new LinkedList<Edge<T>>();
        PriorityQueue<Edge<T>> pq = new PriorityQueue<Edge<T>>(size,Edge.comparator);
        
        Iterator<Vertex<T>> iter = vertices.values().iterator();
        while(iter.hasNext())
        {
            Vertex<T> v = iter.next();
            if(!v.wasVisited()) prim(v,primEdges,pq);
        }
         
        for (Vertex<T> vertex: vertices.values())
        {
            vertex.setAsVisited(false);
        }
        
        return primEdges;
    }
    
    private void prim(Vertex<T> vertex, List<Edge<T>> primEdges, PriorityQueue<Edge<T>> pq)
    {
        vertex.setAsVisited(true);
 
        LinkedList<Edge<T>> adjacentEdges = adjacencyList.get(vertex.getIndex());
        
        Iterator<Edge<T>> iter = adjacentEdges.iterator();
        while(iter.hasNext()) { pq.add(iter.next()); }
        
        Edge<T> e = pq.poll();
        while (e != null && e.getEndPoint().wasVisited()) { e = pq.poll(); }
        
        if (e != null)
        {
            primEdges.add(e);
            prim(e.getEndPoint(),primEdges,pq);
        }
    }
    
    //returns a List of the edges in the minimum spanning tree computed using Kruskal's algorithm
    public List<Edge<T>> kruskal()
    {
        List<Edge<T>> kruskalEdges = new LinkedList<Edge<T>>();
        PriorityQueue<Edge<T>> pq = new PriorityQueue<Edge<T>>(size,Edge.comparator);
        HashMap < Vertex<T>, Node<Vertex<T>> > nodes = new HashMap< Vertex<T>, Node<Vertex<T>> >();
        
        Collection values = vertices.values();
        Iterator<Vertex<T>> iter = values.iterator();
        while (iter.hasNext())
        {
            Vertex<T> vertex = iter.next();
            
            Node<Vertex<T>> node = new Node<Vertex<T>>(vertex);
            Tree<Vertex<T>> tree = new Tree<Vertex<T>>(node);
            node.setParentTree(tree);
            
            nodes.put(vertex, node);
            
            LinkedList<Edge<T>> adjacentEdges = adjacencyList.get(vertex.getIndex());
            Iterator<Edge<T>> edgeIter = adjacentEdges.iterator();
            while (edgeIter.hasNext()) { pq.add(edgeIter.next()); }
        }
        
        kruskal(kruskalEdges, pq, nodes);
        
        return kruskalEdges;
    }
    
    private void kruskal(List<Edge<T>> kruskalEdges, PriorityQueue<Edge<T>> pq, HashMap< Vertex<T>, Node<Vertex<T>> > nodes)
    {
        Edge<T> edge = pq.poll();
        while (edge != null)
        {
            Node<Vertex<T>> start = nodes.get(edge.getStartPoint());
            Node<Vertex<T>> end = nodes.get(edge.getEndPoint());
            
            //quick union
            if (!start.getRoot().equals(end.getRoot()))
            {
                kruskalEdges.add(edge);
                
                Tree<Vertex<T>> startTree = start.getParentTree();
                Tree<Vertex<T>> endTree = end.getParentTree();
                
                if(startTree.getSize() < endTree.getSize())
                {
                    if (!endTree.addTree(startTree))
                        kruskalEdges.remove(kruskalEdges.size()-1);
                    else
                        start.setParentTree(endTree);
                }
                else
                {
                    if (!startTree.addTree(endTree))
                        kruskalEdges.remove(kruskalEdges.size()-1);
                    else
                        end.setParentTree(startTree);
                }
            }
            
            edge = pq.poll();
            
        }
    }
    
    //returns the shortest path between the specified vertices
    public List<Vertex<T>> shortestPath(String start_vertex_key, String end_vertex_key)
    {
        PriorityQueue<Node<Vertex<T>>> pq = new PriorityQueue<Node<Vertex<T>>>(size, Node.comparator);
        Node<Vertex<T>> [] paths = new Node[size];
        
        Vertex<T> start = vertices.get(start_vertex_key);
        Vertex<T> end = vertices.get(end_vertex_key);
     
        Node<Vertex<T>> startNode = new Node<Vertex<T>>(start);
        startNode.setDistance(0);
        startNode.setPrevious(start);
        paths[start.getIndex()] = startNode;
        
        pq.add(startNode);
        
        shortestPath(paths, pq);
        
        LinkedList<Vertex<T>> shortestPath = new LinkedList<Vertex<T>>();
        
        Node<Vertex<T>> endNode = paths[end.getIndex()];
        end.setDistance(endNode.getDistance());
        shortestPath.add(0, end);
        Vertex<T> path = endNode.getPrevious();
        
        while(!path.getKey().equals(start_vertex_key))
        {
            path.setDistance(paths[path.getIndex()].getDistance());
            shortestPath.add(0, path);
            path = paths[path.getIndex()].getPrevious();
        }
        shortestPath.add(0, start);
        
        return shortestPath;
        
     }
    
    
    private void shortestPath(Node<Vertex<T>>[] paths, PriorityQueue<Node<Vertex<T>>> pq)
    {
        while (!pq.isEmpty())
        {
            LinkedList<Edge<T>> adjacentEdges = adjacencyList.get(pq.poll().getItem().getIndex());
            
            for(Edge<T> edge : adjacentEdges)
            {
                Vertex<T> startPoint = edge.getStartPoint();
                Vertex<T> endPoint = edge.getEndPoint();
                
                Node<Vertex<T>> node = new Node<Vertex<T>>(endPoint);
                node.setPrevious(startPoint);
                
                Node<Vertex<T>> pathStart = paths[startPoint.getIndex()];
                Node<Vertex<T>> pathEnd = paths[endPoint.getIndex()];
                
                if(pathStart != null)
                    node.setDistance(pathStart.getDistance() + edge.getWeight());
                else
                    node.setDistance(edge.getWeight());
                
                if(pathEnd != null)
                {
                    if (node.getDistance() < pathEnd.getDistance())
                    {
                        pq.decreaseKey(pathEnd,node);//edited PQ source code
                        paths[endPoint.getIndex()] = node;
                    }
                }
                else
                {
                    paths[endPoint.getIndex()] = node;
                    pq.add(node);
                }
            }
        }
    }
}