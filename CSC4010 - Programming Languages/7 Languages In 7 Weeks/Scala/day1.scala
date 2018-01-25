object Main extends App
{
	type ticTacToe = (Char, Char, Char,
		              Char, Char, Char,
		              Char, Char, Char)

	// this method iterates through possible winning positions to see if any of them contain all X's or O's
	// if no winner can be chosen, check to make sure all moves have been made, return Cat
	// if no winner has been chosen and the game is incomplete, tell that to the user
	def getWinner(game:ticTacToe) : String =
	{
        // get a list of possible winning positions for X's and O's
		val (r1c1, r1c2, r1c3,
		     r2c1, r2c2, r2c3,
		     r3c1, r3c2, r3c3) = game

		val winningPositions = List(
				List(r1c1,r1c2,r1c3), List(r2c1,r2c2,r2c3), List(r3c1,r3c2,r3c3), // rows
				List(r1c1,r2c1,r3c1), List(r1c2,r2c2,r3c2), List(r1c3,r2c3,r3c3), // columns
				List(r1c1,r2c2,r3c3), List(r1c3,r2c2,r3c1) //diagonals
				)
		
        // if a winner can be chosen now, it doesn't matter if the board is full
        // make sure to weed out combinations where each slot is blank
		winningPositions.foreach(pos =>
			if (pos.forall(slot => slot != ' ' && slot == pos(0)))
			{
				return "The winner is " + pos(0) + "."
			}
		)

        // check to make sure that all slots have been filled
		if (winningPositions.forall(pos => pos.forall(slot => slot != ' ')))
		{
			return "Cat's Game."
		}
		else
			"Incomplete board; more moves need to be made before a winner can be chosen."
	}
	
	var game = ('X', 'X', 'X',
            	'O', 'O', 'O',
            	'X', 'O', 'X')
	println(getWinner(game))
	// if there are multiple winners, the first detected winner will be reported
	
	game = ('O', 'X', ' ',
            'O', 'O', 'X',
            'X', 'O', 'O')
    println(getWinner(game))
    // Even if the board is incomplete, a winner can be chosen
            
    game = ('O', 'X', 'O',
            'O', 'O', 'X',
            'X', 'O', 'X')
    println(getWinner(game))
    // If the game is a tie, tell the players it's a Cat's Game.
            
    game = ('O', 'X', 'X',
            'O', ' ', 'X',
            'X', 'O', 'O')
    println(getWinner(game))
	// If a winner can't be chosen and the board is incomplete, notify the players

}
