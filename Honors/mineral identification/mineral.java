/* A class that manages several instance variables that represent the properties 
 of a mineral and uses mineral identification logic (see: flowcharts in lab 
 manual) to identify (or make an educated guess, if given non-textbook 
 parameters) a mineral. - Amber Patterson */
public class mineral
{
    private double mohsHardness;
    private boolean isConchoidal;
    private int cleavageNum;
    private boolean atNinety;
    private boolean hasFlakes;
    private boolean isEarthy;
    private boolean hasStriations;
    private Color streakColor;
    private Color color;
    private mineralType type;
    private boolean accurate;
    private boolean reactive;
    
    /* constructor sets all of the instance variables, calls compare() */
    public mineral(double mohsHardness, boolean isConchoidal, int cleavageNum, boolean atNinety, boolean isEarthy, boolean hasStriations, Color streakColor, Color color, boolean reactive, boolean hasFlakes)
    {
        accurate = true;
        this.mohsHardness = mohsHardness;
        this.isConchoidal = isConchoidal;
        this.cleavageNum = cleavageNum;
        this.atNinety = atNinety;
        this.isEarthy = isEarthy;
        this.hasStriations = hasStriations;
        this.streakColor = streakColor;
        this.color = color;
        this.reactive = reactive;
        this.hasFlakes = hasFlakes;
        type = compare();
    }
    
    /* This is the method that does all of the mineral identification, includes a 
     "guess" section at the very end in case the regular logic doesn't assign a 
     mineralType by then */
    private mineralType compare()
    {
        mineralType minType = mineralType.UNASSIGNED;
        if (isConchoidal)//garnet, pyrite, quartz, rose quartz, kaolinite, hematite, olivine
        {
            if (mohsHardness > 6)//rose quartz, garnet, quartz, olivine, pyrite
            {
                if (isEarthy)
                {
                    if (color == Color.PINK)
                    {
                        minType = mineralType.ROSE_QUARTZ;
                    }
                    else if (color == Color.RED || color == Color.BLACK || color == Color.BROWN)
                    {
                        minType = mineralType.GARNET;
                    }
                    else if (color == Color.GREEN)
                    {
                        minType = mineralType.OLIVINE;
                    }
                    else
                    {
                        minType = mineralType.QUARTZ;
                    }
                }
                else
                {
                    minType = mineralType.PYRITE;
                }
            }
            else if (mohsHardness <= 6 && mohsHardness >= 5)//hematite, pyrite
            {
                if (isEarthy)
                {
                    minType = mineralType.HEMATITE;
                }
                else
                {
                    if (streakColor == Color.RED || streakColor == Color.BROWN)
                    {
                        if (color == Color.GOLD)
                        {
                            accurate = false;
                            minType = mineralType.PYRITE;
                        }
                        else
                            minType = mineralType.HEMATITE;
                    }
                    else if (streakColor == Color.GRAY || streakColor == Color.BLACK)
                        minType = mineralType.PYRITE;
                }
            }
            else
            {
                if (mohsHardness < 3)
                {
                    if (!reactive)
                        minType = mineralType.KAOLINITE;
                    else
                    {
                        minType = mineralType.CALCITE;
                        accurate = false;
                    }
                }
            }
        }
        else if (atNinety)//galena, augite, plagioclase, k-feldspar, halite
        {
            if (mohsHardness >= 5)//augite, plagioclase, k-feldspar
            {
                if (hasStriations)
                    minType = mineralType.PLAGIOCLASE_FELDSPAR;
                else
                {
                    if (color == Color.BLACK || color == Color.GRAY || color == Color.GREEN || streakColor == Color.GREEN)
                    {
                        minType = mineralType.AUGITE;
                    }
                    else
                        minType = mineralType.POTASSIUM_FELDSPAR;
                }
            }
            else//halite, galena
            {
                if (streakColor == Color.GRAY || streakColor == Color.BLACK)
                {
                    minType = mineralType.GALENA;
                }
                else if (streakColor == Color.UNASSIGNED || streakColor == Color.WHITE || streakColor == Color.CLEAR)
                {
                    if (reactive)
                    {
                        accurate = false;
                        minType = mineralType.CALCITE;
                    }
                    else
                    {
                        minType = mineralType.HALITE;
                    }
                }
            }
        }
        else//calcite, hornblende, gypsum, biotite, muscovite
        {
            if (mohsHardness >= 5)
                minType = mineralType.HORNBLENDE;
            else if (cleavageNum > 1)
            {
                if (reactive)
                    minType = mineralType.CALCITE;
                else
                {
                    accurate = false;
                    minType = mineralType.HORNBLENDE;
                }
            }
                
            else//gypsum, biotite, muscovite
            {
                if (color == Color.BLACK || color == Color.BROWN)
                {
                    if (hasFlakes)
                        minType = mineralType.BIOTITE;
                    else
                    {
                        accurate = false;
                        minType = mineralType.HORNBLENDE;
                    }
                }
                else
                {
                    if (hasFlakes)
                        minType = mineralType.MUSCOVITE;
                    else
                        minType = mineralType.GYPSUM;
                }
            }
                
        }
        if (minType == mineralType.UNASSIGNED)//make an educated guess
        {
            accurate = false;
            if (reactive)
                minType = mineralType.CALCITE;
            else if (hasStriations)
            {
                if (cleavageNum == 1)
                    minType = mineralType.GYPSUM;
                else
                    minType = mineralType.PLAGIOCLASE_FELDSPAR;
            }
            else if (hasFlakes)
            {
                if (color == Color.BLACK || color == Color.BROWN || color == Color.RED)
                    minType = mineralType.BIOTITE;
                else
                    minType = mineralType.MUSCOVITE;
            }
            else if (streakColor == Color.RED || color == Color.RED)
                minType = mineralType.HEMATITE;
            else if (streakColor == Color.GRAY || streakColor == Color.BLACK || streakColor == Color.GREEN)
            {
                if (isEarthy || streakColor == Color.GREEN)
                {
                    if (atNinety)
                        minType = mineralType.AUGITE;
                    else
                        minType = mineralType.HORNBLENDE;
                }
                else
                {
                    if (color == Color.GOLD)
                        minType = mineralType.PYRITE;
                    else
                        minType = mineralType.GALENA;
                }
            }
            else if (atNinety)
            {
                    if (color == Color.CLEAR)
                        minType = mineralType.HALITE;
                    else
                        minType = mineralType.POTASSIUM_FELDSPAR;
            }
            else if (color == Color.PINK)
                minType = mineralType.ROSE_QUARTZ;
            else if (color == Color.GREEN)
                minType = mineralType.OLIVINE;
            else if (color == Color.RED || color == Color.BLACK || color == Color.BROWN)
            {
                if (streakColor != Color.UNASSIGNED || mohsHardness < 4)
                    minType = mineralType.KAOLINITE;
                else
                    minType = mineralType.GARNET;
            }
            else
                if (streakColor != Color.UNASSIGNED || mohsHardness < 4)
                    minType = mineralType.KAOLINITE;
                else
                    minType = mineralType.QUARTZ;
        }
        return minType;
    }
    
