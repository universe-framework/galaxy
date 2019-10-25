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

    /*
     * Relations
     */
    @OneToOne(fetch = FetchType.EAGER)
    private Image currentAvatar;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ApplicationUser_Image",
               joinColumns = {
                   @JoinColumn(name = "applicationUser_id", referencedColumnName = "id")},
               inverseJoinColumns = {
                   @JoinColumn(name = "image_id", referencedColumnName = "id")})
    private List<Image> avatars;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Token> tokens;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Employee> employees;

    /*
     * Properties
     */
    @Basic(optional = false)
    @Column(nullable = false,
            unique = true, // used only in DDL generation
            updatable = false,
            length = 50)
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
             message = "Invalid email")
    @Size(min = 1, max = 50, message = "Invalid email size")
    private String email;

    @Basic(optional = false)
    @Column(nullable = false,
            length = 50)
    @Size(min = 1, max = 256, message = "Invalid password size")
    private String password;

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

    public User(final String email, final String password, final String firstName) {
        super(firstName);
        this.email = email;
        this.password = password;
    }

    public User(Image currentAvatar, List<Image> avatars, List<Token> tokens, List<Employee> employees,
                String email, String password,
                String name, User creator, Calendar created, User updater, Calendar updated, Long id) {
        super(name, creator, created, updater, updated, id);
        this.currentAvatar = currentAvatar;
        this.avatars = avatars;
        this.tokens = tokens;
        this.employees = employees;
        this.email = email;
        this.password = password;
    }

    /*
     * Getters/Setters
     */
    public Image getCurrentAvatar() {
        return currentAvatar;
    }

    public void setCurrentAvatar(Image currentAvatar) {
        this.currentAvatar = currentAvatar;
    }

    public List<Image> getAvatars() {
        return avatars;
    }

    public void setAvatars(List<Image> avatars) {
        this.avatars = avatars;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
