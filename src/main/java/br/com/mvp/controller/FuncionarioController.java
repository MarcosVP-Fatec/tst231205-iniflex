package br.com.mvp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.mvp.maonamassa.model.dto.FuncionarioRecordDto;
import br.com.mvp.maonamassa.model.entity.Funcionario;
import br.com.mvp.maonamassa.model.service.FuncionarioService;
import jakarta.validation.Valid;

@Controller
public class FuncionarioController {

    @Autowired
    FuncionarioService funcionarioService;

    @PostMapping("/funcionario")
    public ResponseEntity<Funcionario> saveFuncionario(@RequestBody @Valid FuncionarioRecordDto funcionarioRecordDto) {
        return funcionarioService.saveFuncionario(funcionarioRecordDto);
    }

    @GetMapping("/funcionarios")
    public ResponseEntity<List<Funcionario>> getAllFuncionarios() {
        return funcionarioService.getAllFuncionarios();
    }

    @DeleteMapping("/funcionario/{id}")
    public ResponseEntity<?> deleteFuncionario(@PathVariable(value = "id") Long id) {
        return funcionarioService.deleteFuncionario(id);
    }

}
