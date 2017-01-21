package ch.heigvd.dto;

import ch.heigvd.models.Application;

/**
 * @author jfleroy
 */
public class ApplicationDTOIn
{
    public String name;

    public String password;

    public Application buildApplication(){
        return new Application(name, password);
    }
}
