
public class Pizza extends DecoratedPizza
{
   //the base pizza is just the crust (and tomato sauce) with no toppings
   private Crust crust;

   public Pizza() //constructor-- set default values for new object
   {
      super();  //base level pizza
      crust = new Crust();
   }

   //if trySize is not valid, the set is not performed
   public boolean setSize(char trySize)
   {
      return crust.setSize(trySize);
   }

   //if tryCrust is not valid, the set is not performed
   public boolean setCrust(String tryCrust)
   {
      return crust.setCrust(tryCrust);
   }

   public double pizzaCost()
   {
      return crust.pizzaCost();
   }

   public String getImage()
   {
      String temp = "";
      temp += crust.getSize();
      return temp;
   }

   public String toString() 
   {
      return crust.toString();
   }
}