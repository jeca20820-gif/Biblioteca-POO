/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.uth.poo.biblioteca.dao;

/**
 *
 * @author Jessica
 */
import hn.uth.poo.biblioteca.excepciones.DatosException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {
    private static final String URL = "jdbc:sqlite:biblioteca.db";

    public static Connection obtenerConexion() {
        try {
            Connection conn = DriverManager.getConnection(URL);
            try (Statement st = conn.createStatement()) {
                st.execute("PRAGMA foreign_keys = ON;");
            }
            return conn;
        } catch (SQLException e) {
            throw new DatosException("Error al conectar con la base de datos biblioteca.db", e);
        }
    }

    public static void inicializarBaseDatos() {
        String sqlAutores = "CREATE TABLE IF NOT EXISTS autores ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre TEXT NOT NULL, "
                + "nacionalidad TEXT"
                + ");";

        String sqlLibros = "CREATE TABLE IF NOT EXISTS libros ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "isbn TEXT NOT NULL UNIQUE, "
                + "titulo TEXT NOT NULL, "
                + "anio INTEGER, "
                + "precio REAL NOT NULL, "
                + "autor_id INTEGER NOT NULL REFERENCES autores(id)"
                + ");";

        try (Connection conn = obtenerConexion();
             Statement st = conn.createStatement()) {
            st.execute(sqlAutores);
            st.execute(sqlLibros);
        } catch (SQLException e) {
            throw new DatosException("Error al inicializar las tablas de la base de datos", e);
        }
    }
}
