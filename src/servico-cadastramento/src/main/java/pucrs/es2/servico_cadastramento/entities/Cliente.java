package pucrs.es2.servico_cadastramento.entities;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo; // codigo identificador do cliente

    private String nome; // nome do cliente
    
    private String email; // email do cliente

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Assinatura> assinaturas;

    public Cliente(String nome, String email) {
        this.nome = nome;
        this.email = email;
        this.assinaturas = new LinkedList<>();
    }

    protected Cliente(){}

    public Long getCodigo(){ return this.codigo; }
    public String getNome() { return this.nome; }
    public String getEmail() { return this.email; }
    public List<Assinatura> getAssinaturas() { return Collections.unmodifiableList(assinaturas); }
    public void setNome(String novoNome) { this.nome = novoNome; }
    public void setEmail(String novoEmail) { this.email = novoEmail; }
}
