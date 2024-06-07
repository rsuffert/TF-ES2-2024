package pucrs.es2.servico_pagamentos.web;

import java.time.LocalDate;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pucrs.es2.servico_pagamentos.entities.NotificacaoPagamentoDTO;
import pucrs.es2.servico_pagamentos.entities.Pagamento;
import pucrs.es2.servico_pagamentos.persistence.PagamentoRepository;
import pucrs.es2.servico_pagamentos.rabbit_mq.RabbitMQConfig;

@RestController
public class MainController {
    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/registrarpagamento")
    @CrossOrigin("*")
    public ResponseEntity<String> registrarPagamento(@RequestBody NotificacaoPagamentoDTO notificacao) {
        try {
            Pagamento pgto = new Pagamento(notificacao.codass(), notificacao.valorPago(), LocalDate.of(notificacao.ano(), notificacao.mes(), notificacao.dia()));
            pagamentoRepository.save(pgto);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        // gerar evento de pagamento
        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUTEXCHANGENAME, "", notificacao);

        // retornar resposta de sucesso
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}