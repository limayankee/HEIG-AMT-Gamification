package ch.heigvd.dao;

import ch.heigvd.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
	User findByAppUserIdAndApplicationId(String appUserId, int applicationId);
}
