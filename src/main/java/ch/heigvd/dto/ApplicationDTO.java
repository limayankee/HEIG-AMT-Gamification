package ch.heigvd.dto;

import ch.heigvd.models.Application;

/**
 * @author jfleroy
 */
public class ApplicationDTO
{
    private int id;

    private String name;

    private ApplicationDTO(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public static ApplicationDTO fromApplication(Application app){
        return new ApplicationDTO(app.getId(), app.getName());
    }
}
