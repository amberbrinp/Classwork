import java.util.ArrayList;

public class PegFirstClickState implements PegState
{
   private Pegs pegs;

   public PegFirstClickState(Pegs pegs)
   {
      this.pegs = pegs;
   }

   public void mouseClicked(int x, int y)
   {
      //find out which peg was clicked on (call a method in Pegs)
      int select = pegs.findSelectedPeg(x, y);

      //get the list of possible jumps from Pegs
      ArrayList<Jump> possible_jumps = pegs.getPossibleJumps(select);
      int num_jumps = possible_jumps.size();

      if (num_jumps == 1)
      {
         //only one possible jump, so complete the jump
         Jump jump = possible_jumps.get(0);
         jump.jump(pegs);
         pegs.incrementTurn();
         if (pegs.gameOver())
         {
             jump = possible_jumps.get(0);
             jump.jump(pegs);
             pegs.incrementTurn();
             if (pegs.gameOver())
             {
                 pegs.setState(pegs.getGameOverState());
             }
         }
      }

      //the jump cannot be completed until a legal empty slot has also been clicked on
      else if (num_jumps > 1)
      {
         //set the state to PegSecondClickState, forward required information (see PegSecondClickState)

          PegSecondClickState secondClickState = pegs.getSecondClickState();
          pegs.setState(secondClickState);
          secondClickState.setStart(select);
      }
   }
}