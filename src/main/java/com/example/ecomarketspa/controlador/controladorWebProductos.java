package com.example.ecomarketspa.controlador;

import com.example.ecomarketspa.entidades.productos;
import com.example.ecomarketspa.servicio.servicioProductos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/web/productos")
public class controladorWebProductos {

    @Autowired
    private servicioProductos servicioProductos;

    @GetMapping
    public String listarProductos(Model model) {
        model.addAttribute("productos", servicioProductos.listarProductos());
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new productos());
        model.addAttribute("titulo", "Nuevo Producto");
        return "productos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<productos> producto = servicioProductos.listarProducto(id);
        if (producto.isPresent()) {
            model.addAttribute("producto", producto.get());
            model.addAttribute("titulo", "Editar Producto");
            return "productos/formulario";
        } else {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/web/productos";
        }
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute productos producto, RedirectAttributes redirectAttributes) {
        try {
            servicioProductos.guardarOActualizar(producto);
            redirectAttributes.addFlashAttribute("success",
                    producto.getId() == null ? "Producto creado exitosamente" : "Producto actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el producto: " + e.getMessage());
        }
        return "redirect:/web/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            servicioProductos.borrar(id);
            redirectAttributes.addFlashAttribute("success", "Producto eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el producto: " + e.getMessage());
        }
        return "redirect:/web/productos";
    }

    @GetMapping("/ver/{id}")
    public String verProducto(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<productos> producto = servicioProductos.listarProducto(id);
        if (producto.isPresent()) {
            model.addAttribute("producto", producto.get());
            return "productos/detalle";
        } else {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/web/productos";
        }
    }
}