package pucrs.es2.servico_cadastramento.rabbit_mq;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import pucrs.es2.servico_cadastramento.entities.Assinatura;
import pucrs.es2.servico_cadastramento.persistence.AssinaturaRepository;

@Service
public class ConsumidorNotificacaoPagamento {
    public static final int DURACAO_ASSINATURA_DIAS = 30;

    @Autowired
    private AssinaturaRepository assinaturaRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUENAME)
    @Transactional
    public void receberNotificacao(NotificacaoPagamento notificacao) {
        // obter a assinatura correspondente ao codigo da notificacao
        Optional<Assinatura> assinatura = assinaturaRepository.findById(notificacao.codass());

        // verificar se ela existe
        if (!assinatura.isPresent()) return;

        // se tudo certo, extrair o objeto da assinatura
        Assinatura ass = assinatura.get();

        // verificar se o valor pago eh suficiente
        if (notificacao.valorPago() < ass.getAplicativo().getCustoMensal()) return;

        // prorrogar a vigencia da assinatura
        ass.prorrogarVigencia(LocalDate.of(notificacao.ano(), notificacao.mes(), notificacao.dia()), DURACAO_ASSINATURA_DIAS);

        // persistir no banco
        assinaturaRepository.save(ass);
    }
}