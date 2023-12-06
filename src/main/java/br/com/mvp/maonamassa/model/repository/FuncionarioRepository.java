package br.com.mvp.maonamassa.model.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.mvp.maonamassa.model.entity.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    List<Funcionario> findAllByOrderByIdPessoa();

    List<Funcionario> findAllByOrderByNome();

    List<Funcionario> findAllByOrderByFuncaoAscIdPessoaAsc();

    @Query("select f from Funcionario f join Pessoa p on (f.idPessoa = p.idPessoa) where month(f.dataNascimento) in :meses order by month(f.dataNascimento), day(f.dataNascimento), upper(f.nome)")
    List<Funcionario> findAllByMesesNascimento(List<Integer> meses);

    List<Funcionario> findFirstByOrderByDataNascimentoAsc();

    Optional<Funcionario> findByNome(String nome);

    @Query("select sum(f.salario) from Funcionario f")
    BigDecimal sumBySalario();

}
