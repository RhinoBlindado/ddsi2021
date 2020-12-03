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
import java.sql.*;e
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.*;

// Clase base
public class entregas {

    public static void borrarPedido(Connection conn, int iDPedido) throws SQLException
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
            System.out.println(">>>Pedidos existentes:");
            mostrarPedidos(conn);

            System.out.println(">>>Introduce nuevo pedido:");
            try
            {
                System.out.println(">>>ID Pedido:");
                iDPedido = datosPedido.nextInt();

                System.out.println(">>>ID Cliente:");
                int iDCliente = datosPedido.nextInt();

                Statement st = conn.createStatement();
                st.executeUpdate("INSERT INTO PEDIDO VALUES (" + iDPedido + ", " + iDCliente +", SYSDATE)");

                datosExistentes = false;
                System.out.println(">>>PEDIDO CREADO");
            }
            catch (Exception e)
            {
                System.out.println(">>>ERROR: ID Pedido en uso, introduce otro.");
            }
        }
        return iDPedido;
    }

    public static void insertarDatosStock(Connection conn) throws SQLException
    {
        Statement st = conn.createStatement();

        // insert the data :c
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

    public static boolean existeTabla(Connection conn, String tabla, String user) throws SQLException {

        boolean existe = false;
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, user.toUpperCase(), tabla, null);

        if (tables.next()) {
            existe = true;
        } else {
            existe = false;
        }
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


        System.out.println(">>>Productos existentes:");
        mostrarStock(conn);

        System.out.println(">>>Inserta detalle del producto:");
        System.out.println(">>>ID Producto");
        int idStock = datosDetalle.nextInt();

        System.out.println(">>>Cantidad de producto:");
        int cantidadDetalle = datosDetalle.nextInt();

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT CANTIDAD FROM STOCK WHERE CPRODUCTO="+idStock);

        rs.next();
        int cantidadStock = rs.getInt("Cantidad");
        rs.close();

        

        if(cantidadStock >= cantidadDetalle)
        {
            st.executeUpdate("UPDATE STOCK SET CANTIDAD="+(cantidadStock-cantidadDetalle)+" WHERE CPRODUCTO="+idStock);
            st.executeUpdate("INSERT INTO DETALLEPEDIDO VALUES("+idStock+", "+iDActual+", "+cantidadDetalle+")");
            System.out.println(">>>DETALLES INSERTADOS: OK");
        }
        else
        {
            System.out.println(">>>ERROR: CANTIDAD EN STOCK INSUFICIENTE, HAY "+cantidadStock+" DEL ARTICULO "+idStock);
        }

    }


    public static void mostrarPedidos(Connection conn) throws SQLException
    {

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM PEDIDO");

        while(rs.next()){
            System.out.println("Código pedido: " + rs.getString("CPEDIDO") + " - Cliente: " + rs.getString("CCLIENTE") 
                                + " - Fecha pedido: " + rs.getString("FECHAPEDIDO") );

        }

    }

    public static void mostrarDetallesPedidos(Connection conn) throws SQLException
    {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM DETALLEPEDIDO");

        while(rs.next()){
            System.out.println("Código producto: " + rs.getString("CPRODUCTO") + " - Código pedido: " + rs.getString("CPEDIDO") 
                                + " - Cantidad: " + rs.getString("CANTIDAD") );

        }

    }

    public static void mostrarStock(Connection conn) throws SQLException
    {

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM STOCK");

        while(rs.next())
        {
            System.out.println("Código producto: " + rs.getString("CPRODUCTO") + " - Cantinad: " + rs.getString("CANTIDAD"));
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

            boolean existeStock = false;
            boolean existePedido = false;
            boolean existeDetallePedido = false;

            // Menu principal
            while (running) {
                System.out.println( "\n---SISTEMA GUAPO DE INFORMACIÓN---\n"+"Menú:\n" + "1 - Inicialización de Tablas\n" + 
                                    "2 - Dar Alta Nuevo Pedido\n" + "3 - Borrar un pedido\n" + 
                                    "0 - Cerrar Conexión de Base Datos y Salir");

                existePedido = existeTabla(conn, "PEDIDO", user);
                existeStock = existeTabla(conn, "STOCK", user);
                existeDetallePedido = existeTabla(conn, "DETALLEPEDIDO", user);

                selection = scan.nextInt();

                System.out.println("\n");

                switch (selection) {
                    case 1:
                        System.out.println(">>INICIALIZANDO TABLAS...");
                       
                        if (existeDetallePedido) {
                            borrarTabla(conn, "DETALLEPEDIDO");
                            System.out.println(">>>Se ha borrado la tabla DetallePedido");
                        }
                        if (existeStock) {
                            borrarTabla(conn, "STOCK");
                            System.out.println(">>>Se ha borrado la tabla Stock");

                        }
                        if (existePedido) {
                            borrarTabla(conn, "PEDIDO");
                            System.out.println(">>>Se ha borrado la tabla Pedido");
                        }

                        crearTablas(conn);
                        System.out.println(">>>Se han creado las tablas");
                        insertarDatosStock(conn);
                        System.out.println(">>INICIALIZANDO TABLAS: OK \n");
                    break;

                    case 2:
                        System.out.println(">>DAR ALTA NUEVO PEDIDO");
                        if(existeStock && existePedido && existeDetallePedido)
                        {
                            conn.setAutoCommit(false);
                        
                            Savepoint noPedido = conn.setSavepoint();
    
                            int iDActual = insertarPedido(conn);
                            boolean pedidoAbierto = true;
                            int subSelect;
    
                            Savepoint sinDetallePedido = conn.setSavepoint();
    
                            while(pedidoAbierto)
                            {
                                System.out.println( "Submenú Dar Alta Nuevo Pedido:\n" + "1 - Añadir Detalle de Producto\n" + 
                                "2 - Eliminar todos los Detalles de Producto\n" + "3 - Cancelar Pedido\n" + 
                                "4 - Finalizar Pedido");
    
                                subSelect = scan.nextInt();
                                
                                switch(subSelect)
                                {
                                    case 1:
                                        System.out.println(">>AÑADIR DETALLE DE PRODUCTO");
                                        insertarDetalleP(conn, iDActual);
                                    break;
    
                                    case 2:
                                        System.out.println(">>DETALLE DE PRODUCTO DE PEDIDO ACTUAL ELIMINADO");
                                        conn.rollback(sinDetallePedido);
                                    break;
    
                                    case 3:
                                        System.out.println(">>PEDIDO ACTUAL CANCELADO");
                                        conn.rollback(noPedido);
                                        pedidoAbierto = false;
                                    break;
    
                                    case 4:
                                        conn.commit();
                                        pedidoAbierto = false;
                                        System.out.println(">>PEDIDO FINALIZADO");

                                        System.out.println(">>>Listado de detalles de productos:");
                                        mostrarDetallesPedidos(conn);
                                    break;
                                }
                            }
    
                            conn.setAutoCommit(true);
                        }
                        else
                        {
                            System.out.println(">>ERROR: Tablas no inicializadas, seleccionar 1 para solucionarlo.");
                        }
                    break;

                    case 3:
                        System.out.println(">>BORRAR PEDIDO");
                        if(existeStock && existePedido && existeDetallePedido)
                        {
                            System.out.println(">>>Lista de pedidos");
                            mostrarPedidos(conn);
                            Scanner scanPedido = new Scanner(System.in);
                            System.out.println(">>>Introduzca el código de pedido a eliminar");
                            int pedidoSeleccionado;
                            pedidoSeleccionado = scanPedido.nextInt();
                            borrarPedido(conn, pedidoSeleccionado);
                            System.out.println(">>>PEDIDO BORRADO");
                            System.out.println(">>>Pedidos restantes");
                            mostrarPedidos(conn);
                        }
                        else
                        {
                            System.out.println(">>ERROR: Tablas no inicializadas, seleccionar 1 para solucionarlo.");
                        }
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
