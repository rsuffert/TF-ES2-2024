package pucrs.es2.servico_assinaturas_validas.rabbit_mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import pucrs.es2.servico_assinaturas_validas.CacheService;

public class ConsumidorNotificacaoPagamento {
    @RabbitListener(queues = RabbitMQConfig.QUEUENAME)
    public void receberNotificacao(NotificacaoPagamento notificacao) {
        // remover assinatura paga da cache
        CacheService.removeAssinaturaByCodigo(notificacao.codass());
    }
}