package pucrs.es2.servico_cadastramento.rabbit_mq;

public record NotificacaoPagamento(int dia, int mes, int ano, Long codass, float valorPago) {}
