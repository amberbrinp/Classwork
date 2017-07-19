/** RestaurantGUI is a JFrame class that creates and manages a graphical user
 * interface which is designed to operate a Restaurant object. RestaurantGUI
 * manages input validation and exception handling and sends the appropriate
 * output of each Restaurant method to the display text area. This class can be 
 * run with 1, 2, or 3 command line arguments in the form of restaurantName 
 * fileName isObjectFile. The GUI has several components, including buttons, 
 * textfields, labels, checkboxes, a drop-down box, and panels. The GUI also uses 
 * a file chooser to facilitate file writing. - Bob Scollon and Amber Patterson */
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.text.*;

public class RestaurantGUI extends JFrame
{   
    private static Restaurant restaurant;//the restaurant that the GUI manages
    private JPanel pnlButtons;//holds all the button options at bottom
    private JPanel pnlRestaurant;  //holds all of the labels, text fields, and checkboxes
    private JPanel pnlTop;  //holds all of the buttons along the bottom
    private JTextArea infoArea;//info box that will display restaurant stuff
    private JScrollPane taScroller;//allows infoArea to be scrollable
    
    //buttons to go on pnlButtons
    private JButton btnStatus, btnAllItemNames, btnSort, btnHelp, btnAdd, btnSubtract, btnActivate, btnDiscontinue, btnOrder, btnRate, btnUpdate, btnProfit, btnAvRating, btnWriteFile;
	
    //labels for the text fields
    private JLabel lblReviewerName, lblReviewerRate, lblReviewerDate, lblItemName, lblNumOrder, lblCategory, lblServSize, lblCalories, lblRetailPrice, lblWholesalePrice, lblPercentPriceChange, lblSortfield, lblSortAlgorithm;
   
    //text fields to match labels
    private JTextField txtReviewerName, txtReviewerRate, txtReviewerDate, txtItemName, txtNumOrder, txtServSize, txtCalories, txtRetailPrice, txtWholesalePrice, txtPercentPriceChange, txtSortfield, txtSortAlgorithm;
	
    //checkboxes to handle booleans
    private JCheckBox cbAllItems, cbObjectFile, cbWholesalePrice;
    private JComboBox comboBoxCategory;//drop down box for category
    private static final DecimalFormat FMT = new DecimalFormat("$#,##0.00");//to format profit and ratings
    
     /**
     * Pre: takes in a String[] of command-line arguments of the form restaurantName fileName isObjectFile
     *
     * Post: returns nothing, makes the GUI
     *
     * Purpose: creates the restaurant and GUI and makes the GUI visible
     *
     * Throws: none
     */
    public static void main (String[] args)
    {
        RestaurantGUI gui;
        boolean validArgs = makeRestaurant(args);
        if (!validArgs) return;
        gui = new RestaurantGUI();
        gui.setVisible(true);
        //gui.pack(); // this caused the frame to stick to the same size no matter what
    }
        
    /**
     * Pre: takes no parameters
     *
     * Post: a new GUI is created, returns nothing
     *
     * Purpose: constructor to instantiate variables and create the GUI
     *
     * Throws: none
     */
    public RestaurantGUI()
    {
        super("Restaurant");
        this.setSize(1000, 500);
        this.setLayout(new BorderLayout());
        
        pnlTop = new JPanel();
        pnlTop.setLayout(new GridLayout(1,2));
        initRestaurantPanel();
        pnlTop.add(pnlRestaurant);
        infoArea = new JTextArea();
        taScroller = new JScrollPane(infoArea);  
        pnlTop.add(taScroller);
        this.add(pnlTop, BorderLayout.NORTH);
        
        this.addWindowListener(new WindowAdapter() {public void windowClosing(WindowEvent we) {System.exit(0);} } ); //to actually exit the program when the gui is exited out of
        
        initButtons();
        this.add(pnlButtons, BorderLayout.CENTER);
        
    }
                               
