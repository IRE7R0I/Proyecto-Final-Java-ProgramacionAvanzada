package MySQL;

import Modelo.Cliente;
import Modelo.DetalleVenta;
import Modelo.Interfaces.ClienteRepository;
import Modelo.Interfaces.ProductoRepository;
import Modelo.Interfaces.VentaRepository;
import Modelo.Producto;
import Modelo.Venta;

import java.sql.*;
import java.util.List;

public class MySQL_Repository implements ProductoRepository, ClienteRepository, VentaRepository {

    private final String URL = "jdbc:mysql://localhost:3306/";
    private final String URL_CON_DB = "jdbc:mysql://localhost:3306/textil";
    private final String USER = "root";
    private final String PASSWORD = "root";

    private static boolean baseDeDatosInicializada = false;

    private Connection getConexion() throws SQLException {
        inicializarBaseDeDatos();
        return DriverManager.getConnection(URL_CON_DB, USER, PASSWORD);
    }

    private synchronized void inicializarBaseDeDatos() {
        if (baseDeDatosInicializada) return; //Esto indica que si la base de datos ya se encuentra incializada no hace el metodo

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Crear base de datos si no existe
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS textil");

            try (Connection connTextil = DriverManager.getConnection(URL_CON_DB, USER, PASSWORD);
                 Statement stmtTextil = connTextil.createStatement()) {

                // Crear tabla cliente
                stmtTextil.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS cliente (" +
                                "ID_Cliente INT NOT NULL AUTO_INCREMENT, " +
                                "Nombre_Cliente VARCHAR(50) DEFAULT NULL, " +
                                "Apellido_Cliente VARCHAR(50) DEFAULT NULL, " +
                                "Correo_Electronico VARCHAR(100) DEFAULT NULL, " +
                                "PRIMARY KEY (ID_Cliente)" +
                                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci"
                );

                // Crear tabla producto
                stmtTextil.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS producto (" +
                                "ID_Producto INT NOT NULL AUTO_INCREMENT, " +
                                "Nombre_Producto VARCHAR(100) DEFAULT NULL, " +
                                "Precio DECIMAL(10,2) DEFAULT NULL, " +
                                "Cantidad INT DEFAULT NULL, " +
                                "PRIMARY KEY (ID_Producto)" +
                                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci"
                );

                // Crear tabla venta
                stmtTextil.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS venta (" +
                                "ID_Venta INT NOT NULL AUTO_INCREMENT, " +
                                "ID_Cliente INT DEFAULT NULL, " +
                                "Fecha DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                                "Total DECIMAL(10,2) DEFAULT NULL, " +
                                "PRIMARY KEY (ID_Venta), " +
                                "KEY ID_Cliente (ID_Cliente), " +
                                "CONSTRAINT fk_cliente FOREIGN KEY (ID_Cliente) REFERENCES cliente (ID_Cliente)" +
                                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci"
                );

                // Crear tabla detalleventa
                stmtTextil.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS detalleventa (" +
                                "ID_Detalle INT NOT NULL AUTO_INCREMENT, " +
                                "ID_Venta INT DEFAULT NULL, " +
                                "ID_Producto INT DEFAULT NULL, " +
                                "Cantidad INT DEFAULT NULL, " +
                                "Subtotal DECIMAL(10,2) DEFAULT NULL, " +
                                "PRIMARY KEY (ID_Detalle), " +
                                "KEY ID_Venta (ID_Venta), " +
                                "KEY ID_Producto (ID_Producto), " +
                                "CONSTRAINT fk_venta FOREIGN KEY (ID_Venta) REFERENCES venta (ID_Venta), " +
                                "CONSTRAINT fk_producto_detalle FOREIGN KEY (ID_Producto) REFERENCES producto (ID_Producto)" +
                                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci"
                );

                baseDeDatosInicializada = true;
                System.out.println("Base de datos y tablas inicializadas correctamente");

            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al inicializar la base de datos: " + e.getMessage(), e);
        }
    }

