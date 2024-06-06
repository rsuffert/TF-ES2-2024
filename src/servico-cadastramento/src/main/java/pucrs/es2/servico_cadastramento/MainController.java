package pucrs.es2.servico_cadastramento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import pucrs.es2.servico_cadastramento.persistence.AplicativoRepository;
import pucrs.es2.servico_cadastramento.persistence.AssinaturaRepository;
import pucrs.es2.servico_cadastramento.persistence.ClienteRepository;

@RestController
public class MainController {
    @Autowired
    private AplicativoRepository aplicativoRepository;

    @Autowired
    private AssinaturaRepository assinaturaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/")
    @CrossOrigin("*")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello!");
    }
}