package pucrs.es2.servico_assinaturas_validas.rabbit_mq;

public record NotificacaoPagamento(int dia, int mes, int ano, Long codass, float valorPago) {}
