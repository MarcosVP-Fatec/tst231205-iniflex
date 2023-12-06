package br.com.mvp.maonamassa.model.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    // Seleciona e imprime um Map de funcionarios por funções
    public String listarTodosOsFuncionariosPorFuncao() {
        Map<String, List<Funcionario>> lista = getMapFuncionariosPorFuncao();
        StringBuilder txt = new StringBuilder();
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
        txt.append("");
        final int LARG_TITULO = tit1.trim().length() - 2;
        final String titulo = "FUNCIONÁRIOS AGRUPADOS POR FUNÇÃO";
        txt.append("+" + "-".repeat(LARG_TITULO) + "+\n");
        txt.append("|" + Util.padR(" " + titulo, LARG_TITULO) + "|\n");
        txt.append(tit1 + tit2);
        List<String> chaves = new ArrayList<>(lista.keySet());
        Collections.sort(chaves);
        for (String chave : chaves) {
            txt.append(tit1 + "| " + Util.padR("Função: " + chave.toUpperCase(), LARG_TITULO - 1) + "|\n" + tit1);
            for (Funcionario funcionario : lista.get(chave)) {
                txt.append("|" + Util.padR(funcionario.getNome(), Funcionario.CAMPO_TAMANHO_PESSOA_NOME)
                        + "|" + Util.dToStr(funcionario.getDataNascimento())
                        + "|" + Util.maskDec(funcionario.getSalario(), TAMSAL)
                        + "|" + Util.padR(funcionario.getFuncao(), Funcionario.CAMPO_TAMANHO_FUNCIONARIO_FUNCAO)
                        + "|\n");
            }
        }
        txt.append(tit1);
        return txt.toString();
    }

    // Gera um map de funcionÃ¡rios por aniversÃ¡rio
    public String listarAniversariantesNosMeses(List<Integer> meses) {
        Map<Integer, List<Funcionario>> lista = mapFuncionariosAniversariosNosMeses(meses);
        final String tit1 = "+" + "-".repeat(Funcionario.CAMPO_TAMANHO_PESSOA_NOME)
                + "+" + "-".repeat(10)
                + "+" + "-".repeat(Funcionario.CAMPO_TAMANHO_FUNCIONARIO_FUNCAO)
                + "+\n";
        final String tit2 = "|" + Util.padR("Nome", Funcionario.CAMPO_TAMANHO_PESSOA_NOME)
                + "|" + Util.padR("DtNasc", 10)
                + "|" + Util.padR("Função", Funcionario.CAMPO_TAMANHO_FUNCIONARIO_FUNCAO)
                + "|\n";
        var txt = new StringBuilder();
        txt.append("");
        final int LARG_TITULO = tit1.trim().length() - 2;
        final String titulo = "FUNCIONÁRIOS POR MÊS DE ANIVERSÁRIO";
        txt.append("+" + "-".repeat(LARG_TITULO) + "+\n");
        txt.append("|" + Util.padR(" " + titulo, LARG_TITULO) + "|\n");

        txt.append(tit1 + tit2);
        List<Integer> chaves = new ArrayList<>(lista.keySet());
        Collections.sort(chaves);
        for (Integer chave : chaves) {
            txt.append(tit1 + "| " + Util.padR("Mês: " + String.valueOf(chave), LARG_TITULO - 1) + "|\n" + tit1);
            for (Funcionario funcionario : lista.get(chave)) {
                txt.append("|" + Util.padR(funcionario.getNome(), Funcionario.CAMPO_TAMANHO_PESSOA_NOME)
                        + "|" + Util.dToStr(funcionario.getDataNascimento())
                        + "|" + Util.padR(funcionario.getFuncao(), Funcionario.CAMPO_TAMANHO_FUNCIONARIO_FUNCAO)
                        + "|\n");
            }
        }
        txt.append(tit1);
        return txt.toString();
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

    // Gera um map de funcionários por função
    public Map<String, List<Funcionario>> getMapFuncionariosPorFuncao() {

        List<Funcionario> lista = funcionarioRepository.findAllByOrderByFuncaoAscIdPessoaAsc();
        if (lista.size() == 0)
            throw new RuntimeException("Não há funcionários cadastrados!");

        Map<String, List<Funcionario>> mapaFuncoes = new HashMap<String, List<Funcionario>>();
        String funcaoAtual = lista.get(0).getFuncao();
        List<Funcionario> parcial = new ArrayList<Funcionario>();

        for (Funcionario funcionario : lista) {
            if (!funcaoAtual.equals(funcionario.getFuncao())) {
                mapaFuncoes.put(funcaoAtual, parcial);
                parcial = new ArrayList<Funcionario>();
                funcaoAtual = funcionario.getFuncao();
            }
            parcial.add(funcionario);
        }

        mapaFuncoes.put(funcaoAtual, parcial);
        return mapaFuncoes;
    }

    // Busca todos funcionários anioversariantes
    public Map<Integer, List<Funcionario>> mapFuncionariosAniversariosNosMeses(List<Integer> meses) {
        List<Funcionario> lista = funcionarioRepository.findAllByMesesNascimento(meses);
        if (lista.size() == 0)
            throw new RuntimeException("Não há funcionários cadastrados nos meses solicitados!");

        List<List<Funcionario>> funcs = new ArrayList<List<Funcionario>>();
        for (int i = 0; i < 12; i++) {
            funcs.add(new ArrayList<Funcionario>());
        }

        for (Funcionario funcionario : lista) {
            final int indice = funcionario.getDataNascimento().getMonthValue() - 1;
            funcs.get(indice).add(funcionario);
        }

        Map<Integer, List<Funcionario>> mapaAniversarios = new HashMap<Integer, List<Funcionario>>();
        for (int i = 0; i < 12; i++) {
            if (funcs.get(i).size() > 0)
                mapaAniversarios.put(i + 1, funcs.get(i));
        }
        return mapaAniversarios;
    }

    // Gera lista de funcionários por função
    public String geraListaFuncionariosPorFuncao() {
        StringBuilder txt = new StringBuilder();
        Map<String, List<Funcionario>> mapFuncionariosPorFuncao = getMapFuncionariosPorFuncao();
        for (String chave : mapFuncionariosPorFuncao.keySet()) {
            txt.append(geraListaFuncionarios("FUNÇÃO: " + chave.toUpperCase(), mapFuncionariosPorFuncao.get(chave)));
        }
        return txt.toString();
    }

    // Pega o funcionário mais velho
    public Funcionario getFuncionarioMaisVelho() {
        List<Funcionario> func = funcionarioRepository.findFirstByOrderByDataNascimentoAsc();
        if (func.size() == 0)
            throw new RuntimeException("Não há funcionários cadastrados!");
        return func.get(0);
    }

    // Lista o funcionário com mais idade
    public String listarFuncionarioMaisVelho() {
        Funcionario func = getFuncionarioMaisVelho();
        return String.format("O(a) funcionário(a) com a maior idade é o(a) \"%s\" com %d anos.",
                func.getNome(), Util.idade(func.getDataNascimento(), LocalDate.now()));
    }

    // Lista o total de salários
    public String listarTotalDeSalarios() {
        BigDecimal soma = funcionarioRepository.sumBySalario();
        return String.format("O total dos salários dos funcionários é %s.",
                Util.maskDec(soma, 12));
    }

    // Lista de funcionários por ordem alfabética
    public List<Funcionario> getFuncionariosPorOrdemAlfabetica() {
        List<Funcionario> lista = funcionarioRepository.findAllByOrderByNome();
        if (lista.size() == 0)
            throw new RuntimeException("Não há funcionários cadastrados!");
        return lista;
    }

    // Seleciona e imprime todos os funcionários por ordem alfabética
    public String listarTodosOsFuncionariosPorOrdemAlfabetica(String tit) {
        List<Funcionario> lista = getFuncionariosPorOrdemAlfabetica();
        return geraListaFuncionarios(tit, lista);
    }

}