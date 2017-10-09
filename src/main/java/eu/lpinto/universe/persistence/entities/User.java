package eu.lpinto.universe.persistence.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Entity
@Table(name = "ApplicationUser")
@NamedQueries({
    @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name")
    ,
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
})
public class User extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Column(nullable = false)
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
             message = "Invalid email")
    private String email;

    @Basic(optional = false)
    @Column(nullable = false)
    @Size(min = 1, max = 128, message = "Invalid password size")
    private String password;

    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Token> tokens;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Employee> employees;

    /*
     * Contructors
     */
    public User() {
    }

    public User(final Long id) {
        super(id);
    }

    public User(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public User(final String email, final String password, final String name) {
        super(name);
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, List<Token> tokens, List<Employee> employees, String name, User creator, Calendar created, User updater, Calendar updated, Long id) {
        super(name, creator, created, updater, updated, id);
        this.email = email;
        this.password = password;
        this.tokens = tokens;
        this.employees = employees;
    }

    /*
     * Getters/Setters
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        assertNotNull(email);

        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        assertNotNull(password);

        this.password = password;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(final List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
