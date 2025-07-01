package Main;

import Modelo.Cliente;
import Modelo.Producto;
import Modelo.Tienda;
import Modelo.Venta;
import MySQL.MySQL_Repository;

public class Main {
    public static void main(String[] args) {
        MySQL_Repository repo = new MySQL_Repository();
        Tienda tienda = new Tienda(repo, repo, repo);

        // Crear productos
        Producto camisa = new Producto(0, "Camisa", 25.5, 100);
        Producto pantalon = new Producto(0, "Pantalón", 40.0, 50);
        Producto zapatos = new Producto(0, "Zapatos", 79.99, 30);

        // Persistir productos en BD
        int idCamisa = tienda.agregarProducto(camisa);
        int idPantalon = tienda.agregarProducto(pantalon);
        int idZapatos = tienda.agregarProducto(zapatos);

        System.out.println("Productos creados:");
        System.out.println("- Camisa (ID: " + idCamisa + ")");
        System.out.println("- Pantalón (ID: " + idPantalon + ")");
        System.out.println("- Zapatos (ID: " + idZapatos + ")");
        System.out.println();

        // Crear clientes
        Cliente juan = new Cliente(0, "Juan", "Pérez", "juan@mail.com");
        Cliente ana = new Cliente(0, "Ana", "Gómez", "ana@mail.com");

        // Persistir clientes en BD
        int idJuan = tienda.agregarCliente(juan);
        int idAna = tienda.agregarCliente(ana);

        System.out.println("Clientes registrados:");
        System.out.println("- Juan Pérez (ID: " + idJuan + ")");
        System.out.println("- Ana Gómez (ID: " + idAna + ")");
        System.out.println();

        // Obtener productos completos desde BD (con datos actualizados)
        Producto camisaBD = repo.buscarProducto(idCamisa);
        Producto pantalonBD = repo.buscarProducto(idPantalon);
        Producto zapatosBD = repo.buscarProducto(idZapatos);

        // Obtener clientes completos desde BD
        Cliente juanBD = repo.buscarCliente(idJuan);
        Cliente anaBD = repo.buscarCliente(idAna);

        // Crear ventas
        Venta venta1 = new Venta(juanBD);
        venta1.agregarDetalle(camisaBD, 3);
        venta1.agregarDetalle(pantalonBD, 2);

        Venta venta2 = new Venta(anaBD);
        venta2.agregarDetalle(zapatosBD, 1);
        venta2.agregarDetalle(camisaBD, 1);

        Venta venta3 = new Venta(juanBD);
        venta3.agregarDetalle(zapatosBD, 2);
        venta3.agregarDetalle(pantalonBD, 1);

        try {
            System.out.println("Procesando ventas...");
            tienda.registrarVenta(venta1);
            tienda.registrarVenta(venta2);
            tienda.registrarVenta(venta3);

            // Intentar venta con stock insuficiente
            Venta ventaError = new Venta(anaBD);
            ventaError.agregarDetalle(pantalonBD, 100);

            try {
                tienda.registrarVenta(ventaError);
            } catch (IllegalStateException e) {
                System.out.println("\n¡Error en venta!: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Error crítico: " + e.getMessage());
            e.printStackTrace();
        }

        // Mostrar todas las ventas realizadas
        tienda.mostrarVentas();
    }
}