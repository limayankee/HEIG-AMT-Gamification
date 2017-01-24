package ch.heigvd.api;

import ch.heigvd.Exception.ConflictException;
import ch.heigvd.Exception.NotFoundException;
import ch.heigvd.dao.RuleRepository;
import ch.heigvd.dto.RuleDTO;
import ch.heigvd.models.Application;
import ch.heigvd.models.Level;
import ch.heigvd.models.Rule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jfleroy
 */

@RestController
@RequestMapping(value = "/rules")
@Api(value = "Rules", description = "CRUD on the rules")
@ApiResponses(value = {
        @ApiResponse(
                code = 401,
                message = "Full authentication is required to access this resource"
        )
})
public class RuleController
{
    @Autowired
    private RuleRepository ruleRepository;

    @ApiOperation(value = "Retrive all rules for current application.")

    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Successful operation.",
                    response = RuleDTO.class,
                    responseContainer = "List"
            )
    })

    @RequestMapping(produces = {"application/json"}, method = RequestMethod.GET)
    public List<RuleDTO> getRules(@RequestAttribute("application") Application app) {
        return ruleRepository.findByApplication(app).stream().map(RuleDTO::fromRule).collect(Collectors.toList());
    }

    @ApiOperation(value = "Retrieve a specific rule.")

    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Successful operation.",
                    response = Level.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "Rule do not exist",
                    response = Void.class
            )
    })

    @RequestMapping(method = RequestMethod.GET, value = "/{name}", produces = {"application/json"})
    public RuleDTO getRule(@ApiIgnore @RequestAttribute("application") Application app,
                             @PathVariable("name") String name) {
        Rule rule = ruleRepository.findByNameAndApplication(name, app);

        if(rule == null){
            throw new NotFoundException("Rule do not exist");
        }

        return RuleDTO.fromRule(rule);
    }

    @ApiOperation(value = "Create a rule.")

    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Successful operation.",
                    response = Void.class
            ),
            @ApiResponse(
                    code = 400,
                    message = "Fields are missing",
                    response = Void.class
            ),
            @ApiResponse(
                    code = 409,
                    message = "Rule already exists",
                    response = Void.class
            )
    })

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity addRule(@RequestAttribute("application") Application app, @Valid @RequestBody RuleDTO input) {

        Rule rule = ruleRepository.findByNameAndApplication(input.getName(), app);

        if(rule != null){
            throw new ConflictException("Rule already exists");
        }

        rule = new Rule(input.getName(), input.getEventType(), input.getExpr(), app);

        ruleRepository.save(rule);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "Update a specific rule.")

    @ApiResponses(value = {
            @ApiResponse(
                    code = 204,
                    message = "Successful operation.",
                    response = Void.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "Rule do not exist",
                    response = Void.class
            ),
            @ApiResponse(
                    code = 400,
                    message = "Fields are missing",
                    response = Void.class
            )
    })

    @RequestMapping(method = RequestMethod.PATCH, value = "/{name}", consumes = "application/json")
    public ResponseEntity editRule(@RequestAttribute("application") Application app, @Valid @RequestBody RuleDTO input,
                                   @PathVariable("name") String name) {

        Rule rule = ruleRepository.findByNameAndApplication(name, app);

        if(rule == null){
            throw new NotFoundException("Rule do not exist");
        }

        Rule rulCmp = ruleRepository.findByNameAndApplication(input.getName(), app);

        if(rulCmp != null && rule.getId() != rulCmp.getId()){
            throw new ConflictException("Rule name already used");
        }

        rule.setName(input.getName());
        rule.setEventType(input.getEventType());
        rule.setExpr(input.getExpr());

        ruleRepository.save(rule);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation(value = "Delete a specific rule.")

    @ApiResponses(value = {
            @ApiResponse(
                    code = 204,
                    message = "Successful operation.",
                    response = Void.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "Rule do not exist",
                    response = Void.class
            )
    })

    @RequestMapping(method = RequestMethod.DELETE, value = "/{name}")
    public ResponseEntity deleteRule(@RequestAttribute("application") Application app,
                                     @PathVariable("name") String name) {
        Rule rule = ruleRepository.findByNameAndApplication(name, app);

        if(rule == null){
            throw new NotFoundException("Rule do not exist");
        }

        ruleRepository.delete(rule);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
