package com.example.ecomarketspa.controladores;

import com.example.ecomarketspa.controlador.controladorCompras;
import com.example.ecomarketspa.servicio.servicioCompras;
import com.example.ecomarketspa.entidades.compras;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class controladorComprasTest {

    @Mock
    private servicioCompras servicioCompras;

    @InjectMocks
    private controladorCompras controladorCompras;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testObtenerCompras() {
        when(servicioCompras.listarCompras()).thenReturn(new ArrayList<>());


        List<compras> response = controladorCompras.obtenerTodas();

        assertNotNull(response);
        verify(servicioCompras).listarCompras();
    }

    @Test
    public void testCrearCompra() {
        compras compraMock = new compras();
        compraMock.setId_compra(1L);

        when(servicioCompras.crearCompra(1L, 1L, 1)).thenReturn(compraMock);


        ResponseEntity<?> response = controladorCompras.crearCompra(1L, 1L, 1);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(compraMock, response.getBody());
    }
}