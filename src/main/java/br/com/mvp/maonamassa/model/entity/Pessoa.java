package br.com.mvp.maonamassa.model.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "pessoa", indexes = {
        @Index(name = "pessoa_nome_idx", columnList = "nome")
})
public class Pessoa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idpessoa", nullable = false)
    private UUID idPessoa;

    public static final short CAMPO_TAMANHO_PESSOA_NOME = 40;
    @Column(name = "nome", length = CAMPO_TAMANHO_PESSOA_NOME, nullable = false)
    private String nome;

    @Column(name = "data_nasc", nullable = false)
    private LocalDate dataNascimento;

    // G&S
    public UUID getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(UUID idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
