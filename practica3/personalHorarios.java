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
    
    private static void mostrarPersonalNoTrabaja(Connection conn, int anno) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = ("SELECT * FROM PERSONAL WHERE NOT EXISTS( 
                       SELECT trabaja.idPersonal FROM TRABAJA 
                       WHERE trabaja.idPersonal = Personal.idPersonal 
                       AND trabaja.anno ="+anno );

                                       
        while (rs.next()) {
            System.out.println(
                    "\nID Personal:" + rs.getString("IDPERSONAL") + " Correo: " + rs.getString("CORREO")
                            + " Nombre: " + rs.getString("NOMBRE") + " Apellidos: "
                            + rs.getString("APELLIDOS") + " Numero de Telefono: " + rs.getString("TELEFONO"));
        }
    }

// SELECT * FROM PERSONAL WHERE NOT EXISTS( 
// SELECT trabaja.idPersonal FROM TRABAJA WHERE trabaja.idPersonal = Personal.idPersonal);

    
    public static void main(String[] args) {
        boolean running = true;
        Scanner scan = new Scanner(System.in);
        int selection;

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

            mostrarPersonalNoTrabaja(conn);
            conn.close();

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
        }
    }
}
