import java.util.ArrayList;

public class WorstCDs implements Command
{
    private ArrayList<CD> cds = new ArrayList<CD>();
    private int worstRating = 10;//assuming 10/10 rating
    
    public void execute(Object item)
    {
        CD cd = (CD) item;
        int rating = cd.getRating();
        if (rating < worstRating)
        {
            cds = new ArrayList<CD>();//essentially empty the list
            cds.add(cd);
            worstRating = rating;
        }
        else if (rating == worstRating)
            cds.add(cd);
    }
    
    public ArrayList<CD> getWorstCDs()
    {
        return cds;
    }
}