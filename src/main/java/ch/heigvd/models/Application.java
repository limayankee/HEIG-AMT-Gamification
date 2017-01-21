package ch.heigvd.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author jfleroy
 */

@Entity
@Table(name = "applications")
public class Application
{
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    public Application(){}
    public Application(String name, String password){
        this.name = name;
        this.password = password;
    }

}