    /**
    * Pre: takes in a String[] parameter,should be the first thing called in the program, uses the command line arguments to create the Restaurant
    *
    * Post: returns a boolean telling whether or not restaurant could be created
    *
    * Purpose: parses command-line arguments and calls the appropriate Restaurant constructor according to the number of arguments given, handles exceptions and returns false if bad arguments are given
    *
    * Throws: none
    */
    private static boolean makeRestaurant(String[] args)
    {
        try
        {
            if (args.length >= 3)
            {
                restaurant = new Restaurant(args[0], args[1], Boolean.parseBoolean(args[2]));
            }
            else if (args.length == 2)
            {
                restaurant = new Restaurant(args[0], args[1]);
            }
            else if (args.length == 1)
            {
                restaurant = new Restaurant(args[0]);
            }
            else
            {
                System.out.println("Usage: java RestaurantGUI restaurantName fileName isObject");
                return false; //program will end in main
            }
        }
        catch (RestaurantException re)
        {
            System.out.println(re.getMessage());
            System.out.println("Problem creating Restaurant - exiting program.");
            return false;
        }
        return true;
    }
       
    /**
    * Pre: takes no parameters
    *
    * Post: restaurant panel containing labels and textfields is instantiated, returns nothing
    *
    * Purpose: to create all of the labels and textfields and add them to the panel
    *
    * Throws: none
    */
    private void initRestaurantPanel()
    {
        pnlRestaurant = new JPanel();
        pnlRestaurant.setLayout(new GridLayout(15,2));
        
        //create all of the labels and their corresponding textfields
        lblReviewerName = new JLabel("Reviewer Name");
        txtReviewerName = new JTextField();
        
        lblReviewerRate= new JLabel("Reviewer Rating");
        txtReviewerRate = new JTextField();
        
        lblReviewerDate = new JLabel("Reivewer Date");
        txtReviewerDate = new JTextField();
        
        lblItemName = new JLabel("Item Name");
        txtItemName = new JTextField();
        
        lblNumOrder = new JLabel("# Orders");
        txtNumOrder = new JTextField();
        
        lblCategory = new JLabel("Category");
        String[] category = {"MAIN","DESSERT","SIDE","DRINK"};
        comboBoxCategory = new JComboBox(category);
        
        lblServSize = new JLabel("Serving Size(oz.)");
        txtServSize = new JTextField();
        
        lblCalories = new JLabel("# Calories");
        txtCalories = new JTextField();
        
        lblRetailPrice = new JLabel("Retail Price");
        txtRetailPrice = new JTextField();
        
        lblWholesalePrice = new JLabel("Wholesale Price");
        txtWholesalePrice = new JTextField();
        
        lblPercentPriceChange = new JLabel("% Price Change");
        txtPercentPriceChange = new JTextField();
        
        lblSortfield = new JLabel("Sort Field");
        txtSortfield = new JTextField();
        
        lblSortAlgorithm = new JLabel("Sort Algorithm");
        txtSortAlgorithm = new JTextField();
        
        cbAllItems = new JCheckBox("All Items?");
        cbObjectFile = new JCheckBox("Use Object Files?");
        cbWholesalePrice = new JCheckBox("Wholesale Price?");
        
        //add everything to the panel
        pnlRestaurant.add(lblReviewerName);
        pnlRestaurant.add(txtReviewerName);
        pnlRestaurant.add(lblReviewerRate);
        pnlRestaurant.add(txtReviewerRate);
        pnlRestaurant.add(lblReviewerDate);
        pnlRestaurant.add(txtReviewerDate);
        pnlRestaurant.add(lblItemName);
        pnlRestaurant.add(txtItemName);
        pnlRestaurant.add(lblNumOrder);
        pnlRestaurant.add(txtNumOrder);
        pnlRestaurant.add(lblCategory);
        pnlRestaurant.add(comboBoxCategory);
        pnlRestaurant.add(lblServSize);
        pnlRestaurant.add(txtServSize);
        pnlRestaurant.add(lblCalories);
        pnlRestaurant.add(txtCalories);
        pnlRestaurant.add(lblRetailPrice);
        pnlRestaurant.add(txtRetailPrice);
        pnlRestaurant.add(lblWholesalePrice);
        pnlRestaurant.add(txtWholesalePrice);
        pnlRestaurant.add(lblPercentPriceChange);
        pnlRestaurant.add(txtPercentPriceChange);
        pnlRestaurant.add(lblSortfield);
        pnlRestaurant.add(txtSortfield);
        pnlRestaurant.add(lblSortAlgorithm);
        pnlRestaurant.add(txtSortAlgorithm);
        pnlRestaurant.add(cbAllItems);
        pnlRestaurant.add(cbObjectFile);
        pnlRestaurant.add(cbWholesalePrice);
    }
       
