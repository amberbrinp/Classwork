public class Section
{
   private int sectionID;
   private Students students;
   private String file;

   public Section(int sectionID, int total_num_labs)
   {
      this.sectionID = sectionID;
      file = "section" + sectionID + ".txt";
      students = new Students(file, total_num_labs);
   }

   //DO THIS
   //several short methods must be implemented (note: some have been done, see below)

   public int getNumStudents()
   {
       return students.getNumStudents();
   }
    
    public int getSectionID()
    {
        return sectionID;
    }
   
   public int getNumLabs()
   {
       return students.getNumLabs();
   }
   
   public int getNumLabs(int studentID)
   {
       return students.getNumLabs(studentID);
   }
   
   public int getPartnerID(int studentID, int labID)
   {
       return students.getPartnerID(studentID,labID);
   }
   
   public String getStudentName(int studentID)
   {
       return students.getStudentName(studentID);
   }
   
   public boolean isPresent(int studentID, int labID)
   {
       return students.isPresent(studentID,labID);
   }
   
   public String studentInfo(int studentID)
   {
       return students.studentInfo(studentID);
   }
   
   public String partnerList(int labID)
   {
       return students.partnerList(labID);
   }
   
   public boolean isActive(int studentID)
   {
       return students.isActive(studentID);
   }
   
   public void setInactive(int studentID)
   {
       students.setInactive(studentID);
   }
   
   public void setNotPresent(int studentID, int labID)
   {
       students.setNotPresent(studentID,labID);
   }
   
   public void setGrade(int studentID, int labID, char grade)
   {
       students.setGrade(studentID, labID, grade);
   }
   
   public void addLab()
   {
       students.addLab();
   }
   
   //no work below this point
   public void computePartners(Random rand)
   {
      students.computePartners(rand);
   }

   public void writeFile(double[] grade_constants, int total_num_labs)
   {
      students.writeFile(file, grade_constants, total_num_labs);
   }
}