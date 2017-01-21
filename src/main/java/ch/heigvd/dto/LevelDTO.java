package ch.heigvd.dto;

import ch.heigvd.models.Level;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author jfleroy
 */
public class LevelDTO
{
    @NotEmpty
    private String name;

    @NotNull
    private int threshold;

    public LevelDTO(){

    }

    public LevelDTO(String name, int threshold){
        this.name = name;
        this.threshold = threshold;
    }

    public String getName()
    {
        return name;
    }

    public int getThreshold(){
        return threshold;
    }

    public static LevelDTO fromLevel(Level level){
        return new LevelDTO(level.getName(), level.getThreshold());
    }
}
