
enum CrustType {thin, hand, pan};

public class Crust
{
   private char size;
   private CrustType type;  

   public Crust()
   {
      size = 'S';
      type = CrustType.thin;
   }

   public boolean setSize(char trySize)
   {
      boolean test = false;
      trySize = Character.toUpperCase(trySize);

      if (trySize == 'S' || trySize == 'M' || trySize == 'L')
      {
         size = trySize; //only set for valid entries
         test = true;
      }
    
      return test;
   }

   public boolean setCrust(String tryCrust)
   {
      boolean test = false;
      tryCrust = tryCrust.toLowerCase();

      if (tryCrust.equals("thin"))
      {
         type = CrustType.thin;
         test = true;
      }
      else if (tryCrust.equals("hand"))
      {
         type = CrustType.hand;
         test = true;
      }
      else if (tryCrust.equals("pan"))
      {
         type = CrustType.pan;
         test = true;
      }

      return test;
   }

   public String getCrust()
   {
      return type.toString();
   }

   public char getSize()
   {
      return size;
   }

   public double pizzaCost()
   {
      double pizzaCost = 0;

      //calculate pizza cost based on size of pie
      switch (size)
      {
         case 'S':
            pizzaCost += 5.99;
            break;
         case 'M':
            pizzaCost += 7.99;
            break;
         case 'L':
            pizzaCost += 9.99;
            break;
      }

      //calculate pizza cost based on type of crust
      if (type == CrustType.hand)
      {
         pizzaCost += 0.50;
      }
      else if (type == CrustType.pan)
      {
         pizzaCost += 1.00;
      }
	
      return pizzaCost;
   }

   public String toString()  //return the current pizza configuration
   {
      String pizzaConfig;
      pizzaConfig = "Size: " + size + "\r\nCrust: " + type.toString() + "\r\nToppings:\r\n";
      return pizzaConfig;
   }

}