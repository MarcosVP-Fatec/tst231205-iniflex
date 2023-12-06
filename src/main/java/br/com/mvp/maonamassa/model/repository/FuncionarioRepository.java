package br.com.mvp.maonamassa.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.mvp.maonamassa.model.entity.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    List<Funcionario> findAllByOrderByIdPessoa();

    List<Funcionario> findAllByOrderByFuncaoAscIdPessoaAsc();

    Optional<Funcionario> findByNome(String nome);

}
