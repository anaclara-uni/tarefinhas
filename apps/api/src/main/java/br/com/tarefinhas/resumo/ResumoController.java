package br.com.tarefinhas.resumo;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.tarefinhas.resumo.ResumoRepository.Contagens;
import br.com.tarefinhas.resumo.ResumoRepository.Notificacao;
import br.com.tarefinhas.resumo.ResumoRepository.ProximaEntrega;

/**
 * Endpoint com os números do painel (progresso, contadores, calendário, próximas entregas
 * e notificações do sino).
 * Como aqui só tem leitura, chamamos o repository direto, sem service.
 */
@RestController
@RequestMapping("/api/resumo")
public class ResumoController {

    // Formato da resposta
    public record ResumoResponse(
            Contagens contagens,
            List<Integer> diasComTarefa,
            List<ProximaEntrega> proximasEntregas,
            List<Notificacao> notificacoes) {
    }

    private final ResumoRepository resumoRepository;

    public ResumoController(ResumoRepository resumoRepository) {
        this.resumoRepository = resumoRepository;
    }

    // GET /api/resumo?mes=2026-11  (o mês serve para as bolinhas do calendário)
    @GetMapping
    public ResumoResponse resumo(@RequestHeader("X-Usuario-Id") Long usuarioId,
            @RequestParam(required = false) YearMonth mes) {
        if (mes == null) {
            mes = YearMonth.now();
        }
        LocalDate inicio = mes.atDay(1);
        LocalDate fim = mes.plusMonths(1).atDay(1);

        return new ResumoResponse(
                resumoRepository.contar(usuarioId),
                resumoRepository.diasComTarefa(usuarioId, inicio, fim),
                resumoRepository.proximasEntregas(usuarioId),
                resumoRepository.notificacoes(usuarioId));
    }
}
