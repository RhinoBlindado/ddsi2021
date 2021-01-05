/*
    Titulo:         Subsistema 2 - Usuarios / Entradas
    Fecha:          Diciembre 2020
    Autor:          Valentino Lugli
    Descripción:    Implementación de parte del Subsistema 2 - Usuarios / Entradas, la funcionalidad de Añadir Entradas a Compra
*/

// Librerias
import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.lang.model.util.ElementScanner6;

import java.util.ArrayList;
import java.lang.*; 

// Clase Base
public class usuariosEntradas 
{
    // Metodos Privados
    private static void mostrarTipoEntradas(Connection conn, int anno) throws SQLException
    {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM TIENE WHERE ANNO="+anno);

        while(rs.next())
        {
            System.out.println("ID Entrada:" + rs.getString("IDENTRADA") + " Precio: " + rs.getString("PRECIO") 
                                + " Cantidad: " + rs.getString("CANTIDAD") );
        }
    }

    private static int obtenerCantidadEntrada(Connection conn, int idEntrada) throws SQLException
    {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT CANTIDAD FROM TIENE WHERE IDENTRADA="+idEntrada);

        rs.next();
        int cantidad = rs.getInt("Cantidad");
        rs.close();

        return cantidad;
    }

    private static void obtenerIDCompras(Connection conn, int anno) throws SQLException
    {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM COMPRA_REALIZA_INICIA WHERE ANNO="+anno);

        while(rs.next())
        {
            System.out.println("ID Compra:" + rs.getString("IDCOMPRA") + " Fecha: " + rs.getString("FECHAINICIO") 
                                + "ID Usuario: " + rs.getString("IDUSUARIO") );
        }
    }

    // Metodos Publicos
    public static void annadirEntradasACompra(Connection conn) throws SQLException
    {
        Scanner scan = new Scanner(System.in);
        int idCompra, anno, idEntrada, cantidad, finCompra = 0;
        bool enCompra;

        //  DATOS PREVIOS
        //      Elegir edicion en particular.
        // padelTournamentSystem.mostrarEdicion(conn);
        System.out.print(">>Seleccionar Edicion: ");
        anno = scan.nextInt();

        //      Elegir ID de la Compra.
        obtenerIDCompras(conn, anno);
        System.out.print(">>Seleccionar Compra: ");
        idCompra = scan.nextInt();

        // INICIO SUBSISTEMA
        System.out.print(">>SUBSISTEMA 2 - USUARIOS / ENTRADAS\n" +
                         ">>Añadir Entradas a una compra\n\n");
        
        //      Inicio de Transacción
        conn.setAutoCommit(false);
        Savepoint entradasNoAnnadidas = conn.setSavepoint();

        //      Seleccionar tipo Entrada y Cantidad
        mostrarTipoEntradas(conn, anno);
        System.out.print(">>Seleccionar Tipo Entrada: ");
        idEntrada = scan.nextInt();

        System.out.print(">>Seleccionar Cantidad: ");
        cantidad = scan.nextInt();

        //      Obtener cantidad original.
        int cantidadEntradas = obtenerCantidadEntrada(conn, idEntrada);

        //      Finalizar y "pagar", añadir más entradas o cancelar.
        if(cantidad <= cantidadEntradas)
        {
            Statement st = conn.createStatement();
            st.executeUpdate("INSERT INTO ANADE VALUES ("+idCompra+", "+anno+", "+idEntrada+", "+cantidad+")");
            st.executeUpdate("UPDATE TIENE SET CANTIDAD="+(cantidad-cantidadEntradas)+" WHERE IDENTRADA="+idEntrada+" AND ANNO="+anno);
        }
        else
        {
            System.out.printl(">>ERROR: Cantidad ingresada ("+cantidad+") es mayor que la cantidad de entradas disponibles ("+cantidadEntradas+").");
        }

        while (finCompra != 1)
        {
            System.out.print(">>¿Desea finalizar la compra? [1 - Si, 0 - No]: ");
            finCompra = scan.nextInt();
        
            if(finCompra == 0)
            {
                // Si se cancela, hacer rollback
                conn.rollback(entradasNoAnnadidas);
            }
            else if(finCompra == 1)
            {
                // Sino, realizar cambios en tablas.
                st.executeUpdate("INSERT INTO COMPRA_FINALIZADA VALUES ("+idCompra+", SYSDATE)");
                conn.commit();
    
                // Fin transacción
                conn.setAutoCommit(true);
            }
            else
            {
                System.out.println(">>Selección Incorrecta. Vuelva a Intentar.");
            }
        }
    }

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

            annadirEntradasACompra(conn);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
        }
    }
}