    /**
     * Pre: takes no parameters
     *
     * Post: buttons panel containing all the buttons is instantiated, returns nothing
     *
     * Purpose: to create all of the buttons and make them functional and add them to the panel
     *
     * Throws: none
     */
    private void initButtons()
    {
        pnlButtons = new JPanel();
        pnlButtons.setLayout(new FlowLayout());
        
        //create each of the buttons and add the appropriate ActionListener
        //add each button to the panel
        btnStatus = new JButton("Status");
        btnStatus.addActionListener(new StatusListener());
        pnlButtons.add(btnStatus);
        
        btnAllItemNames = new JButton("All Item Names");
        btnAllItemNames.addActionListener(new AllItemNameListener());
        pnlButtons.add(btnAllItemNames);
        
        btnSort = new JButton("Sort");
        btnSort.addActionListener(new SortListener());
        pnlButtons.add(btnSort);
        
        btnHelp = new JButton("Help");
        btnHelp.addActionListener(new HelpListener());
        pnlButtons.add(btnHelp);
        
        btnAdd = new JButton("+");
        btnAdd.addActionListener(new AddListener());
        pnlButtons.add(btnAdd);
        
        btnSubtract = new JButton("-");
        btnSubtract.addActionListener(new SubtractListener());
        pnlButtons.add(btnSubtract);
        
        btnActivate = new JButton("Activate");
        btnActivate.addActionListener(new ActivateListener());
        pnlButtons.add(btnActivate);
        
        btnDiscontinue = new JButton("Discontinue");
        btnDiscontinue.addActionListener(new DiscontinueListener());
        pnlButtons.add(btnDiscontinue);
        
        btnOrder = new JButton("Order");
        btnOrder.addActionListener(new OrderListener());
        pnlButtons.add(btnOrder);
        
        btnRate = new JButton("Rate Item");
        btnRate.addActionListener(new RateListener());
        pnlButtons.add(btnRate);
        
        btnUpdate = new JButton("Update Price");
        btnUpdate.addActionListener(new UpdateListener());
        pnlButtons.add(btnUpdate);
        
        btnProfit = new JButton("Profit");
        btnProfit.addActionListener(new ProfitListener());
        pnlButtons.add(btnProfit);
        
        btnAvRating = new JButton("Average Rating");
        btnAvRating.addActionListener(new AvRatingListener());
        pnlButtons.add(btnAvRating);
        
        btnWriteFile = new JButton("Write File");
        btnWriteFile.addActionListener(new WriteFileListener());
        pnlButtons.add(btnWriteFile);
    }
        
    /**
    * Pre: takes no parameters, to be used after a button is clicked
    *
    * Post: returns nothing, textfields are empty
    *
    * Purpose: makes sure that all textfields are empty
    *
    * Throws: none
    */
    private void clearAllTextFields()
    {
        txtReviewerName.setText("");
        txtReviewerRate.setText("");
        txtReviewerDate.setText("");
        txtItemName.setText("");
        txtNumOrder.setText("");
        txtServSize.setText("");
        txtCalories.setText("");
        txtRetailPrice.setText("");
        txtWholesalePrice.setText("");
        txtPercentPriceChange.setText("");
        txtSortfield.setText("");
        txtSortAlgorithm.setText("");
    }
    
