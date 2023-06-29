package com.diego.posinfnet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

class Assinatura {
    private BigDecimal mensalidade;
    private LocalDate begin;
    private LocalDate end;
    private Cliente cliente;

    public Assinatura(BigDecimal mensalidade, LocalDate begin, Cliente cliente) {
        this.mensalidade = mensalidade;
        this.begin = begin;
        this.cliente = cliente;
    }

    public Assinatura(BigDecimal mensalidade, LocalDate begin, LocalDate end, Cliente cliente) {
        this.mensalidade = mensalidade;
        this.begin = begin;
        this.end = end;
        this.cliente = cliente;
    }

    public BigDecimal getMensalidade() {
        return mensalidade;
    }

    public LocalDate getBegin() {
        return begin;
    }

    public LocalDate getEnd() {
        return end;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public boolean isAtiva() {
        return end == null || end.isAfter(LocalDate.now());
    }

    public int getTempoEmMeses() {
        if (isAtiva()) {
            return (int) Period.between(begin, LocalDate.now()).toTotalMonths();
        } else {
            return (int) Period.between(begin, end).toTotalMonths();
        }
    }

    public BigDecimal getValorPago() {
        if (isAtiva()) {
            int meses = getTempoEmMeses();
            return mensalidade.multiply(BigDecimal.valueOf(meses));
        } else {
            return mensalidade.multiply(BigDecimal.valueOf(getTempoEmMeses()));
        }
    }
}