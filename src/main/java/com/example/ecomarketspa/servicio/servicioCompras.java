package com.example.ecomarketspa.servicio;

import com.example.ecomarketspa.entidades.*;
import com.example.ecomarketspa.repositorio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@Service
public class servicioCompras {
    @Autowired
    repositorioCompras repositorioCompras;

    @Autowired
    repositorioProductos repositorioProductos;

    @Autowired
    repositorioUsuarios repositorioUsuarios;

    public List<compras> listarCompras() {
        return repositorioCompras.findAll();
    }

    public Optional<compras> listarCompra(Long id_compra) {
        return repositorioCompras.findById(id_compra);
    }

    public void guardarOActualizar(compras compra){
        repositorioCompras.save(compra);
    }

    public void borrar(Long id_compra) {
        repositorioCompras.deleteById(id_compra);
    }

    public compras crearCompra(Long id_usuario, Long id_producto, int cantidad) {
        usuarios usuario = repositorioUsuarios.findById(id_usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        productos producto = repositorioProductos.findById(id_producto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        LocalDate fechaCompra = LocalDate.now();
        BigDecimal precioUnitario = producto.getPrecio();
        BigDecimal total = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

        compras compra = new compras();
        compra.setUsuario(usuario);
        compra.setProducto(producto);
        compra.setFechaCompra(fechaCompra);
        compra.setCantidad(cantidad);
        compra.setPrecioUnitario(precioUnitario);
        compra.setTotal(total);
        compra.setEstado(compras.EstadoCompra.PROCESANDO);

        return repositorioCompras.save(compra);
    }

    public compras confirmarCompra(Long id_compra) {
        compras compra = repositorioCompras.findById(id_compra)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        if (compra.getEstado() == compras.EstadoCompra.CANCELADA) {
            throw new RuntimeException("No se puede confirmar una compra cancelada");
        }

        compra.setEstado(compras.EstadoCompra.CONFIRMADA);
        return repositorioCompras.save(compra);
    }

    public List<compras> listarComprasPorUsuario(Long id_usuario) {
        usuarios usuario = repositorioUsuarios.findById(id_usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return repositorioCompras.findByUsuario(usuario);
    }

    public List<compras> listarComprasPorProducto(Long id_producto) {
        productos producto = repositorioProductos.findById(id_producto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return repositorioCompras.findByProducto(producto);
    }

    public compras actualizarEstadoCompra(Long id_compra, compras.EstadoCompra nuevoEstado) {
        compras compra = repositorioCompras.findById(id_compra)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        compra.setEstado(nuevoEstado);
        return repositorioCompras.save(compra);
    }

    public compras cancelarCompra(Long id_compra) {
        compras compra = repositorioCompras.findById(id_compra)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        if (compra.getEstado() == compras.EstadoCompra.ENTREGADA) {
            throw new RuntimeException("No se puede cancelar una compra ya entregada");
        }

        compra.setEstado(compras.EstadoCompra.CANCELADA);
        return repositorioCompras.save(compra);
    }
}