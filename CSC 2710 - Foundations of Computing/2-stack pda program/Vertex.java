/*
 This is essentially a wrapper class for int.
 I could have used Integer or an int[] and made the Graph class simpler.
 Represents a state in the PDA.
 - Amber Patterson */

public class Vertex
{
    private int index;
    
    public Vertex(int index)
    {
        this.index = index;
    }
    
    public int getIndex()
    {
        return index;
    }
    
}