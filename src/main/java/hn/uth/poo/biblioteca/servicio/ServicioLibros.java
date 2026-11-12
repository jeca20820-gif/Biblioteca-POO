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
import java.time.LocalDate;
import java.util.List;

public class ServicioLibros {
    private final LibroDAO libroDAO;
    private final AutorDAO autorDAO;

    public ServicioLibros(LibroDAO libroDAO, AutorDAO autorDAO) {
        this.libroDAO = libroDAO;
        this.autorDAO = autorDAO;
    }

    public void registrarLibro(Libro libro) {
        validarDatosLibro(libro, true);
        libroDAO.insertar(libro);
    }

    public Libro buscarPorId(int id) {
        Libro libro = libroDAO.buscarPorId(id);
        if (libro == null) {
            throw new DatoInvalidoException("No existe un libro registrado con el ID: " + id);
        }
        return libro;
    }

    public List<Libro> listarLibros() {
        return libroDAO.listar();
    }

    public List<Libro> listarLibrosPorAutor(int autorId) {
        Autor autor = autorDAO.buscarPorId(autorId);
        if (autor == null) {
            throw new DatoInvalidoException("No existe un autor con el ID: " + autorId);
        }
        return libroDAO.listarPorAutor(autorId);
    }

    public void actualizarLibro(Libro libro) {
        buscarPorId(libro.getId());
        validarDatosLibro(libro, false);
        libroDAO.actualizar(libro);
    }

    public void eliminarLibro(int id) {
        buscarPorId(id);
        libroDAO.eliminar(id);
    }

    private void validarDatosLibro(Libro libro, boolean esNuevo) {
        if (libro == null) {
            throw new DatoInvalidoException("El objeto libro no puede ser nulo.");
        }
        if (libro.getIsbn() == null || libro.getIsbn().trim().isEmpty()) {
            throw new DatoInvalidoException("El código ISBN del libro es obligatorio.");
        }
        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            throw new DatoInvalidoException("El título del libro es obligatorio y no puede estar vacío.");
        }

        int anioActual = LocalDate.now().getYear();
        if (libro.getAnio() < 1450 || libro.getAnio() > anioActual) {
            throw new DatoInvalidoException(
                    "El año de publicación (" + libro.getAnio() + ") es inválido. Debe estar entre 1450 y " + anioActual + "."
            );
        }

        if (libro.getPrecio() < 0) {
            throw new DatoInvalidoException("El precio del libro no puede ser negativo (debe ser >= 0).");
        }

        Autor autorExistente = autorDAO.buscarPorId(libro.getAutorId());
        if (autorExistente == null) {
            throw new DatoInvalidoException(
                    "No se puede registrar el libro porque el autor con ID " + libro.getAutorId() + " no existe."
            );
        }

        String isbnLimpio = libro.getIsbn().trim();
        Libro libroConMismoIsbn = libroDAO.buscarPorIsbn(isbnLimpio);
        if (libroConMismoIsbn != null) {
            if (esNuevo || libroConMismoIsbn.getId() != libro.getId()) {
                throw new DatoInvalidoException(
                        "Regla de negocio violada: Ya existe un libro registrado con el ISBN '" + isbnLimpio + "'."
                );
            }
        }

        libro.setIsbn(isbnLimpio);
        libro.setTitulo(libro.getTitulo().trim());
    }
}
