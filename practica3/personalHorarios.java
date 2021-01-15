/*
    Titulo:         Subsistema 4 - Personal / Horarios
    Fecha:          Enero 2021
    Autor:          Jesús Muela 
    Descripción:    Implementación de parte del Subsistema 4 - Personal / Horarios , la funcionalidad 'mostrar el personal que no trabaja en una edición'
*/

// Librerias
import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.*; 

// Clase Base
public class personalHorarios {
    // Metodos Privados

	 /**
     * @param conn Objeto que proporciona el vínculo entre la base de datos y la
     *             aplicación en java
     * @brief Muestra todas las Ediciones registradas
     */
    private static void mostrarEdiciones(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM EDICION");

        while (rs.next()) {
            System.out.println("Anno:" + rs.getString("ANNO"));
        }
    }

 /**
     * @param conn Objeto que proporciona el vínculo entre la base de datos y la
     *             aplicación en java
     * @return vectorAnno, ArrayList de entero que almacena los años de las
     *         ediciones registradas
     * @brief Guarda las Ediciones en un ArrayList
     */
    private static ArrayList<Integer> annoEdiciones(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM EDICION");

        ArrayList<Integer> vectorAnno = new ArrayList<>();

        while (rs.next()) {
            vectorAnno.add(rs.getInt("ANNO"));
        }
        return vectorAnno;
    }

    private static void mostrarPersonal(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM PERSONAL");

        System.out.println("Personal del torneo");
        System.out.println("-------------------");

        while (rs.next()) {
            System.out.print(rs.getString("NOMBRE"));
            if(!rs.isLast()) {
            	System.out.print(", ");
            }
            else {
            	System.out.print(".");
            }

        }
    }

    
    public static void mostrarPersonalNoTrabaja(Connection conn) throws SQLException {
		Scanner scan = new Scanner(System.in);
        Statement st = conn.createStatement();
        int anno;

        mostrarPersonal(conn);

        System.out.println(">>>Introduce la edicion de la que obtener informacion del personal: ");
        anno = scan.nextInt();

		ArrayList<Integer> vectorEdiciones = annoEdiciones(conn);

        while (!vectorEdiciones.contains(anno)) {
        	System.out.println("\n>>>ERROR al introducir la edición, estas son las ediciones disponibles: ");
            mostrarEdiciones(conn);

           	System.out.print("\n>>>Pruebe a insertar otra edicion: ");
            anno = scan.nextInt();
        }

        ResultSet rs = st.executeQuery("SELECT * FROM PERSONAL WHERE EXISTS(SELECT trabaja.idPersonal FROM TRABAJA WHERE trabaja.idPersonal = Personal.idPersonal AND trabaja.anno ="+anno+ ")");
        System.out.println("\nEl personal que trabajo en la edicion " + anno + " fue: ");																															                                     
        while (rs.next()) {
            System.out.println(
					rs.getString("NOMBRE") + " " + rs.getString("APELLIDOS") +
					", con idPersonal " + rs.getString("IDPERSONAL") + ", correo -> " + rs.getString("CORREO") + " y nº de telefono: " + 
                    rs.getString("TELEFONO"));
    	}
		
		System.out.println("-------------------------------------------------------------------");

        rs = st.executeQuery("SELECT * FROM PERSONAL WHERE NOT EXISTS(SELECT trabaja.idPersonal FROM TRABAJA WHERE trabaja.idPersonal = Personal.idPersonal AND trabaja.anno ="+anno+ ")");
        System.out.println("\nEl personal que no trabajo en la edicion " + anno + " fue: ");																															                                     
        while (rs.next()) {
            System.out.println(
					rs.getString("NOMBRE") + " " + rs.getString("APELLIDOS") +
					", con idPersonal " + rs.getString("IDPERSONAL") + ", correo -> " + rs.getString("CORREO") + " y nº de telefono: " + 
                    rs.getString("TELEFONO"));
    	}    
	}
}
