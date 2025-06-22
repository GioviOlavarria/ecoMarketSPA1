package com.example.ecomarketspa.controlador;

import org.springframework.web.bind.annotation.GetMapping;

public class controladorPrincipal {

    @GetMapping("/")
    public String inicio() {
        return "redirect:/web/compras";
    }

    @GetMapping("/web")
    public String dashboard() {
        return "redirect:/web/compras";
    }
}
