package com.example.ecomarketspa.controladores;

import com.example.ecomarketspa.servicio.servicioUsuarios;
import com.example.ecomarketspa.entidades.usuarios;
import com.example.ecomarketspa.controlador.controladorUsuarios;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

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

        CollectionModel<EntityModel<usuarios>> response = controladorUsuarios.getAll();

        assertNotNull(response);
        assertTrue(response.getContent().isEmpty());
    }

    @Test
    public void testCrearUsuario() {
        usuarios usuarioMock = new usuarios();
        usuarioMock.setId_usuario(1L);
        usuarioMock.setPrimerNombre("Juan");
        usuarioMock.setPrimerApellido("Perez");

        EntityModel<usuarios> response = controladorUsuarios.guardar(usuarioMock);

        verify(servicioUsuarios).guardarOActualizar(usuarioMock);
        assertNotNull(response);
        assertEquals(usuarioMock, response.getContent());
    }

    @Test
    public void testObtenerUsuarioPorId() {
        usuarios usuarioMock = new usuarios();
        usuarioMock.setId_usuario(1L);
        when(servicioUsuarios.listarUsuarios(1L)).thenReturn(Optional.of(usuarioMock));

        ResponseEntity<EntityModel<usuarios>> response = controladorUsuarios.getBId(1L);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getContent().getId_usuario());
    }

    @Test
    public void testObtenerUsuarioNoExistente() {
        when(servicioUsuarios.listarUsuarios(1L)).thenReturn(Optional.empty());

        ResponseEntity<EntityModel<usuarios>> response = controladorUsuarios.getBId(1L);

        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    public void testActualizarUsuario() {
        usuarios usuarioMock = new usuarios();
        usuarioMock.setId_usuario(1L);
        usuarioMock.setPrimerNombre("NuevoNombre");

        when(servicioUsuarios.listarUsuarios(1L)).thenReturn(Optional.of(usuarioMock));

        ResponseEntity<EntityModel<usuarios>> response = controladorUsuarios.actualizar(1L, usuarioMock);

        verify(servicioUsuarios).guardarOActualizar(usuarioMock);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(usuarioMock, response.getBody().getContent());
    }

    @Test
    public void testEliminarUsuario() {
        usuarios usuarioMock = new usuarios();
        usuarioMock.setId_usuario(1L);
        when(servicioUsuarios.listarUsuarios(1L)).thenReturn(Optional.of(usuarioMock));

        ResponseEntity<CollectionModel<EntityModel<usuarios>>> response = controladorUsuarios.delete(1L);

        verify(servicioUsuarios).borrar(1L);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}