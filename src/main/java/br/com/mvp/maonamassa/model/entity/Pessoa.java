package br.com.mvp.maonamassa.model.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "pessoa", indexes = {
        @Index(name = "pessoa_nome_idx", columnList = "nome") })
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("pessoa")
public class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idpessoa")
    private Long idPessoa;

    public static final short CAMPO_TAMANHO_PESSOA_NOME = 40;
    @Column(name = "nome", length = CAMPO_TAMANHO_PESSOA_NOME, nullable = false)
    private String nome;

    @Column(name = "data_nasc", nullable = false)
    private LocalDate dataNascimento;

    // G&S
    public Long getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Long idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

}
