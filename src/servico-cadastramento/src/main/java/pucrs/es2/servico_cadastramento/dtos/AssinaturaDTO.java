package pucrs.es2.servico_cadastramento.dtos;

import java.time.LocalDate;

public record AssinaturaDTO (Long codigoAssinatura, String nomeCliente, String nomeAplicativo, LocalDate dataInicio, LocalDate dataFim, String status) {}
