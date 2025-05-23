package com.example.ecomarketspa.controlador;

import com.example.ecomarketspa.entidades.usuarios;
import com.example.ecomarketspa.servicio.servicioUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/web/usuarios")
public class controladorWebUsuarios {

    @Autowired
    private servicioUsuarios servicioUsuarios;

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", servicioUsuarios.listarUsuarios());
        return "usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuario", new usuarios());
        model.addAttribute("titulo", "Nuevo Usuario");
        return "usuarios/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<usuarios> usuario = servicioUsuarios.listarUsuarios(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            model.addAttribute("titulo", "Editar Usuario");
            return "usuarios/formulario";
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/web/usuarios";
        }
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute usuarios usuario, RedirectAttributes redirectAttributes) {
        try {
            servicioUsuarios.guardarOActualizar(usuario);
            redirectAttributes.addFlashAttribute("success",
                    usuario.getId_usuario() == null ? "Usuario creado exitosamente" : "Usuario actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el usuario: " + e.getMessage());
        }
        return "redirect:/web/usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            servicioUsuarios.borrar(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario: " + e.getMessage());
        }
        return "redirect:/web/usuarios";
    }

    @GetMapping("/ver/{id}")
    public String verUsuario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<usuarios> usuario = servicioUsuarios.listarUsuarios(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            return "usuarios/detalle";
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/web/usuarios";
        }
    }
}