package pucrs.es2.servico_assinaturas_validas.web;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import pucrs.es2.servico_assinaturas_validas.CacheService;
import pucrs.es2.servico_assinaturas_validas.entities.Assinatura;

@RestController
public class MainController {
    @GetMapping("/assinvalidas/{codass}")
    @CrossOrigin("*")
    public ResponseEntity<Boolean> verificarValidade(@PathVariable Long codass) {
        boolean valid;
        
        Assinatura ass = CacheService.getAssinaturaByCodigo(codass);
        if (ass != null) { // cache hit
            valid = !(LocalDate.now().isAfter(ass.fimVigencia()));
        }
        else { // cache miss
            // [TODO] implementar RPC para servico-cadastramento para verificar se a assinatura de codigo codass ainda eh valida
            valid = false;
        }

        return ResponseEntity.ok(valid);
    }
}
