/*
    Titulo:         Subsistema 1 - Jugadores / Entrenadores
    Fecha:          Diciembre 2020
    Autor:          Jose Maldonado Correa
    Descripción:    Implementación de parte del Subsistema 1 - Jugadores / Entrenadores, la funcionalidad de "Asignar un ranking a una pareja de jugadores en una edición".
*/

// Librerias
import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.*;

// Clase Base
public class jugadoresEntrenadores {
    // Metodos Privados
    /**
     * @brief Función que muestra todas las parejas de una edición
     * @param conn Objeto que proporciona el vínculo entre la base de datos y la aplicación en java 
     * @param anno Edicion sobre la cual mostrar las parejas
     */
    private static void mostrarParejas(Connection conn,int anno) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM PARTICIPA_ENTRENA WHERE ANNO="+anno);


        while (rs.next()) {
            System.out.println(
                    "\nID Anio de la edicion:" + rs.getString("ANNO") + " Id Jugador 1: " + rs.getString("IDJUGADOR1")
                            + "   Id Jugador 2: " + rs.getString("IDJUGADOR2") + " Entrenador : "
                            + rs.getString("IDENTRENADOR") + " Ranking de la pareja: " + rs.getString("RANKING"));
        }
    }
    /**
     * @brief Función que muestra todas las ediciones del torneo
     * @param conn Objeto que proporciona el vínculo entre la base de datos y la aplicación en java 
     */

    private static void mostrarEdiciones(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM EDICION");

        while (rs.next()) {
            System.out.println("Anno:" + rs.getString("ANNO"));
        }
    }
    

    // Metodos Publicos
    /**
     * @brief Función que actualiza el ranking a una pareja de una edicion del torneo
     * @param conn Objeto que proporciona el vínculo entre la base de datos y la aplicación en java 
     */
    public static void annadirRankingAPareja(Connection conn) throws SQLException
    {
        Scanner scan = new Scanner(System.in);
        int rankin,anno,idPareja1,idPareja2;
        boolean annobien = true;
        boolean pareja1 = true;
        boolean pareja2 = true;
      
        System.out.print(">>SUBSISTEMA 1 - JUGADORES / ENTRENADORES\n" +
                         ">>Añadir Ranking a una pareja\n\n");
        
        Statement st = conn.createStatement();
        ResultSet rs;
        //  Elegir edicion en particular
            
            mostrarEdiciones(conn);
            
            while(annobien){
                System.out.println(">>Seleccionar Edicion: ");
                anno = scan.nextInt();
               
                 rs = st.executeQuery("SELECT * FROM EDICION WHERE anno=" + anno);
                //Comprobamos que ese año está en la base de datos
                if (rs.next() == false) {
                        System.out.println("\nEl anno introducido no es valido");
                        rs.close();
                }
                else{

                  mostrarParejas(conn, anno);
                  rs = st.executeQuery("SELECT * FROM PARTICIPA_ENTRENA WHERE anno=" + anno);
                  //Comprobamos que hay parejas en el año seleccionado
                  if(rs.next() == false){
                    System.out.println("\n En el año seleccionado no existen parejas de jugadores");
                    annobien = true;
                  }else{  
                            annobien = false;
                 try{

                        while(pareja1){   
                                System.out.println(">>>Seleccionar Pareja : ");
                                System.out.print(">>Seleccionar ID Jugador 1 : ");
                                idPareja1 = scan.nextInt();        
                                rs = st.executeQuery("SELECT * FROM PARTICIPA_ENTRENA WHERE IDJUGADOR1=" + idPareja1);
                                //Comprobamos que el id del jugador 1 esté en la base de datos
                                if (rs.next() == false) {
                                    System.out.println("\nEl ID jugador1 introducido no es valido");
                                    rs.close();
                                } else {   
                                        pareja1 = false;
                                        System.out.println("");
                                while(pareja2){   
                                               
                                                System.out.print(">>Seleccionar ID Jugador 2 : ");
                                                idPareja2 = scan.nextInt();
                                                //Comprobamos que el id del jugador 2 este en la base de datos y emparejado con el jugador 1
                                                rs = st.executeQuery("SELECT * FROM PARTICIPA_ENTRENA WHERE IDJUGADOR1=" + idPareja1 + "AND IDJUGADOR2=" + idPareja2);

                                    //Comprobamos que el id del jugador 2 este en la base de datos y emparejado con el jugador 1
                                if (rs.next() == false) {
                                                System.out.println("\nEl ID jugador2 introducido no es valido");
                                                rs.close();
                                }else {   
                                    pareja2 = false;
                                        System.out.println("");
                                        System.out.print(">>Insertar Ranking: ");
                                        rankin = scan.nextInt();
                                 if(rankin < 0 ){
                                        System.err.format("Ranking no valido");
                                }else {
                                        st.executeUpdate("UPDATE  PARTICIPA_ENTRENA SET RANKING="+rankin+"WHERE ANNO="+anno+"AND IDJUGADOR1=" + idPareja1+" AND IDJUGADOR2=" +idPareja2);
                                        System.out.println("Se ha actualizado el ranking ");
                                      }
                                }
                        } // END WHILE PAREJA 2
                    }

                }//END WHILE PAREJA 1   
       }
       catch (SQLException e) {
           System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
       }
    }
    } // END WHILE DE ANNO
}
    
}

    public static void main(String[] args) {
        
        Scanner scan = new Scanner(System.in);
        

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

            annadirRankingAPareja(conn);
            //mostrarParejas(conn, 2018);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
        }
        scan.close();
    }
}


