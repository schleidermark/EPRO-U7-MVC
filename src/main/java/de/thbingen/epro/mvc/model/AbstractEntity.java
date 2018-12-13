package de.thbingen.epro.mvc.model;


import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "seqGenerator", sequenceName = "DICTIONARY_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGenerator")
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
