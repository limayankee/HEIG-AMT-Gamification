package ch.heigvd.dto;

import ch.heigvd.models.Rule;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author jfleroy
 */
public class RuleDTO
{
    @NotEmpty
    private String name;

    @NotEmpty
    private String eventType;

    @NotEmpty
    private String expr;

    public RuleDTO(){

    }

    public RuleDTO(String name, String eventType, String expr){
        this.name = name;
        this.eventType = eventType;
        this.expr = expr;
    }

    public String getName()
    {
        return name;
    }

    public String getEventType(){
        return eventType;
    }

    public String getExpr(){
        return expr;
    }

    public static RuleDTO fromRule(Rule rule){
        return new RuleDTO(rule.getName(), rule.getEventType(), rule.getExpr());
    }
}
