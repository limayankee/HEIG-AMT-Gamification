package ch.heigvd.api;

import ch.heigvd.Exception.BadRequestException;
import ch.heigvd.Exception.NotFoundException;
import ch.heigvd.dao.LevelRepository;
import ch.heigvd.dao.UserRepository;
import ch.heigvd.dto.ApplicationDTO;
import ch.heigvd.dto.BadgeDTO;
import ch.heigvd.dto.LevelDTO;
import ch.heigvd.dto.UserDTO;
import ch.heigvd.models.Application;
import ch.heigvd.models.Level;
import ch.heigvd.models.User;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LevelRepository levelRepository;

	@ApiResponses(value = {
			@ApiResponse(
					code = 201,
					message = "Successful operation.",
					response = ApplicationDTO.class
			),
			@ApiResponse(
					code = 400,
					message = "Bad request for client",
					response = Void.class
			)
	})
	@ApiParam(value = "The information of the new application", required = true)
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
}
