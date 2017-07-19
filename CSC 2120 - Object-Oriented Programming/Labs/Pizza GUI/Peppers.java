
public class Peppers extends DecoratedPizza
{
   public Peppers(DecoratedPizza pizza) 
   {
      super(pizza);
   }

   public String toString()  //return the current pizza configuration
   {
      String pizzaConfig;

      pizzaConfig = pizza.toString();
      pizzaConfig += "peppers\r\n";

      return pizzaConfig;
   }

   public double toppingCost()
   {
      return 0.69;
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
      return pizza.getImage() + "G";
   }
}


