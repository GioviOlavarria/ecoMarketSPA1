package com.example.ecomarketspa.controladores;

import com.example.ecomarketspa.controlador.controladorCompras;
import com.example.ecomarketspa.servicio.servicioCompras;
import com.example.ecomarketspa.entidades.compras;
import com.example.ecomarketspa.entidades.usuarios;
import com.example.ecomarketspa.entidades.productos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class controladorComprasTest {

    @Mock
    private servicioCompras servicioCompras;

    @InjectMocks
    private controladorCompras controladorCompras;

    private compras compraTest;
    private usuarios usuarioTest;
    private productos productoTest;

    @BeforeEach
    public void setUp() {
        usuarioTest = new usuarios();
        usuarioTest.setId_usuario(1L);

        productoTest = new productos();
        productoTest.setId(1L);
        productoTest.setPrecio(BigDecimal.valueOf(100.0));

        compraTest = new compras();
        compraTest.setId_compra(1L);
        compraTest.setUsuario(usuarioTest);
        compraTest.setProducto(productoTest);
        compraTest.setCantidad(1);
        compraTest.setFechaCompra(LocalDate.now());
        compraTest.setPrecioUnitario(BigDecimal.valueOf(100.0));
        compraTest.setTotal(BigDecimal.valueOf(100.0));
        compraTest.setEstado(compras.EstadoCompra.PROCESANDO);
    }

    @Test
    public void testObtenerCompras() {
        List<compras> comprasList = new ArrayList<>();
        comprasList.add(compraTest);

        when(servicioCompras.listarCompras()).thenReturn(comprasList);

        CollectionModel<EntityModel<compras>> response = controladorCompras.obtenerTodas();

        assertNotNull(response);
        assertFalse(response.getContent().isEmpty());
        verify(servicioCompras).listarCompras();
    }

    @Test
    public void testObtenerCompraPorId() {
        when(servicioCompras.listarCompra(1L)).thenReturn(Optional.of(compraTest));

        ResponseEntity<EntityModel<compras>> response = controladorCompras.obtenerPorId(1L);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(1L, response.getBody().getContent().getId_compra());
    }

    @Test
    public void testObtenerComprasPorUsuario() {
        List<compras> comprasList = new ArrayList<>();
        comprasList.add(compraTest);

        when(servicioCompras.listarComprasPorUsuario(1L)).thenReturn(comprasList);

        CollectionModel<EntityModel<compras>> response = controladorCompras.obtenerPorUsuario(1L);

        assertNotNull(response);
        assertFalse(response.getContent().isEmpty());
    }

    @Test
    public void testCrearCompra() {
        when(servicioCompras.crearCompra(1L, 1L, 1)).thenReturn(compraTest);

        ResponseEntity<?> response = controladorCompras.crearCompra(1L, 1L, 1);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof EntityModel);
    }

    @Test
    public void testConfirmarCompra() {
        compraTest.setEstado(compras.EstadoCompra.CONFIRMADA);
        when(servicioCompras.confirmarCompra(1L)).thenReturn(compraTest);

        ResponseEntity<?> response = controladorCompras.confirmarCompra(1L);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(compras.EstadoCompra.CONFIRMADA, ((EntityModel<compras>) response.getBody()).getContent().getEstado());
    }

    @Test
    public void testCancelarCompra() {
        compraTest.setEstado(compras.EstadoCompra.CANCELADA);
        when(servicioCompras.cancelarCompra(1L)).thenReturn(compraTest);

        ResponseEntity<?> response = controladorCompras.cancelarCompra(1L);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(compras.EstadoCompra.CANCELADA, ((EntityModel<compras>) response.getBody()).getContent().getEstado());
    }
}