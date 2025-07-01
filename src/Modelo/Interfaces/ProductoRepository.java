package Modelo.Interfaces;

import Modelo.Producto;

public interface ProductoRepository {
    int ingresarProducto(Producto producto);
    void actualizarProducto(Producto producto);
    Producto buscarProducto(int id);
}