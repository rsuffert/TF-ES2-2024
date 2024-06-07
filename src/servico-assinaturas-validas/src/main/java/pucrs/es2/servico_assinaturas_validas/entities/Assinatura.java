package pucrs.es2.servico_assinaturas_validas.entities;

import java.time.LocalDate;
import java.util.Objects;

public record Assinatura (Long codigo, LocalDate fimVigencia) {
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Assinatura)) return false;
        Assinatura ass = (Assinatura) obj;
        return this.codigo == ass.codigo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.codigo);
    }
}