/*
    Titulo:         Subsistema 5 - Materiales / Pedidos
    Fecha:          Diciembre 2020
    Autor:          Amanda Moyano Romero
    Descripción:    Implementación de parte del Subsistema 5 - Materiales / Pedidos, la funcionalidad de "Realizar un pedido de materiales".
*/

// Librerias
import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.*;

// Clase Base
public class pedidosMateriales {
       

   /**
	* @param conn 		Objeto que proporciona el vínculo entre la base de datos y la aplicación en java 
	* @brief 			Crea un nuevo pedido
	*/
    public static int crearPedido(Connection conn) throws SQLException {

        Statement st = conn.createStatement();
        int numPedido;
        string num;

        ResultSet rs = st.executeQuery("SELECT TOP 1 * FROM PEDIDO ORDER BY numPedido DESC");

        numPedido = rs.getInt("numPedido") + 1;

        st.executeUpdate("INSERT INTO PEDIDO VALUES ("+ numPedido + ", 'INICIADO')");

        st.executeUpdate();

        return numPedido;
    }




    /**
	* @param conn 	Objeto que proporciona el vínculo entre la base de datos y la aplicación en java
	* @brief 		Muestra todos los materiales
	*/
    public static void mostrarMateriales(Connection conn) throws SQLException
    {

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM MATERIAL_PROPORCIONADO");

        while(rs.next())
        {
            System.out.println("id Material: " + rs.getString("idMaterial") + " - nombre: " + rs.getString("nombreMaterial") 
                                + " - id Entidad: " + rs.getString("idEntidad") + " - año: " + rs.getString("anno") );
        }

    }


   /**
	* @param conn 		Objeto que proporciona el vínculo entre la base de datos y la aplicación en java 
	* @brief 			Crea un nuevo pedido
	*/
    public static int aniadirMaterial(Connection conn, int numPedido, int idMaterial, int cantidad) throws SQLException {

        Statement st = conn.createStatement();


        st.executeUpdate("INSERT INTO PIDE VALUES ("+ numPedido + ", " + idMaterial ", " + cantidad + ")");



        st.executeUpdate();

        return numPedido;
    }

    /**
    * @param conn       Objeto que proporciona el vínculo entre la base de datos y la aplicación en java 
    * @brief            Realizar un pedido de materiales
    */
    public static int pedirMateriales(Connection conn) throws SQLException {

        Statement st = conn.createStatement();
        Scanner datosDetalle = new Scanner(System.in);
        int numPedido = -1;
        boolean parar = false;
        int selection;

        while(!parar){

            System.out.println( "- SUBSISTEMA 3 - PEDIDOS/MATERIALES -\n" + "Realizar pedido de materiales:\n" + "1 - Crear pedido\n" + 
                                    "2 - Añadir material\n" + "3 - Borrar un pedido\n" + 
                                    "0 - Salir");
            selection = scan.nextInt();
            System.out.println("\n");
            Savepoint noPedido = conn.setSavepoint();

            case 1:
                        System.out.println(">>CREANDO PEDIDO...");
                       
                            numPedido = crearPedido(conn); 
                            System.out.println(">>>El número de pedido es:"+numPedido);

            break;

            case 2: 
                    if(numPedido != -1){
                         System.out.println(">>>Productos materiales existentes:");
                         mostrarMateriales(conn);

                         System.out.println(">>>Inserta el material a añadir al pedido:");
                         System.out.println(">>>ID Material");
                         int idMaterial = datosDetalle.nextInt();


                         System.out.println(">>>Inserta la cantidad:");
                         int cantidad = datosDetalle.nextInt();

                         aniadirMaterial(conn, numPedido, idMaterial, cantidad);

                    }

                    else
                        System.out.println(">>ERROR:Debe crear primero un nuevo pedido");


            break;

            case 3:
                        if(numPedido != -1){
                            System.out.println(">>PEDIDO ACTUAL CANCELADO");
                            conn.rollback(noPedido);
                            numPedido = -1;
                        }

                        else
                            System.out.println(">>ERROR:Debe crear primero un nuevo pedido");


            break;

            case 0:
                    parar = true;
            break;

        }
    }


    public static void main(String[] args) {
        boolean running = true;
        Scanner scan = new Scanner(System.in);
        int selection;

        // Conexión a la BD System.out.println("---CONEXIÓN A BASE DE DATOS---");
        String user, pass;

        System.out.println(">USUARIO: ");
        user = scan.next();

        System.out.println(">CONTRASEÑA: ");
        pass = scan.next();

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:oracle:thin:@//oracle0.ugr.es:1521/practbd.oracle0.ugr.es", user,
                    pass);

            if (conn != null) {
                System.out.println(">CONEXION BASE DE DATOS: ABIERTA");

            } else {
                System.out.println(">CONEXION BASE DE DATOS: ERROR");
            }

            pedirMateriales(conn);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
        }
    }
}
