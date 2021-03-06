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
    * @param conn       Objeto que proporciona el vínculo entre la base de datos y la aplicación en java 
    * @brief            Crea un nuevo pedido
    */
    public static int crearPedido(Connection conn) throws SQLException {

        Statement st = conn.createStatement();
        int numPedido;
        String num;

        ResultSet rs = st.executeQuery("SELECT * FROM PEDIDO WHERE NUMPEDIDO=(SELECT MAX(NUMPEDIDO) FROM PEDIDO)");
        rs.next();
        numPedido = rs.getInt("NUMPEDIDO") + 1;

        st.executeUpdate("INSERT INTO PEDIDO VALUES ("+ numPedido + ", 'INICIADO')");


        return numPedido;
    }




    /**
    * @param conn   Objeto que proporciona el vínculo entre la base de datos y la aplicación en java
    * @brief        Muestra todos los materiales
    */
    public static void mostrarMateriales(Connection conn) throws SQLException
    {

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM MATERIAL_PROPORCIONADO");

        while (rs.next()) {
            System.out.println("\nid Material:" + rs.getString("IDMATERIAL") + " nombre: " + rs.getString("nombreMaterial")
                    + " id Patrocinadora: " + rs.getString("idEntidad") + " año: " + rs.getString("anno")
                    + " cantidad: " + rs.getString("cantidad")  );

        }

    }


    /**
    * @param conn   Objeto que proporciona el vínculo entre la base de datos y la aplicación en java
    * @brief        Muestra los detalles del pedido
    */
    public static void mostrarDetallesPedido(Connection conn, int numPedido) throws SQLException
    {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM PIDE WHERE NUMPEDIDO=" + numPedido);


        while(rs.next()){
            System.out.println(" - idMaterial: " + rs.getString("idMaterial") 
                                    + " -> cantidad: " + rs.getString("cantidad")); 
        }

        System.out.println("\n");
    
    }

    


   /**
    * @param conn       Objeto que proporciona el vínculo entre la base de datos y la aplicación en java 
    * @brief            Añadir material al pedido
    */
    public static void aniadirMaterial(Connection conn, int numPedido, int idMaterial, int cantidad) throws SQLException {

        Statement st = conn.createStatement();

        st.executeUpdate("INSERT INTO PIDE VALUES (" + numPedido + ", " + idMaterial + ", " + cantidad + ")");

        
        //se actualiza la cantidad en la tabla MATERIAL_PROPORCIONADO
       
        ResultSet rs = st.executeQuery("SELECT * FROM MATERIAL_PROPORCIONADO WHERE idMaterial=" + idMaterial);
        rs.next();
        int nuevaCantidad = rs.getInt("CANTIDAD") - cantidad;

        st.executeUpdate("UPDATE MATERIAL_PROPORCIONADO SET CANTIDAD=" + nuevaCantidad + "WHERE idMaterial=" + idMaterial);

    }

    /**
    * @param conn       Objeto que proporciona el vínculo entre la base de datos y la aplicación en java 
    * @brief            Realizar un pedido de materiales
    */
    public static void pedirMateriales(Connection conn) throws SQLException {

        
        Statement st = conn.createStatement();
        Scanner datosDetalle = new Scanner(System.in);
        conn.setAutoCommit(false);
        int numPedido = -1;
        boolean parar = false;
        int selection;

        while(!parar){

            System.out.println( "- SUBSISTEMA 5 - PEDIDOS/MATERIALES -\n" + "Realizar pedido de materiales:\n" + "1 - Crear pedido\n" + 
                                    "2 - Añadir material\n" +  "0 - Finalizar pedido");
            selection = datosDetalle.nextInt();
            System.out.println("\n");

            switch (selection) {

                case 1:
                            System.out.println(">>CREANDO PEDIDO...");
                            numPedido = crearPedido(conn); 
                            System.out.println(">>>El número de pedido es: "+numPedido + "\n");
						

                break;

                case 2: 

                        if(numPedido != -1){

                             //se muestran los diferentes materiales disponibles
                             System.out.println(">>>Productos materiales existentes:");
                             mostrarMateriales(conn);
            
                                 System.out.println(">>>Inserta el material a añadir al pedido (Todos tienen que ser de la misma patrocinadora):");
                                 System.out.println(">>>ID Material");
                                 int idMaterial = datosDetalle.nextInt();

                                 
                                 // se comprueba que el idMaterial es correcto
                                 ResultSet rs = st.executeQuery("SELECT * FROM MATERIAL_PROPORCIONADO WHERE idMaterial=" + idMaterial);
                                 while(rs.next() == false){
                                    System.out.println(
                                    "\n>>>ERROR El idMaterial no existe, inténtelo de nuevo");
                                    
                                    idMaterial = datosDetalle.nextInt();
                                    rs = st.executeQuery("SELECT * FROM MATERIAL_PROPORCIONADO WHERE idMaterial=" + idMaterial);

                                 }

                                 

                                 System.out.println(">>>Inserta la cantidad:");
                                 int cantidad = datosDetalle.nextInt();

                                 //se comprueba que cantidad > 0
                                 while (cantidad < 0) {
                                    System.out.print("\n>>ERROR La cantidad debe de ser superior a 0, vuelve a introducir una: ");
                                    cantidad = datosDetalle.nextInt();
                                 }

                            

                                 // se comprueba que haya suficiente cantidad en el inventario
                                 ResultSet rs2 = st.executeQuery("SELECT * FROM MATERIAL_PROPORCIONADO WHERE idMaterial=" + idMaterial);
                                 rs2.next();
                                 int nuevaCantidad = rs2.getInt("CANTIDAD") - cantidad;
                                 while (nuevaCantidad < 0){
                                    System.out.println(
                                    "\n>>>ERROR La cantidad es superior a la disponible, inténtelo de nuevo");

                                    cantidad = datosDetalle.nextInt();

                                    //se comprueba que cantidad > 0
	                                while (cantidad < 0) {
	                                    System.out.print("\n>>ERROR La cantidad debe de ser superior a 0, vuelve a introducir una: ");
	                                    cantidad = datosDetalle.nextInt();
	                                }
                                 	nuevaCantidad = rs2.getInt("CANTIDAD") - cantidad;

                                 }
	
                                 //se añade el material al pedido
                                 aniadirMaterial(conn, numPedido, idMaterial, cantidad);

                                 //se muestran los detalles del pedido actualizados
                                 mostrarDetallesPedido(conn, numPedido);

                        }

                        else
                            System.out.println(">>ERROR:Debe crear primero un nuevo pedido");


                break;


                case 0:
                        parar = true;
                break;

            }

            conn.setAutoCommit(true);
        }

    }

}

