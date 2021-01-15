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
    
    public static void mostrarPersonalNoTrabaja(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        int anno;
        System.out.println(">>>Introduce el anno del que obtener el personal que no trabaja: ");
        anno = scan.nextInt();
        ResultSet rs = ("SELECT * FROM PERSONAL WHERE NOT EXISTS(SELECT trabaja.idPersonal FROM TRABAJA WHERE trabaja.idPersonal = Personal.idPersonal AND trabaja.anno ="+anno+ ")");																															                                     
        while (rs.next()) {
            System.out.println(
                    "\nID Personal:" + rs.getString("IDPERSONAL") + " Correo: " + rs.getString("CORREO")
                            + " Nombre: " + rs.getString("NOMBRE") + " Apellidos: "
                            + rs.getString("APELLIDOS") + " Numero de Telefono: " + rs.getString("TELEFONO"));
        }
    }    
}
