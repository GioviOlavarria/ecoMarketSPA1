package com.example.ecomarketspa.controlador;

import com.example.ecomarketspa.servicio.servicioProductos;
import com.example.ecomarketspa.servicio.servicioUsuarios;
import com.example.ecomarketspa.servicio.servicioCompras;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class controladorHome {

    @Autowired
    private servicioProductos servicioProductos;

    @Autowired
    private servicioUsuarios servicioUsuarios;

    @Autowired
    private servicioCompras servicioCompras;

    @GetMapping("/")
    public String home(Model model) {
        // Agregar datos básicos para mostrar en la página principal
        model.addAttribute("totalProductos", servicioProductos.listarProductos().size());
        model.addAttribute("totalUsuarios", servicioUsuarios.listarUsuarios().size());
        model.addAttribute("totalCompras", servicioCompras.listarCompras().size());
        model.addAttribute("productosRecientes", servicioProductos.listarProductos());
        
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("productos", servicioProductos.listarProductos());
        model.addAttribute("usuarios", servicioUsuarios.listarUsuarios());
        model.addAttribute("compras", servicioCompras.listarCompras());
        
        return "dashboard";
    }
}