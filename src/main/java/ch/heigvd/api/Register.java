package ch.heigvd.api;

import ch.heigvd.dto.ApplicationDTO;
import ch.heigvd.dao.ApplicationRepository;
import ch.heigvd.dto.ApplicationRegisterDTO;
import ch.heigvd.models.Application;
import io.swagger.annotations.*;
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
@Api(value = "auth", description = "the auth API")
public class Register
{
    @Autowired
    private ApplicationRepository applicationRepository;

    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Successful operation.",
                    response = ApplicationDTO.class
            )
    })


    @RequestMapping(method = RequestMethod.POST)
    @ApiParam(value = "The information of the new application", required = true)
    String add(@RequestBody ApplicationRegisterDTO input) {
        Application app = input.buildApplication();
        applicationRepository.save(app);
        System.out.println(input.name + " " + input.password);
        return "Hello";
    }
}
