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
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

import javax.lang.model.util.ElementScanner6;
import javax.management.AttributeValueExp;

import jdk.nashorn.api.tree.WhileLoopTree;

import java.util.ArrayList;
import java.lang.*; 

class Ticket
{
    public static int ID;
    public static int cantidad;
    public static float costo;

    public Ticket(int _ID, int _cantidad, float _costo)
    {
        ID = _ID;
        cantidad = _cantidad;
        costo = _costo;
    }
}

public class usuariosEntradas 
{
    //      METODOS AUXILIARES

    /**
     * @param conn Objeto que proporciona el vínculo entre la base de datos y la
     *             aplicación en java
     * @return      
     * @brief Muestra todas las Ediciones registradas
     */
    private static ArrayList<Integer> mostrarEdiciones(Connection conn) throws SQLException 
    {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM EDICION");

        ArrayList<Integer> vectorAnno = new ArrayList<>();
        System.out.println("\n--EDICIONES--");
        while (rs.next()) {
            System.out.println("  Año: " + rs.getString("ANNO"));
            vectorAnno.add(rs.getInt("ANNO"));
        }
        return vectorAnno;
    }

    /**
     * 
     * @param conn
     * @param anno
     * @throws SQLException
     */
    private static ArrayList<Integer> obtenerIDCompras(Connection conn, int anno) throws SQLException
    {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM COMPRA_REALIZA_INICIA WHERE ANNOEDICION="+anno);

        ArrayList<Integer> vectorIDCompras = new ArrayList<>();

        System.out.println("\n--COMPRAS INICIADAS--");
        while(rs.next())
        {
            System.out.println("  ID de Compra: " + rs.getString("IDCOMPRA") + "\tFecha de Inicio: " + rs.getString("FECHAINICIO") 
                                + "\tID del Usuario: " + rs.getString("IDUSUARIO") );
            vectorIDCompras.add(rs.getInt("IDCOMPRA"));
        }
        return vectorIDCompras;
    }

    /**
     * 
     * @param conn
     * @param anno
     * @throws SQLException
     */
    private static ArrayList<Ticket> obtenerEntradas(Connection conn, int anno) throws SQLException
    {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM TIENE WHERE ANNO="+anno);
        ArrayList<Ticket> vectorEntradas = new ArrayList<Ticket>();

        while(rs.next())
        {
            vectorEntradas.add( new Ticket(rs.getInt("IDENTRADA"), rs.getInt("CANTIDAD"), rs.getFloat("PRECIO")));
        }
        return vectorEntradas;
    }

    private static void mostrarTipoEntradas(ArrayList<Ticket> entradas)
    {
        for(int i=0; i < entradas.size(); i++)
        {
            System.out.println(" Entrada Tipo: "+entradas.get(i).ID+"\tCantidad: "+entradas.get(i).cantidad+"\tPrecio: "+entradas.get(i).costo+"€");
        }
    }

