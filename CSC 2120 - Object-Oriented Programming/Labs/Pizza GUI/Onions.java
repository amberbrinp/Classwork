
public class Onions extends DecoratedPizza
{
   public Onions(DecoratedPizza pizza) 
   {
      super(pizza);
   }

   public String toString()  //return the current pizza configuration
   {
      String pizzaConfig;

      pizzaConfig = pizza.toString();
      pizzaConfig += "onions\r\n";

      return pizzaConfig;
   }

   public double toppingCost()
   {
      return 0.79;
   }

   public double pizzaCost()
   {
      double pizzaCost = 0;
      pizzaCost += pizza.pizzaCost();

      pizzaCost += toppingCost();	
      return pizzaCost;
   }

   public String getImage()
   {
      return pizza.getImage() + "O";
   }
}


