import java.util.ArrayList;
import java.text.*;
import restaurant.*;
public class RestaurantDriver
{
    
    private static Keyboard key = Keyboard.getKeyboard();
    private static final DecimalFormat FMT = new DecimalFormat("$#,##0.00");
    
    public static void main(String[] args)
    {
        Restaurant theRestaurant = null;
        try
        {
            if (args.length >= 3)
            {
                theRestaurant = new Restaurant(args[0], args[1], Boolean.parseBoolean(args[2]));
            }
            else if (args.length == 2)
            {
                theRestaurant = new Restaurant(args[0], args[1]);
            }
            else if (args.length == 1)
            {
                theRestaurant = new Restaurant(args[0]);
            }
            else
            {
                System.out.println("Usage: java RestaurantDriver restName fileName isObject");
                return;
            }
        }
        catch (RestaurantException re)
        {
            System.out.println(re.getMessage());
            System.out.println("Problem creating Restaurant - exiting program.");
            return;
        }

        System.out.println("Welcome to RestaurantDriver beta version!!!");
        String choice = menu();
        while (!choice.equalsIgnoreCase("Q"))
        {
            if (choice.equalsIgnoreCase("s"))
            {
                System.out.println(theRestaurant);
            }
            else if (choice.equals("+"))
            {
                doAddItem(theRestaurant);
            }
            else if (choice.equals("-"))
            {
                doRemoveItem(theRestaurant);
            }
            else if (choice.equalsIgnoreCase("a"))
            {
                doActivateItem(theRestaurant);
            }
            else if (choice.equalsIgnoreCase("d"))
            {
                doDiscontinueItem(theRestaurant);
            }
            else if (choice.equalsIgnoreCase("u"))
            {
                doUpdatePrice(theRestaurant);
            }
            else if (choice.equalsIgnoreCase("n"))
            {
                doNames(theRestaurant);
            }
            else if (choice.equalsIgnoreCase("r"))
            {
                doRateItem(theRestaurant);
            }
            else if (choice.equalsIgnoreCase("o"))
            {
                doOrderItem(theRestaurant);
            }
            else if (choice.equalsIgnoreCase("v"))
            {
                doAverageItemRating(theRestaurant);
            }
            else if (choice.equals("$"))
            {
                doProfit(theRestaurant);
            }
            else if (choice.equalsIgnoreCase("w"))
            {
                doWriteFile(theRestaurant);
            }
            else if (choice.equals("*"))
            {
                doSortWork(theRestaurant);
            }
            else
            {
                System.out.println("Invalid choice -- please try again!");
            }
            choice = menu();
        }
        System.out.println("Thanks for using RestaurantDriver beta version!!!");
    }
        
    private static String menu()
    {
        return key.readString("Enter your choice: S for status, + for add restaurant item, - for remove restaurant item, " +
                             "N for names of restaurant items, A for activate, D for discontinue, U for update price,\r\n" +
                             "R for rating, O for order, V for average rating, $ for profit, * for sort, W for write file, Q for quit. ");
    }
    
    private static void doNames(Restaurant rest)
    {
        ArrayList<String> names = rest.getAllItemNames();
        System.out.println("The restaurant item names are as follows:");
        for (String name: names)
        {
            System.out.println(name);
        }        
    }
    
    private static void doAddItem(Restaurant rest)
    {
        System.out.println("Processing add...");
        String name = key.readString("Please enter the item name. ");
        MenuCategory[] categories = MenuCategory.values();
        System.out.println("Category choices:");
        for (MenuCategory mc : categories)
        {
            System.out.println(" " + mc);
        }
        String cat = key.readString("Please enter the item category. ");
        try
        {
           MenuCategory category = MenuCategory.valueOf(cat.toUpperCase());
           int servingSize = key.readInt("Please enter the serving size in ounces. ");
           int numCalories = key.readInt("Please enter the number of calories. ");          
           double price = key.readDouble("Please enter the retail price. ");
           double wholesale = key.readDouble("Please enter the wholesale cost. ");
           boolean success = rest.addToMenu(name, category, servingSize, numCalories, price, wholesale);
           if (success)
           {
               System.out.println("Item " + name + " added successfully.");
           }
           else
           {
               System.out.println("Item " + name + " not added successfully.");
           }
        }
        catch (RestaurantException re)
        {
            System.out.println(re.getMessage());
            System.out.println("Item " + name + " not added to menu.");
        }
        catch (IllegalArgumentException iae)
        {
            System.out.println("Invalid category -- item " + name + " not added to menu.");
        }
    }
    
    private static void doRemoveItem(Restaurant rest)
    {
        System.out.println("Processing remove...");
        String name = key.readString("Please enter the name of the item to be removed from the menu. ");
        if (rest.removeFromMenu(name))
        {
            System.out.println(name + " successfully removed from menu.");
        }
        else
        {
            System.out.println(name + " unsuccessfully removed from menu.");
        }
    }
    
    private static void doActivateItem(Restaurant rest)
    {
        System.out.println("Processing activate...");
        String choice = key.readString("Activate all? (y/n): ");
        if (choice.equalsIgnoreCase("n"))
        {
            String name = key.readString("Please enter the name of the item to be activated on the menu. ");
            if (rest.activate(name))
            {
                System.out.println(name + " successfully activated on menu.");
            }
            else
            {
                System.out.println(name + " unsuccessfully activated on menu.");
            }
        }
        else if (choice.equalsIgnoreCase("y"))
        {
            rest.activate();
            System.out.println("Activated all items on menu.");
        }
        else
        {
            System.out.println("Invalid choice.  Need y or n.");
        }
    }
    
