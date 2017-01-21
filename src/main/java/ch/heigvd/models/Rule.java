package ch.heigvd.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author jfleroy
 */
@Entity
@Table(name = "rules")
public class Rule
{
    @Id
    @Column(name = "id")
    private int id;
}
