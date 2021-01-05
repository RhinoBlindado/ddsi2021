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

    private static void mostrarEdiciones(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM EDICION");

        while (rs.next()) {
            System.out.println("Anno:" + rs.getString("ANNO"));
        }
    }
    

    // Metodos Publicos
    public static void annadirRankingAPareja(Connection conn) throws SQLException
    {
        Scanner scan = new Scanner(System.in);
        int rankin,anno,idPareja1,idPareja2;

        System.out.print(">>SUBSISTEMA 1 - JUGADORES / ENTRENADORES\n" +
                         ">>Añadir Ranking a una pareja\n\n");
        
       
        //  Elegir edicion en particular
            
            mostrarEdiciones(conn);
            System.out.println(">>Seleccionar Edicion: ");
            anno = scan.nextInt();

        //  Seleccionar tipo Entrada y Cantidad
            mostrarParejas(conn, anno);
            System.out.println(">>>Seleccionar Pareja : ");
            System.out.print(">>Seleccionar ID Jugador 1 : ");
            idPareja1 = scan.nextInt();
            System.out.println("");
            System.out.print(">>Seleccionar ID Jugador 2 : ");
            idPareja2 = scan.nextInt();
            System.out.println("");
            System.out.print(">>Insertar Ranking: ");
            rankin = scan.nextInt();

        //  Finalizar y "pagar", añadir más entradas o cancelar.
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE  PARTICIPA_ENTRENA SET RANKING="+rankin+"WHERE ANNO="+anno+"AND IDJUGADOR1=" + idPareja1+" AND IDJUGADOR2=" +idPareja2);
        

     


        
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

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
        }
    }
}


