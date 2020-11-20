import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;  
import java.io.FileNotFoundException;  
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.*;

public class ejemplo {

    public static void main(String[] args) {

        ArrayList<String> list = new ArrayList<String>();

        try 
        {
            File myObj = new File("credenciales.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) 
            {
              list.add(myReader.nextLine());
            }
            myReader.close();

            if (list.isEmpty())
            {
                throw new SecurityException("PROVEER CREDENCIALES DE LA BD EN FICHERO 'credenciales.txt'");
            }
        } 
        catch (FileNotFoundException e) 
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


       try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@//oracle0.ugr.es:1521/practbd.oracle0.ugr.es", list.get(0), list.get(1))) {

            if (conn != null) {
                System.out.println("Connected to the database!");

                conn.close();

            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
 
