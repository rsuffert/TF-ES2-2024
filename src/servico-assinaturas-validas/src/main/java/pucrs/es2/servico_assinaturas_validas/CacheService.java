package pucrs.es2.servico_assinaturas_validas;

import java.util.HashSet;
import java.util.Set;

import pucrs.es2.servico_assinaturas_validas.entities.Assinatura;

public class CacheService {
    public static Set<Assinatura> codigosAssinaturasValidas = new HashSet<>();

    public static Assinatura getAssinaturaByCodigo(Long codigo) {
        for (Assinatura a : codigosAssinaturasValidas) {
            if (a.codigo() == codigo) return a;
        }
        return null;
    }

    public static boolean removeAssinaturaByCodigo(Long codigo) {
        for (Assinatura a : codigosAssinaturasValidas) {
            if (a.codigo() == codigo) {
                codigosAssinaturasValidas.remove(a);
                return true;
            }
        }
        return false;
    }
}
