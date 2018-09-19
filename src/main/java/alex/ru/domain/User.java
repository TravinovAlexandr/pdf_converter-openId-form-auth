package alex.ru.domain;

import lombok.*;
import org.springframework.lang.NonNull;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Entity @Data
@Table(name ="user_table")
@ToString(of = {"id", "nick", "email"})
@EqualsAndHashCode(of = {"password", "email"})
public class User implements Serializable {

    @Id @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @Size(min = 3, max = 25)
    @Column(name = "nick")
    private String nick;

    @NotNull @Email
    @Size(min = 5, max = 255)
    @Column(name = "email")
    private String email;

    @NotNull
    @Size(min = 5, max = 255)
    @Column(name = "password")
    private String password;

    @NotNull
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "creation_date")
    private LocalDate creationDate;

    @NotNull
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "creation_time")
    private LocalTime creationTime;

    @Column(name = "name")
    @Size(min = 3, max = 50)
    private String name;

    @Column(name = "last_name")
    @Size(min = 3, max = 50)
    private String lastName;

    @Column(name = "avatar")
    private Byte [] avatar;

    @NotNull
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() {}

    public User(@NonNull String nick,
                @NonNull String email,
                @NonNull String password,
                @NonNull LocalDate creationDate,
                @NonNull LocalTime creationTime)
    {
        this.nick = nick;
        this.email = email;
        this.password = password;
        this.creationDate = creationDate;
        this.creationTime = creationTime;
    }

    public User(@NonNull final String nick, @NonNull final String email, @NonNull final String password) {
        this.nick = nick;
        this.email = email;
        this.password = password;
    }
}
