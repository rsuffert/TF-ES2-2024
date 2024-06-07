package pucrs.es2.servico_cadastramento.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
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
    public ResponseEntity<?> criarAssinatura(@RequestBody RequisicaoAssinaturaDTO reqAssinaturaDto) {
        // buscar o aplicativo e o cliente correspondentes aos codigos fornecidos
        Optional<Aplicativo> aplicativo = aplicativoRepository.findById(reqAssinaturaDto.codigoAplicativo());
        Optional<Cliente> cliente = clienteRepository.findById(reqAssinaturaDto.codigoCliente());

        // verificar se eles existem (se a busca retornou alguma coisa)
        if (!aplicativo.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Código de aplicativo inexistente");
        if (!cliente.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Código de cliente inexistente");

        // se tudo certo, instanciar a assinatura e salvar na base de dados
        Assinatura assinatura = new Assinatura(aplicativo.get(), cliente.get());
        assinaturaRepository.save(assinatura);

        // retornar a assinatura criada
        String status = LocalDate.now().isAfter(assinatura.getFimVigencia())? "CANCELADA" : "ATIVA"; // se a data atual for depois da data de termino, esta cancelado; senao esta ativo
        return ResponseEntity.status(HttpStatus.CREATED).body(new AssinaturaDTO(assinatura.getCodigo(), assinatura.getCliente().getNome(), 
                                                              assinatura.getAplicativo().getNome(), assinatura.getInicioVigencia(), assinatura.getFimVigencia(), status));
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

    @GetMapping("/servcad/assinaturas/{tipo}")
    @CrossOrigin("*")
    public ResponseEntity<?> getAssinaturasPorTipo(@PathVariable String tipo) {
        // padronizar em minusculas o tipo recebido como parametro
        tipo = tipo.toLowerCase();

        // buscar os objetos de assinatura do tipo especificado no banco
        List<Assinatura> assinaturas = new LinkedList<>();
        switch(tipo) {
            case "todas":      assinaturas = assinaturaRepository.findAll();                     break;
            case "ativas":     assinaturas = assinaturaRepository.findActive(LocalDate.now());   break;
            case "canceladas": assinaturas = assinaturaRepository.findInactive(LocalDate.now()); break;
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tipo inválido. Tipos suportados: 'TODAS', 'ATIVAS' e 'CANCELADAS'");
        }

        // criar lista de DTOs a partir dos objetos de assinatura buscados no banco
        List<AssinaturaDTO> assinaturasDto = new ArrayList<>(assinaturas.size());
        LocalDate hoje = LocalDate.now();
        for (Assinatura ass : assinaturas) {
            String status = hoje.isAfter(ass.getFimVigencia())? "CANCELADA" : "ATIVA"; // se a data atual for depois da data de termino, esta cancelado; senao esta ativo
            assinaturasDto.add(new AssinaturaDTO(ass.getCodigo(), ass.getCliente().getNome(), ass.getAplicativo().getNome(), 
                               ass.getInicioVigencia(), ass.getFimVigencia(), status));
        }

        // retornar a resposta de sucesso
        return ResponseEntity.ok(assinaturasDto);
    }

    @GetMapping("/servcad/asscli/{codcli}")
    @CrossOrigin("*")
    public ResponseEntity<?> getAssinaturasPorCliente(@PathVariable Long codcli) {
        // obter o registro do cliente informado
        Optional<Cliente> cliente = clienteRepository.findById(codcli);

        // verificar se o cliente existe
        if (!cliente.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Código de cliente inexistente");

        // se tudo certo, extrair o objeto do cliente e recuperar suas assinaturas
        List<Assinatura> assinaturas = cliente.get().getAssinaturas();

        // criar a lista de DTOs a partir dos objetos de assinatura buscados no banco
        List<AssinaturaDTO> assinaturasDto = new ArrayList<>(assinaturas.size());
        LocalDate hoje = LocalDate.now();
        for (Assinatura ass : assinaturas) {
            String status = hoje.isAfter(ass.getFimVigencia())? "CANCELADA" : "ATIVA"; // se a data atual for depois da data de termino, esta cancelado; senao esta ativo
            assinaturasDto.add(new AssinaturaDTO(ass.getCodigo(), ass.getCliente().getNome(), ass.getAplicativo().getNome(),
                               ass.getInicioVigencia(), ass.getFimVigencia(), status));
        }

        // retornar a resposta de sucesso
        return ResponseEntity.ok(assinaturasDto);
    }

    @GetMapping("/servcad/assapp/{codapp}")
    @CrossOrigin("*")
    public ResponseEntity<?> getAssinaturasPorAplicativo(@PathVariable Long codapp) {
        // obter o registro do aplicativo informado
        Optional<Aplicativo> aplicativo = aplicativoRepository.findById(codapp);

        // verificar se o aplicativo existe
        if (!aplicativo.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Código de aplicativo inexistente");

        // se der tudo certo, extrair o objeto do aplicativo e recuperar suas assinaturas
        List<Assinatura> assinaturas = aplicativo.get().getAssinaturas();

        // criar a lista de DTOs a partir dos objetos de assinatura buscados no banco
        List<AssinaturaDTO> assinaturasDTO = new ArrayList<>(assinaturas.size());
        LocalDate hoje = LocalDate.now();
        for (Assinatura ass : assinaturas) {
            String status = hoje.isAfter(ass.getFimVigencia())? "CANCELADA" : "ATIVA"; // se a data atual for depois da data de termino, esta cancelado; senao esta ativo
            assinaturasDTO.add(new AssinaturaDTO(ass.getCodigo(), ass.getCliente().getNome(), ass.getAplicativo().getNome(), 
                               ass.getInicioVigencia(), ass.getFimVigencia(), status));
        }

        // retornar a resposta de sucesso
        return ResponseEntity.ok(assinaturasDTO);
    }
}