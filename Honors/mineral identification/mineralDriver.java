/* This is the user interface that creates a mineral and reports the results.
 Involves some helper methods to get and validate user input, and only asks for 
 values of certain variables based on the values of other variables.
 -Amber Patterson */

import java.util.Scanner;
import java.util.InputMismatchException;
public class mineralDriver
{
    /* main user interface, gets variables and creates a mineral, reporting the results */
    public static void main(String[] args)
    {
        System.out.println("Mineral Identifier");
        System.out.println();
        double mohsHardness = getValidatedDouble("On Moh's Hardness Scale, how hard is the mineral?");
        while(mohsHardness > 7 || mohsHardness < 0)
        {
            System.out.println("We don't have any minerals with a hardness higher than 7. Try again.");
            mohsHardness = getValidatedDouble("On Moh's Hardness Scale, how hard is the mineral?");
        }
        boolean isConchoidal = getValidatedBoolean("Does the mineral have conchoidal fracturing? Press 1 if it does, otherwise press 2.");
        int cleavageNum = 0;
        boolean atNinety = false;
        boolean hasFlakes = false;
        boolean hasStriations = false;
        if (!isConchoidal)
        {
            cleavageNum = getValidatedInt("How many planes of cleavage does the mineral have?");
            while (cleavageNum < 1 || cleavageNum > 3)
            {
                System.out.println("There are only 1 to 3 planes of cleavage. Try again.");
                cleavageNum = getValidatedInt("How many planes of cleavage does the mineral have?");
            }
            hasStriations = getValidatedBoolean("Does the mineral have striations along its surface? Press 1 if it does, otherwise press 2.");
            if (cleavageNum > 1)
                atNinety = getValidatedBoolean("Do the planes of cleavage  intersect at 90 degrees? Press 1 if they do, otherwise press 2.");
            else
                hasFlakes = getValidatedBoolean("Does the mineral tend to peel into sheets or flakes? Press 1 if it does, otherwise press 2.");
        }
        boolean isEarthy = getValidatedBoolean("Does the mineral have a nonmetallic or metallic luster? Press 1 if it's nonmetallic, or press 2 of it's metallic.");
        boolean reactive = getValidatedBoolean("Does the mineral react to hydrochloric acid? Press 1 if it does, otherwise press 2.");
        boolean hasStreak = getValidatedBoolean("Does the mineral leave a streak? Press 1 if it does, otherwise press 2.");
        Color streakColor = Color.UNASSIGNED;
        if (hasStreak)
            streakColor = getValidatedColor("What color is the streak?");
        Color color = getValidatedColor("Lastly, what's the dominant color in the mineral?");
        mineral min = new mineral(mohsHardness, isConchoidal, cleavageNum, atNinety, isEarthy, hasStriations, streakColor, color, reactive, hasFlakes);
        mineralType minType = min.getMineral();
        System.out.println();
        System.out.println("Your mineral is: " + minType + "\n");
        if (!min.isAccurate())
            System.out.println("These results may not be accurate.");
        System.out.println("You said:");
        min.printMineral();
            
    }
    
    /* private helper method to get a valid double (supports decimals) from the user */
    private static double getValidatedDouble(String prompt)
    {
        Scanner in = new Scanner(System.in);
        System.out.println(prompt);
        System.out.println("Enter a number, decimals are allowed: ");
        double d = 0;
        try
        {
            d = in.nextDouble();
        }
        catch (InputMismatchException ime)
        {
            System.out.println("Not a valid number. Try again.");
            d = getValidatedDouble(prompt);
        }
        return d;
    }
    
    /* private helper method to get a valid boolean (true/false value) from the user */
    private static boolean getValidatedBoolean(String prompt)
    {
        System.out.println(prompt);
        int a = getValidatedInt("Choose either 1 or 2: ");
        while (a < 1 || a > 2)
        {
            a = getValidatedInt("That's not 1 or 2. Try again.");
        }
        boolean b;
        if (a == 1)
            b = true;
        else
            b = false;
        return b;
    }
    
    /* private helper method to get a valid int (whole number) from the user */
    private static int getValidatedInt(String prompt)
    {
        Scanner in = new Scanner(System.in);
        System.out.println(prompt);
        int a = 0;
        try
        {
            a = in.nextInt();
        }
        catch (InputMismatchException ime)
        {
            System.out.println("Not a valid whole number. Try again.");
            a = getValidatedInt(prompt);
        }
        return a;
    }
    
    /* private helper method to get a valid Color from the user */
    private static Color getValidatedColor(String prompt)
    {
        System.out.println(prompt);
        int a = getValidatedInt("1) White \n2) Gray \n3) Black \n4) Red \n5) Green \n6) Pink \n7) Brown \n8) Gold \n9) Clear");
        while (a < 1 || a > 9)
        {
            a = getValidatedInt("Try again. Enter a whole number between 1 and 9.");
        }
        Color c = Color.UNASSIGNED;
        
        if(a == 1)
            c = Color.WHITE;
        else if (a ==2)
            c = Color.GRAY;
        else if (a == 3)
            c = Color.BLACK;
        else if (a == 4)
            c = Color.RED;
        else if (a == 5)
            c = Color.GREEN;
        else if (a == 6)
            c = Color.PINK;
        else if (a ==7)
            c = Color.BROWN;
        else if (a == 8)
            c = Color.GOLD;
        else if (a == 9)
            c = Color.CLEAR;
        
        return c;
    }
}