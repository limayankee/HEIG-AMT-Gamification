package ch.heigvd.dao;

import ch.heigvd.models.UserBadge;
import ch.heigvd.models.UserBadgeId;
import org.springframework.data.repository.CrudRepository;

public interface UserBadgeRepository extends CrudRepository<UserBadge, Integer> {
	UserBadge findByPk(UserBadgeId pk);
}
