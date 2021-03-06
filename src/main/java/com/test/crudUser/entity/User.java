package com.test.crudUser.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

@Document(collection = "user")
@Entity
@Data // @ToString, @EqualsAndHashCode, @Getter, @Setter , and @RequiredArgsConstructor
@AllArgsConstructor // argument for every field
@NoArgsConstructor // empty constructor generation
public class User {
    @Id
    private String id;
    private String cpf;
    private String nome;
    private String sexo;
    private String email;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dataNascimento;
    private String naturalidade;
    private String nacionalidade;

    private Date created;
    private Date updated;

    @PrePersist
    public void onCreate() {
        created = new Date();
    }

    @PreUpdate
    public void onUpdate() {
        updated = new Date();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
