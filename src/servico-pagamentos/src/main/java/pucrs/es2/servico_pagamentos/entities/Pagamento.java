package pucrs.es2.servico_pagamentos.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    private Long codAssinatura;

    private float valorPago;

    private LocalDate dataPagamento;

    public Pagamento(Long codAssinatura, float valorPago, LocalDate dataPagamento) throws IllegalArgumentException {
        if (valorPago <= 0) throw new IllegalArgumentException("Valor pago precisa ser positivo");
        this.codAssinatura = codAssinatura;
        this.valorPago = valorPago;
        this.dataPagamento = dataPagamento;
    }

    protected Pagamento() {}

    public Long getCodigo() { return this.codigo; }
    public Long getCodigoAssinatura() { return this.codAssinatura; }
    public float getValorPago() { return this.valorPago; }
    public LocalDate getDataPagamento() { return this.dataPagamento; }
}
