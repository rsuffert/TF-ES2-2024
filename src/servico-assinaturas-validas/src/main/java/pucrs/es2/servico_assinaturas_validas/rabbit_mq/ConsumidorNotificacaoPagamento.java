package pucrs.es2.servico_assinaturas_validas.rabbit_mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import pucrs.es2.servico_assinaturas_validas.CacheService;

@Service
public class ConsumidorNotificacaoPagamento {
    @RabbitListener(queues = RabbitMQConfig.QUEUENAME)
    public void receberNotificacao(NotificacaoPagamento notificacao) {
        // remover assinatura paga da cache
        CacheService.removeAssinaturaByCodigo(notificacao.codass());
    }
}