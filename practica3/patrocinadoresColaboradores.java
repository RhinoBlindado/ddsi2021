/*
    Titulo:         Subsistema 3 - Patrocinadores / Colaboradores
    Fecha:          Diciembre 2020
    Autor:          Eladia Gómez Morales
    Descripción:    Implementación de parte del Subsistema 3 - Patrocinadores / Colaboradores, la funcionalidad de "Registrar entidad como patrocinadora en una edición con una cantidad de dinero".
*/

// Librerias
import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.*;

// Clase Base
public class patrocinadoresColaboradores {
    // Metodos Privados
    private static void mostrarEntidades(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM ENTIDAD");

        while (rs.next()) {
            System.out.println(
                    "\nID Entidad:" + rs.getString("IDENTIDAD") + " Nombre Entidad: " + rs.getString("NOMBREENTIDAD")
                            + " Nombre Persona Contacto: " + rs.getString("NOMBRECONTACTO") + " Correo Electronico: "
                            + rs.getString("CORREO") + " Numero de Telefono: " + rs.getString("TELEFONO"));
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
    public static void registrarEntidadPatrocinadora(Connection conn) throws SQLException {
        Scanner scan = new Scanner(System.in);
        int idEntidad, anno;
        float cantidadDinero;
        boolean patrocinadorExistente = true;

        System.out.println(">>SUBSISTEMA 3 - PATROCINADORES / COLABORADORES\n"
                + ">>Registrar entidad como patrocinadora en una edición con una cantidad de dinero\n\n");

        while (patrocinadorExistente) {
            System.out.println(">>Entidades Registradas en el torneo: ");
            mostrarEntidades(conn);

            System.out.println(">>Insertar ID Entidad que se quiere como Patrocinadora: ");
            idEntidad = scan.nextInt();

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM ENTIDAD WHERE IDENTIDAD=" + idEntidad);
            if (rs.next() == false) {
                System.out.println(
                        "\nEl ID Entidad introducido no pertenece al de ninguna Entidad Registrada, introduce otra");
                rs.close();
            } else {
                try {
                    System.out.println(">>Ediciones en las que la Entidad podria Patrocinar: ");
                    mostrarEdiciones(conn);

                    System.out.print(">>Insertar Edicion en la que la Entidad Patrocina: ");
                    anno = scan.nextInt();

                    System.out.print(">>Insertar Cantidad de Dinero con la que la Entidad va a patrocinar: ");
                    cantidadDinero = scan.nextFloat();

                    Statement st2 = conn.createStatement();
                    st2.executeUpdate(
                            "INSERT INTO PATROCINA VALUES (" + idEntidad + ", " + anno + ", " + cantidadDinero + ")");

                    patrocinadorExistente = false;
                    System.out.println(">>>ENTIDAD REGISTRADA COMO PATROCINADORA");

                } catch (Exception e) {
                    System.out.println("\n>>>ERROR: ID Entidad en uso para esa Edición introducida, introduce otro.");
                }
            }
        }
    }

    /*
     * public static void main(String[] args) { boolean running = true; Scanner scan
     * = new Scanner(System.in); int selection;
     * 
     * // Conexión a la BD System.out.println("---CONEXIÓN A BASE DE DATOS---");
     * String user, pass;
     * 
     * System.out.println(">USUARIO: "); user = scan.next();
     * 
     * System.out.println(">CONTRASEÑA: "); pass = scan.next();
     * 
     * Connection conn = null; try { conn = DriverManager.getConnection(
     * "jdbc:oracle:thin:@//oracle0.ugr.es:1521/practbd.oracle0.ugr.es", user,
     * pass);
     * 
     * if (conn != null) { System.out.println(">CONEXION BASE DE DATOS: ABIERTA");
     * 
     * } else { System.out.println(">CONEXION BASE DE DATOS: ERROR"); }
     * 
     * registrarEntidadPatrocinadora(conn);
     * 
     * } catch (SQLException e) { System.err.format("SQL State: %s\n%s\n",
     * e.getSQLState(), e.getMessage()); } }
     */
}