    /**
     * An inner class implementing ActionListener that serves to manage an event
     * that happens to the status button. Contains a single method,
     * actionPerformed to satisfy the ActionListener interface */
    private class StatusListener implements ActionListener
    {
        /**
         * Pre: takes in an ActionEvent (a button click)
         *
         * Post: returns nothing
         *
         * Purpose: displays restaurant's status to text area
         *
         * Throws: none
         */
        public void actionPerformed(ActionEvent e) //get the status
        {
            infoArea.setText("");
            
            infoArea.append(restaurant.toString());
            
            clearAllTextFields();
        }
    }
    
    /** An inner class implementing ActionListener that serves to manage an event 
     * that happens to the all item names button. Contains a single method, 
     * actionPerformed to satisfy the ActionListener interface */
    private class AllItemNameListener implements ActionListener
    {
        /**
         * Pre: takes in an ActionEvent (a button click)
         *
         * Post: returns nothing
         *
         * Purpose: displays restaurant's item names to text area
         *
         * Throws: none
         */
        public void actionPerformed(ActionEvent e)
        {
            infoArea.setText("");

            ArrayList<String> al = restaurant.getAllItemNames();
            for(String s: al)//ArrayList is Iterable
            {
                infoArea.append(s + "\n");
            }
            
            clearAllTextFields();
        }
    }
    
    /** An inner class implementing ActionListener that serves to manage an event 
     * that happens to the sort button. Contains a single method, actionPerformed 
     * to satisfy the ActionListener interface */
    private class SortListener implements ActionListener
    {
        /**
         * Pre: takes in an ActionEvent (a button click)
         *
         * Post: returns nothing
         *
         * Purpose: checks the validity of textfield input and displays restaurant's sorted items to text area
         *
         * Throws: none
         */
        public void actionPerformed(ActionEvent e)
        {
            infoArea.setText("");
            
            try
            {
               int field = Integer.parseInt(txtSortfield.getText());  
               int alg = Integer.parseInt(txtSortAlgorithm.getText());
               if(field > 2 || field < 1 || alg > 3 || alg < 1)
               {//check for bad data
                   infoArea.append("Bad numeric data has been entered. Please re-enter. \n");
               }
               else 
               {
                   String sort = restaurant.sort(field, alg);
                   infoArea.append(sort);
               }
            }
            catch(NumberFormatException nfe)//for parsing
            {
                infoArea.append("Bad numeric data has been entered. Please re-enter. \n");
            }
            finally
            {
                clearAllTextFields();
            }
        }
    }
    
    /** An inner class implementing ActionListener that serves to manage an event 
     * that happens to the help button. Contains a single method, actionPerformed 
     * to satisfy the ActionListener interface */
    private class HelpListener implements ActionListener
    {
        /**
         * Pre: takes in an ActionEvent (a button click)
         *
         * Post: returns nothing
         *
         * Purpose: displays guide on sort parameters to text area
         *
         * Throws: none
         */
        public void actionPerformed(ActionEvent e)
        {
            infoArea.setText("");
            
            infoArea.append("Sort fields: \n1.Item Name (asc) \n2.Item Profit (desc) \n3.Item Average Rating (desc) \nSort Algorithms \n1.Selection Sort \n2.Insertion Sort");
            clearAllTextFields();
        }
    }
    
