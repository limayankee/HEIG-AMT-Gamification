package ch.heigvd.api;

import ch.heigvd.dto.ApplicationDTOIn;
import ch.heigvd.models.Application;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jfleroy
 */

@RestController
@RequestMapping("/register")
public class Register
{

    @RequestMapping(method = RequestMethod.POST)
    String add(@RequestBody ApplicationDTOIn input) {
        Application app = input.buildApplication();
        Application test = new Application();
        System.out.println(input.name + " " + input.password);
        return "Hello";
    }
}
