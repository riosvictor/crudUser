package com.test.crudUser.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private String id;
    private String cpf;
    private String nome;
    private String sexo;
    private String email;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dataNascimento;
    private String naturalidade;
    private String nacionalidade;

    public UserDTO(String cpf, String nome, String sexo, String email, Date dataNascimento, String naturalidade, String nacionalidade) {
        this.cpf = cpf;
        this.nome = nome;
        this.sexo = sexo;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.naturalidade = naturalidade;
        this.nacionalidade = nacionalidade;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
