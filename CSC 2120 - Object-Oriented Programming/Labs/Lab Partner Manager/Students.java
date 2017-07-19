import java.util.ArrayList;
import java.io.*;

public class Students
{
   private ArrayList<Student> students;
   private int current_labID = 1;
   private int max_rank = 1;

   public Students(String file, int total_num_labs)
   {
      students = readFile(file, total_num_labs);
      current_labID = determineCurrentLabNumber();
      max_rank = determineMaxRank();
   }

   private int determineCurrentLabNumber()
   {
      //DO THIS
      //figure out from the labs stored for each student which lab we are currently on
      //this is necessary as students stop attending and their number of labs will not reflect the true number of labs
      //for efficiency, store this value in an instance variable to avoid computing it over and over
      int currentLab = 0;
      for(Student student : students)
      {
          if(student.getNumLabs() > currentLab)
            currentLab = student.getNumLabs();
      }
      return currentLab;
   }
   
   public int getNumStudents()
   {
       return students.size();
   }
   
   public int getNumLabs()
   {
       return current_labID;
   }
   
   public int getPartnerID(int studentID, int labID)
    {
        if (studentID < 1 || studentID > students.size())
            return 0;
        else
            return students.get(studentID-1).getPartnerID(labID);
   }
   
   public int getNumLabs(int studentID)
    {
        if (studentID < 1 || studentID > students.size())
            return 0;
        else
            return students.get(studentID-1).getNumLabs();
   }
   
   public boolean isPresent(int studentID, int labID)
    {
        if (studentID < 1 || studentID > students.size())
            return false;
        else
            return students.get(studentID-1).isPresent(labID);
   }
   
   public boolean isActive(int studentID)
    {
        if (studentID < 1 || studentID > students.size())
            return false;
        else
            return students.get(studentID-1).isActive();
   }
   
   public char getGrade(int studentID,int labID)
    {
        if (studentID < 1 || studentID > students.size())
            return 'X';
        else
            return students.get(studentID-1).getGrade(labID);
   }
   
   public void setInactive(int studentID)
    {
        if (studentID < 1 || studentID > students.size())
            return;
        else
            students.get(studentID-1).setInactive();
   }
   
   public void setNotPresent(int studentID, int labID)
    {
        if (studentID < 1 || studentID > students.size())
            return;
        else
            students.get(studentID-1).setNotPresent(labID);
   }
   
   public void setGrade(int studentID, int labID, char grade)
    {
        if (studentID < 1 || studentID > students.size())
            return;
        else
        {
            students.get(studentID-1).setGrade(labID,grade);
            int partner = getPartnerID(studentID,labID);
            if (partner!=0)
                students.get(partner-1).setGrade(labID, grade);
        }

   }
   
   public void addLab()
   {
       for (Student student : students)
       {
           student.addLab();
       }
       current_labID++;
   }

   //DO THIS
   //several short methods must be implemented (note: some have been done, see below)

   public String partnerList(int labID)
   {
      int num_labs = current_labID;
      if (labID <= 0 || labID > num_labs)
      {
         return "";
      }

      String temp = "";
      for(Student student : students)
      {
         boolean present = false;
         try
         {
            present = student.isPresent(labID); //if the student is inactive, this can cause an exception
         }
         catch(IndexOutOfBoundsException ioobe)
         {
         }

         if(present)
         {
            //DO THIS
            int partnerID = student.getPartnerID(labID);
            String partnerName = "";
            if(partnerID >= 0)
            {
               partnerName = students.get(partnerID-1).getName();
            }

            String str = student.getName() + " ";
            char grade = student.getGrade(labID);

            int len = str.length();
            temp += str;
            temp += spaces(30 - len);
            temp += partnerName + "\r\n";
         }
      }
      return temp;
   }

   //no work below this point
   private int determineMaxRank()
   {
      int maxRank = 1;
      for (Student student : students)
      {
         int currRank = student.getRank();
         if (currRank > maxRank)
         {
            maxRank = currRank;
         }
      }
      return maxRank;
   }

   public String getStudentName(int studentID)
   {
      if (studentID <= 0 || studentID > getNumStudents())
      {
         return "";
      }

      return students.get(studentID - 1).getName();
   }

   public String studentInfo(int studentID)
   {
      if (studentID <= 0 || studentID > getNumStudents())
      {
         return "";
      }

      String info = getStudentName(studentID);
      Student student = students.get(studentID - 1);
      int numLabs = student.getNumLabs();
      for (int labID = 1; labID <= numLabs; labID++)
      {
         info += " " + labID + ":" + getGrade(studentID, labID);
      }

      return info;
   }

   private ArrayList<Student> getAvailableStudentsOfSpecificRank(int rank, Random rand)
   {
      ArrayList<Student> sameRank = new ArrayList<Student>();

      for (Student student : students)
      {
         if (student.isActive() && student.isPresent(current_labID) && !student.hasPartner())
         {
            int student_rank = student.getRank();
            if (student_rank == rank)
            {
               sameRank.add(student);
            }
         }
      }

      return sameRank;
   }

