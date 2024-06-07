package pucrs.es2.servico_pagamentos.web;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pucrs.es2.servico_pagamentos.entities.Pagamento;
import pucrs.es2.servico_pagamentos.entities.RequisicaoRegistroPgtoDTO;
import pucrs.es2.servico_pagamentos.persistence.PagamentoRepository;

@RestController
public class MainController {
    @Autowired
    private PagamentoRepository pagamentoRepository;

    @PostMapping("/registrarpagamento")
    @CrossOrigin("*")
    public ResponseEntity<String> registrarPagamento(@RequestBody RequisicaoRegistroPgtoDTO body) {
        try {
            Pagamento pgto = new Pagamento(body.codass(), body.valorPago(), LocalDate.of(body.ano(), body.mes(), body.dia()));
            pagamentoRepository.save(pgto);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        // [TODO] emitir evento de pagamento

        // retornar resposta de sucesso
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}