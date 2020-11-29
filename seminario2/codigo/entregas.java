/*
    Titulo: DDSI Seminario 2
    Fecha: Noviembre 2020
    Autores:    Gomez Morales, Eladia
                Lugli, Valentino Glauco
                Maldonado Correa, José
                Moyano Romero, Amanda
                Muela Cárdenas, Jesús
    Descripción: Pequeño SI que se conecta a una base de datos para realizar operaciones.
*/

// Librerias
import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.*;

// Clase base
public class entregas {


    //static Integer iDPedido = 0;.

    public static void cancelarPedido(Connection conn, int iDPedido) throws SQLException
    {
        Statement st = conn.createStatement();
        st.executeUpdate("DELETE FROM PEDIDO WHERE CPEDIDO="+iDPedido);
    }

    public static int insertarPedido(Connection conn) throws SQLException 
    {
        Scanner datosPedido = new Scanner(System.in);
        boolean datosExistentes = true;
        int iDPedido = -1;
        while(datosExistentes)
        {
            try
            {
                System.out.println(">> ID PEDIDO:");
                iDPedido = datosPedido.nextInt();

                System.out.println(">> ID CLIENTE:");
                int iDCliente = datosPedido.nextInt();

                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO PEDIDO VALUES (" + iDPedido + ", " + iDCliente +", SYSDATE)");

                datosExistentes = false;
                System.out.println(">> PEDIDO CREADO");
            }
            catch (Exception e)
            {
                System.out.println(">> PEDIDO: ERROR - ID PEDIDO YA EN USO");
            }
        }
        return iDPedido;

    }

    public static void insertarDatosStock(Connection conn) throws SQLException
    {
      /*  File datos = new File("./src/datosStock.txt");
        Scanner scan = new Scanner(datos);
        String aux;
        String[] entrada;

        while(scan.hasNextLine())
        {
            aux = scan.nextLine();
            entrada = aux.split(' ')
            System.out.println(entrada[0]+" "+entrada[1]);
        }
        scan.close();
        */
        Statement st = conn.createStatement();

        // insert the data
        st.executeUpdate("INSERT INTO STOCK VALUES (01, 10)");
        st.executeUpdate("INSERT INTO STOCK VALUES (02, 20)");
        st.executeUpdate("INSERT INTO STOCK VALUES (03, 15)");
        st.executeUpdate("INSERT INTO STOCK VALUES (04, 7)");
        st.executeUpdate("INSERT INTO STOCK VALUES (05, 100)");
        st.executeUpdate("INSERT INTO STOCK VALUES (99, 124)");
        st.executeUpdate("INSERT INTO STOCK VALUES (06, 27)");
        st.executeUpdate("INSERT INTO STOCK VALUES (42, 42)");
        st.executeUpdate("INSERT INTO STOCK VALUES (12, 21)");
        st.executeUpdate("INSERT INTO STOCK VALUES (66, 99)");

    }

    public static boolean existeTabla(Connection conn, String tabla) throws SQLException {

        boolean existe = false;
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, tabla, null);

        if (tables.next()) {
            System.out.println("Existe " + tabla);
            existe = true;
        } else {
            System.out.println("no Existe " + tabla);
            existe = false;
        }

      //  System.out.println(tables.getString(1));

