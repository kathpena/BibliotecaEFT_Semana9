package controlador;

import dao.CategoriaDAO;
import dao.LibroDAO;
import modelo.Categoria;
import modelo.Libro;
import java.util.List;

public class LibroController {
    private LibroDAO libroDAO = new LibroDAO();
    private CategoriaDAO categoriaDAO = new CategoriaDAO();

    public List<Libro> listarLibros(){
        return libroDAO.listar();
    }
    public List<Categoria> listarCategorias(){
        return categoriaDAO.listar();
    }

    public String agregarLibro(String titulo, String autor, String isbn, String editorial,
                               String stockTexto, Categoria categoria) {
        String error = validar(titulo, autor, isbn, stockTexto, categoria);
        if(error != null) {
            return error;
        }
        Libro libro = new Libro(0, titulo, autor, isbn, editorial,
                Integer.parseInt(stockTexto), categoria.getId());
        if(libroDAO.insertar(libro)) {
            return "Libro agregado correctamente";
        }
        return "No se pudo agregar el libro (revisa que el ISBN no esté repetido)";
    }

    public String modificarLibro(int id, String titulo, String autor, String isbn, String editorial,
                                 String stockTexto, Categoria categoria) {
        String error = validar(titulo, autor, isbn, stockTexto, categoria);
        if(error != null) {
            return error;
        }
        Libro libro = new Libro(id, titulo, autor, isbn, editorial,
                Integer.parseInt(stockTexto), categoria.getId());
        if(libroDAO.actualizar(libro)) {
            return "Libro modificado correctamente";
        }
        return "No se pudo modificar el libro";
    }
    public String eliminarLibro(int id) {
        if(libroDAO.eliminar(id)){
            return "Libro eliminado correctamente";}
        return "No se pudo eliminar: el libro tiene préstamos registrados";
    }
    private String validar(String titulo, String autor, String isbn, String stockTexto, Categoria categoria) {
        if(titulo.isEmpty() || autor.isEmpty() || isbn.isEmpty()) {return "Título, autor e ISBN son obligatorios";}

        if(categoria == null) {return "Debes seleccionar una categoría";}
        try{
            int stock = Integer.parseInt(stockTexto);
            if(stock < 0) {return "El stock no puede ser negativo";}
        }catch (NumberFormatException e) {
            return "El stock debe ser un número";
        }
        return null;
    }

}
