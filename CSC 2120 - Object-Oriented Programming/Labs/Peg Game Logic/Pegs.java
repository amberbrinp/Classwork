import java.awt.Graphics;
import java.util.ArrayList;

public class Pegs
{
   private ArrayList<Peg> pegs;
   private Jumps jumps;

   //DO THIS (instance variables for the state design pattern)
    private PegState state;
    private PegFirstClickState firstState;
    private PegSecondClickState secondState;
    private PegGameOverState gameOverState;
    
   private PegGameAI pgai = PegGameAI.getPegGameAI();
   private int turn;
 
   //DO THIS
   public Pegs(int open_loc)  
   {
      //instantiate the states
       firstState = new PegFirstClickState(this);
       secondState = new PegSecondClickState(this);
       gameOverState = new PegGameOverState(this);

      startGame(open_loc);
      jumps = new Jumps(this);
   }

   //DO THIS (short methods required in the state design pattern, five all together including mouseClicked)

    public PegFirstClickState getFirstClickState()
    {
        return firstState;
    }
    
    public PegSecondClickState getSecondClickState()
    {
        return secondState;
    }
    
    public PegGameOverState getGameOverState()
    {
        return gameOverState;
    }
    
    public void setState(PegState state)
    {
        this.state = state;
    }
    
    public void mouseClicked(int x, int y)
    {
        state.mouseClicked(x, y);
    }


   //no work below this point (but you will need to call some of these methods)
   public void startGame(int open_loc)
   {
      pegs = new ArrayList<Peg>();

      for (int i = 1; i <= 15; i++)
      {
         Peg peg = new Peg(i);
         pegs.add(peg);
      }

      Peg open_peg = getPeg(open_loc);
      open_peg.setLocation(-1);

       state = firstState;    //set the current state to your first click state
      turn = 1;
   }

   public ArrayList<Jump> getPossibleJumps(int select)
   {
      ArrayList<Jump> possible_jumps = jumps.getJumps(select);
      return possible_jumps;
   }

   public Jump getJump(int start, int end)
   {
      Jump jump = jumps.getJumps(start, end);
      return jump;
   }

   public int findSelectedPeg(int x, int y)
   {
      int select = -1;
      for (Peg peg : pegs)
      {
         if (peg.isPegClicked(x, y))
         {
            select = peg.getLocation();
            break;
         }
      }
      return select;
   }

   public int findSelectedSlot(int x, int y)
   {
      if (findSelectedPeg(x, y) > -1) return -1;  //make sure there is no peg at this spot

      int select = -1;
      int count = 1;
      for (Peg peg : pegs)
      {
         if (peg.isSlotClicked(x, y))
         {
            select = count;
            break;
         }
         count++;
      }
      return select;
   }

   public void incrementTurn()
   {
      turn++;
   }

   public boolean gameOver()
   {
      ArrayList<Jump> validJumps = jumps.getValidJumps();
      int num_jumps = validJumps.size();
      if (num_jumps == 0)
      {
         return true;
      }
      return false;
   }

   //find the Peg at specific locations on the board
   public Peg getPeg(int peg_loc)
   {
      for (Peg peg : pegs)
      {
         int loc = peg.getLocation();
         if (loc == peg_loc) return peg;
      }

      return null;
   }

   public void setImages()
   {
      for (Peg peg : pegs)
      {
         peg.setImage(0); //reset all pegs to the red image
      }

      ArrayList<Jump> validJumps = jumps.getValidJumps();
      for (Jump jump : validJumps)
      {
         int start = jump.getStart();
         Peg peg = getPeg(start);
         peg.setImage(1);  //pegs that can jump another peg are magneta
      }

      if (turn > 1)
      {
         int loc = pgai.getBestMove(pegs);
         Peg peg = getPeg(loc);
         if (peg != null)
         {
            peg.setImage(2);
         }
      }
   }

   public void draw(Graphics g)
   {
      setImages();

      for (Peg peg : pegs)
      {
         peg.draw(g);
      }
   }
}