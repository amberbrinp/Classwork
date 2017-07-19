import java.util.ArrayList;

public class Labs
{
   private ArrayList<Lab> labs;

   public Labs()
   {
      labs = new ArrayList<Lab>();
   }

   public int getNumLabs()
   {
      return labs.size();
   }
   
   public void setNotPresent(int labID)
   {
       labs.get(labID).setGrade('F');
   }

   //important that these parameters are valid
   //Lab should throw an exception if they are not
   public void addLab(char grade, int partnerID)
   {
      labs.add(new Lab(grade, partnerID));
   }

   //brand new lab with initial default values
   public void addLab()
   {
      labs.add(new Lab());
   }

   //1-based
   public void setGrade(int labID, char grade)
   {
      if (labID <= 0 || labID > getNumLabs())
      {
         return;
      }

      labs.get(labID - 1).setGrade(grade);
   }

   public char getGrade(int labID)
   {
      if (labID <= 0 || labID > getNumLabs())
      {
         return 'F';
      }

      return labs.get(labID - 1).getGrade();
   }

   public void setPartnerID(int labID, int partnerID)
   {
      if (labID <= 0 || labID > getNumLabs())
      {
         return;
      }

      labs.get(labID - 1).setPartnerID(partnerID);
   }

   public int getPartnerID(int labID)
   {
      if (labID <= 0 || labID > getNumLabs())
      {
         return -1;
      }

      return labs.get(labID - 1).getPartnerID();
   }

   public boolean isPresent(int labID)
   {
      if (labID <= 0 || labID > getNumLabs())
      {
         return false;
      }

      return labs.get(labID - 1).isPresent();
   }

   public char computeFinalGrade(double[] grade_constants)
   {
      int f_count = 0;
      double grade_count = 0;
      
      for (Lab lab : labs)
      {
         char grade = lab.getGrade();
         if (grade == 'A')
         {
            grade_count += 1.00;
         }
         else if (grade == 'B')
         {
            grade_count += 0.75;
         }
         else if (grade == 'C')
         {
            grade_count += 0.50;
         }
         else if (grade == 'D')
         {
            grade_count += 0.25;
         }
         else
         {
            f_count++;
         }

         if (f_count == (int) grade_constants[4]) 
         {
            return 'F';
         }
      }

      if (grade_count >= grade_constants[0])
      {
         return 'A';
      }
      else if (grade_count >= grade_constants[1])
      {
         return 'B';
      }
      else if (grade_count >= grade_constants[2])
      {
         return 'C';
      }
      else if (grade_count >= grade_constants[3])
      {
         return 'D';
      }
      else
      {
         return 'F';
      }
   }
}