   private void checkAndCorrectForOdd(int rank, ArrayList<Student> sameRank, Random rand)
   {
      int numInList = sameRank.size();

      if (numInList % 2 == 0) //number of students in this rank is even, so no corrections are needed
      {
         return;
      }
      else if (rank < max_rank) //add a student from the next higher rank to this pool to make it even
      {
         ArrayList<Student> nextRank = getAvailableStudentsOfSpecificRank(rank + 1, rand);
         int numAvailableStudents = nextRank.size();

         //pull a student from the available pool at random
         int temp_index = rand.randomInt(1, numAvailableStudents);
         //figure out what that student's ID is
         Student student = nextRank.get(temp_index - 1);
         sameRank.add(student);
      }
      else  //rank is maxRank and there are an odd number of students-- one student will work alone
      {
         //pull a student from the available pool at random
         int temp_index = rand.randomInt(1, numInList);
         Student student = sameRank.get(temp_index - 1);
         int studentID = student.getStudentID();
         student.setPartnerID(0);  //self-partner
      }
   }

   private int getRandomPartnerID(int studentID, ArrayList<Student> sameRank, Random rand)
   {
      int numAvailableStudents = sameRank.size();
      
      //pull a student from the available pool at random
      int temp_index = rand.randomInt(1, numAvailableStudents);
      //figure out what that student's ID is
      Student partner = sameRank.get(temp_index - 1);
      int partnerID = partner.getStudentID();

      while (partner.hasPartner() || partnerID == studentID)  //must have a partner (work alone only in special case)
      {
         temp_index = rand.randomInt(1, numAvailableStudents);
         partner = sameRank.get(temp_index - 1);
         partnerID = partner.getStudentID();
      }

      return partnerID;
   }

   public void computePartners(Random rand)
   {
      //fill in partners from lowest rank (1, weakest students) to highest rank (maxRank, strongest students)
      for (int rank = 1; rank <= max_rank; rank++)
      {
         ArrayList<Student> sameRank = getAvailableStudentsOfSpecificRank(rank, rand);
         checkAndCorrectForOdd(rank, sameRank, rand);

         for (Student student : sameRank)
         {
            if (!student.hasPartner())
            {
               int studentID = student.getStudentID();
               int partnerID = getRandomPartnerID(studentID, sameRank, rand);
               student.setPartnerID(partnerID);
               Student partner = students.get(partnerID - 1);
               partner.setPartnerID(studentID);
            }
         }
      }
   }

   private ArrayList<Student> readFile(String file, int total_num_labs)
   {
      ArrayList<Student> students = new ArrayList<Student>();

      try
      {
         FileReader fr = new FileReader(file);
         BufferedReader br = new BufferedReader(fr);

         String line = br.readLine();
         int count = 1;

         while(line != null)
         {
            String[] split = line.split(",");
            
            String name;
            char active = 'Y';
            int rank = 0;
            int studentID = 0;

            studentID = Integer.parseInt(split[0]);
            name = split[1];
            active = split[2].charAt(0);
            rank = Integer.parseInt(split[3]);

            Labs labs = new Labs();

            int countLabs = 1;

            for (int i = 4; i < split.length; i += 2)  //loop to get grades and partners
            {
               if (countLabs == total_num_labs + 1)
               {
                  break;
               }

               try
               {
                  char grade = split[i].charAt(0);
                  int partnerID = Integer.parseInt(split[i +  1]);

                  labs.addLab(grade, partnerID);
               }
               catch(ArrayIndexOutOfBoundsException aioobe) {}

               countLabs++;
            }

            Student student = new Student(count, name, active == 'Y', rank, labs);
            students.add(student);

            count++;
            line = br.readLine();
         }

         br.close();
      }
      catch (FileNotFoundException fnfe)
      {
         //do nothing (an empty ArrayList will be returned, see below)
      }
      catch (IOException ioe)
      {
         //stop processing if something goes seriously wrong
         System.out.println(ioe.getMessage());
         System.exit(0);
      }
      
      return students;
   }

   public void writeFile(String file, double[] grade_constants, int total_num_labs)
   {
      int num_labs = current_labID;

      try
      {
         FileWriter fw = new FileWriter(file);
         BufferedWriter bw = new BufferedWriter(fw);
         PrintWriter pw = new PrintWriter(bw);

         for (Student student : students)
         {
            String temp = student.toString();

            if (num_labs == total_num_labs)  //compute and append the final grade
            {
               char final_grade = student.computeFinalGrade(grade_constants);
               temp += "," + final_grade;
            }

            pw.println(temp);
         }

         pw.close();
      }
      catch (IOException ioe)
      {
         System.out.println(ioe.getMessage());
      }
   }

   private static String spaces(int num_spaces)
   {
      String spaces = "";
      for (int i = 1; i < num_spaces; i++)
      {
         spaces += " ";
      }
      return spaces;
   }
}