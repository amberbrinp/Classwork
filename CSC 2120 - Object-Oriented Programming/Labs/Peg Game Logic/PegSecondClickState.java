
public class PegSecondClickState implements PegState
{
   //need another instance variable
   private int start;  //remember which peg was clicked on (info forwarded from PegFirstClickState)
    private Pegs pegs;

   public PegSecondClickState(Pegs pegs)
   {
       this.pegs = pegs;
       start = -1;
   }

   //method called by PegFirstClickState
   public void setStart(int start)
   {
      this.start = start;
   }

   public void mouseClicked(int x, int y)
   {
      //find which slot was clicked on for the second mouse click
      int end = pegs.findSelectedSlot(x,y);
       
      Jump jump = pegs.getJump(start, end);

      //perform the jump (refer to PegFirstClickState)
      //check for end of game
      if (jump != null)  
      {
          jump.jump(pegs);
          
          pegs.incrementTurn();
          
          if (pegs.gameOver())
          {
              pegs.setState(pegs.getGameOverState());
          }
          
        //if you in fact change to game over state, make sure to not change to first click state below
      }
       
      if(!pegs.gameOver())
      {
            pegs.setState(pegs.getFirstClickState());
              
            start = -1;
      }

      //reset to the first click state (reset start as well)

   }
}