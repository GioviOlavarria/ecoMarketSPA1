package com.example.ecomarketspa.controladores;

import com.example.ecomarketspa.servicio.servicioUsuarios;
import com.example.ecomarketspa.entidades.usuarios;
import com.example.ecomarketspa.controlador.controladorUsuarios;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class controladorUsuariosTest {

    @Mock
    private servicioUsuarios servicioUsuarios;

    @InjectMocks
    private controladorUsuarios controladorUsuarios;

    @BeforeEach
    public void setUp() {
        // Inicializar los objetos Mock si es necesario
    }

    @Test
    public void testObtenerUsuarios() {
        when(servicioUsuarios.listarUsuarios()).thenReturn(new ArrayList<>());

        List<usuarios> response = controladorUsuarios.getAll();

        assertNotNull(response);
        assertTrue(response instanceof ArrayList);
    }

    @Test
    public void testCrearUsuario() {
        usuarios usuarioMock = new usuarios();
        usuarioMock.setId_usuario(1L);
        usuarioMock.setPrimerNombre("Juan");
        usuarioMock.setPrimerApellido("Perez");

        controladorUsuarios.guardarActualizar(usuarioMock);

        verify(servicioUsuarios).guardarOActualizar(usuarioMock);
    }

    @Test
    public void testObtenerUsuarioPorId() {
        usuarios usuarioMock = new usuarios();
        usuarioMock.setId_usuario(1L);
        when(servicioUsuarios.listarUsuarios(1L)).thenReturn(Optional.of(usuarioMock));

        Optional<usuarios> response = controladorUsuarios.getBId(1L);

        assertTrue(response.isPresent());
        assertEquals(1L, response.get().getId_usuario());
    }

    @Test
    public void testEliminarUsuario() {
        controladorUsuarios.delete(1L);

        verify(servicioUsuarios).borrar(1L);
    }
}