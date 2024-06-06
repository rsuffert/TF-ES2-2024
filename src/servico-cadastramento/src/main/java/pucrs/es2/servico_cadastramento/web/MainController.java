package pucrs.es2.servico_cadastramento.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rabbitmq.client.RpcClient.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import pucrs.es2.servico_cadastramento.persistence.AplicativoRepository;
import pucrs.es2.servico_cadastramento.persistence.AssinaturaRepository;
import pucrs.es2.servico_cadastramento.persistence.ClienteRepository;
import pucrs.es2.servico_cadastramento.dtos.AplicativoDTO;
import pucrs.es2.servico_cadastramento.dtos.AssinaturaDTO;
import pucrs.es2.servico_cadastramento.dtos.ClienteDTO;
import pucrs.es2.servico_cadastramento.dtos.RequisicaoAssinaturaDTO;
import pucrs.es2.servico_cadastramento.dtos.RequisicaoAtualizarPrecoAppDTO;
import pucrs.es2.servico_cadastramento.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/servcad/clientes")
    @CrossOrigin("*")
    public ResponseEntity<List<ClienteDTO>> getClients() {
        List<Cliente> clients = clienteRepository.findAll();
        List<ClienteDTO> clientsDto = new ArrayList<>(clients.size());
        for (Cliente cli : clients)
            clientsDto.add(new ClienteDTO(cli.getCodigo(), cli.getNome(), cli.getEmail()));
        return ResponseEntity.ok(clientsDto);
    }

    @GetMapping("servcad/aplicativos")
    @CrossOrigin("*")
    public ResponseEntity<List<AplicativoDTO>> getAplicativos() {
        List<Aplicativo> aplicativos = aplicativoRepository.findAll();
        List<AplicativoDTO> aplicativosDto = new ArrayList<>(aplicativos.size());
        for (Aplicativo app : aplicativos)
            aplicativosDto.add(new AplicativoDTO(app.getCodigo(), app.getNome(), app.getCustoMensal()));
        return ResponseEntity.ok(aplicativosDto);
    }

    @PostMapping("/servcad/assinaturas")
    @CrossOrigin("*")
    public ResponseEntity<AssinaturaDTO> criarAssinatura(@RequestBody RequisicaoAssinaturaDTO reqAssinaturaDto) {
        // buscar o aplicativo e o cliente correspondentes aos codigos fornecidos
        Optional<Aplicativo> aplicativo = aplicativoRepository.findById(reqAssinaturaDto.codigoAplicativo());
        Optional<Cliente> cliente = clienteRepository.findById(reqAssinaturaDto.codigoCliente());

        // verificar se eles existem (se a busca retornou alguma coisa)
        if (!aplicativo.isPresent() || !cliente.isPresent()) return ResponseEntity.notFound().build();

        // se tudo certo, instanciar a assinatura e salvar na base de dados
        Assinatura assinatura = new Assinatura(aplicativo.get(), cliente.get());
        assinaturaRepository.save(assinatura);

        // retornar a assinatura criada
        return ResponseEntity.status(HttpStatus.CREATED).body(new AssinaturaDTO(assinatura.getCodigo(), assinatura.getCliente().getNome(), 
                                                              assinatura.getAplicativo().getNome(), assinatura.getInicioVigencia(), assinatura.getFimVigencia()));
    }

    @PatchMapping("/servcad/aplicativos/{codigoAplicativo}")
    @CrossOrigin("*")
    public ResponseEntity<String> atualizarValorDeAplicativo(@PathVariable Long codigoAplicativo, @RequestBody RequisicaoAtualizarPrecoAppDTO body) {
        // obter inputs
        Optional<Aplicativo> aplicativo = aplicativoRepository.findById(codigoAplicativo);
        float novoPreco = body.preco();

        // validar inputs
        if (!aplicativo.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Código de aplicativo inexistente");
        if (novoPreco < 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O novo preço do aplicativo não pode ser negativo");

        // efetuar a atualizacao
        Aplicativo app = aplicativo.get();
        app.setCustoMensal(novoPreco);
        aplicativoRepository.save(app);

        // retornar sucesso
        return ResponseEntity.ok("Valor atualizado com sucesso");
    }
}