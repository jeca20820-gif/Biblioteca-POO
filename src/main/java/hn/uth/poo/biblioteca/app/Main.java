/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hn.uth.poo.biblioteca.app;

/**
 *
 * @author Jessica
 */
import hn.uth.poo.biblioteca.dao.AutorDAO;
import hn.uth.poo.biblioteca.dao.LibroDAO;
import hn.uth.poo.biblioteca.dao.impl.AutorDAOImpl;
import hn.uth.poo.biblioteca.dao.impl.LibroDAOImpl;
import hn.uth.poo.biblioteca.excepciones.DatoInvalidoException;
import hn.uth.poo.biblioteca.excepciones.DatosException;
import hn.uth.poo.biblioteca.modelo.Autor;
import hn.uth.poo.biblioteca.modelo.Libro;
import hn.uth.poo.biblioteca.servicio.ServicioAutores;
import hn.uth.poo.biblioteca.servicio.ServicioLibros;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static ServicioAutores servicioAutores;
    private static ServicioLibros servicioLibros;

    public static void main(String[] args) {

        AutorDAO autorDAO = new AutorDAOImpl();
        LibroDAO libroDAO = new LibroDAOImpl();

        servicioAutores = new ServicioAutores(autorDAO, libroDAO);
        servicioLibros = new ServicioLibros(libroDAO, autorDAO);

        int opcion = -1;
        do {
            mostrarMenuPrincipal();
            opcion = leerEntero("Seleccione una opción: ");
            try {
                switch (opcion) {
                    case 1 -> crearAutor();
                    case 2 -> listarAutores();
                    case 3 -> buscarAutorPorId();
                    case 4 -> actualizarAutor();
                    case 5 -> eliminarAutor();
                    case 6 -> crearLibro();
                    case 7 -> listarLibros();
                    case 8 -> buscarLibroPorId();
                    case 9 -> actualizarLibro();
                    case 10 -> eliminarLibro();
                    case 11 -> listarLibrosDeUnAutor();
                    case 0 -> System.out.println("\nSaliendo del sistema de Biblioteca...");
                    default -> System.out.println("\n[!] Opción no válida. Intente de nuevo.");
                }
            } catch (DatoInvalidoException e) {
                System.out.println("\n[ERROR DE VALIDACIÓN]: " + e.getMessage());
            } catch (DatosException e) {
                System.out.println("\n[ERROR DE BASE DE DATOS]: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\n[ERROR INESPERADO]: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n=======================================================");
        System.out.println("          SISTEMA DE GESTIÓN DE BIBLIOTECA             ");
        System.out.println("=======================================================");
        System.out.println("--- GESTIÓN DE AUTORES ---");
        System.out.println(" 1. Crear autor");
        System.out.println(" 2. Listar autores");
        System.out.println(" 3. Buscar autor por ID");
        System.out.println(" 4. Actualizar autor");
        System.out.println(" 5. Eliminar autor");
        System.out.println("--- GESTIÓN DE LIBROS ---");
        System.out.println(" 6. Crear libro");
        System.out.println(" 7. Listar todos los libros");
        System.out.println(" 8. Buscar libro por ID");
        System.out.println(" 9. Actualizar libro");
        System.out.println("10. Eliminar libro");
        System.out.println("11. Listar libros de un autor");
        System.out.println("-------------------------------------------------------");
        System.out.println(" 0. Salir");
        System.out.println("=======================================================");
    }

    private static void crearAutor() {
        System.out.println("\n--- REGISTRAR NUEVO AUTOR ---");
        System.out.print("Nombre del autor: ");
        String nombre = scanner.nextLine();
        System.out.print("Nacionalidad: ");
        String nacionalidad = scanner.nextLine();

        Autor nuevo = new Autor(nombre, nacionalidad);
        servicioAutores.registrarAutor(nuevo);
        System.out.println("[OK] Autor registrado exitosamente con ID generado: " + nuevo.getId());
    }

    private static void listarAutores() {
        System.out.println("\n--- LISTA DE AUTORES ---");
        List<Autor> autores = servicioAutores.listarAutores();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
            return;
        }
        for (Autor a : autores) {
            System.out.println(a);
        }
    }

    private static void buscarAutorPorId() {
        System.out.println("\n--- BUSCAR AUTOR POR ID ---");
        int id = leerEntero("Ingrese el ID del autor: ");
        Autor autor = servicioAutores.buscarPorId(id);
        System.out.println("[OK] Autor encontrado: " + autor);
    }

    private static void actualizarAutor() {
        System.out.println("\n--- ACTUALIZAR AUTOR ---");
        int id = leerEntero("Ingrese el ID del autor a actualizar: ");
        Autor actual = servicioAutores.buscarPorId(id);
        System.out.println("Datos actuales: " + actual);

        System.out.print("Nuevo nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Nueva nacionalidad: ");
        String nacionalidad = scanner.nextLine();

        Autor actualizado = new Autor(id, nombre, nacionalidad);
        servicioAutores.actualizarAutor(actualizado);
        System.out.println("[OK] Autor actualizado correctamente.");
    }

    private static void eliminarAutor() {
        System.out.println("\n--- ELIMINAR AUTOR ---");
        int id = leerEntero("Ingrese el ID del autor a eliminar: ");
        servicioAutores.eliminarAutor(id);
        System.out.println("[OK] Autor eliminado correctamente.");
    }

    private static void crearLibro() {
        System.out.println("\n--- REGISTRAR NUEVO LIBRO ---");
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        int anio = leerEntero("Año de publicación: ");
        double precio = leerDecimal("Precio (L.): ");
        int autorId = leerEntero("ID del Autor: ");

        Libro nuevo = new Libro(isbn, titulo, anio, precio, autorId);
        servicioLibros.registrarLibro(nuevo);
        System.out.println("[OK] Libro registrado exitosamente con ID generado: " + nuevo.getId());
    }

    private static void listarLibros() {
        System.out.println("\n--- LISTA DE TODOS LOS LIBROS ---");
        List<Libro> libros = servicioLibros.listarLibros();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }
        for (Libro l : libros) {
            System.out.println(l);
        }
    }

    private static void buscarLibroPorId() {
        System.out.println("\n--- BUSCAR LIBRO POR ID ---");
        int id = leerEntero("Ingrese el ID del libro: ");
        Libro libro = servicioLibros.buscarPorId(id);
        System.out.println("[OK] Libro encontrado: " + libro);
    }

    private static void actualizarLibro() {
        System.out.println("\n--- ACTUALIZAR LIBRO ---");
        int id = leerEntero("Ingrese el ID del libro a actualizar: ");
        Libro actual = servicioLibros.buscarPorId(id);
        System.out.println("Datos actuales: " + actual);

        System.out.print("Nuevo ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Nuevo título: ");
        String titulo = scanner.nextLine();
        int anio = leerEntero("Nuevo año de publicación: ");
        double precio = leerDecimal("Nuevo precio (L.): ");
        int autorId = leerEntero("Nuevo ID del Autor: ");

        Libro actualizado = new Libro(id, isbn, titulo, anio, precio, autorId);
        servicioLibros.actualizarLibro(actualizado);
        System.out.println("[OK] Libro actualizado correctamente.");
    }

    private static void eliminarLibro() {
        System.out.println("\n--- ELIMINAR LIBRO ---");
        int id = leerEntero("Ingrese el ID del libro a eliminar: ");
        servicioLibros.eliminarLibro(id);
        System.out.println("[OK] Libro eliminado correctamente.");
    }

    private static void listarLibrosDeUnAutor() {
        System.out.println("\n--- LISTAR LIBROS POR AUTOR ---");
        int autorId = leerEntero("Ingrese el ID del autor: ");
        Autor autor = servicioAutores.buscarPorId(autorId);
        System.out.println("Autor: " + autor.getNombre());

        List<Libro> libros = servicioLibros.listarLibrosPorAutor(autorId);
        if (libros.isEmpty()) {
            System.out.println("Este autor no tiene libros registrados.");
            return;
        }
        for (Libro l : libros) {
            System.out.println(l);
        }
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[!] Por favor, ingrese un número entero válido.");
            }
        }
    }

    private static double leerDecimal(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[!] Por favor, ingrese un valor numérico válido.");
            }
        }
    }
}