    /** An inner class implementing ActionListener that serves to manage an event 
     * that happens to the add button. Contains two methods, actionPerformed to 
     * satisfy the ActionListener interface and getCat to get a MenuCategory */
    private class AddListener implements ActionListener
    {
        /**
         * Pre: takes in an ActionEvent (a button click)
         *
         * Post: returns nothing
         *
         * Purpose: gets and validates information from textfields and adds an item to the restaurant. tells the user whether or not the addition was successful
         *
         * Throws: none
         */
        public void actionPerformed(ActionEvent e)
        {
              infoArea.setText("");
            
              try 
              {
                 String itemName = txtItemName.getText();
                 if(itemName.equals(""))
                 {
                     infoArea.append("Item name was not entered. Please try again.");
                     clearAllTextFields();
                     return;
                 }
                 MenuCategory cat = getCat((String) comboBoxCategory.getSelectedItem());//get the appropriate MenuCategory
                 int servingSize = Integer.parseInt(txtServSize.getText());
                 int numCalories = Integer.parseInt(txtCalories.getText());
                 double price = Double.parseDouble(txtRetailPrice.getText());
                 double wholesale = Double.parseDouble(txtWholesalePrice.getText());
                 boolean added = restaurant.addToMenu(itemName, cat, servingSize, numCalories, price, wholesale);
                 if (added)
                     infoArea.append(itemName + " was successfully added to Menu. \n");
                 else 
                    infoArea.append(itemName + " was not successfully added to Menu. \n");
              }
              catch (RestaurantException | NumberFormatException ex)
              {//parsing and restaurant errors
                  infoArea.append("Was not successfully added to Menu. \n");
                  infoArea.append(ex.getMessage());
              }
              finally 
              {
                  clearAllTextFields();
              }
        }
        
        /**
         * Pre: takes in a String representing a MenuCategory
         *
         * Post: returns the appropriate MenuCategory
         *
         * Purpose: provides the appropriate MenuCategory corresponding to a String
         *
         * Throws: none
         */
        private MenuCategory getCat(String cat)
        {
            if (cat.equalsIgnoreCase("main"))
                return MenuCategory.MAIN;
            else if (cat.equalsIgnoreCase("side"))
                return MenuCategory.SIDE;
            else if (cat.equalsIgnoreCase("dessert"))
                return MenuCategory.DESSERT;
            else
                return MenuCategory.DRINK; //combobox only allows the 4 options
        }
    }
    
    /** An inner class implementing ActionListener that serves to manage an event 
     * that happens to the subtract button. Contains a single method, 
     * actionPerformed to satisfy the ActionListener interface */
    private class SubtractListener implements ActionListener
    {
        /**
         * Pre: takes in an ActionEvent (a button click)
         *
         * Post: returns nothing
         *
         * Purpose: validates textfield input and removes the item corresponding to the input. tells the user if the removal was successful or not
         *
         * Throws: none
         */
        public void actionPerformed(ActionEvent e)
        {
            infoArea.setText("");
            
            String itemName = txtItemName.getText();
            if(itemName.equals(""))
            {//in case name was not entered
                infoArea.append("Nothing entered for ItemName to take out.");
                clearAllTextFields();
                return;
            }
            boolean removed = restaurant.removeFromMenu(itemName);
            if (removed)
                infoArea.append(itemName + " was removed successfully.");
            else
                infoArea.append(itemName + " was not removed successfully.");
            
            clearAllTextFields();
        }
    }
    
    /** An inner class implementing ActionListener that serves to manage an event 
     * that happens to the activate button. Contains a single method, 
     * actionPerformed to satisfy the ActionListener interface */
    private class ActivateListener implements ActionListener
    {
        /**
         * Pre: takes in an ActionEvent (a button click)
         *
         * Post: returns nothing
         *
         * Purpose: activates all items if the checkbox AllItems is selected or the item corresponding to the textfield input
         *
         * Throws: none
         */
        public void actionPerformed(ActionEvent e)
        {
            infoArea.setText("");
            
            boolean activated;
            if(cbAllItems.isSelected())
                restaurant.activate();
            else
            {
                String itemName = txtItemName.getText();
                if(itemName.equals(""))
                {
                    infoArea.append("Nothing entered for ItemName to activate.");
                    clearAllTextFields();
                    return;
                }
                activated = restaurant.activate(itemName);
                if (activated)
                    infoArea.append(itemName + " was activated successfully.");
                else
                    infoArea.append(itemName + " was not activated successfully.");
            }
            
            clearAllTextFields();
        }
    }
    
