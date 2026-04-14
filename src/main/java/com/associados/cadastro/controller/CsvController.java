package com.associados.cadastro.controller;

import com.associados.cadastro.service.CsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/csv")
@RequiredArgsConstructor
@Tag(name = "Importação/Exportação", description = "Importação e exportação de dados via CSV")
public class CsvController {

    private final CsvService csvService;

    @GetMapping("/exportar")
    @Operation(summary = "Exportar associados para CSV")
    public ResponseEntity<byte[]> exportar() {
        String csv = csvService.exportarCsv();
        byte[] bytes = csv.getBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=associados.csv")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .contentLength(bytes.length)
                .body(bytes);
    }

    @PostMapping("/importar")
    @Operation(summary = "Importar associados de arquivo CSV")
    public ResponseEntity<Map<String, Object>> importar(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Arquivo CSV está vazio"));
        }

        CsvService.ImportResult result = csvService.importarCsv(file);

        return ResponseEntity.ok(Map.of(
                "importados", result.importados(),
                "erros", result.erros()
        ));
    }
}
