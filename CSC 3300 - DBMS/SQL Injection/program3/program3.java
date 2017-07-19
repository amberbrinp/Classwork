import java.sql.*;

public class program3
{
    public static void main(String args[])
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            
            String url = "jdbc:mysql://" + args[0] + ":3306/" + args[1];
            
            Connection connect = DriverManager.getConnection(url, args[2], args[3]);
            
            Statement statement = connect.createStatement();
            
            ResultSet result = statement.executeQuery("select * from students where LastName like 'D%'");
            
            while (result.next())
            {
                String tnumber = result.getString("students.TNumber");
                String firstname = result.getString("students.FirstName");
                String lastname = result.getString("students.LastName");
                String dateofbirth = result.getString("students.DateOfBirth");
                
                System.out.format("%8s %-20s %-20s %8s %n", tnumber, firstname, lastname, dateofbirth);
            }
            connect.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
