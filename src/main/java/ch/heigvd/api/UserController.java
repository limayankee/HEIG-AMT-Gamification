package ch.heigvd.api;

import ch.heigvd.Exception.BadRequestException;
import ch.heigvd.Exception.ConflictException;
import ch.heigvd.Exception.NotFoundException;
import ch.heigvd.dao.LevelRepository;
import ch.heigvd.dao.UserRepository;
import ch.heigvd.dto.BadgeDTO;
import ch.heigvd.dto.LevelDTO;
import ch.heigvd.dto.UserDTO;
import ch.heigvd.models.Application;
import ch.heigvd.models.Level;
import ch.heigvd.models.User;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users")
@Api(value = "Users", description = "CRUD on the users")
@ApiResponses(value = {
		@ApiResponse(
				code = 401,
				message = "Full authentication is required to access this resource"
		)
})
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LevelRepository levelRepository;

	@ApiResponses(value = {
			@ApiResponse(
					code = 200,
					message = "Successful fetch",
					response = UserDTO.class
			),
			@ApiResponse(
					code = 403,
					message = "You need at least one Level with threshold 0 on your application",
					response = Void.class
			),
			@ApiResponse(
					code = 404,
					message = "Could not find given user",
					response = Void.class
			)
	})
	@ApiOperation(value = "Fetches an user")
	@ApiParam(name = "userId", value = "The application custom user id")
	@RequestMapping(method = RequestMethod.GET, value = "/{userId}", produces = {"application/json"})
	public UserDTO getUser(@RequestAttribute("application") Application app, @PathVariable String userId) {
		User u = userRepository.findByAppUserIdAndApplicationId(userId, app.getId());
		if (u == null) {
			throw new NotFoundException("Could not find given user");
		} else {
			int level = u.getUserBadges().stream()
					.mapToInt(ub -> ub.getCount() * ub.getPk().getBadge().getPoints()).sum();
			List<BadgeDTO> badges = u.getUserBadges().stream()
					.map(ub -> ub.getPk().getBadge())
					.map(BadgeDTO::fromBadge)
					.collect(Collectors.toList());
			Level l = levelRepository.findByMaxThreshold(level);
			if (l == null) {
				throw new BadRequestException("You need at least one Level with threshold 0 on your application");
			}
			return new UserDTO(u.getAppUserId(), new LevelDTO(l.getName(), l.getThreshold()), level, badges);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{userId}")
	public ResponseEntity createUser(@RequestAttribute("application") Application app, @PathVariable String userId) {
		User u = userRepository.findByAppUserIdAndApplicationId(userId, app.getId());
		if (u != null) {
			throw new ConflictException("User already exists");
		}

		userRepository.save(new User(app, userId));
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@ApiResponses(value = {
			@ApiResponse(
					code = 200,
					message = "",
					response = UserDTO.class),
			@ApiResponse(
					code = 404,
					message = "Could not find given user",
					response = Void.class
			)
	})
	@ApiOperation(value = "Deletes an user.")
	@RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
	public void deleteUser(@RequestAttribute("application") Application app, @PathVariable String userId) {
		User u = userRepository.findByAppUserIdAndApplicationId(userId, app.getId());
		if (u == null) {
			throw new NotFoundException("Could not find given user");
		} else {
			userRepository.delete(u.getId());
		}
	}
}
