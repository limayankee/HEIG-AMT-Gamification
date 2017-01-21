package ch.heigvd.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author jfleroy
 */
@Entity
@Table(name = "badges")
public class Badge
{
    @Id
    @Column(name = "id")
    private int id;
}