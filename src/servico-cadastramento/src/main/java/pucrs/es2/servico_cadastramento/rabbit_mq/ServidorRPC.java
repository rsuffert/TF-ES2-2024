package pucrs.es2.servico_cadastramento.rabbit_mq;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pucrs.es2.servico_cadastramento.entities.Assinatura;
import pucrs.es2.servico_cadastramento.persistence.AssinaturaRepository;

@Service
public class ServidorRPC {
    @Autowired
    private AssinaturaRepository assinaturaRepository;
    
    @RabbitListener(queues = RabbitMQConfig.RPC_QUEUE)
    public LocalDate obterValidadeAssinatura(Long codass) {
        System.out.println("Chamada RPC recebida! " + codass);
        // localizar a assinatura
        Optional<Assinatura> assinatura = assinaturaRepository.findById(codass);

        // verificar sua existencia
        if (!assinatura.isPresent()) return null;

        System.out.println("A assinatura solicitada existe. Prosseguindo...");

        // verificar sua validade
        Assinatura ass = assinatura.get();
        System.out.println("Validade da assinatura solicitada: " + ass.getFimVigencia());
        return ass.getFimVigencia();
    }
}