package br.com.mvp.maonamassa.model.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.mvp.maonamassa.model.dto.FuncionarioRecordDto;
import br.com.mvp.maonamassa.model.entity.Funcionario;
import br.com.mvp.maonamassa.model.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

    @Autowired
    FuncionarioRepository funcionarioRepository;

    public ResponseEntity<List<Funcionario>> getAllFuncionarios() {
        return ResponseEntity.status(HttpStatus.OK).body(funcionarioRepository.findAll());
    }

    public ResponseEntity<Funcionario> saveFuncionario(FuncionarioRecordDto funcionarioRecordDto) {

        var funcionario = new Funcionario();
        BeanUtils.copyProperties(funcionarioRecordDto, funcionario);
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioRepository.save(funcionario));

    }

    public ResponseEntity<Object> updateFuncionario(Long id, FuncionarioRecordDto funcionarioRecordDto) {

        Optional<Funcionario> optFuncionario = funcionarioRepository.findById(id);
        if (optFuncionario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionário não encontrado!");
        }
        var funcionario = optFuncionario.get();
        BeanUtils.copyProperties(funcionarioRecordDto, funcionario);
        return ResponseEntity.status(HttpStatus.OK).body(funcionarioRepository.save(funcionario));

    }

    public ResponseEntity<Object> deleteFuncionario(@PathVariable(value = "id") Long id) {
        Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
        if (funcionario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Funcionário não encontrado.");
        }
        funcionarioRepository.delete(funcionario.get());
        return ResponseEntity.status(HttpStatus.OK).body("Funcionário excluído com sucesso!");
    }

    public FuncionarioRecordDto toDto(String nome, LocalDate dataNascimento, BigDecimal salario, String funcao) {
        return new FuncionarioRecordDto(nome, dataNascimento, salario, funcao);
    }

    public FuncionarioRecordDto toDto(Funcionario func) {
        return new FuncionarioRecordDto(func.getNome(), func.getDataNascimento(), func.getSalario(), func.getFuncao());
    }

    public Funcionario getByNome(String nome) {
        Optional<Funcionario> func = funcionarioRepository.findByNome(nome);
        if (func.isPresent()) {
            return func.get();
        } else {
            return null;
        }
    }

    // Seleciona e imprime todos os funcionários por ordem de inclusão
    public String listarTodosOsFuncionarios(String tit) {
        List<Funcionario> lista = funcionarioRepository.findAllByOrderByIdPessoa();
        return geraListaFuncionarios(tit, lista);
    }

    // Imprime a List<Funcionario>
    private static String geraListaFuncionarios(String titulo, List<Funcionario> lista) {
        final int TAMSAL = 12;
        final String tit1 = "+" + "-".repeat(Funcionario.CAMPO_TAMANHO_PESSOA_NOME)
                + "+" + "-".repeat(10)
                + "+" + "-".repeat(TAMSAL)
                + "+" + "-".repeat(Funcionario.CAMPO_TAMANHO_FUNCIONARIO_FUNCAO)
                + "+\n";
        final String tit2 = "|" + Util.padR("Nome", Funcionario.CAMPO_TAMANHO_PESSOA_NOME)
                + "|" + Util.padR("DtNasc", 10)
                + "|" + Util.padL("Salário", TAMSAL)
                + "|" + Util.padR("Função", Funcionario.CAMPO_TAMANHO_FUNCIONARIO_FUNCAO)
                + "|\n";

        final int LARG_TITULO = tit1.trim().length() - 2;

        StringBuilder txt = new StringBuilder();
        txt.append("+" + "-".repeat(LARG_TITULO) + "+\n");
        txt.append("|" + Util.padR(" " + titulo, LARG_TITULO) + "|");
        txt.append("\n");

        txt.append(tit1 + tit2 + tit1);

        for (Funcionario funcionario : lista) {
            txt.append("|" + Util.padR(funcionario.getNome(), Funcionario.CAMPO_TAMANHO_PESSOA_NOME)
                    + "|" + Util.dToStr(funcionario.getDataNascimento())
                    + "|" + Util.maskDec(funcionario.getSalario(), TAMSAL)
                    + "|" + Util.padR(funcionario.getFuncao(), Funcionario.CAMPO_TAMANHO_FUNCIONARIO_FUNCAO)
                    + "|");
            txt.append("\n");
        }
        txt.append(tit1);
        return txt.toString();
    }

    // Concede aumento de salário
    public void aumentarSalarios(BigDecimal porcento) {
        List<Funcionario> lista = funcionarioRepository.findAllByOrderByIdPessoa();
        final BigDecimal CEM = new BigDecimal(100);
        for (Funcionario funcionario : lista) {
            funcionario.setSalario(funcionario.getSalario()
                    .add(funcionario.getSalario().divide(CEM, RoundingMode.HALF_EVEN).multiply(porcento)));

            updateFuncionario(funcionario.getIdPessoa(), toDto(funcionario));
        }
    }

}