    private static void doDiscontinueItem(Restaurant rest)
    {
        System.out.println("Processing discontinue...");
        String choice = key.readString("Discontinue all? (y/n): ");
        if (choice.equalsIgnoreCase("n"))
        {
            String name = key.readString("Please enter the name of the item to be discontinued on the menu. ");
            if (rest.discontinue(name))
            {
                System.out.println(name + " successfully discontinued on menu.");
            }
            else
            {
                System.out.println(name + " unsuccessfully discontinued on menu.");
            }
        }
        else if (choice.equalsIgnoreCase("y"))
        {
            rest.discontinue();
            System.out.println("Discontinued all items on menu.");
        }
        else
        {
            System.out.println("Invalid choice. Need y or n.");
        }
    }
    
    private static void doUpdatePrice(Restaurant rest)
    {
        System.out.println("Processing price update...");
        int percent = key.readInt("Please enter the price change percentage. ");
        String wholesale = key.readString("Wholesale? (y/anything else): ");
        boolean isWholesale = false;
        if (wholesale.equalsIgnoreCase("y"))
        {
            isWholesale = true;
        }
        String choice = key.readString("Update all prices? (y/n): ");
        if (choice.equalsIgnoreCase("n"))
        {
            String name = key.readString("Please enter the name of the item to have its price changed. ");
            if (rest.updatePrice(isWholesale, name, percent))
            {
                System.out.println("Price for " + name + " successfully changed.");
            }
            else
            {
                System.out.println("Price for " + name + " unsuccessfully changed.");
            }
        }
        else if (choice.equalsIgnoreCase("y"))
        {
            if (rest.updatePrice(isWholesale, percent))
            {
                System.out.println("Successfully changed prices for all items on menu.");
            }
            else
            {
                System.out.println("Unsuccessfully changed prices for all items on menu.");
            }
        }
        else
        {
            System.out.println("Invalid choice. Need y or n.");
        }
    }
    
    private static void doRateItem(Restaurant rest)
    {
        System.out.println("Processing item rating...");
        String itemName = key.readString("Please enter the item name. ");
        String reviewerName = key.readString("Please enter the reviewer name. ");
        String date = key.readString("Please enter the date (mm/dd/yyyy). ");
        int rating = key.readInt("Please enter the rating. ");
        try
        {
            boolean success = rest.addRating(itemName, reviewerName, date, rating);
            if (success)
            {
                System.out.println("Rating successfully added for " + itemName);
            }
            else
            {
                System.out.println("Rating unsuccessfully added for " + itemName);
            }
        }
        catch (RestaurantException re)
        {
            System.out.println(re.getMessage());
            System.out.println("Rating unsuccessfully added for " + itemName);
        }
    }
    
    private static void doOrderItem(Restaurant rest)
    {
        System.out.println("Processing item ordering...");
        String itemName = key.readString("Please enter the item name. ");
        int numOrders = key.readInt("Please enter the number of orders. ");
        boolean success = rest.order(itemName, numOrders);
        if (success)
        {
            System.out.println(numOrders + " of " + itemName + " successfully processed.");
        }
        else
        {
            System.out.println(numOrders + " of " + itemName + " unsuccessfully processed.");
        }
    }
    
    private static void doProfit(Restaurant rest)
    {
        System.out.println("Processing profit...");
        System.out.println("The total profit of restaurant " + rest.getName() + " is " + FMT.format(rest.getTotalProfit()) + ".");
    }
    
    private static void doAverageItemRating(Restaurant rest)
    {
        System.out.println("Processing average item rating...");
        System.out.println("The average rating for menu items at restaurant " + rest.getName() + " is " + FMT.format(rest.getAverageItemRating()).substring(1) + ".");
    }
    
    private static void doWriteFile(Restaurant rest)
    {
        System.out.println("Processing write file...");
        String fileName = key.readString("Please enter the name of the output file.");
        boolean isObject = key.readString("Is the file to be an object file? (y/anything else): ").equalsIgnoreCase("Y") ? true : false;
        try
        {
            rest.writeToFile(fileName, isObject);
            System.out.println((isObject ? "Object" : "Text") + " file " + fileName + " written successfully.");
        }
        catch (RestaurantException re)
        {
            System.out.println(re.getMessage());
            System.out.println("File written unsuccessfully.");
        }        
    }
    
    private static void doSortWork(Restaurant rest)
    {
        System.out.println("Processing sort...");
        int sortField;
        do
        {
            showSortFieldMenu();          
            sortField = key.readInt("Enter the sort field: ");
        } while (sortField < 1 || sortField > 3);
        int sortAlg;
        do
        {
            showSortAlgorithmMenu();
            sortAlg = key.readInt("Enter the algorithm number: ");
        } while (sortAlg < 1 || sortAlg > 2);
        String result = rest.sort(sortField, sortAlg);
        System.out.println("Sort results:\n" + result);
    }
    
    private static void showSortFieldMenu()
    {
        System.out.println("1. item name (asc)\n" +
           "2. item profit (desc)\n" +
           "3. item average rating (desc)\n");
    }  
    
    private static void showSortAlgorithmMenu()
    {
        System.out.println("1. Selection Sort\n" +
           "2. Insertion Sort\n");
    }
}