    /** An inner class implementing ActionListener that serves to manage an event 
     * that happens to the discontinue button. Contains a single method, 
     * actionPerformed to satisfy the ActionListener interface */
    private class DiscontinueListener implements ActionListener
    {
        /**
         * Pre: takes in an ActionEvent (a button click)
         *
         * Post: returns nothing
         *
         * Purpose: discontinue all items if the checkbox AllItems is selected or the item corresponding to the textfield input
         *
         * Throws: none
         */
        public void actionPerformed(ActionEvent e)
        {
            infoArea.setText("");
            
            boolean discontinued;
            if(cbAllItems.isSelected())
                restaurant.discontinue();
            else
            {
                String itemName = txtItemName.getText();
                if(itemName.equals(""))
                {
                    infoArea.append("Nothing entered for ItemName to discontinue.");
                    clearAllTextFields();
                    return;
                }
                discontinued = restaurant.discontinue(itemName);
                if (discontinued)
                    infoArea.append("The discontinue was successful");
                else
                    infoArea.append("The discontinue was unsuccessfully.");
            }
            
            clearAllTextFields();
        }
    }
    
    /** An inner class implementing ActionListener that serves to manage an event 
     * that happens to the order button. Contains a single method, 
     * actionPerformed to satisfy the ActionListener interface */
    private class OrderListener implements ActionListener
    {
        /**
         * Pre: takes in an ActionEvent (a button click)
         *
         * Post: returns nothing
         *
         * Purpose: validates textfield input and orders the appropriate item the appropriate number of times. informs the user of whether or not the order was successful
         *
         * Throws: none
         */
        public void actionPerformed(ActionEvent e)
        {
            infoArea.setText("");
            
            try
            {
                String itemName = txtItemName.getText();
                if(itemName.equals(""))
                {
                    infoArea.append("Nothing entered for ItemName to Order.");
                    clearAllTextFields();
                    return;
                }
                int numOrders = Integer.parseInt(txtNumOrder.getText());
                boolean ordered = restaurant.order(itemName, numOrders);
                if(ordered)
                    infoArea.append(itemName + " was ordered successfully.");
                else
                    infoArea.append(itemName + " was not ordered successfully.");
            }
            catch (NumberFormatException nfe)
            {
                infoArea.append("Bad numeric data has been entered. Please re-enter. \n");
            }
            finally 
            {
                clearAllTextFields();
            }
        }
    }
    
    /** An inner class implementing ActionListener that serves to manage an event 
     * that happens to the rate button. Contains a single method, actionPerformed 
     * to satisfy the ActionListener interface */
    private class RateListener implements ActionListener
    {
        /**
         * Pre: takes in an ActionEvent (a button click)
         *
         * Post: returns nothing
         *
         * Purpose: validates textfield input and adds the rating to the appropriate item and tells the user whether or not the addition was successful
         *
         * Throws: none
         */
        public void actionPerformed(ActionEvent e)
        {
            infoArea.setText("");
            
            String itemName = txtItemName.getText();
            if(itemName.equals(""))
            {
                infoArea.append("Nothing entered for ItemName to Rate.");
                clearAllTextFields();
                return;
            }
            String reviewerName = txtReviewerName.getText();
            String date = txtReviewerDate.getText();
            int rating = Integer.parseInt(txtReviewerRate.getText());
            try
            {
                boolean rated = restaurant.addRating(itemName, reviewerName, date, rating);
                if (rated)
                    infoArea.append(itemName + " was successfully rated.");
                else
                    infoArea.append(itemName + " was not successfully rated.");
            }
            catch (RestaurantException re)
            {
                infoArea.append(re.getMessage() + "\n" + itemName + " was not successfully rated.");
            }
            finally 
            {
                clearAllTextFields();
            }
        }
    }
    
