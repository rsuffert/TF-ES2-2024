package pucrs.es2.servico_cadastramento.persistence;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pucrs.es2.servico_cadastramento.entities.Assinatura;

public interface AssinaturaRepository extends JpaRepository<Assinatura, Long> {
    @Query("SELECT a FROM Assinatura a WHERE a.fimVigencia >= :currentDate")
    List<Assinatura> findActive(LocalDate currentDate);

    @Query("SELECT a FROM Assinatura a WHERE a.fimVigencia < :currentDate")
    List<Assinatura> findInactive(LocalDate currentDate);
}