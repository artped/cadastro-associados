package com.associados.cadastro.service;

import com.associados.cadastro.model.Associado;
import com.associados.cadastro.repository.AssociadoRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvService {

    private final AssociadoRepository associadoRepository;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final String[] CSV_HEADER = {
            "Nome", "CPF", "E-mail", "Telefone",
            "Logradouro", "Número", "Complemento", "Bairro",
            "Cidade", "Estado", "CEP", "Data Nascimento", "Ativo"
    };

    private static final String FORMULA_INJECTION_CHARS = "=+-@\t\r";

    /**
     * Sanitizes a CSV field to prevent formula injection attacks.
     * Fields starting with =, +, -, @, tab, or carriage return are prefixed with a single quote.
     */
    private static String sanitizeCsvField(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        if (FORMULA_INJECTION_CHARS.indexOf(value.charAt(0)) >= 0) {
            return "'" + value;
        }
        return value;
    }

    public String exportarCsv() {
        List<Associado> associados = associadoRepository.findAllAssociados();

        StringWriter stringWriter = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(stringWriter)) {
            csvWriter.writeNext(CSV_HEADER);

            for (Associado a : associados) {
                String[] linha = {
                        sanitizeCsvField(a.getNome()),
                        sanitizeCsvField(a.getCpf()),
                        sanitizeCsvField(a.getEmail()),
                        sanitizeCsvField(a.getTelefone()),
                        sanitizeCsvField(a.getLogradouro()),
                        sanitizeCsvField(a.getNumero()),
                        sanitizeCsvField(a.getComplemento()),
                        sanitizeCsvField(a.getBairro()),
                        sanitizeCsvField(a.getCidade()),
                        sanitizeCsvField(a.getEstado()),
                        sanitizeCsvField(a.getCep()),
                        a.getDataNascimento() != null ? a.getDataNascimento().format(DATE_FORMAT) : "",
                        a.getAtivo() != null ? a.getAtivo().toString() : "true"
                };
                csvWriter.writeNext(linha);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao exportar CSV", e);
        }

        return stringWriter.toString();
    }

    public ImportResult importarCsv(MultipartFile file) {
        List<String> erros = new ArrayList<>();
        int importados = 0;

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] header = csvReader.readNext();
            if (header == null) {
                erros.add("Arquivo CSV vazio");
                return new ImportResult(0, erros);
            }

            String[] linha;
            int linhaNum = 1;
            while ((linha = csvReader.readNext()) != null) {
                linhaNum++;
                try {
                    if (linha.length < 12) {
                        erros.add("Linha " + linhaNum + ": número insuficiente de colunas");
                        continue;
                    }

                    String nome = sanitizeCsvField(linha[0].trim());
                    String cpf = linha[1].trim().replaceAll("[^0-9]", "");

                    if (nome.isEmpty() || cpf.isEmpty()) {
                        erros.add("Linha " + linhaNum + ": Nome e CPF são obrigatórios");
                        continue;
                    }

                    if (cpf.length() != 11) {
                        erros.add("Linha " + linhaNum + ": CPF inválido (" + cpf + ")");
                        continue;
                    }

                    LocalDate dataNascimento = null;
                    if (!linha[11].trim().isEmpty()) {
                        try {
                            dataNascimento = LocalDate.parse(linha[11].trim(), DATE_FORMAT);
                        } catch (DateTimeParseException e) {
                            erros.add("Linha " + linhaNum + ": Data de nascimento inválida (" + linha[11] + ")");
                            continue;
                        }
                    }

                    Boolean ativo = true;
                    if (linha.length > 12 && !linha[12].trim().isEmpty()) {
                        ativo = Boolean.parseBoolean(linha[12].trim());
                    }

                    Associado associado = Associado.builder()
                            .id(UUID.randomUUID())
                            .nome(nome)
                            .cpf(cpf)
                            .email(sanitizeCsvField(linha[2].trim()))
                            .telefone(sanitizeCsvField(linha[3].trim()))
                            .logradouro(sanitizeCsvField(linha[4].trim()))
                            .numero(sanitizeCsvField(linha[5].trim()))
                            .complemento(sanitizeCsvField(linha[6].trim()))
                            .bairro(sanitizeCsvField(linha[7].trim()))
                            .cidade(sanitizeCsvField(linha[8].trim()))
                            .estado(sanitizeCsvField(linha[9].trim()))
                            .cep(linha[10].trim().replaceAll("[^0-9]", ""))
                            .dataNascimento(dataNascimento)
                            .dataCadastro(LocalDateTime.now())
                            .dataAtualizacao(LocalDateTime.now())
                            .ativo(ativo)
                            .build();

                    associadoRepository.save(associado);
                    importados++;

                } catch (Exception e) {
                    erros.add("Linha " + linhaNum + ": " + e.getMessage());
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Erro ao processar arquivo CSV", e);
        }

        log.info("Importação concluída: {} registros importados, {} erros", importados, erros.size());
        return new ImportResult(importados, erros);
    }

    public record ImportResult(int importados, List<String> erros) {}
}
