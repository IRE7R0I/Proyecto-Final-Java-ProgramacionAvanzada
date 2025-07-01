package Modelo;

import Modelo.Interfaces.ClienteRepository;
import Modelo.Interfaces.ProductoRepository;
import Modelo.Interfaces.VentaRepository;
import java.util.ArrayList;
import java.util.List;

public class Tienda {
    private final ProductoRepository productoRepo;
    private final ClienteRepository clienteRepo;
    private final VentaRepository ventaRepo;
    private final List<Venta> ventas = new ArrayList<>();

    public Tienda(ProductoRepository productoRepo,ClienteRepository clienteRepo,VentaRepository ventaRepo) {
        this.productoRepo = productoRepo;
        this.clienteRepo = clienteRepo;
        this.ventaRepo = ventaRepo;
    }

    public int agregarProducto(Producto producto) {
        return productoRepo.ingresarProducto(producto);
    }

    public int agregarCliente(Cliente cliente) {
        return clienteRepo.almacenarCliente(cliente);
    }

    public void registrarVenta(Venta venta) {
        // Validar stock y existencia de productos
        for (DetalleVenta detalle : venta.getListDetalles()) {
            Producto p = productoRepo.buscarProducto(detalle.getProducto().getId());
            if (p == null) {
                throw new IllegalStateException("Producto no encontrado: ID " + detalle.getProducto().getId());
            }
            if (p.getCantidad() < detalle.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para: " + p.getNombre() +
                        " (Disponible: " + p.getCantidad() +
                        ", Solicitado: " + detalle.getCantidad() + ")");
            }
        }

        // Registrar en BD
        int idVenta = ventaRepo.registrarVenta(venta);
        ventas.add(venta);
        System.out.println("Venta registrada con ID: " + idVenta);

        // Actualizar cantidades en objetos en memoria
        for (DetalleVenta detalle : venta.getListDetalles()) {
            Producto p = detalle.getProducto();
            p.setCantidad(p.getCantidad() - detalle.getCantidad());
        }
    }

    public void mostrarVentas() {
        System.out.println("\n=== HISTORIAL DE VENTAS ===");
        for (Venta venta : ventas) {
            System.out.println(venta);
            System.out.println("---------------------------");
        }

        double totalGeneral = ventas.stream()
                .mapToDouble(Venta::getTotal)
                .sum();
        System.out.println("TOTAL GENERAL: $" + String.format("%.2f", totalGeneral));
    }
}
