package br.com.mvp.maonamassa.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.mvp.maonamassa.model.entity.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    List<Funcionario> findAllByOrderByIdPessoa();

}
