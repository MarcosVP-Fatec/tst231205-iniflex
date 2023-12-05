package br.com.mvp.maonamassa.model.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "funcionario")
@DiscriminatorValue("funcionario")
@PrimaryKeyJoinColumn(name = "idpessoa")
public class Funcionario extends Pessoa {

    @Column(name = "salario", nullable = false, precision = 2)
    private BigDecimal salario;

    public final static short CAMPO_TAMANHO_FUNCIONARIO_FUNCAO = 40;
    @Column(name = "funcao", nullable = false, length = CAMPO_TAMANHO_FUNCIONARIO_FUNCAO)
    private String funcao;

    // G&S
    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

}
