
public class Lab
{
   private char grade;
   private int partnerID; //a partnerID of 0 indicates no partner

   //if these values are invalid, should throw an exception
   //this constructor is used to read in old labs from the text file
   public Lab(char grade, int partnerID)
   {
      this.grade = grade;
      this.partnerID = partnerID;
   }

   //a new lab is being created, so assign default values
   public Lab()
   {
      this.grade = 'D'; //default grade for coming to lab
      this.partnerID = 0; //no partner assigned (as yet)
   }

   public boolean isPresent()
   {
      return (grade != 'F');
   }

   public void setGrade(char grade)
   {
      if (grade >= 'A' && grade <= 'F')
      {
         this.grade = grade;
      }
   }

   public char getGrade()
   {
      return grade;
   }

   public void setPartnerID(int id)
   {
      if (id >= 0)
      {
         partnerID = id;
      }
   }

   public int getPartnerID()
   {
      return partnerID;
   }
}