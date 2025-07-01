package Modelo;

import java.util.ArrayList;
import java.util.List;

public class Venta {
    private Cliente cliente;
    private List<DetalleVenta> listDetalles = new ArrayList<>();
    private double total;

    public Venta(Cliente cliente) {
        this.cliente = cliente;
        this.total = 0.0;
    }

    public void agregarDetalle(Producto producto, int cantidad) {
        DetalleVenta detalle = new DetalleVenta(producto, cantidad);
        listDetalles.add(detalle);
        total += detalle.getSubtotal();
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<DetalleVenta> getListDetalles() {
        return listDetalles;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Venta a: ").append(cliente.getNombre()).append(" ").append(cliente.getApellido())
                .append("\nDetalles:\n");

        for (DetalleVenta detalle : listDetalles) {
            sb.append("- ")
                    .append(detalle.getProducto().getNombre())
                    .append(": ")
                    .append(detalle.getCantidad())
                    .append(" x $").append(detalle.getProducto().getPrecio())
                    .append(" = $").append(String.format("%.2f", detalle.getSubtotal()))
                    .append("\n");
        }

        sb.append("TOTAL VENTA: $").append(String.format("%.2f", total));
        return sb.toString();
    }
}
