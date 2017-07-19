/* 
 The Transition class represents a transition from one Vertex to another.
 Popping and Pushing from the stacks in the Graph is handled here.
 */

import java.util.Stack;
import java.util.EmptyStackException;

public class Transition
{
    private Vertex start;//state you're in
    private char input;//character you're parsing
    private Vertex end;//state you're moving to
    
    //the respective strings that you're popping and pushing from each stack
    private String popOne;
    private String pushOne;
    private String popTwo;
    private String pushTwo;
    
    
    public Transition(Vertex start, char input, Vertex end, String popOne, String pushOne, String popTwo, String pushTwo)
    {
        this.start = start;
        this.input = input;
        this.end = end;
        this.popOne = popOne;
        this.pushOne = pushOne;
        this.popTwo = popTwo;
        this.pushTwo = pushTwo;
    }
    
    public Vertex getStart()
    {
        return start;
    }
    
    public Vertex getEnd()
    {
        return end;
    }
    
    public char getInput()//not actually used
    {
        return input;
    }
    
    //if false, reject the string
    public boolean popOne(Stack<Character> stack1)
    {
        if (!popOne.equals("!"))
        {
            try
            {
                String stackPop = "";
                for (int i = 0; i  < popOne.length(); i++)
                {
                    stackPop = stackPop + stack1.pop();
                }
                if (!stackPop.equals(popOne))
                {//if the correct characters can't be popped from the stack
                    return false;
                }
            }
            catch(EmptyStackException e)
            {
                return false;
            }

        }
        return true;
    }
    
    public void pushOne(Stack<Character> stack1)
    {
        if (!pushOne.equals("!"))
        {
            for (int i = 0; i  < pushOne.length(); i++)
            {
                stack1.push(pushOne.charAt(i));
            }
        }
    }
    
    //if false, reject the string
    public boolean popTwo(Stack<Character> stack2)
    {
        if (!popTwo.equals("!"))
        {
            try
            {
                String stackPop = "";
                for (int i = 0; i  < popTwo.length(); i++)
                {
                    stackPop = stackPop + stack2.pop();
                }
                if (!stackPop.equals(popTwo))
                {//if the correct characters can't be popped from the stack

                    return false;
                }

            }
            catch(EmptyStackException e)
            {
                return false;
            }
        }
        return true;
    }
    
    public void pushTwo(Stack<Character> stack2)
    {
        if(!pushTwo.equals("!"))
        {
            for (int i = 0; i  < pushTwo.length(); i++)
            {
                stack2.push(pushTwo.charAt(i));
            }
        }
    }
    
    //for debugging purposes
    public String toString()
    {
        return "State " + start.getIndex() + " to State " + end.getIndex() + "\nInput: " + input + "\nPop from Stack 1: " + popOne + "\nPush to Stack 1: " + pushOne + "\nPop from Stack 2: " + popTwo + "\nPush to Stack 2: " + pushTwo;
    }
}