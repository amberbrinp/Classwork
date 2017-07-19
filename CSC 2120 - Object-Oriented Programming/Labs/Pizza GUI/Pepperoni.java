
public class Pepperoni extends DecoratedPizza
{
   public Pepperoni(DecoratedPizza pizza) 
   {
      super(pizza);
   }

   public double toppingCost()
   {
      return 0.99;
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
      return pizza.getImage() + "P";
   }

   public String toString()  //return the current pizza configuration
   {
      String pizzaConfig;

      pizzaConfig = pizza.toString();
      pizzaConfig += "pepperoni\r\n";

      return pizzaConfig;
   }
}


