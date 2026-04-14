package com.associados.cadastro.service;

import com.associados.cadastro.model.Associado;
import com.associados.cadastro.repository.AssociadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final AssociadoRepository associadoRepository;

    public Map<String, Object> gerarRelatorioGeral() {
        List<Associado> todos = associadoRepository.findAllAssociados();

        Map<String, Object> relatorio = new LinkedHashMap<>();
        relatorio.put("totalAssociados", todos.size());
        relatorio.put("totalAtivos", todos.stream().filter(a -> Boolean.TRUE.equals(a.getAtivo())).count());
        relatorio.put("totalInativos", todos.stream().filter(a -> !Boolean.TRUE.equals(a.getAtivo())).count());

        Map<String, Long> porEstado = todos.stream()
                .filter(a -> a.getEstado() != null && !a.getEstado().isEmpty())
                .collect(Collectors.groupingBy(Associado::getEstado, Collectors.counting()));
        relatorio.put("associadosPorEstado", porEstado);

        Map<String, Long> porCidade = todos.stream()
                .filter(a -> a.getCidade() != null && !a.getCidade().isEmpty())
                .collect(Collectors.groupingBy(Associado::getCidade, Collectors.counting()));
        relatorio.put("associadosPorCidade", porCidade);

        return relatorio;
    }

    public Map<String, Long> gerarRelatorioPorEstado() {
        List<Associado> todos = associadoRepository.findAllAssociados();
        return todos.stream()
                .filter(a -> a.getEstado() != null && !a.getEstado().isEmpty())
                .collect(Collectors.groupingBy(Associado::getEstado, Collectors.counting()));
    }

    public Map<String, Long> gerarRelatorioPorCidade() {
        List<Associado> todos = associadoRepository.findAllAssociados();
        return todos.stream()
                .filter(a -> a.getCidade() != null && !a.getCidade().isEmpty())
                .collect(Collectors.groupingBy(Associado::getCidade, Collectors.counting()));
    }
}
