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
                                + " Cantidad: " + rs.getString("FECHAPEDIDO") );
        }
    }

    // Metodos Publicos
    public static void annadirEntradasACompra(Connection conn) throws SQLException
    {
        Scanner scan = new Scanner(System.in);
        int idCompra, anno, idEntrada, cantidad;

        System.out.print(">>SUBSISTEMA 2 - USUARIOS / ENTRADAS\n" +
                         ">>Añadir Entradas a una compra\n\n");
        
        // Chequear que las tablas existan

        // Inicio de Transacción
        conn.setAutoCommit(false);
        Savepoint entradasNoAnnadidas = conn.setSavepoint();
        //      Obtener Entradas en Edicion en Particular

        //      Mostrar ediciones existentes
        // padelTournamentSystem.mostrarEdicion(conn);

        //  Elegir edicion en particular
            System.out.print(">>Seleccionar Edicion: ");
            anno = scan.nextInt();

        //  Seleccionar tipo Entrada y Cantidad
            mostrarTipoEntradas(conn, anno);
            System.out.print(">>Seleccionar Tipo Entrada: ");
            idEntrada = scan.nextInt();

            System.out.print(">>Seleccionar Cantidad: ");
            cantidad = scan.nextInt();

        //  Finalizar y "pagar", añadir más entradas o cancelar.
            Statement st = conn.createStatement();
            st.executeUpdate("INSERT INTO ANADE VALUES ("+idCompra+", "+anno+", "+idEntrada+", "+cantidad+")");
            st.executeUpdate("UPDATE TIENE SET CANTIDAD="+cantidad-oldCantidad+" WHERE IDENTRADA="+idEntrada+" AND ANNO="+anno);

        //      Si se cancela, hacer rollback
        conn.rollback(entradasNoAnnadidas);

        //      Sino, realizar cambios en tablas.
        st.executeUpdate("INSERT INTO COMPRA_FINALIZADA VALUES ("+idCompra+", SYSDATE)");
        conn.commit();
        
        // Fin transacción
        conn.setAutoCommit(true);

        // Contener excepción si ocurre, puede ser que se genere por el disparador.
    }
}