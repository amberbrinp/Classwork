public class PegGameOverState implements PegState
{
    private Pegs pegs;
    public PegGameOverState(Pegs pegs)
    {
        this.pegs = pegs;
    }
    
    public void mouseClicked(int x, int y)
    {
        if(pegs.gameOver())
        {
            int loc = pegs.findSelectedSlot(x,y);
            if (loc != -1)
            {
                pegs.startGame(loc);
            }
            else
            {
                loc = pegs.findSelectedPeg(x, y);
                if (loc != -1)
                {
                    pegs.startGame(loc);
                }
            }
        }
    }
}