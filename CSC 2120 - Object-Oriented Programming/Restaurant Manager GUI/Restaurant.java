import java.io.*;
import java.util.*;
import java.text.*;
import utilities.Sorting;

public class Restaurant implements Statable, Serializable
{
    private String name;
    private HashMap<String, MenuItem> menu;
    private static Comparator[] comps = {new MenuItemNameComparator(), new MenuItemProfitComparator(), new MenuItemRatingComparator()};
    
    private static final DecimalFormat FMT = new DecimalFormat("$#,##0.00");
    
    public Restaurant(String name) throws RestaurantException
    {
        this.name = name;
        loadMenu();
    }
    
    public Restaurant(String name, String fileName, boolean isObjectFile) throws RestaurantException
    {
        this.name = name;
        loadMenu(fileName, isObjectFile);
    }
    
    public Restaurant(String name, String fileName) throws RestaurantException
    {
        this(name, fileName, false);
    }
    
    private void loadMenu() throws RestaurantException
    {
        menu = new HashMap<String, MenuItem>();
        addToMenu("Steak", MenuCategory.MAIN, 8, 400, 10.99, 2.00);
        addToMenu("Spaghetti", MenuCategory.MAIN, 12, 500, 8.99, 1.50);
        addToMenu("Salad", MenuCategory.SIDE, 6, 200, 2.99, 0.75);
        addToMenu("Baked Potato", MenuCategory.SIDE, 6, 300, 2.99, 0.82);
        addToMenu("Apple Pie", MenuCategory.DESSERT, 5, 375, 3.29, 0.94);
        addToMenu("Lava Cake", MenuCategory.DESSERT, 5, 425, 3.29, 1.26);
    }
    
    private void loadMenu(String fileName, boolean isObjectFile) throws RestaurantException
    {
        try
        {
            if (isObjectFile)
            {
                FileInputStream fis = new FileInputStream(fileName);
                ObjectInputStream ois = new ObjectInputStream(fis);
                menu = (HashMap<String, MenuItem>) ois.readObject();
            }
            else
            {
                menu = new HashMap<String, MenuItem>();
                FileIO br = new FileIO(fileName, FileIO.FOR_READING);
                String line = br.readLine();
                while (line != null)
                {
                    System.out.println("Current line: " + line);
                    processLine(line);
                    line = br.readLine();
                }
            }
        }
        catch (FileIOException | IOException | ClassNotFoundException e)
        {
            throw new RestaurantException(e.getMessage());
        }
        
    }
    
