package com.example.ecomarketspa.controladores;

import com.example.ecomarketspa.servicio.servicioProductos;
import com.example.ecomarketspa.entidades.productos;
import com.example.ecomarketspa.controlador.controladorProductos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class controladorProductosTest {

    @Mock
    private servicioProductos servicioProductos;

    @InjectMocks
    private controladorProductos controladorProductos;

    private productos productoTest;

    @BeforeEach
    public void setUp() {
        productoTest = new productos();
        productoTest.setId(1L);
        productoTest.setNombre("Producto Test");
        productoTest.setDescripcion("Descripción test");
        productoTest.setPrecio(BigDecimal.valueOf(100.00));
    }

    @Test
    public void testObtenerProductos() {
        List<productos> productosList = new ArrayList<>();
        productosList.add(productoTest);

        when(servicioProductos.listarProductos()).thenReturn(productosList);

        CollectionModel<EntityModel<productos>> response = controladorProductos.getAll();

        assertNotNull(response);
        assertFalse(response.getContent().isEmpty());
    }

    @Test
    public void testObtenerProductoPorId() {
        when(servicioProductos.listarProducto(1L)).thenReturn(Optional.of(productoTest));

        ResponseEntity<EntityModel<productos>> response = controladorProductos.getBId(1L);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(1L, response.getBody().getContent().getId());
    }

    @Test
    public void testObtenerProductoNoExistente() {
        when(servicioProductos.listarProducto(99L)).thenReturn(Optional.empty());

        ResponseEntity<EntityModel<productos>> response = controladorProductos.getBId(99L);

        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    public void testCrearProducto() {
        EntityModel<productos> response = controladorProductos.guardar(productoTest);

        verify(servicioProductos).guardarOActualizar(productoTest);
        assertNotNull(response);
        assertEquals(productoTest, response.getContent());
    }

    @Test
    public void testActualizarProducto() {
        productos productoActualizado = new productos();
        productoActualizado.setId(1L);
        productoActualizado.setNombre("Nombre Actualizado");
        productoActualizado.setPrecio(BigDecimal.valueOf(150.00));

        when(servicioProductos.listarProducto(1L)).thenReturn(Optional.of(productoTest));

        ResponseEntity<EntityModel<productos>> response = controladorProductos.actualizar(1L, productoActualizado);

        verify(servicioProductos).guardarOActualizar(productoActualizado);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testEliminarProducto() {
        when(servicioProductos.listarProducto(1L)).thenReturn(Optional.of(productoTest));

        ResponseEntity<CollectionModel<EntityModel<productos>>> response = controladorProductos.delete(1L);

        verify(servicioProductos).borrar(1L);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}