    /*returns a flag that tells if the results are accurate i.e. if the 
     parameters follow the mineralType's properties exactly or not */
    public boolean isAccurate()
    {
        return accurate;
    }
    
    /* returns the name of the mineral */
    public mineralType getMineral()
    {
        return type;
    }
    
    /* prints out the input taken in by the constructor */
    public void printMineral()
    {
        System.out.println("Hardness: " + mohsHardness);
        if (isConchoidal)
            System.out.println("Conchoidal fracturing");
        else if (hasFlakes)
            System.out.println("Cleavage: 1 direction, breaks into sheets or flakes");
        else
        {
            if(atNinety)
                System.out.println("Cleavage: " + cleavageNum + " directions intersecting at 90 degrees");
            else
                System.out.println("Cleavage: " + cleavageNum + " directions not intersecting at 90 degrees");
        }
        if (isEarthy)
            System.out.println("Luster: Nonmetallic");
        else
            System.out.println("Luster: Metallic");
        if (hasStriations)
            System.out.println("Has striations along its surface");
        if (streakColor == Color.UNASSIGNED)
            System.out.println("Left no streak");
        else
            System.out.println("Streak: " + streakColor);
        System.out.println("Color: " + color);
        if (reactive)
            System.out.println("Reacted to HCl acid");
        else
            System.out.println("Didn't react to HCl acid");

    }
}