package com.associados.cadastro.controller;

import com.associados.cadastro.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@Tag(name = "Relatórios", description = "Relatórios e estatísticas dos associados")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/geral")
    @Operation(summary = "Relatório geral dos associados")
    public ResponseEntity<Map<String, Object>> relatorioGeral() {
        return ResponseEntity.ok(reportService.gerarRelatorioGeral());
    }

    @GetMapping("/por-estado")
    @Operation(summary = "Relatório de associados por estado")
    public ResponseEntity<Map<String, Long>> relatorioPorEstado() {
        return ResponseEntity.ok(reportService.gerarRelatorioPorEstado());
    }

    @GetMapping("/por-cidade")
    @Operation(summary = "Relatório de associados por cidade")
    public ResponseEntity<Map<String, Long>> relatorioPorCidade() {
        return ResponseEntity.ok(reportService.gerarRelatorioPorCidade());
    }
}
