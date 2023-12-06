package br.com.mvp.maonamassa.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FuncionarioRecordDto(@NotBlank String nome, @NotNull LocalDate dataNascimento,
        @NotNull BigDecimal salario, @NotBlank String funcao) {

}
