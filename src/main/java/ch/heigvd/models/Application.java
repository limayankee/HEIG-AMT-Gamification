package ch.heigvd.models;

import javax.persistence.*;

@Entity
@Table(name = "applications")
public class Application
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "role")
    private String role;

    public Application(){}
    public Application(String name, String password){
        this.name = name;
        this.password = password;
    }

}
