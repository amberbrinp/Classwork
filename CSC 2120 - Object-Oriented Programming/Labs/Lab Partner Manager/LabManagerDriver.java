
public class LabManagerDriver
{
   //entry point
   public static void main(String[] args)
   {
      double[] grade_constants = new double[5];
      grade_constants[0] = 13.25;
      grade_constants[1] = 11.00;
      grade_constants[2] = 8.50;
      grade_constants[3] = 4.50;
      grade_constants[4] = 2;
      int total_num_labs = 14;
      int num_sections = 2;

      LabManagerView manager = new LabManagerView(800, 750, "Lab Manager", num_sections, grade_constants, total_num_labs);
   }
}