    /** An inner class implementing ActionListener that serves to manage an event 
     * that happens to the update button. Contains a single method, 
     * actionPerformed to satisfy the ActionListener interface */
    private class UpdateListener implements ActionListener
    {
        /**
         * Pre: takes in an ActionEvent (a button click)
         *
         * Post: returns nothing
         *
         * Purpose: validates textfield input and updates all items if the checkbox AllItems is selected or the item corresponding to the textfield input
         *
         * Throws: none
         */
        public void actionPerformed(ActionEvent e)
        {
            infoArea.setText("");
            
            boolean updated;
            try
            {
                int percent = Integer.parseInt(txtPercentPriceChange.getText());
                if(cbAllItems.isSelected())
                    updated = restaurant.updatePrice(cbWholesalePrice.isSelected(), percent);
                else
                {
                    String itemName = txtItemName.getText();
                    if(itemName.equals(""))
                    {
                        infoArea.append("Nothing entered for ItemName to update price on.");
                        clearAllTextFields();
                        return;
                    }
                    updated = restaurant.updatePrice(cbWholesalePrice.isSelected(), itemName, percent);
                }
                if (updated)
                    infoArea.append("The update was successfull.");
                else
                    infoArea.append("The update was not successfull.");
            }
            catch(NumberFormatException nfe)
            {
                infoArea.append(nfe.getMessage() + "\nUpdate was not successfull.");
            }
            finally 
            {
                clearAllTextFields();
            }
        }
    }
    
    /** An inner class implementing ActionListener that serves to manage an event 
     * that happens to the profit button. Contains a single method, 
     * actionPerformed to satisfy the ActionListener interface */
    private class ProfitListener implements ActionListener
    {
        /**
         * Pre: takes in an ActionEvent (a button click)
         *
         * Post: returns nothing
         *
         * Purpose: displays restaurant's profit to text area
         *
         * Throws: none
         */
        public void actionPerformed(ActionEvent e)
        {
            infoArea.setText("");
            
            infoArea.append("The restaurant's profit is " + FMT.format(restaurant.getTotalProfit()));
            
            clearAllTextFields();
        }
    }
    
    /** An inner class implementing ActionListener that serves to manage an event 
     * that happens to the average rating button. Contains a single method, 
     * actionPerformed to satisfy the ActionListener interface */
    private class AvRatingListener implements ActionListener
    {
        /**
         * Pre: takes in an ActionEvent (a button click)
         *
         * Post: returns nothing
         *
         * Purpose: displays restaurant's average rating to text area
         *
         * Throws: none
         */
        public void actionPerformed(ActionEvent e)
        {
            infoArea.setText("");
            
            infoArea.append("The restaurant's average rating is " + FMT.format(restaurant.getAverageItemRating()).substring(1));
            
            clearAllTextFields();
        }
    }
    
    /** An inner class implementing ActionListener that serves to manage an event 
     * that happens to the write to file button. Contains a single method, 
     * actionPerformed to satisfy the ActionListener interface */
    private class WriteFileListener implements ActionListener
    {
        /**
         * Pre: takes in an ActionEvent (a button click)
         *
         * Post: returns nothing
         *
         * Purpose: validates textfield input and uses a file chooser to get the name of the file to write. writes the restaurant's state to a the file and tells the user whether or not the file was written successfully
         *
         * Throws: none
         */
        public void actionPerformed(ActionEvent e)
        {
            infoArea.setText("");
            
            try
            {
                final JFileChooser fc = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT & SER files", "txt", "ser");
                fc.setFileFilter(filter);//only allow .txt and .ser files
                fc.setCurrentDirectory(new java.io.File("."));//working directory
                int returnVal = fc.showOpenDialog(pnlRestaurant);
                if(returnVal == JFileChooser.APPROVE_OPTION)//if it's a valid file
                {
                    restaurant.writeToFile(fc.getSelectedFile().getName(), cbObjectFile.isSelected());
                }
                else
                    infoArea.append("Write to file " + fc.getSelectedFile().getName() + " didn't work. Please try again.");
            }
            catch(RestaurantException | NullPointerException ex)
            {
                infoArea.append(ex.getMessage());
            }
            finally 
            {
                 clearAllTextFields();
            }
        }
    }
}