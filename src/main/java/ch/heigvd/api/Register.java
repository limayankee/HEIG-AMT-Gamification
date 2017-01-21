package ch.heigvd.api;

import ch.heigvd.dao.ApplicationRepository;
import ch.heigvd.dto.ApplicationRegisterDTO;
import ch.heigvd.models.Application;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ApplicationRepository applicationRepository;

    @RequestMapping(method = RequestMethod.POST)
    String add(@RequestBody ApplicationRegisterDTO input) {
        Application app = input.buildApplication();
        applicationRepository.save(app);
        System.out.println(input.name + " " + input.password);
        return "Hello";
    }
}
