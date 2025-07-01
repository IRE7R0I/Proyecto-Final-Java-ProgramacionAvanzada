package Modelo.Interfaces;

import Modelo.Cliente;

public interface ClienteRepository {
    int almacenarCliente(Cliente cliente);
    Cliente buscarCliente(int id);
}