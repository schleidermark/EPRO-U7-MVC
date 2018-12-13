package de.thbingen.epro.mvc.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "groups")
@AttributeOverride(name = "id", column = @Column(name = "group_id"))
public class Group extends AbstractEntity {

    @Column(name = "group_name")
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy = "group")
    private List<User> users;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "Groups_Rights",
            joinColumns = { @JoinColumn(name = "group_id") },
            inverseJoinColumns = { @JoinColumn(name = "right_id") }
    )
    private List<Right> rights;

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

    public List<Right> getRights() {
        return rights;
    }

    public void setRights(List<Right> rights) {
        this.rights = rights;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
