package ch.heigvd.api;

import ch.heigvd.Exception.ConflictException;
import ch.heigvd.Exception.NotFoundException;
import ch.heigvd.dao.RuleRepository;
import ch.heigvd.dto.RuleDTO;
import ch.heigvd.models.Application;
import ch.heigvd.models.Rule;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@Api(value = "Rules", description = "Rules management")
@ApiResponses(value = {
        @ApiResponse(
                code = 401,
                message = "Full authentication is required to access this resource"
        )
})
public class RuleController {
    @Autowired
    private RuleRepository ruleRepository;

    @ApiOperation(value = "Retrieve all rules for current application.")
    @RequestMapping(produces = {"application/json"}, method = RequestMethod.GET)
    public List<RuleDTO> getRules(@ApiIgnore @RequestAttribute("application") Application app) {
        return ruleRepository.findByApplication(app).stream().map(RuleDTO::fromRule).collect(Collectors.toList());
    }

    @ApiOperation(value = "Retrieve a specific rule")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 404,
                    message = "Rule does not exist"
            )
    })
    @RequestMapping(method = RequestMethod.GET, value = "/{ruleName}", produces = {"application/json"})
    public RuleDTO getRule(@ApiIgnore @RequestAttribute("application") Application app,
                           @ApiParam(required = true) @PathVariable("ruleName") String name) {
        Rule rule = ruleRepository.findByNameAndApplication(name, app);

        if(rule == null){
            throw new NotFoundException("Rule do not exist");
        }

        return RuleDTO.fromRule(rule);
    }

    @ApiOperation(value = "Create a rule")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 409,
                    message = "Rule already exists"
            )
    })
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public void addRule(@ApiIgnore @RequestAttribute("application") Application app,
                        @ApiParam(required = true, name = "rule") @Valid @RequestBody RuleDTO input) {

        Rule rule = ruleRepository.findByNameAndApplication(input.getName(), app);

        if(rule != null){
            throw new ConflictException("Rule already exists");
        }

        rule = new Rule(input.getName(), input.getEventType(), input.getExpr(), app);

        ruleRepository.save(rule);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Update a specific rule")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 404,
                    message = "Rule does not exist"
            ),
            @ApiResponse(
                    code = 400,
                    message = "Fields are missing"
            )
    })
    @RequestMapping(method = RequestMethod.PATCH, value = "/{ruleName}", consumes = "application/json")
    public void editRule(@ApiIgnore @RequestAttribute("application") Application app,
                         @ApiParam(name = "rule", required = true) @Valid @RequestBody RuleDTO input,
                         @ApiParam(required = true) @PathVariable("ruleName") String name) {

        Rule rule = ruleRepository.findByNameAndApplication(name, app);

        if(rule == null){
            throw new NotFoundException("Rule does not exist");
        }

        Rule rulCmp = ruleRepository.findByNameAndApplication(input.getName(), app);

        if(rulCmp != null && rule.getId() != rulCmp.getId()){
            throw new ConflictException("Rule name already used");
        }

        rule.setName(input.getName());
        rule.setEventType(input.getEventType());
        rule.setExpr(input.getExpr());

        ruleRepository.save(rule);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a specific rule")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 404,
                    message = "Rule does not exist"
            )
    })
    @RequestMapping(method = RequestMethod.DELETE, value = "/{ruleName}")
    public void deleteRule(@ApiIgnore @RequestAttribute("application") Application app,
                           @ApiParam(required = true) @PathVariable("ruleName") String name) {
        Rule rule = ruleRepository.findByNameAndApplication(name, app);

        if(rule == null){
            throw new NotFoundException("Rule does not exist");
        }

        ruleRepository.delete(rule);
    }
}
