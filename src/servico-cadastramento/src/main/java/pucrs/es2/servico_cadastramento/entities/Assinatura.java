package pucrs.es2.servico_cadastramento.entities;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Assinatura {
    public static final int DIAS_GRATIS_NOVA_ASSINATURA = 7;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo; // codigo da assinatura

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "codapp")
    private Aplicativo aplicativo; // aplicativo assinado

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "codcli")
    private Cliente cliente; // cliente proprietario da assinatura

    @Column(name = "inicio_vigencia")
    private LocalDate inicioVigencia; // inicio da vigencia da assinatura

    @Column(name = "fim_vigencia")
    private LocalDate fimVigencia; // fim da vigencia da assinatura

    public Assinatura(Aplicativo aplicativo, Cliente cliente) {
        this.aplicativo = aplicativo;
        this.cliente = cliente;
        this.inicioVigencia = LocalDate.now();
        this.fimVigencia = inicioVigencia.plusDays(DIAS_GRATIS_NOVA_ASSINATURA);
    }

    protected Assinatura() {}

    public Long getCodigo() { return this.codigo; }
    public Aplicativo getAplicativo() { return this.aplicativo; }
    public Cliente getCliente() { return this.cliente; }
    public LocalDate getInicioVigencia() { return this.inicioVigencia; }
    public LocalDate getFimVigencia() { return this.fimVigencia; }
    public boolean prorrogarVigencia(LocalDate dataPagamento, int dias) throws IllegalArgumentException {
        if (dias <= 0) throw new IllegalArgumentException("A quantidade de dias para prorrogar a vigência deve ser positiva");
        if (LocalDate.now().isAfter(fimVigencia)) { // somente prorroga se a assinatura ja nao estiver expirada
            fimVigencia = dataPagamento.plusDays(dias);
            return true;
        }
        return false;
    }
}