    private void processLine(String line)
    {
        try
        {
            String[] tokens = line.split(",");
            String name = tokens[0];
            MenuCategory cat = MenuCategory.valueOf(tokens[1]);
            int servingSize = Integer.parseInt(tokens[2]);
            int numCalories = Integer.parseInt(tokens[3]);
            double price = Double.parseDouble(tokens[4]);
            double wholesale = Double.parseDouble(tokens[5]);
            int numOrders = Integer.parseInt(tokens[6]);
            double totalCost = Double.parseDouble(tokens[7]);
            double totalSales = Double.parseDouble(tokens[8]);
            boolean active = Boolean.parseBoolean(tokens[9]);
            int numRatings = Integer.parseInt(tokens[11]);
            ArrayList<Rating> ratings = new ArrayList<Rating>();
            for (int i = 1; i <= numRatings; i++)
            {
                String[] ratingTokens = tokens[11+i].split(":");
                ratings.add(new Rating(ratingTokens[0], ratingTokens[1], Integer.parseInt(ratingTokens[2])));
            }
            boolean recommended = Boolean.parseBoolean(tokens[10]);
            boolean success = addToMenu(name, cat, servingSize, numCalories, price, wholesale, numOrders, totalCost, totalSales, active, ratings, recommended);
            System.out.println(line + " added: " + success);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public boolean addToMenu(String itemName, MenuCategory cat, int servingSize, int numCalories, double price,
                           double wholesale) throws RestaurantException
    {
        return addToMenu(itemName, cat, servingSize, numCalories, price, wholesale, 0, 0, 0, false, new ArrayList<Rating>(), false);
    }
    
    private boolean addToMenu(String itemName, MenuCategory cat, int servingSize, int numCalories, double price,
                             double wholesale, int numOrders, double totalCost, double totalSales, boolean active, ArrayList<Rating> ratings, boolean recommended)
                   throws RestaurantException
    {
        boolean result = false;
        MenuItem mi = findMenuItem(itemName);
        if (mi == null)
        {
            try
            {
               addToMenu(new MenuItem(itemName, cat, servingSize, numCalories, price, wholesale, numOrders, totalCost, totalSales, active, recommended, ratings));
               result = true;
            }
            catch (MenuItemException mie)
            {
                throw new RestaurantException(mie.getMessage());
            }
        }
        return result;
    }
    
    private void addToMenu(MenuItem item)
    {
        menu.put(item.getName().toLowerCase(), item);
    }
    
    private MenuItem findMenuItem(String key)
    {
        return menu.get(key.toLowerCase());
    }
    
    public boolean removeFromMenu(String key)
    {
        return menu.remove(key.toLowerCase()) != null;
    }
    
    public boolean activate(String key)
    {
        boolean result = false;
        MenuItem mi = findMenuItem(key.toLowerCase());
        if (mi != null)
        {
            mi.activate();
            result = true;
        }
        return result;
    }
    
    public void activate()
    {
        Collection<MenuItem> allItems = menu.values();
        for (MenuItem item: allItems)
        {
            item.activate();
        }
    }
    
    public boolean discontinue(String key)
    {
        boolean result = false;
        MenuItem mi = findMenuItem(key.toLowerCase());
        if (mi != null)
        {
            mi.discontinue();
            result = true;
        }
        return result;
    }
    
    public void discontinue()
    {
        Collection<MenuItem> allItems = menu.values();
        for (MenuItem item: allItems)
        {
            item.discontinue();
        }
    }
    
    public boolean setCalories(String key, int numCalories)
    {
        boolean result = false;
        MenuItem which = findMenuItem(key.toLowerCase());
        if (which != null)
        {
            result = which.setCalories(numCalories);
        }
        return result;
    }
    
    public boolean setServingSize(String key, int servingSize)
    {
        boolean result = false;
        MenuItem which = findMenuItem(key.toLowerCase());
        if (which != null)
        {
            result = which.setServingSize(servingSize);
        }
        return result;
    }
    
    public boolean updatePrice(boolean isWholesale, String key, int percentage)
    {
        boolean result = false;
        MenuItem which = findMenuItem(key.toLowerCase());
        if (which != null)
        {
            result = which.updatePrice(isWholesale, percentage);
        }
        return result;
    }
    
    public boolean updatePrice(boolean isWholesale, int percentage)
    {
        boolean result = false;
        Collection<MenuItem> allItems = menu.values();
        for (MenuItem item: allItems)
        {
            result = item.updatePrice(isWholesale, percentage);
        }
        return result;
    }

    public boolean addRating(String itemName, String reviewerName, String date, int rating) throws RestaurantException
    {
        boolean result = false;
        MenuItem which = findMenuItem(itemName.toLowerCase());
        if (which != null)
        {
            try
            {
                result = which.addRating(reviewerName, date, rating);
            }
            catch (MenuItemException mie)
            {
                throw new RestaurantException(mie.getMessage());
            }
        }
        return result;
    }
    
    public boolean order(String itemName, int numOrders)
    {
        boolean result = false;
        MenuItem which = findMenuItem(itemName.toLowerCase());
        if (which != null)
        {
            result = which.order(numOrders);
        }
        return result;
    }
    
    public double getTotalProfit()
    {
        double result = 0;
        Collection<MenuItem> allItems = menu.values();
        for (MenuItem item: allItems)
        {
            result += item.getTotalSales() - item.getTotalWholesaleCost();
        }
        return result;
    }
    
    
    public double getAverageItemRating()
    {
        int numRaters = 0;
        double result = 0;
        Collection<MenuItem> allItems = menu.values();
        for (MenuItem item: allItems)
        {
            numRaters += item.getNumRatings();
            result += item.getAverageRating() * item.getNumRatings();
        }
        if (numRaters > 0)
        {
            result = result / numRaters;
        }
        return result;
    }
    
    public ArrayList<String> getAllItemNames()
    {
        ArrayList result = new ArrayList<String>();
        Set<String> allKeys = menu.keySet();
        for (String s: allKeys)
        {
            result.add(s);
        }
        return result;
    }
    
    public String toString()
    {
        Collection<MenuItem> allItems = menu.values();
        StringBuffer buf = new StringBuffer("Restaurant " + name + " with the following menu items:\r\n");
        for (MenuItem item: allItems)
        {
            buf.append("\r\n****\r\n" + item);
        }
        return buf.toString();
    }
    
    public String getState()
    {
        Collection<MenuItem> allItems = menu.values();
        StringBuffer buf = new StringBuffer("");
        for (MenuItem item: allItems)
        {
            buf.append(item.getState() + "\r\n");
        }
        return buf.toString().trim();        
    }
    
    public void writeToFile(String fileName, boolean isObjectFile) throws RestaurantException
    {
        try
        {
            if (isObjectFile)
            {
                FileOutputStream fos = new FileOutputStream(fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(menu);
                fos.close();
            }
            else
            {
                FileIO fw = new FileIO(fileName, FileIO.FOR_WRITING);
                fw.writeLine(getState());
                fw.close();              
            }
        }
        catch (FileIOException | IOException e)
        {
            throw new RestaurantException(e.getMessage());
        }
    }
    
    public String getName()
    {
        return name;
    }
    
    public String sort(int field, int alg)
    {
       Collection<MenuItem> allItems = menu.values();
       MenuItem[] allItemsArray = allItems.toArray(new MenuItem[0]);
       Comparator<MenuItem> currComp;
       if (field >= 1 && field <= comps.length)
       {
           currComp = (Comparator<MenuItem>) comps[field - 1];
       }
       else
       {
           currComp = (Comparator<MenuItem>) comps[0];
       }
       if (alg == 1)
       {
            Sorting.<MenuItem> selectionSort(allItemsArray, currComp);
       }  
       else if (alg == 2)
       {
            Sorting.<MenuItem> insertionSort(allItemsArray, currComp);
       }
       else
       {
            Sorting.<MenuItem> bubbleSort(allItemsArray, currComp);
       }
       
       StringBuffer result = new StringBuffer("");
       for (MenuItem item: allItemsArray)
       {
           result.append(item + "\r\n");
       }
       return result.toString();
    }
}


