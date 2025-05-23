package com.example.ecomarketspa.entidades;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "compras")
public class compras {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_compra;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonBackReference("usuario-compras")
    private usuarios usuario;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private productos producto;

    @Column(nullable = false)
    private LocalDate fechaCompra;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column
    @Enumerated(EnumType.STRING)
    private EstadoCompra estado;

    public enum EstadoCompra {
        PROCESANDO,
        CONFIRMADA,
        ENVIADA,
        ENTREGADA,
        CANCELADA
    }
}