    // Productos
    @Override
    public int ingresarProducto(Producto producto) {
        String sql = "INSERT INTO producto (Nombre_Producto, Precio, Cantidad) VALUES (?, ?, ?)";
        try (Connection conexion = getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getCantidad());
            ps.executeUpdate(); //Aca es donde se va a mandar la query para el mysql 

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);//Devuelve el ID del producto
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar producto: " + e.getMessage(), e);
        }
        return -1;
    }

    @Override
    public void actualizarProducto(Producto producto) {
        String sql = "UPDATE producto SET Nombre_Producto = ?, Precio = ?, Cantidad = ? WHERE ID_Producto = ?";
        try (Connection conexion = getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getCantidad());
            ps.setInt(4, producto.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar producto: " + e.getMessage(), e);
        }
    }

    @Override
    public Producto buscarProducto(int id) {
        String sql = "SELECT * FROM producto WHERE ID_Producto = ?";
        try (Connection conexion = getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getInt("ID_Producto"),
                            rs.getString("Nombre_Producto"),
                            rs.getDouble("Precio"),
                            rs.getInt("Cantidad")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar producto: " + e.getMessage(), e);
        }
        return null;
    }

    // Clientes
    @Override
    public int almacenarCliente(Cliente cliente) {
        String sql = "INSERT INTO cliente (Nombre_Cliente, Apellido_Cliente, Correo_Electronico) VALUES (?, ?, ?)";
        try (Connection conexion = getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getEmail());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar cliente: " + e.getMessage(), e);
        }
        return -1;
    }

    @Override
    public Cliente buscarCliente(int id) {
        String sql = "SELECT * FROM cliente WHERE ID_Cliente = ?";
        try (Connection conexion = getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("ID_Cliente"),
                            rs.getString("Nombre_Cliente"),
                            rs.getString("Apellido_Cliente"),
                            rs.getString("Correo_Electronico")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente: " + e.getMessage(), e);
        }
        return null;
    }

    // Ventas
    @Override
    public int registrarVenta(Venta venta) {
        try (Connection conexion = getConexion()) {
            conexion.setAutoCommit(false); // Iniciar transacción

            try {
                // Insertar venta principal
                String sqlVenta = "INSERT INTO venta (ID_Cliente, Fecha, Total) VALUES (?, CURRENT_TIMESTAMP, ?)";
                int idVenta;

                try (PreparedStatement psVenta = conexion.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                    psVenta.setInt(1, venta.getCliente().getId());
                    psVenta.setDouble(2, venta.getTotal());
                    psVenta.executeUpdate();

                    try (ResultSet rs = psVenta.getGeneratedKeys()) {
                        if (rs.next()) idVenta = rs.getInt(1);
                        else throw new SQLException("No se obtuvo ID de venta");
                    }
                }

                // Insertar detalles y actualizar stock
                for (DetalleVenta detalle : venta.getListDetalles()) {
                    registrarDetalleVenta(conexion, idVenta, detalle);
                    actualizarStockProducto(conexion, detalle.getProducto().getId(), -detalle.getCantidad());
                }

                conexion.commit(); // Confirmar transacción
                return idVenta;

            } catch (SQLException e) {
                conexion.rollback(); // Revertir en caso de error
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar venta: " + e.getMessage(), e);
        }
    }

    private void registrarDetalleVenta(Connection conexion, int idVenta, DetalleVenta detalle) throws SQLException {
        String sql = "INSERT INTO detalleventa (ID_Venta, ID_Producto, Cantidad, Subtotal) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            ps.setInt(2, detalle.getProducto().getId());
            ps.setInt(3, detalle.getCantidad());
            ps.setDouble(4, detalle.getSubtotal());
            ps.executeUpdate();
        }
    }

    private void actualizarStockProducto(Connection conexion, int idProducto, int cambio) throws SQLException {
        String sql = "UPDATE producto SET Cantidad = Cantidad + ? WHERE ID_Producto = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, cambio);
            ps.setInt(2, idProducto);
            ps.executeUpdate();
        }
    }
}