package ch.heigvd.api;

import ch.heigvd.Exception.ConflictException;
import ch.heigvd.dao.ApplicationRepository;
import ch.heigvd.dto.ApplicationDTO;
import ch.heigvd.dto.ApplicationRegisterDTO;
import ch.heigvd.models.Application;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jfleroy
 */

@RestController
@RequestMapping("/applications")
@Api(value = "auth", description = "the auth API")
public class ApplicationController
{
    @Autowired
    private ApplicationRepository applicationRepository;

    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Successful operation.",
                    response = ApplicationDTO.class
            ),
            @ApiResponse(
                    code = 409,
                    message = "Application already registered",
                    response = Void.class
            )
    })

    @RequestMapping(produces = {"application/json"}, method = RequestMethod.POST)
    @ApiParam(value = "The information of the new application", required = true)
    public ApplicationDTO post(@RequestBody ApplicationRegisterDTO input) {
        Application app = applicationRepository.findByName(input.name);
        if(app != null){
            throw new ConflictException("Application already registered");
        }

        app = input.buildApplication();

        applicationRepository.save(app);

        return ApplicationDTO.fromApplication(app);
    }
}
