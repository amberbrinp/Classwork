/* 
 The Graph class represents a 2-stack PDA in which each state is a Vertex connected to other states by Transitions.
 States are added in the constructor and Transitions are added individually from the driver. 
 The graph can parse multiple strings through the PDA doing either a fast run or a step by step run of each string.
 - Amber Patterson */

import java.util.LinkedList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Stack;
import java.util.Scanner;

public class Graph
{
    private int numstates; //number of states
    
    private LinkedList<Vertex> states;//0-based
    
    private LinkedList<Vertex> finalStates;
    // a proper subset of states
    
    private LinkedList<HashMap<Character, Transition>> transitions;
    // for each state store a list of edges that represent transitions
    // index of each state in states = index of list of that state's transitions
    
    private LinkedList<Character> alphabet;
    
    //calls addStates() and addFinalStates after creating all of private objects
   public Graph(LinkedList<Character> alphabet, int numstates, int[] finalStatesArray)
    {
        this.alphabet = alphabet;
        this.numstates = numstates;
        
        states = new LinkedList<Vertex>();
        finalStates = new LinkedList<Vertex>();
        transitions = new LinkedList<HashMap<Character, Transition>>();
        
        addStates();
        addFinalStates(finalStatesArray);
    }
    
    // create each state and its corresponding hashmap of transitions
    private void addStates()
    {
        for (int i = 0; i < numstates; i++)
        {
            transitions.add(new HashMap<Character, Transition>());
            states.add(new Vertex(i));
        }
    }
    
    //add the final states from states to finalStates
    private void addFinalStates(int[] finalStatesArray)
    {
        for (int i = 0; i < finalStatesArray.length; i++)
        {
            int index = finalStatesArray[i];
            finalStates.add(states.get(index));
        }
    }
    
    //add each transition separately from driver while reading file
    public void addTransition(int startIndex, int inputIndex, int endIndex, String popOne, String pushOne, String popTwo, String pushTwo)
    {
        //get the correct values based on their indices
        Vertex start = states.get(startIndex);
        char input = alphabet.get(inputIndex);
        Vertex end = states.get(endIndex);
        
        // create the transition with those values
        // add it to the start state's hashmap of transitions with the input character key
        Transition t = new Transition(start, input, end, popOne, pushOne, popTwo, pushTwo);
        transitions.get(start.getIndex()).put(input,t);
    }
    
    // make sure the proper number of transitions exist in the PDA
    public boolean checkTransitions()
    {
        if (transitions.size() != numstates)
            return false;
        else
        {
            for(int i = 0; i < numstates; i++)
            {
                if (transitions.get(i).size() != alphabet.size())
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    // make sure the input string is an element of alphabet* before parsing
    public boolean checkString(String input, boolean sbs)
    {
        //if input is empty string, skip the whole loop because i = length
        for (int i = 0; i < input.length(); i++)
        {
            char character = input.charAt(i);
            if (!alphabet.contains(character))
            {
                System.out.println("String does not exist in alphabet*.");
                return false;
            }
            
        }
        return parse(input, sbs);
    }
    
    // sbs is false for fast, true for step-by-step
    private boolean parse(String input, boolean sbs)
    {
        Stack<Character> stack1 = new Stack<Character>();
        Stack<Character> stack2 = new Stack<Character>();
        
        if (input.equals(""))
        {
            // assume q0 is initial state
            if (sbs) printConfig(0,stack1,stack2);
            if (finalStates.contains(states.get(0)))
                return true;
            else
                return false;
        }
        
        int index = 0; //assume q0 is initial state
        for (int i = 0; i < input.length(); i++)
        {
            // get the transition corresponding to the character in input that you're parsing coming out of the state you're in
            Vertex state = states.get(index);
            HashMap<Character, Transition> rules = transitions.get(index);
            Transition forInput = rules.get(input.charAt(i));
            //System.out.println(forInput.toString()); //for debugging purposes
            boolean popOne = forInput.popOne(stack1);
            if (!popOne)
            {
                System.out.println("Could not pop from stack one.");
                return false;
            }
            forInput.pushOne(stack1);
            boolean popTwo = forInput.popTwo(stack2);
            if (!popTwo)
            {
                System.out.println("Could not pop from stack two.");
                return false;
            }
            forInput.pushTwo(stack2);
            index = forInput.getEnd().getIndex(); //entering a new state
            
            if(sbs) printConfig(index, stack1, stack2);
            
        }// you have to go through all the input even if you reach a dead state
        //accept by end-of-string and empty stacks
        boolean finalState = finalStates.contains(states.get(index));
        if (finalState && stack1.isEmpty() && stack2.isEmpty())
            return true;
        else
            return false;
    }
    
    //iterate through 2 stacks and print contents as strings
    private void printConfig(int state, Stack<Character> stack1, Stack<Character> stack2)
    {
        String stackOne = "";
        String stackTwo = "";
        Iterator<Character> iter = stack1.iterator();
        if (iter.hasNext())
        {
            stackOne = "" + iter.next();
            while(iter.hasNext()) { stackOne = stackOne + " " + iter.next(); }
        }
        else
            stackOne = "Empty";
        
        iter = stack2.iterator();
        if (iter.hasNext())
        {
            stackTwo = "" + iter.next();
            while(iter.hasNext()) { stackTwo = stackTwo + " " + iter.next(); }
        }
        else
            stackTwo = "Empty";
        
        System.out.println("Current configuration:\nState: " + state + "\nContents of Stack 1: " + stackOne + "\nContents of Stack 2: " + stackTwo + "\nEnter to continue.");
        
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();//press Enter to continue
        
    }

}