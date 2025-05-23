package com.example.ecomarketspa.controlador;

import com.example.ecomarketspa.entidades.compras;
import com.example.ecomarketspa.servicio.servicioCompras;
import com.example.ecomarketspa.servicio.servicioUsuarios;
import com.example.ecomarketspa.servicio.servicioProductos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/web/compras")
public class controladorWebCompras {

    @Autowired
    private servicioCompras servicioCompras;

    @Autowired
    private servicioUsuarios servicioUsuarios;

    @Autowired
    private servicioProductos servicioProductos;

    @GetMapping
    public String listarCompras(Model model) {
        model.addAttribute("compras", servicioCompras.listarCompras());
        return "compras/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("usuarios", servicioUsuarios.listarUsuarios());
        model.addAttribute("productos", servicioProductos.listarProductos());
        model.addAttribute("titulo", "Nueva Compra");
        return "compras/formulario";
    }

    @PostMapping("/realizar")
    public String realizarCompra(@RequestParam Long id_usuario,
                                 @RequestParam Long id_producto,
                                 @RequestParam(defaultValue = "1") int cantidad,
                                 RedirectAttributes redirectAttributes) {
        try {
            compras nuevaCompra = servicioCompras.crearCompra(id_usuario, id_producto, cantidad);
            redirectAttributes.addFlashAttribute("success", "Compra realizada exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al realizar la compra: " + e.getMessage());
        }
        return "redirect:/web/compras";
    }

    @GetMapping("/ver/{id}")
    public String verCompra(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<compras> compra = servicioCompras.listarCompra(id);
        if (compra.isPresent()) {
            model.addAttribute("compra", compra.get());
            model.addAttribute("estados", compras.EstadoCompra.values());
            return "compras/detalle";
        } else {
            redirectAttributes.addFlashAttribute("error", "Compra no encontrada");
            return "redirect:/web/compras";
        }
    }

    @PostMapping("/{id}/estado")
    public String actualizarEstado(@PathVariable Long id,
                                   @RequestParam compras.EstadoCompra estado,
                                   RedirectAttributes redirectAttributes) {
        try {
            servicioCompras.actualizarEstadoCompra(id, estado);
            redirectAttributes.addFlashAttribute("success", "Estado actualizado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el estado: " + e.getMessage());
        }
        return "redirect:/web/compras/ver/" + id;
    }

    @PostMapping("/{id}/confirmar")
    public String confirmarCompra(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            servicioCompras.confirmarCompra(id);
            redirectAttributes.addFlashAttribute("success", "Compra confirmada exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al confirmar la compra: " + e.getMessage());
        }
        return "redirect:/web/compras/ver/" + id;
    }

    @PostMapping("/{id}/cancelar")
    public String cancelarCompra(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            servicioCompras.cancelarCompra(id);
            redirectAttributes.addFlashAttribute("success", "Compra cancelada exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al cancelar la compra: " + e.getMessage());
        }
        return "redirect:/web/compras/ver/" + id;
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCompra(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            servicioCompras.borrar(id);
            redirectAttributes.addFlashAttribute("success", "Compra eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la compra: " + e.getMessage());
        }
        return "redirect:/web/compras";
    }

    @GetMapping("/usuario/{id}")
    public String comprasPorUsuario(@PathVariable Long id, Model model) {
        model.addAttribute("compras", servicioCompras.listarComprasPorUsuario(id));
        model.addAttribute("titulo", "Compras del Usuario");
        return "compras/lista";
    }

    @GetMapping("/producto/{id}")
    public String comprasPorProducto(@PathVariable Long id, Model model) {
        model.addAttribute("compras", servicioCompras.listarComprasPorProducto(id));
        model.addAttribute("titulo", "Compras del Producto");
        return "compras/lista";
    }
}