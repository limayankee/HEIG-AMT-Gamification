
package ch.heigvd.dto;

import ch.heigvd.models.Application;

/**
 * @author jfleroy
 */
public class ApplicationRegisterDTO
{
    public String name;
    public String password;

    public ApplicationRegisterDTO(){

    }

    public ApplicationRegisterDTO(String name, String password){
        this.name = name;
        this.password = password;
    }

    public Application buildApplication(){
        return new Application(name, password);
    }
}
