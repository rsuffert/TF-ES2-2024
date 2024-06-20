package pucrs.es2.servico_assinaturas_validas.web;

import java.time.LocalDate;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import pucrs.es2.servico_assinaturas_validas.CacheService;
import pucrs.es2.servico_assinaturas_validas.entities.Assinatura;
import pucrs.es2.servico_assinaturas_validas.rabbit_mq.RabbitMQConfig;

@RestController
public class MainController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/assinvalidas/{codass}")
    @CrossOrigin("*")
    public ResponseEntity<Boolean> verificarValidade(@PathVariable Long codass) {
        boolean valid;
        Assinatura ass = CacheService.getAssinaturaByCodigo(codass);

        if (ass != null) // cache hit
            valid = !LocalDate.now().isAfter(ass.fimVigencia());
        else { // cache miss (fazer RPC ao servico-cadastramento para saber a validade da assinatura)
            Object responseObj = rabbitTemplate.convertSendAndReceive(RabbitMQConfig.DIRECTEXCHANGENAME, "rpc", codass);
            if (responseObj instanceof LocalDate) {
                LocalDate response = (LocalDate) responseObj;
                // adicionar na cache e verificar validade da assinatura
                CacheService.codigosAssinaturasValidas.add(new Assinatura(codass, response));
                valid = !LocalDate.now().isAfter(response);
            }
            else // assinatura nao existe 
                valid = false;
        }

        return ResponseEntity.ok(valid);
    }
}