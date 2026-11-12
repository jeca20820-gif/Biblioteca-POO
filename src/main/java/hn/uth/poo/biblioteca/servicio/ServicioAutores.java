/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.uth.poo.biblioteca.servicio;

/**
 *
 * @author Jessica
 */
import hn.uth.poo.biblioteca.dao.AutorDAO;
import hn.uth.poo.biblioteca.dao.LibroDAO;
import hn.uth.poo.biblioteca.excepciones.DatoInvalidoException;
import hn.uth.poo.biblioteca.modelo.Autor;
import hn.uth.poo.biblioteca.modelo.Libro;
import java.util.List;

public class ServicioAutores {
    private final AutorDAO autorDAO;
    private final LibroDAO libroDAO;

    public ServicioAutores(AutorDAO autorDAO, LibroDAO libroDAO) {
        this.autorDAO = autorDAO;
        this.libroDAO = libroDAO;
    }

    public void registrarAutor(Autor autor) {
        validarDatosAutor(autor);
        autorDAO.insertar(autor);
    }

    public Autor buscarPorId(int id) {
        Autor autor = autorDAO.buscarPorId(id);
        if (autor == null) {
            throw new DatoInvalidoException("No existe un autor registrado con el ID: " + id);
        }
        return autor;
    }

    public List<Autor> listarAutores() {
        return autorDAO.listar();
    }

    public void actualizarAutor(Autor autor) {
        buscarPorId(autor.getId());
        validarDatosAutor(autor);
        autorDAO.actualizar(autor);
    }

    public void eliminarAutor(int id) {
        buscarPorId(id);
        List<Libro> librosAsociados = libroDAO.listarPorAutor(id);
        if (!librosAsociados.isEmpty()) {
            throw new DatoInvalidoException(
                    "Regla de negocio violada: No se puede eliminar el autor (ID " + id
                    + ") porque tiene " + librosAsociados.size() + " libro(s) registrado(s)."
            );
        }
        autorDAO.eliminar(id);
    }

    private void validarDatosAutor(Autor autor) {
        if (autor == null) {
            throw new DatoInvalidoException("El objeto autor no puede ser nulo.");
        }
        if (autor.getNombre() == null || autor.getNombre().trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre del autor es obligatorio y no puede estar vacío.");
        }
        autor.setNombre(autor.getNombre().trim());
        if (autor.getNacionalidad() != null) {
            autor.setNacionalidad(autor.getNacionalidad().trim());
        }
    }
}