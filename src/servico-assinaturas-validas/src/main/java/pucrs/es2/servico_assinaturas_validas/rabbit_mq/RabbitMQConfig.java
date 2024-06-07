package pucrs.es2.servico_assinaturas_validas.rabbit_mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String FANOUTEXCHANGENAME = "exchange-notificacao-pagamento-assinatura";
    public static final String QUEUENAME = "fila-notificacao-pagamento-servico-assinaturas-validas";

    public static final String RPC_QUEUE = "rpc-requests-queue";
    public static final String DIRECTEXCHANGENAME = "rpc-exchange";

    // configuracao para suporte a eventos
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUTEXCHANGENAME);
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUENAME);
    }

    @Bean
    public Binding fanoutBinding(@Qualifier("queue") Queue q, FanoutExchange f) {
        return BindingBuilder.bind(q).to(f);
    }

    // configuracoes para suporte a RPC
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(DIRECTEXCHANGENAME);
    }

    @Bean
    public Queue rpcQueue() {
        return new Queue(RPC_QUEUE);
    }

    @Bean
    public Binding rpcBinding(@Qualifier("rpcQueue") Queue q, DirectExchange d) {
        return BindingBuilder.bind(q).to(d).with("rpc");
    }

    // configuracoes gerais
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory, MessageConverter messageConverter) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}