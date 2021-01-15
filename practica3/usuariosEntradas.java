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
import java.util.ArrayList;
import java.lang.*; 


/**
 * 
 */
class Ticket
{
    public  int ID;
    public  int cantidad;
    public  float costo;

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
     * @brief       Muestra por pantalla las ediciones y las añade una lista.
     * @throws      SQLException - Excepción de SQL
     * @param conn  Objeto que proporciona el vínculo entre la base de datos y la
     *              aplicación en java
     * @return      Lista de Enteros conteniendo los años de las ediciones.    
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
     * @brief       Muestra por pantalla los compras iniciadas y añadir el ID a la lista.
     * @param conn  Objeto que proporciona el vínculo entre la base de datos y la
     *              aplicación en java
     * @param anno  Año de la edición
     * @throws      SQLException
     */
    private static ArrayList<Integer> obtenerIDCompras(Connection conn, int anno) throws SQLException
    {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM COMPRA_REALIZA_INICIA WHERE ANNOEDICION="+ anno +
                                       " AND IDCOMPRA NOT IN (SELECT IDCOMPRA FROM COMPRA_FINALIZADA)");

        ArrayList<Integer> vectorIDCompras = new ArrayList<>();

        System.out.println("\n--COMPRAS INICIADAS SIN FINALIZAR--");
        while(rs.next())
        {
            System.out.println("  ID de Compra: " + rs.getString("IDCOMPRA") + "\tFecha de Inicio: " + rs.getString("FECHAINICIO") 
                                + "\tID del Usuario: " + rs.getString("IDUSUARIO") );
            vectorIDCompras.add(rs.getInt("IDCOMPRA"));
        }
        return vectorIDCompras;
    }

    /**
     * @brief       Obtener las Entradas de la Base de Datos e insertarlas en una clase auxiliar Ticket para 
     *              manejar la adición de entradas.
     * @param conn  Objeto que proporciona el vínculo entre la base de datos y la
     *              aplicación en java.
     * @param anno  Año de la edición.
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

    /**
     * @brief           Mostrar pon pantalla los detalles de las Entradas por medio de la lista de objetos Ticket
     * @param entradas  Lista de objetos tipo Ticket
     */
    private static void mostrarTipoEntradas(ArrayList<Ticket> entradas)
    {
        for(int i=0; i < entradas.size(); i++)
        {
            System.out.println(" Entrada Tipo: "+entradas.get(i).ID+"\tCantidad: "+entradas.get(i).cantidad+"\tPrecio: "+entradas.get(i).costo+"€");
        }
    }

    /**
     * @brief                   Mostrar el resumen de las entradas añadidas para finalizarse.
     * @param vectorEntradas    Lista de objetos tipo Ticket
     * @param vectorFin         Mapa que almacena cuantas entradas se han añadido de cada tipo.
     */
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
     * @brief               Para una compra ya iniciada, añadirle entradas para luego marcarla como finalizada para que se pueda comprar.
     * @param conn          Objeto que proporciona el vínculo entre la base de datos y la
     *                      aplicación en java.
     * @throws SQLException
     */
    public static void annadirEntradasACompra(Connection conn) throws SQLException
    {
        Scanner scan = new Scanner(System.in);
        int actIDCompra = 0, actAnno = 0, actIDEntrada = 0, cantidadCompra = 0, finCompra = 0;
        boolean enCompra = true, enBucle, enFinCompra = true;
        Statement st = conn.createStatement();


        // DATOS PREVIOS
        //      Se capturan los datos previos necesarios para realizar el subsistema como tal.
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

            if(!idCompras.isEmpty())
            {
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
            else
            {
                System.out.print(">>>AVISO: No hay compras iniciadas.\n");
                enBucle = false;
                enCompra = false;
                enFinCompra = false;
            }
        }

        // INICIO SUBSISTEMA
        System.out.print("\n>>SUBSISTEMA 2 - USUARIOS / ENTRADAS\n" +
                         ">>Añadir Entradas a una compra\n\n");

        int indiceEntrada = -1, statusCompra = 0;
        ArrayList<Ticket>  vectorEntradas = obtenerEntradas(conn, actAnno);
        Map<Integer, Integer> vectorFin =  new HashMap<>();
        
        //      Inicializar el Mapa para que tenga dentro los IDs de los tipos de Entradas a cero.
        for(int i=0; i < vectorEntradas.size(); i++)
        {
            vectorFin.put(vectorEntradas.get(i).ID, 0);
        }

        //      Comenzar el proceso de compra como tal.
        while(enCompra)
        {
            enBucle = true;
            //  Seleccionar entradas de las disponibles.
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
        
        //  Mostrar los cambios a realizarse, crear un savepoint dado que se va a realizar una transacción.
        conn.setAutoCommit(false);
        Savepoint entradasNoAnnadidas = conn.setSavepoint();

        while (enFinCompra)
        {
            mostrarEntradasAnnadidas(vectorEntradas, vectorFin);
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
                // Si se acepta, realizar los cambios pertinentes y luego realizar commit.
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
                System.out.print(">>>COMPRA FINALIZADA\n\n");
                enFinCompra = false;
            }
            else
            {
                System.out.println(">>>Selección Incorrecta. Vuelva a Intentar.");
            }
        }
        conn.setAutoCommit(true);
    }
}