    private static void mostrarEntradasAnnadidas(ArrayList<Ticket> vectorEntradas, Map<Integer, Integer> vectorFin)
    {
        float suma = 0;
        System.out.println("\n--RESUMEN DE COMPRA--");
        for(int i=0; i<vectorEntradas.size(); i++)
        {
            if(vectorFin.get(vectorEntradas.get(i).ID) > 0)
            {
                System.out.println(" Entrada Tipo: "+vectorEntradas.get(i).ID+"\tCantidad: "+vectorFin.get(vectorEntradas.get(i).ID));
                suma += (vectorEntradas.get(i).costo * vectorFin.get(vectorEntradas.get(i).ID));
            }
        }
        System.out.println("\t------------------------------");
        System.out.println("\tTotal a pagar: "+suma+"€");
    }
    /**   METODO PRINCIPAL
     * 
     * @param conn
     * @throws SQLException
     */
    public static void annadirEntradasACompra(Connection conn) throws SQLException
    {
        Scanner scan = new Scanner(System.in);
        int actIDCompra = 0, actAnno = 0, actIDEntrada = 0, cantidadCompra = 0, finCompra = 0;
        boolean enCompra = true, enBucle, enFinCompra = true;


        // DATOS PREVIOS
        // Se capturan los datos previos necesarios para realizar el subsistema como tal.
        System.out.println(">>DATOS PREVIOS");

        //      Mostrar Ediciones 
        enBucle = true;
        ArrayList<Integer> ediciones;
        while(enBucle)
        {
            ediciones = mostrarEdiciones(conn);        
            System.out.print(">>>Seleccionar Edición: ");
            actAnno = scan.nextInt();

            if(ediciones.contains(actAnno))
            {
                enBucle = false;
            }
            else
            {
                System.out.print(">>>ERROR: Edición incorrecta. Vuelva a intentar.\n");
            }
        }

        //      Seleccionar un ID de Compra que esté en la Edición elegida.
        enBucle = true;
        ArrayList<Integer> idCompras;
        while(enBucle)
        {
            idCompras = obtenerIDCompras(conn, actAnno);
            System.out.print(">>>Seleccionar ID Compra: ");
            actIDCompra = scan.nextInt();

            if(idCompras.contains(actIDCompra))
            {
                enBucle = false;
            }
            else
            {
                System.out.print(">>>ERROR: ID de Compra Incorrecta. Vuelva a intentar.\n");
            }
        }

        //      INICIO SUBSISTEMA
        System.out.print("\n>>SUBSISTEMA 2 - USUARIOS / ENTRADAS\n" +
                         ">>Añadir Entradas a una compra\n\n");

        //      Seleccionar tipo Entrada y Cantidad
        int indiceEntrada = -1, statusCompra = 0;
        ArrayList<Ticket>  vectorEntradas = obtenerEntradas(conn, actAnno);
        Map<Integer, Integer> vectorFin =  new HashMap<>();
        
        for(int i=0; i < vectorEntradas.size(); i++)
        {
            vectorFin.put(vectorEntradas.get(i).ID, 0);
        }

        while(enCompra)
        {
            enBucle = true;
            while(enBucle)
            {
                mostrarTipoEntradas(vectorEntradas);
                System.out.print(">>>Seleccionar Tipo Entrada: ");
                actIDEntrada = scan.nextInt();
    
                for(int i=0; i < vectorEntradas.size(); i++)
                {
                    if(actIDEntrada == vectorEntradas.get(i).ID)
                    {
                        enBucle = false;
                        indiceEntrada = i;
                    }
                }
    
                if(enBucle)
                {
                    System.out.print(">>>ERROR: ID de Entrada incorrecto. Vuelva a intentar.\n");
                }
            }

            System.out.print(">>>Seleccionar Cantidad: ");
            cantidadCompra = scan.nextInt();
    
            if(cantidadCompra > 0)
            {
                if(cantidadCompra <= vectorEntradas.get(indiceEntrada).cantidad)
                {
                    vectorEntradas.get(indiceEntrada).cantidad = vectorEntradas.get(indiceEntrada).cantidad - cantidadCompra;
                    vectorFin.replace(actIDEntrada, (vectorFin.get(actIDEntrada)) + cantidadCompra);
                }
                else
                {
                    System.out.println(">>>ERROR: Cantidad de entradas ("+cantidadCompra+") excede la cantidad disponible("+vectorEntradas.get(indiceEntrada).cantidad+").");
                }
            }
            else
            {
                System.out.println(">>>ERROR: Cantidad de entradas tiene que ser mayor que 0.");
            }
    
            enBucle = true;
            while(enBucle)
            {
                System.out.print("¿Finalizar adición de entradas? [1 - Sí | 0 - No]: ");
                statusCompra = scan.nextInt();
                if(statusCompra == 0 || statusCompra == 1)
                {
                    enBucle = false;
                    if(statusCompra == 1)
                    {
                        enCompra = false;
                    }
                }
                else
                {
                    System.out.println(">>>ERROR: Selección incorrecta.");
                }
            }
        }
        
        mostrarEntradasAnnadidas(vectorEntradas, vectorFin);
        //      Finalizar y "pagar", añadir más entradas o cancelar.

        conn.setAutoCommit(false);
        Savepoint entradasNoAnnadidas = conn.setSavepoint();

        while (enFinCompra)
        {
            System.out.print(">>>¿Desea finalizar la compra? [1 - Si, finalizar | 0 - No, cancelar]: ");
            finCompra = scan.nextInt();
        
            if(finCompra == 0)
            {
                // Si se cancela, hacer rollback
                conn.rollback(entradasNoAnnadidas);
                System.out.print(">>>COMPRA CANCELADA\n\n");
                enFinCompra = false;
            }
            else if(finCompra == 1)
            {
                Statement st = conn.createStatement();
                for(int i=0; i < vectorEntradas.size(); i++)
                {
                    if(vectorFin.get(vectorEntradas.get(i).ID) > 0)
                    {
                        st.executeUpdate("INSERT INTO ANADE VALUES ("+actAnno+", "+actIDCompra+", "+vectorEntradas.get(i).ID+", "+vectorFin.get(vectorEntradas.get(i).ID)+")");
                        st.executeUpdate("UPDATE TIENE SET CANTIDAD="+vectorFin.get(vectorEntradas.get(i).ID)+" WHERE IDENTRADA="+actIDEntrada+" AND ANNO="+actAnno);
                    }
                }

                st.executeUpdate("INSERT INTO COMPRA_FINALIZADA VALUES ("+actIDCompra+", SYSDATE)");
                // Fin transacción
                conn.commit();
                conn.setAutoCommit(true);
                System.out.print(">>>COMPRA FINALIZADA\n\n");
                enFinCompra = false;
            }
            else
            {
                System.out.println(">>>Selección Incorrecta. Vuelva a Intentar.");
            }
        }
    }
}