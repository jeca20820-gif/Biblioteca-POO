/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.uth.poo.biblioteca.dao.impl;

/**
 *
 * @author Jessica
 */
import hn.uth.poo.biblioteca.dao.Conexion;
import hn.uth.poo.biblioteca.dao.LibroDAO;
import hn.uth.poo.biblioteca.excepciones.DatosException;
import hn.uth.poo.biblioteca.modelo.Autor;
import hn.uth.poo.biblioteca.modelo.Libro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LibroDAOImpl implements LibroDAO {

    public LibroDAOImpl() {
        Conexion.inicializarBaseDatos();
    }

    private Libro mapear(ResultSet rs) throws SQLException {
        Libro libro = new Libro(
                rs.getInt("id"),
                rs.getString("isbn"),
                rs.getString("titulo"),
                rs.getInt("anio"),
                rs.getDouble("precio"),
                rs.getInt("autor_id")
        );
        Autor autor = new Autor(
                rs.getInt("autor_id"),
                rs.getString("autor_nombre"),
                rs.getString("autor_nacionalidad")
        );
        libro.setAutor(autor);
        return libro;
    }

    @Override
    public void insertar(Libro libro) {
        String sql = "INSERT INTO libros (isbn, titulo, anio, precio, autor_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, libro.getIsbn());
            ps.setString(2, libro.getTitulo());
            ps.setInt(3, libro.getAnio());
            ps.setDouble(4, libro.getPrecio());
            ps.setInt(5, libro.getAutorId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    libro.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DatosException("Error al insertar el libro en la base de datos", e);
        }
    }

    @Override
    public Libro buscarPorId(Integer id) {
        String sql = "SELECT l.id, l.isbn, l.titulo, l.anio, l.precio, l.autor_id, "
                + "a.nombre AS autor_nombre, a.nacionalidad AS autor_nacionalidad "
                + "FROM libros l INNER JOIN autores a ON l.autor_id = a.id WHERE l.id = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatosException("Error al buscar el libro con ID " + id, e);
        }
    }

    @Override
    public Libro buscarPorIsbn(String isbn) {
        String sql = "SELECT l.id, l.isbn, l.titulo, l.anio, l.precio, l.autor_id, "
                + "a.nombre AS autor_nombre, a.nacionalidad AS autor_nacionalidad "
                + "FROM libros l INNER JOIN autores a ON l.autor_id = a.id WHERE l.isbn = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatosException("Error al buscar el libro por ISBN " + isbn, e);
        }
    }

    @Override
    public List<Libro> listar() {
        String sql = "SELECT l.id, l.isbn, l.titulo, l.anio, l.precio, l.autor_id, "
                + "a.nombre AS autor_nombre, a.nacionalidad AS autor_nacionalidad "
                + "FROM libros l INNER JOIN autores a ON l.autor_id = a.id ORDER BY l.id ASC";
        List<Libro> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DatosException("Error al listar los libros", e);
        }
    }

    @Override
    public List<Libro> listarPorAutor(int autorId) {
        String sql = "SELECT l.id, l.isbn, l.titulo, l.anio, l.precio, l.autor_id, "
                + "a.nombre AS autor_nombre, a.nacionalidad AS autor_nacionalidad "
                + "FROM libros l INNER JOIN autores a ON l.autor_id = a.id WHERE l.autor_id = ? ORDER BY l.id ASC";
        List<Libro> lista = new ArrayList<>();
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, autorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new DatosException("Error al listar los libros del autor con ID " + autorId, e);
        }
    }

    @Override
    public void actualizar(Libro libro) {
        String sql = "UPDATE libros SET isbn = ?, titulo = ?, anio = ?, precio = ?, autor_id = ? WHERE id = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, libro.getIsbn());
            ps.setString(2, libro.getTitulo());
            ps.setInt(3, libro.getAnio());
            ps.setDouble(4, libro.getPrecio());
            ps.setInt(5, libro.getAutorId());
            ps.setInt(6, libro.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatosException("Error al actualizar el libro con ID " + libro.getId(), e);
        }
    }

    @Override
    public void eliminar(Integer id) {
        String sql = "DELETE FROM libros WHERE id = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatosException("Error al eliminar el libro con ID " + id, e);
        }
    }
}
