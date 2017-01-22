package ch.heigvd.api;

import ch.heigvd.Exception.ConflictException;
import ch.heigvd.Exception.NotFoundException;
import ch.heigvd.dao.LevelRepository;
import ch.heigvd.dto.LevelDTO;
import ch.heigvd.models.Application;
import ch.heigvd.models.Level;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jfleroy
 */

@RestController
@RequestMapping(value = "/levels", consumes = "application/json")
@Api(value = "Levels", description = "CRUD on the levels")
public class LevelController
{
    @Autowired
    private LevelRepository levelRepository;

    @ApiOperation(value = "Retrive all levels for current application.")

    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Successful operation.",
                    response = LevelDTO.class,
                    responseContainer = "List"
            )
    })

    @RequestMapping(produces = {"application/json"}, method = RequestMethod.GET)
    public List<LevelDTO> getLevels(@RequestAttribute("application") Application app) {
        return levelRepository.findByApplication(app).stream().map(LevelDTO::fromLevel).collect(Collectors.toList());
    }

    @ApiOperation(value = "Retrieve a specific level.")

    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Successful operation.",
                    response = Level.class
            ),
            @ApiResponse(
                    code = 404,
                    message = "Level do not exist",
                    response = Void.class
            )
    })

    @RequestMapping(method = RequestMethod.GET, value = "/{name}")
    public LevelDTO getLevel(@RequestAttribute("application") Application app,
                                      @PathVariable("name") String name) {
        Level level = levelRepository.findByNameAndApplication(name, app);

        if(level == null){
            throw new NotFoundException("Level do not exist");
        }

        return LevelDTO.fromLevel(level);
    }

    @ApiOperation(value = "Create a level.")

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
                    message = "Level already exists",
                    response = Void.class
            )
    })

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addLevel(@RequestAttribute("application") Application app, @Valid @RequestBody LevelDTO
            input) {

        Level level = levelRepository.findByNameAndApplication(input.getName(), app);

        if(level != null){
            throw new ConflictException("Level already exists");
        }

        level = new Level(input.getName(), input.getThreshold(), app);

        levelRepository.save(level);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "Updates a specific level.")

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
            ),
            @ApiResponse(
                    code = 400,
                    message = "Fields are missing",
                    response = Void.class
            )
    })

    @RequestMapping(method = RequestMethod.PATCH, value = "/{name}")
    public ResponseEntity editLevel(@RequestAttribute("application") Application app, @Valid @RequestBody LevelDTO input,
                                   @PathVariable("name") String name) {

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

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation(value = "Deletes a specific level.")

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

    @RequestMapping(method = RequestMethod.DELETE, value = "/{name}")
    public ResponseEntity deleteLevel(@RequestAttribute("application") Application app,
                                     @PathVariable("name") String name) {
        Level level = levelRepository.findByNameAndApplication(name, app);

        if(level == null){
            throw new NotFoundException("Level do not exist");
        }

        levelRepository.delete(level);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
