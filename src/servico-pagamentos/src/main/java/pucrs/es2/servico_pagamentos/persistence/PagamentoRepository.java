package pucrs.es2.servico_pagamentos.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import pucrs.es2.servico_pagamentos.entities.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long>{}