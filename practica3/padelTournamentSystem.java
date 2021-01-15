/*
    Titulo: DDSI Practica 3
    Fecha: Diciembre 2020
    Autores:    Gomez Morales, Eladia
                Lugli, Valentino Glauco
                Maldonado Correa, José
                Moyano Romero, Amanda
                Muela Cárdenas, Jesús
    Descripción: Implementación de parte del SI prototipado en las prácticas anteriores.
*/

// Librerias
import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.*;

// Clase base
public class padelTournamentSystem {



    // Función Main
    public static void main(String[] args) {
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
        try {
            conn = DriverManager.getConnection("jdbc:oracle:thin:@//oracle0.ugr.es:1521/practbd.oracle0.ugr.es", user,
                    pass);

            if (conn != null) {
                System.out.println(">CONEXION BASE DE DATOS: ABIERTA");

            } else {
                System.out.println(">CONEXION BASE DE DATOS: ERROR");
            }


            // Menu principal
            while (running) {
                System.out.println( "\n---PADEL TOURNAMENT SYSTEM---\n"+"Menú:\n" + "1 - Asignar un ranking a una pareja de jugadores en una edición\n" + 
                                    "2 - Añadir Entradas a Compra\n" + "3 - Registrar entidad como patrocinadora en una edición con una cantidad de dinero\n" + 
                                    "4 - Mostrar el personal que no trabaja en una edición\n" + "5 - Realizar un pedido de materiales\n" +
                                    "0 - Cerrar Conexión de Base Datos y Salir");

       

                selection = scan.nextInt();

                System.out.println("\n");

                switch (selection) {
                    case 1:
                       jugadoresEntrenadores jE = new jugadoresEntrenadores();
                       jE.annadirRankingAPareja(conn);

                    break;

                    case 2:
                        usuariosEntradas uE = new usuariosEntradas();
                        uE.annadirEntradasACompra(conn);
            
                    break;

                    case 3:
                        patrocinadoresColaboradores pC = new patrocinadoresColaboradores();
                        pC.registrarEntidadPatrocinadora(conn);
                    
                    break;

                    case 4:
                    	personalHorarios pH = new personalHorarios();
						pH.mostrarPersonalNoTrabaja(conn);
                    break;

                    case 5:
                       	pedidosMateriales pM = new pedidosMateriales();
                        pM.pedirMateriales(conn);
                    
                    break;

                    case 0:
                        running = false;
                        conn.close();
                        System.out.println(">CONEXION BASE DE DATOS: CERRADA");
                    break;


                }
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
