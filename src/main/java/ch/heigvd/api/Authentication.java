package ch.heigvd.api;

import ch.heigvd.dao.ApplicationRepository;
import ch.heigvd.models.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author jfleroy
 */

@RestController
@RequestMapping("/login")
public class Authentication
{
    @Autowired
    private ApplicationRepository applicationRepository;

    @RequestMapping(method = RequestMethod.GET, produces = {"application/json"})
    public Application currentUserNameSimple(@ApiIgnore @RequestAttribute("application") Application app) {
        return app;
    }
}
