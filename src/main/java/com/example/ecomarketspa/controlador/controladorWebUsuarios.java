package com.example.ecomarketspa.controlador;

import com.example.ecomarketspa.entidades.usuarios;
import com.example.ecomarketspa.servicio.servicioUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/web/usuarios")
public class controladorWebUsuarios {

    @Autowired
    private servicioUsuarios servicioUsuarios;

    @GetMapping
    public String listarUsuarios(Model model) {
        try {

            System.out.println("=== EJECUTANDO listarUsuarios() ===");

            List<usuarios> usuarios = servicioUsuarios.listarUsuarios();


            System.out.println("Usuarios obtenidos del servicio: " +
                    (usuarios != null ? usuarios.size() : "NULL"));


            if (usuarios == null) {
                usuarios = new ArrayList<>();
                System.out.println("⚠️ servicioUsuarios.listarUsuarios() devolvió NULL - creando lista vacía");
            }


            for (int i = 0; i < usuarios.size(); i++) {
                usuarios usuario = usuarios.get(i);
                System.out.println("Usuario " + (i+1) + ": " +
                        (usuario != null ?
                                (usuario.getPrimerNombre() != null ? usuario.getPrimerNombre() : "Sin nombre") +
                                        " - " +
                                        (usuario.getEmail() != null ? usuario.getEmail() : "Sin email")
                                : "NULL"));
            }

            model.addAttribute("usuarios", usuarios);


            System.out.println("Usuarios agregados al modelo: " + usuarios.size());
            System.out.println("Retornando vista: usuarios/lista");

            return "usuarios/lista";

        } catch (Exception e) {
            System.err.println("❌ ERROR en listarUsuarios(): " + e.getMessage());
            e.printStackTrace();


            model.addAttribute("usuarios", new ArrayList<usuarios>());
            model.addAttribute("error", "Error al cargar usuarios: " + e.getMessage());
            return "usuarios/lista";
        }
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        System.out.println("=== EJECUTANDO mostrarFormularioNuevo() ===");
        model.addAttribute("usuario", new usuarios());
        model.addAttribute("titulo", "Nuevo Usuario");
        return "usuarios/nuevo";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute usuarios usuario, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== EJECUTANDO guardarUsuario() ===");


            if (usuario != null) {
                System.out.println("Usuario a guardar:");
                System.out.println("- ID: " + usuario.getId_usuario());
                System.out.println("- Nombre: " + (usuario.getPrimerNombre() != null ? usuario.getPrimerNombre() : "NULL"));
                System.out.println("- Email: " + (usuario.getEmail() != null ? usuario.getEmail() : "NULL"));
                System.out.println("- Teléfono: " + (usuario.getTelefono() != null ? usuario.getTelefono() : "NULL"));
                System.out.println("- Activo: " + usuario.getActivo());
            } else {
                System.out.println("⚠️ Usuario es NULL");
            }


            if (usuario.getPrimerNombre() == null || usuario.getPrimerNombre().trim().isEmpty()) {
                throw new RuntimeException("El nombre del usuario es obligatorio");
            }

            if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
                throw new RuntimeException("El email del usuario es obligatorio");
            }

            servicioUsuarios.guardarOActualizar(usuario);

            System.out.println("✅ Usuario guardado exitosamente");

            redirectAttributes.addFlashAttribute("success",
                    usuario.getId_usuario() == null ? "Usuario creado exitosamente" : "Usuario actualizado exitosamente");

            System.out.println("Redirigiendo a: /web/usuarios");

        } catch (Exception e) {
            System.err.println("❌ ERROR al guardar usuario: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al guardar el usuario: " + e.getMessage());
        }

        return "redirect:/web/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== EJECUTANDO mostrarFormularioEditar() con ID: " + id + " ===");

            Optional<usuarios> usuario = servicioUsuarios.listarUsuarios(id);
            if (usuario.isPresent()) {
                System.out.println("Usuario encontrado: " + usuario.get().getPrimerNombre());
                model.addAttribute("usuario", usuario.get());
                model.addAttribute("titulo", "Editar Usuario");
                return "usuarios/nuevo";
            } else {
                System.out.println("⚠️ Usuario con ID " + id + " no encontrado");
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/web/usuarios";
            }
        } catch (Exception e) {
            System.err.println("❌ ERROR en mostrarFormularioEditar(): " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al cargar usuario: " + e.getMessage());
            return "redirect:/web/usuarios";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== EJECUTANDO eliminarUsuario() con ID: " + id + " ===");


            Optional<usuarios> usuario = servicioUsuarios.listarUsuarios(id);
            if (!usuario.isPresent()) {
                System.out.println("⚠️ Usuario con ID " + id + " no encontrado para eliminar");
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/web/usuarios";
            }

            servicioUsuarios.borrar(id);

            System.out.println("✅ Usuario eliminado exitosamente");
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente");

        } catch (Exception e) {
            System.err.println("❌ ERROR al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario: " + e.getMessage());
        }
        return "redirect:/web/usuarios";
    }

    @GetMapping("/ver/{id}")
    public String verUsuario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== EJECUTANDO verUsuario() con ID: " + id + " ===");

            Optional<usuarios> usuario = servicioUsuarios.listarUsuarios(id);
            if (usuario.isPresent()) {
                System.out.println("Usuario encontrado para mostrar: " + usuario.get().getPrimerNombre());
                model.addAttribute("usuario", usuario.get());
                return "usuarios/detalle";
            } else {
                System.out.println("⚠️ Usuario con ID " + id + " no encontrado");
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/web/usuarios";
            }
        } catch (Exception e) {
            System.err.println("❌ ERROR en verUsuario(): " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al cargar usuario: " + e.getMessage());
            return "redirect:/web/usuarios";
        }
    }
}