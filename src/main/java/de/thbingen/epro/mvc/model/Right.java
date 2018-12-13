package de.thbingen.epro.mvc.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "rights")
@AttributeOverride(name = "id", column = @Column(name = "right_id"))
public class Right extends AbstractEntity {

    @Column(name = "right_name")
    private String name;

    @Column
    private String description;

    @Column(name = "right_key")
    private String key;


    @ManyToMany(mappedBy = "rights")
    private List<Group> groups;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
