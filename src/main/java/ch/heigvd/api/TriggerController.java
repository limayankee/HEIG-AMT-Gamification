package ch.heigvd.api;

import ch.heigvd.Exception.ConflictException;
import ch.heigvd.Exception.NotFoundException;
import ch.heigvd.dao.TriggerRepository;
import ch.heigvd.dto.TriggerDTO;
import ch.heigvd.models.Application;
import ch.heigvd.models.Trigger;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(value = "Triggers", description = "Triggers management")
@RequestMapping(value = "/triggers", consumes = "application/json")
@ApiResponses(value = {
		@ApiResponse(
				code = 401,
				message = "Full authentication is required to access this resource"
		)
})
public class TriggerController {

	@Autowired
	private TriggerRepository triggerRepository;


	@ApiOperation(value = "Fetch the triggers list")
	@RequestMapping(produces = {"application/json"}, method = RequestMethod.GET)
	public List<TriggerDTO> getTriggers(@ApiIgnore @RequestAttribute("application") Application app) {
		return triggerRepository.findByApplication(app).stream().map(TriggerDTO::fromTrigger).collect(Collectors.toList());
	}


	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Add a trigger")
	@ApiResponses(value = {
			@ApiResponse(
					code = 201,
					message = "Successful operation"
			),
			@ApiResponse(
					code = 409,
					message = "Trigger already exist"
			)
	})
	@RequestMapping(method = RequestMethod.POST)
	public void addTrigger(@ApiIgnore @RequestAttribute("application") Application app,
						   @ApiParam(name = "trigger", required = true) @Valid @RequestBody TriggerDTO input) {
		Trigger trigger = triggerRepository.findByNameAndApplication(input.getName(), app);
		if(trigger != null){
			throw new ConflictException("Trigger already exists");
		}
		trigger = new Trigger(input.getName(), trigger.getExpr(), app, input.getCriteria());
		triggerRepository.save(trigger);
	}


	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Add a trigger")
	@ApiResponses(value = {
			@ApiResponse(
					code = 204,
					message = "Successful operation"
			),
			@ApiResponse(
					code = 409,
					message = "Trigger already exist"
			)
	})
	@RequestMapping(method = RequestMethod.PATCH, value = "/{triggerName}", consumes = "application/json")
	public void editTrigger(@ApiIgnore @RequestAttribute("application") Application app,
	                                  @ApiParam(name = "trigger", required = true) @Valid @RequestBody TriggerDTO input,
	                                  @PathVariable("triggerName") String name) {
		Trigger trigger = triggerRepository.findByNameAndApplication(name, app);
		if(trigger == null){
			throw new NotFoundException("Trigger do not exist");
		}
		Trigger trigger2 = triggerRepository.findByNameAndApplication(input.getName(), app);
		if(trigger2 != null && trigger.getId() != trigger2.getId()){
			throw new ConflictException("Trigger name already used");
		}

		trigger.setName(input.getName());
		trigger.setExpr(input.getExpr());
		trigger.setTriggerCriterias(input.getCriteria());

		triggerRepository.save(trigger);
	}


	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Remove a trigger")
	@ApiResponses(value = {
			@ApiResponse(
					code = 204,
					message = "Successful operation"
			),
			@ApiResponse(
					code = 404,
					message = "Trigger do not exist"
			)
	})
	@RequestMapping(method = RequestMethod.DELETE, value="/{triggerName}")
	public void deleteTrigger(@ApiIgnore @RequestAttribute("application") Application app,
	                                    @PathVariable("triggerName") String name) {
		Trigger trigger = triggerRepository.findByNameAndApplication(name, app);
		if(trigger == null){
			throw new NotFoundException("Trigger do not exist");
		}

		triggerRepository.delete(trigger);
	}
}
