package pucrs.es2.servico_cadastramento.entities;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Aplicativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo; // codigo identificador do aplicativo

    private String nome; // nome fantasia pelo qual o aplicativo eh conhecido

    @Column(name = "custo_mensal")
    private float custoMensal; // valor da assinatura mensal

    @OneToMany(mappedBy = "aplicativo", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Assinatura> assinaturas;

    public Aplicativo(String nome, float custoMensal) throws IllegalArgumentException {
        if (custoMensal < 0) throw new IllegalArgumentException("Custo mensal do aplicativo não pode ser negativo");
        this.nome = nome;
        this.custoMensal = custoMensal;
        this.assinaturas = new LinkedList<>();
    }

    protected Aplicativo(){}

    public Long getCodigo() { return this.codigo; }
    public String getNome() { return this.nome; }
    public float getCustoMensal() { return this.custoMensal; }
    public List<Assinatura> getAssinaturas() { return Collections.unmodifiableList(assinaturas); }
    public void setNome(String novoNome) { this.nome = novoNome; }
    public void setCustoMensal(float novoCusto) throws IllegalArgumentException {
        if (novoCusto < 0) throw new IllegalArgumentException("Custo mensal do aplicativo não pode ser negativo");
        this.custoMensal = novoCusto;
    }
}
