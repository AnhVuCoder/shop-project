package com.myshop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Email(message = "Email not valid")
    @Column(length = 128, nullable = false)
    private String email;
    @Column(length = 64, nullable = false)
    private String firstName;
    @Column(length = 64, nullable = false)
    private String lastName;
    @Column(length = 64, nullable = false)
    private String password;
    @Column(length = 64)
    private String photos;
    private boolean enabled;
    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles=new HashSet<>();

    public User(String email, String firstName, String lastName, String password, String photos, boolean enabled, Set<Role> roles) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.photos = photos;
        this.enabled = enabled;
        this.roles = roles;
    }

    public User(String email, String firstName, String lastName, String password, String photos, boolean enabled) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.photos = photos;
        this.enabled = enabled;
    }
    public void addNewRole(Role role){
        roles.add(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", photos='" + photos + '\'' +
                ", enabled=" + enabled +
                ", roles=" + roles +
                '}';
    }
    public String getAllRoles(){

        return roles.stream().map(Role::toString).reduce((a, b)->a+","+b).orElse("");
    };

    @Transient
    public String getPhotosImagePath(){
        if(id==null || photos==null){
            return "/image/user.png";
        }
        return "/user-photos/"+this.id+"/"+this.photos;
    }
}
