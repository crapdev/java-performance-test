package controller;

import exception.ExceptionesNegocio;
import java.util.List;
import model.Repuesto;
import model.aux.Categoria;
import model.aux.Proveedor;
import service.RepuestoService;
import view.TallerExpressView;

/**
 *
 * @author cohorte5
 */
public class RepuestoController {
    private final TallerExpressView view = new TallerExpressView();
    private final RepuestoService repuestoService = new RepuestoService();

    public void iniciarMenu() {

        String[] opciones = {
            "Registrar Repuesto",
            "Editar Repuesto",
            "Listar Repuestos",
            "Filtrar por Categoría",
            "Filtrar por Proveedor",
            "Volver"
        };

        int seleccion;

        do {

            seleccion = view.option("GESTIÓN DE REPUESTOS", "Seleccione una opción:", opciones);

            switch (seleccion) {

                case 0 -> registrarRepuesto();
                case 1 -> editarRepuesto();
                case 2 -> mostrarRepuestos(repuestoService.listarRepuestos());
                case 3 -> filtrarPorCategoria();
                case 4 -> filtrarPorProveedor();
                case 5 -> {
                }
                default -> seleccion = 5;
            }
        } while (seleccion != 5);
    }

    
    private void registrarRepuesto() {

        try {

            String codigo = view.input("Código de referencia:");
            if (codigo == null) return;

            String nombre = view.input("Nombre:");
            if (nombre == null) return;

            mostrarCategorias();

            String categoriaTexto = view.input("ID de categoría:");
            if (categoriaTexto == null) return;

            mostrarProveedores();

            String proveedorTexto = view.input("ID de proveedor:");
            if (proveedorTexto == null) return;

            String stockTexto = view.input("Stock total:");
            if (stockTexto == null) return;

            String precioTexto = view.input("Precio unitario:");
            if (precioTexto == null) return;

            int categoria = Integer.parseInt(categoriaTexto.trim());
            int proveedor = Integer.parseInt(proveedorTexto.trim());
            long stock = Long.parseLong(stockTexto.trim());
            double precio = Double.parseDouble(precioTexto.trim());

            Repuesto repuesto = new Repuesto(0,codigo.trim(),nombre.trim(),categoria,proveedor,stock,stock,precio,true);

            repuestoService.registrarRepuesto(repuesto);

            view.message("Repuesto registrado correctamente.");

        } catch (NumberFormatException e) {

            view.error("Los valores numéricos no tienen un formato válido.");

        } catch (ExceptionesNegocio e) {

            view.error(e.getMessage());
        }
    }

    private void editarRepuesto() {

        try {

            mostrarRepuestos(repuestoService.listarRepuestos());

            String idTexto = view.input("ID del repuesto:");
            if (idTexto == null) return;

            long id = Long.parseLong(idTexto.trim());

            String codigo = view.input("Código de referencia:");
            if (codigo == null) return;

            String nombre = view.input("Nombre:");
            if (nombre == null) return;

            mostrarCategorias();

            String categoriaTexto = view.input("ID de categoría:");
            if (categoriaTexto == null) return;

            mostrarProveedores();

            String proveedorTexto = view.input("ID de proveedor:");
            if (proveedorTexto == null) return;

            String stockTotalTexto = view.input("Stock total:");
            if (stockTotalTexto == null) return;

            String stockDisponibleTexto = view.input("Stock disponible:");
            if (stockDisponibleTexto == null) return;

            String precioTexto = view.input("Precio unitario:");
            if (precioTexto == null) return;

            int categoria = Integer.parseInt(categoriaTexto.trim());
            int proveedor = Integer.parseInt(proveedorTexto.trim());
            long stockTotal = Long.parseLong(stockTotalTexto.trim());
            long stockDisponible = Long.parseLong(stockDisponibleTexto.trim());
            double precio = Double.parseDouble(precioTexto.trim());

            Repuesto repuesto = new Repuesto(
                    id,
                    codigo.trim(),
                    nombre.trim(),
                    categoria,
                    proveedor,
                    stockTotal,
                    stockDisponible,
                    precio,
                    true
            );

            repuestoService.actualizarRepuesto(repuesto);

            view.message("Repuesto actualizado correctamente.");

        } catch (NumberFormatException e) {

            view.error("Ingrese valores numéricos válidos.");

        } catch (ExceptionesNegocio e) {

            view.error(e.getMessage());
        }
    }

    private void filtrarPorCategoria() {

        try {

            mostrarCategorias();

            String texto = view.input("ID de categoría:");
            if (texto == null) return;

            int id = Integer.parseInt(texto.trim());

            List<Repuesto> repuestos = repuestoService.filtrarPorCategoria(id);

            mostrarRepuestos(repuestos);

        } catch (NumberFormatException e) {

            view.error("El ID debe ser numérico.");

        } catch (ExceptionesNegocio e) {

            view.error(e.getMessage());
        }
    }

    private void filtrarPorProveedor() {

        try {

            mostrarProveedores();

            String texto = view.input("ID de proveedor:");
            if (texto == null) return;

            int id = Integer.parseInt(texto.trim());

            List<Repuesto> repuestos = repuestoService.filtrarPorProveedor(id);

            mostrarRepuestos(repuestos);

        } catch (NumberFormatException e) {

            view.error("El ID debe ser numérico.");

        } catch (ExceptionesNegocio e) {

            view.error(e.getMessage());
        }
    }

    private void mostrarRepuestos(List<Repuesto> repuestos) {

        if (repuestos.isEmpty()) {
            view.message("No se encontraron repuestos.");
            return;
        }

        StringBuilder texto = new StringBuilder("REPUESTOS\n\n");

        for (Repuesto repuesto : repuestos) {

            texto.append("ID: ").append(repuesto.getId())
                    .append("\nCódigo: ").append(repuesto.getCodigoReferencia())
                    .append("\nNombre: ").append(repuesto.getNombre())
                    .append("\nCategoría ID: ").append(repuesto.getIdCategoria())
                    .append("\nProveedor ID: ").append(repuesto.getIdProveedor())
                    .append("\nStock total: ").append(repuesto.getStockTotal())
                    .append("\nStock disponible: ").append(repuesto.getStockDisponible())
                    .append("\nPrecio: $").append(repuesto.getPrecioUnitario())
                    .append("\n--------------------------\n");
        }

        view.message(texto.toString());
    }

    private void mostrarCategorias() {

        List<Categoria> categorias = repuestoService.obtenerCategorias();

        StringBuilder texto = new StringBuilder("CATEGORÍAS\n\n");

        for (Categoria categoria : categorias) {

            texto.append(categoria.getId())
                    .append(" - ")
                    .append(categoria.getNombre())
                    .append("\n");
        }

        view.message(texto.toString());
    }

    private void mostrarProveedores() {

        List<Proveedor> proveedores = repuestoService.obtenerProveedores();

        StringBuilder texto = new StringBuilder("PROVEEDORES\n\n");

        for (Proveedor proveedor : proveedores) {

            texto.append(proveedor.getId())
                    .append(" - ")
                    .append(proveedor.getNombre())
                    .append("\n");
        }

        view.message(texto.toString());
}
}
