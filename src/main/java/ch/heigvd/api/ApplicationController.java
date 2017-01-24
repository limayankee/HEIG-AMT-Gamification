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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author jfleroy
 */

@RestController
@RequestMapping("/applications")
@Api(value = "applications", description = "Applications management")
public class ApplicationController {

	@Autowired
	private ApplicationRepository applicationRepository;

	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponses(value = {
			@ApiResponse(
					code = 201,
					message = "Application successfully created",
					response = ApplicationDTO.class
			),
			@ApiResponse(
					code = 409,
					message = "Application already registered"
            )
	})
	@RequestMapping(produces = {"application/json"}, method = RequestMethod.POST)
	@ApiParam(value = "The information of the new application", required = true)
	public ApplicationDTO post(@RequestBody ApplicationRegisterDTO input) {
		Application app = applicationRepository.findByName(input.name);
		if (app != null) {
			throw new ConflictException("Application already registered");
		}

		app = input.buildApplication();

		applicationRepository.save(app);

		return ApplicationDTO.fromApplication(app);
	}

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
			@ApiResponse(
					code = 204,
					message = "Application successfully removed",
					response = ApplicationDTO.class
			),
			@ApiResponse(
					code = 409,
					message = "Application already registered"
			),
            @ApiResponse(
                    code = 401,
                    message = "Full authentication is required to access this resource"
            )
	})
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity delete(@RequestAttribute("application") Application app) {
		applicationRepository.delete(app);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
