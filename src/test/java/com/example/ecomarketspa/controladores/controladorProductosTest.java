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

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testObtenerProductos() {

        when(servicioProductos.listarProductos()).thenReturn(new ArrayList<>());


        CollectionModel<EntityModel<productos>> response = controladorProductos.getAll();


        assertNotNull(response);

        assertTrue(response.getContent().isEmpty());
    }

    @Test
    public void testCrearProducto() {

        productos productoMock = new productos();
        productoMock.setId(1L);
        productoMock.setNombre("Producto1");
        productoMock.setPrecio(BigDecimal.valueOf(100.00));


        EntityModel<productos> response = controladorProductos.guardarActualizar(productoMock);


        verify(servicioProductos).guardarOActualizar(productoMock);
        assertNotNull(response);
        assertEquals(productoMock, response.getContent());
    }

    @Test
    public void testObtenerProductoPorId() {

        productos productoMock = new productos();
        productoMock.setId(1L);
        when(servicioProductos.listarProducto(1L)).thenReturn(Optional.of(productoMock));


        EntityModel<productos> response = controladorProductos.getBId(1L);


        assertNotNull(response);
        assertEquals(1L, response.getContent().getId());
    }

    @Test
    public void testEliminarProducto() {

        controladorProductos.delete(1L);


        verify(servicioProductos).borrar(1L);
    }
}