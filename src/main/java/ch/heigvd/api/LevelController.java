package ch.heigvd.api;

import ch.heigvd.Exception.ConflictException;
import ch.heigvd.Exception.NotFoundException;
import ch.heigvd.dao.LevelRepository;
import ch.heigvd.dto.LevelDTO;
import ch.heigvd.models.Application;
import ch.heigvd.models.Level;
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
@RequestMapping(value = "/levels")
@Api(value = "Levels", description = "Level management")
@ApiResponses(value = {
        @ApiResponse(
                code = 401,
                message = "Full authentication is required to access this resource"
        )
})
public class LevelController
{
    @Autowired
    private LevelRepository levelRepository;

    @ApiOperation(value = "Retrive all levels for current application.")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Successful operation."
            )
    })
    @RequestMapping(produces = {"application/json"}, method = RequestMethod.GET)
    public List<LevelDTO> getLevels(@RequestAttribute("application") @ApiIgnore Application app) {
        return levelRepository.findByApplication(app).stream().map(LevelDTO::fromLevel).collect(Collectors.toList());
    }

    @ApiOperation(value = "Retrieve a specific level.")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Successful operation."
            ),
            @ApiResponse(
                    code = 404,
                    message = "Level do not exist"
            )
    })
    @RequestMapping(method = RequestMethod.GET, value = "/{levelName}", produces = {"application/json"})
    public LevelDTO getLevel(@ApiIgnore @RequestAttribute("application") Application app,
                             @PathVariable("levelName") @ApiParam(required = true) String name) {
        Level level = levelRepository.findByNameAndApplication(name, app);

        if(level == null){
            throw new NotFoundException("Level do not exist");
        }

        return LevelDTO.fromLevel(level);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create a level.")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Successful operation."
            ),
            @ApiResponse(
                    code = 409,
                    message = "Level already exists"
            )
    })

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public void addLevel(@RequestAttribute("application") @ApiIgnore Application app,
                                   @Valid @RequestBody @ApiParam(name = "level", required = true) LevelDTO input) {

        Level level = levelRepository.findByNameAndApplication(input.getName(), app);

        if(level != null){
            throw new ConflictException("Level already exists");
        }

        level = new Level(input.getName(), input.getThreshold(), app);

        levelRepository.save(level);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Update a specific level.")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 204,
                    message = "Successful operation.",
                    response = Void.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "Level do not exist",
                    response = Void.class
            )
    })
    @RequestMapping(method = RequestMethod.PATCH, value = "/{levelName}", consumes = "application/json")
    public void editLevel(@RequestAttribute("application") @ApiIgnore Application app,
                                    @Valid @RequestBody @ApiParam(name = "level", required = true)  LevelDTO input,
                                    @PathVariable("levelName")  String name) {

        Level level = levelRepository.findByNameAndApplication(name, app);

        if(level == null){
            throw new NotFoundException("Level do not exist");
        }

        Level levelCmp = levelRepository.findByNameAndApplication(input.getName(), app);

        if(levelCmp != null && level.getId() != levelCmp.getId()){
            throw new ConflictException("Level name already used");
        }

        level.setName(input.getName());
        level.setThreshold(input.getThreshold());

        levelRepository.save(level);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a specific level.")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 204,
                    message = "Successful operation.",
                    response = Void.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "Level do not exist",
                    response = Void.class
            )
    })

    @RequestMapping(method = RequestMethod.DELETE, value = "/{levelName}")
    public void deleteLevel(@RequestAttribute("application") @ApiIgnore Application app,
                                     @PathVariable("levelName") String name) {
        Level level = levelRepository.findByNameAndApplication(name, app);

        if(level == null){
            throw new NotFoundException("Level do not exist");
        }

        levelRepository.delete(level);
    }
}
