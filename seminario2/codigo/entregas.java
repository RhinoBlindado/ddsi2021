/*
    Titulo: DDSI Seminario 2
    Fecha: Noviembre 2020
    Autores:    Gomez Morales, Eladia
                Lugli, Valentino Glauco
                Maldonado Correa, José
                Moyano Romero, Amanda
                Muela Cárdenas, Jesús
    Descripción: Pequeño SI que se conecta a una base de datos para realizar operaciones.
*/

// Librerias
import java.sql.*;
import java.io.File;  
import java.io.FileNotFoundException;  
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.*;

// Clase base
public class entregas
{

    public static void crearTabla()
    {
        // 
    }


// Función Main
    public static void main (String[] args)
    {
        boolean running = true;
        Scanner scan = new Scanner(System.in);
        int selection;

        // Conexión a la BD
        System.out.println("---CONEXIÓN A BASE DE DATOS---");
        String user, pass;
        
        System.out.println(">USUARIO: ");
        user = scan.next();

        System.out.println(">CONTRASEÑA: ");
        pass = scan.next();


        Connection conn = null;
        try 
        {
            conn = DriverManager.getConnection("jdbc:oracle:thin:@//oracle0.ugr.es:1521/practbd.oracle0.ugr.es", user, pass);
        
            if (conn != null) 
            {
                System.out.println(">CONEXION BASE DE DATOS: ABIERTA");

            } else 
            {
                System.out.println(">CONEXION BASE DE DATOS: ERROR");
            }

            boolean tablasExisten = false;

/*          PreparedStatement sql = null;
            sql = conn.prepareStatement("SELECT table_name FROM user_tables where table_name='Stock'");

            ResultSet whatever;
            whatever = sql.executeQuery();
            System.out.println("STOCK:" + whatever.getString(1));

            sql = conn.prepareStatement("SELECT table_name FROM user_tables where table_name='TEST1'");
            whatever = sql.executeQuery();
            System.out.println("TEST: " + whatever.getString(1));
*/

            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "Stock", null);
            if (tables.next()) {
            System.out.println("Existe");
            }
            else {
                System.out.println("no Existe");

            }

            // Menu principal
            while(running)
            {
                System.out.println("---SISTEMA GUAPO DE INFORMACION---  \n"+
                                    "Menú:                              \n"+ 
                                    "1- Test                            \n"+
                                    "0- Salir");

                selection = scan.nextInt();

                switch(selection)
                {
                    case 1:
                        crearTabla();
                    break;
        
                    case 2:
                        System.out.println("2");

                    break;

                    case 3:
                        System.out.println("3");
                    break;

                    case 0:
                        running = false;
                        conn.close();
                        System.out.println(">CONEXION BASE DE DATOS: CERRADA");
                    break; 
                }
            }
            
        } catch (SQLException e) 
        {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
        } catch (Exception e) 
        {
            e.printStackTrace();
        }


     /*   try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@//oracle0.ugr.es:1521/practbd.oracle0.ugr.es", list.get(0), list.get(1))) {

            if (conn != null) {
                System.out.println("Connected to the database!");

                PreparedStatement st = null;

                st = conn.prepareStatement("CREATE TABLE test1(hola INTEGER)");
                st.executeUpdate();
                conn.close();

            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }
}
