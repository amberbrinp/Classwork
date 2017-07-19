/* 
 PDAParser is the driver for the Graph class.
 It manages some user input, reads an input file and creates the PDA, then calls the parsing function based on the user's input.
 The driver allows the user to parse as many strings as they want from the same PDA or create as many PDA's from different input files as they want before exiting.
 - Amber Patterson */

import java.util.LinkedList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

import java.io.IOException;
import java.lang.NumberFormatException;
import java.lang.ArrayIndexOutOfBoundsException;

public class PDAParser
{
    //user interaction
    public static void main(String args[])
    {
        Scanner scanner = new Scanner(System.in);
        
        char newGraph = 'Y';
        while(newGraph == 'Y' | newGraph == 'y')
        {
            System.out.println("Please enter the name of the input file you want to use.");
        
            String fileName = scanner.next();
            Graph graph = readFile(fileName);
            if (graph == null) return;
            
            char newString = 'Y';
            while(newString == 'Y' | newString == 'y')
            {
                System.out.println("Please enter the string that you want to parse.");
                scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                // for some reason nextLine() was skipping the input until i re-created scanner
                
                System.out.println("Press S if you want to go through a step-by-step run of the parse or press F if you want to do a fast run of the parse.");
                char runType = scanner.next().charAt(0);
                if (runType == 'S' | runType == 's')//step-by-step run
                {
                    boolean result = graph.checkString(input, true);
                    if (result)
                        System.out.println("String " + input + " accepted! :^)");
                    else
                        System.out.println("String " + input + " rejected. :^(");
                }
                else if (runType == 'F' | runType == 'f')//fast run
                {
                    boolean result = graph.checkString(input, false);
                    if (result)
                        System.out.println("String " + input + " accepted! :^)");
                    else
                        System.out.println("String " + input + " rejected. :^(");
                }
                else //not a runtype!
                {
                    System.out.println("Not a valid run option.");
                }
                
                System.out.println("Do you want to parse another string? (y/n)");
                newString = scanner.next().charAt(0);
            }
            
            System.out.println("Do you want to create another PDA? (y/n)");
            newGraph = scanner.next().charAt(0);
        }

    }
    
    //parse the input file, create the graph, and add all the transitions to it
    private static Graph readFile(String fileName)
    {
        try
        {
            File file = new File(fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            
            //parse the first line of the file for the alphabet
            LinkedList<Character> alphabet = new LinkedList<Character>();
            String[] characters = reader.readLine().split(" ");
            for (int i = 0; i < characters.length; i++)
            {
                Character character = characters[i].charAt(0);
                alphabet.add(character);
            }
            
            //parse the second line of the file for the number of states
            int numstates = Integer.parseInt(reader.readLine());
            
            //parse the third line of the file for the final states
            String[] finalStatesString = reader.readLine().split(" ");
            int[] finalStates = new int[finalStatesString.length];
            for (int i = 0; i < finalStatesString.length; i++)
            {
                int state = Integer.parseInt(finalStatesString[i]);
                finalStates[i] = state;
            }
        
            //create the graph
            Graph graph = new Graph(alphabet, numstates, finalStates);
        
            //parse lines 4 through 4 + (n-1) for each state's transitions
            //and add the transitions to the graph individually
            String transitions = reader.readLine();
            int lineNumber = 0;
            while (transitions != null)
            {
                String[] rules = transitions.split(" ");
                for (int i = 0; i < rules.length; i++)
                {//parse each individual rule and add it to the graph
                    String[] values = rules[i].split(",");
                    int endIndex = Integer.parseInt(values[0]);
                    graph.addTransition(lineNumber, i, endIndex, values[1], values[2], values[3], values[4]);
                    // this assumes the input is in the specified format
                }
                transitions = reader.readLine();
                lineNumber++;
            }
            reader.close();
            boolean validTransitions = graph.checkTransitions();
            if (!validTransitions)// make sure you have the correct number of transitions before letting the user parse
            {
                System.out.println("This program only works with deterministic 2-stack PDA's. This input doesn't have the right number of transitions.");
                return null;
            }
            return graph;
        }
        catch(IOException | NumberFormatException | ArrayIndexOutOfBoundsException e)
        {
            System.out.println("error reading file\n" + e.getMessage());
            return null;
        }
    }
}