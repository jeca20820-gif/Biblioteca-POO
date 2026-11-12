/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.uth.poo.biblioteca.modelo;

/**
 *
 * @author Jessica
 */
public class Libro {
    private int id;
    private String isbn;
    private String titulo;
    private int anio;
    private double precio;
    private int autorId;
    private Autor autor;

    public Libro() {
    }

    public Libro(String isbn, String titulo, int anio, double precio, int autorId) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.anio = anio;
        this.precio = precio;
        this.autorId = autorId;
    }

    public Libro(int id, String isbn, String titulo, int anio, double precio, int autorId) {
        this.id = id;
        this.isbn = isbn;
        this.titulo = titulo;
        this.anio = anio;
        this.precio = precio;
        this.autorId = autorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getAutorId() {
        return autorId;
    }

    public void setAutorId(int autorId) {
        this.autorId = autorId;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
        if (autor != null) {
            this.autorId = autor.getId();
        }
    }

    @Override
    public String toString() {
        String infoAutor = (autor != null && autor.getNombre() != null)
                ? autor.getNombre() + " (ID: " + autorId + ")"
                : "ID Autor: " + autorId;
        return String.format("Libro [ID=%d | ISBN=%s | Título=%s | Año=%d | Precio=L. %.2f | Autor=%s]",
                id, isbn, titulo, anio, precio, infoAutor);
    }
}