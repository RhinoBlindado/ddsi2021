/*
    Titulo:         Subsistema 3 - Patrocinadores / Colaboradores
    Fecha:          Diciembre 2020
    Autor:          Eladia Gómez Morales
    Descripción:    Implementación de parte del Subsistema 3 - Patrocinadores / Colaboradores, la funcionalidad de "Registrar entidad como patrocinadora en una edición con una cantidad de dinero".
*/

// Librerias
import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.*;

// Clase Base
public class patrocinadoresColaboradores {
    // Metodos Privados

    /**
     * @param conn Objeto que proporciona el vínculo entre la base de datos y la
     *             aplicación en java
     * @brief Muestra todos las Entidades registradas
     */
    private static void mostrarEntidades(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM ENTIDAD");

        while (rs.next()) {
            System.out.println(
                    "ID Entidad:" + rs.getString("IDENTIDAD") + " Nombre Entidad: " + rs.getString("NOMBREENTIDAD")
                            + " Nombre Persona Contacto: " + rs.getString("NOMBRECONTACTO") + " Correo Electronico: "
                            + rs.getString("CORREO") + " Numero de Telefono: " + rs.getString("TELEFONO"));
        }
    }

    /**
     * @param conn Objeto que proporciona el vínculo entre la base de datos y la
     *             aplicación en java
     * @brief Muestra todos los Patrocinadores registrados
     */
    private static void mostrarPatrocinadores(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM PATROCINA");

        while (rs.next()) {
            System.out.println("ID Entidad:" + rs.getString("IDENTIDAD") + " Anno: " + rs.getString("ANNO")
                    + " Cantidad de dinero con la que patrocina: " + rs.getString("CANTIDADDINERO"));

        }
    }
	
	/**
     * @param conn Objeto que proporciona el vínculo entre la base de datos y la
     *             aplicación en java
     * @brief Muestra todos los Colaboradores registrados
     */
    private static void mostrarColaboradores(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM COLABORA");

        while (rs.next()) {
            System.out.println("ID Entidad:" + rs.getString("IDENTIDAD") + " Anno: " + rs.getString("ANNO")
                    + " Cantidad de dinero con la que colabora: " + rs.getString("CANTIDADDINERO"));

        }
    }


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

    // Metodos Publicos

    /**
     * @param conn Objeto que proporciona el vínculo entre la base de datos y la
     *             aplicación en java
     * @brief Registra una Entidad como Patrocinadora en una determinada Edición y
     *        una determinada Cantidad de Dinero
     */
    public static void registrarEntidadPatrocinadora(Connection conn) throws SQLException {
        Scanner scan = new Scanner(System.in);
        int idEntidad, anno;
        double cantidadDinero;
        boolean patrocinadorExistente = true;

        System.out.println(">>SUBSISTEMA 3 - PATROCINADORES / COLABORADORES\n"
                + ">>Registrar entidad como patrocinadora en una edición con una cantidad de dinero\n");

        // Sale del bucle si se inserta, de no ser así se queda en el bucle, para volver
        // a insertar el idEntidad
        // por haber introducido uno no registrado en la tabla Entidad o porque salta el
        // control de excepciones al
        // intentar insertar una entidad que patrocine en un año ya insertado en
        // Patrocina o por el disparador.
        while (patrocinadorExistente) {
            System.out.println("\n>>Entidades Registradas en el torneo: ");
            mostrarEntidades(conn);

            System.out.println("\n>>Entidades Registradas como PATROCINADORAS en el torneo: ");
            mostrarPatrocinadores(conn);

			 System.out.println("\n>>Entidades Registradas como COLABORADORAS en el torneo: ");
            mostrarColaboradores(conn);

            System.out.println("\n>>Insertar ID Entidad que se quiere como Patrocinadora: ");
            idEntidad = scan.nextInt();

            // Comprueba la exitencia del idEntidad introducido en la tabla Entidad, de no
            // estar,
            // hace que volvamos a introducir otro idEntidad hasta que sea válido(esté entre
            // los registrados en la taba Entidad)
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM ENTIDAD WHERE IDENTIDAD=" + idEntidad);
            if (rs.next() == false) {
                System.out.println(
                        "\n>>>ERROR El ID Entidad introducido no pertenece al de ninguna Entidad Registrada, introduce otra");
                rs.close();
            } else {
                try {
                    System.out.println("\n>>Ediciones en las que la Entidad podria Patrocinar: ");
                    mostrarEdiciones(conn);

                    System.out.print("\n>>Insertar Edicion en la que la Entidad Patrocina: ");
                    anno = scan.nextInt();

                    // Comprueba la existencia de la Edición introducida en la tabla Edicion, de no
                    // estar
                    // hace que volvamos a introducir otra anno de Edición hasta que sea válido(esté
                    // entre
                    // los registrados en la taba Edición)
                    ArrayList<Integer> vectorEdiciones = annoEdiciones(conn);

                    while (!vectorEdiciones.contains(anno)) {
                        System.out.println(
                                "\n>>>ERROR al introducir la edición, estas son las ediciones en las que la Entidad podría patrocinar: ");
                        mostrarEdiciones(conn);

                        System.out.print("\n>>Vuelva a insertar Edicion en la que la Entidad Patrocina: ");
                        anno = scan.nextInt();
                    }

                    System.out.print(
                            "\n>>Insertar Cantidad de Dinero con la que la Entidad va a patrocinar (los decimales se ponen con coma y no con punto): ");
                    cantidadDinero = scan.nextDouble();

                    // Comprueba que la Cantidad de Dinero introducida es >= 0 para que no de fallo
                    // al
                    // introducir en la tabla Patrocina. Hace que volvamos a introducir la Cantidad
                    // de
                    // Dinero hasta que esta sea >= 0.
                    while (cantidadDinero <= 0) {
                        System.out.print("\n>>ERROR Cantidad de dinero incorrecta, vuelve a introducir una: ");
                        cantidadDinero = scan.nextDouble();
                    }

                    // Inserta los datos en la tabla Patrocina, registrando así una Entidad como
                    // Patrocinadora.
                    // Además hace false patrocinadorExiste para salir del while del que no podemos
                    // salir si no
                    // completa la inserción.
                    Statement st2 = conn.createStatement();
                    st2.executeUpdate(
                            "INSERT INTO PATROCINA VALUES (" + idEntidad + ", " + anno + ", " + cantidadDinero + ")");
                    patrocinadorExistente = false;
                    System.out.println("\n\n>>>ENTIDAD REGISTRADA COMO PATROCINADORA");

                } catch (SQLException e) {
                    System.err.format("\n\nSQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
                    System.out.println("\n\n>>>Vuelve a probar");
                }
            }
        }
    }
}