        tables.close();
        return existe;
    }

    public static void borrarTabla(Connection conn, String tabla) throws SQLException {
        PreparedStatement st = null;
        st = conn.prepareStatement("DROP TABLE " + tabla);
        st.executeUpdate();
    }

    public static void crearTablas(Connection conn) throws SQLException {
        PreparedStatement st = null;
        st = conn.prepareStatement("CREATE TABLE Stock(CProducto INTEGER PRIMARY KEY, Cantidad INTEGER)");
        st.executeUpdate();

        st = conn.prepareStatement(
                "CREATE TABLE Pedido(CPedido INTEGER PRIMARY KEY, CCliente INTEGER, FechaPedido DATE)");
        st.executeUpdate();

        st = conn.prepareStatement(
                "CREATE TABLE DetallePedido("+
                    "CProducto REFERENCES Stock(CProducto),"+ 
                    "CPedido REFERENCES Pedido(CPedido) ON DELETE CASCADE,"+ 
                    "Cantidad INTEGER,"+ 
                    "PRIMARY KEY (CProducto, CPedido)"+
                ")");

        st.executeUpdate();
    }

    // Funcion: INSERTAR DETALLE PEDIDO
    public static void insertarDetalleP(Connection conn, int iDActual) throws SQLException
    {
        Scanner datosDetalle = new Scanner(System.in);

        System.out.println(">> ID ARTÍCULO");
        int idStock = datosDetalle.nextInt();

        System.out.println(">> CANTIDAD:");
        int cantidadDetalle = datosDetalle.nextInt();

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT CANTIDAD FROM STOCK WHERE CPRODUCTO="+idStock);

        rs.next();
        int cantidadStock = rs.getInt("Cantidad");
        rs.close();


        if(cantidadStock >= cantidadDetalle)
        {
            st.executeUpdate("UPDATE STOCK SET CANTIDAD="+(cantidadStock-cantidadDetalle)+" WHERE CPRODUCTO="+idStock);
            st.executeUpdate("INSERT INTO DETALLEPEDIDO VALUES("+iDActual+", "+idStock+", "+cantidadDetalle+")");
        }
        else
        {
            System.out.println(">> ERROR: CANTIDAD EN STOCK INSUFICIENTE, HAY "+cantidadStock+" DEL ARTICULO "+idStock);
        }

    }

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

            /*
             * PreparedStatement sql = null; sql = conn.
             * prepareStatement("SELECT table_name FROM user_tables where table_name='Stock'"
             * );
             * 
             * ResultSet whatever; whatever = sql.executeQuery();
             * System.out.println("STOCK:" + whatever.getString(1));
             * 
             * sql = conn.
             * prepareStatement("SELECT table_name FROM user_tables where table_name='TEST1'"
             * ); whatever = sql.executeQuery(); System.out.println("TEST: " +
             * whatever.getString(1));
             */

            boolean existeStock = false;
            boolean existePedido = false;
            boolean existeDetallePedido = false;

            // Menu principal
            while (running) {
                System.out.println( "---SISTEMA GUAPO DE INFORMACIÓN---\n"+"Menú:\n" + "1 - Inicialización de Tablas\n" + 
                                    "2 - Dar Alta Nuevo Pedido\n" + "3 - Borrar un pedido\n" + 
                                    "0 - Cerrar Conexión de Base Datos y Salir");

                existePedido = existeTabla(conn, "PEDIDO");
                existeStock = existeTabla(conn, "STOCK");
                existeDetallePedido = existeTabla(conn, "DETALLEPEDIDO");

                selection = scan.nextInt();

                // Comprueba que las tablas estén creadas

                switch (selection) {
                    case 1:
                        System.out.println(">>INICIALIZANDO TABLAS...");

                        if (existeDetallePedido) {
                            borrarTabla(conn, "DETALLEPEDIDO");
                            System.out.println("Se ha borrado la tabla DetallePedido");
                        }
                        if (existeStock) {
                            borrarTabla(conn, "STOCK");
                            System.out.println("Se ha borrado la tabla Stock");

                        }
                        if (existePedido) {
                            borrarTabla(conn, "PEDIDO");
                            System.out.println("Se ha borrado la tabla Pedido");
                        }

                        crearTablas(conn);
                        System.out.println("Se han creado las tablas");
                        insertarDatosStock(conn);
                        System.out.println(">>INICIALIZANDO TABLAS: OK");

                    break;

                    case 2:
                        System.out.println(">>DAR ALTA NUEVO PEDIDO");

                        int iDActual = insertarPedido(conn);

                        boolean pedidoAbierto = true;
                        int subSelect;
                        System.out.println("ID COSA:"+iDActual+" PRUEBA RESTA"+(iDActual*2));
                        while(pedidoAbierto)
                        {
                            System.out.println( "Menú:\n" + "1 - Añadir Detalle de Producto\n" + 
                            "2 - Eliminar todos los Detalles de Producto\n" + "3 - Cancelar Pedido\n" + 
                            "4 - Finalizar Pedido");

                            subSelect = scan.nextInt();
                            
                            switch(subSelect)
                            {
                                case 1:
                                    insertarDetalleP(conn, iDActual);
                                break;

                                case 2:

                                break;

                                case 3:
                                    cancelarPedido(conn, iDActual);
                                break;

                                case 4:
                                    pedidoAbierto = false;
                                break;
                            }
                        }

                    break;

                    case 3:
                        System.out.println("3");
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

        /*
         * try (Connection conn = DriverManager.getConnection(
         * "jdbc:oracle:thin:@//oracle0.ugr.es:1521/practbd.oracle0.ugr.es",
         * list.get(0), list.get(1))) {
         * 
         * if (conn != null) { System.out.println("Connected to the database!");
         * 
         * PreparedStatement st = null;
         * 
         * st = conn.prepareStatement("CREATE TABLE test1(hola INTEGER)");
         * st.executeUpdate(); conn.close();
         * 
         * } else { System.out.println("Failed to make connection!"); }
         * 
         * } catch (SQLException e) { System.err.format("SQL State: %s\n%s\n",
         * e.getSQLState(), e.getMessage()); } catch (Exception e) {
         * e.printStackTrace(); }
         */

    }
}
