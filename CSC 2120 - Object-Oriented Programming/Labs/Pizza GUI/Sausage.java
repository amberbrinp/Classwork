
public class Sausage extends DecoratedPizza
{
   public Sausage(DecoratedPizza pizza) 
   {
      super(pizza);
   }

   public String toString()  //return the current pizza configuration
   {
      String pizzaConfig;

      pizzaConfig = pizza.toString();
      pizzaConfig += "sausage\r\n";

      return pizzaConfig;
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
      return pizza.getImage() + "S";
   }
}


