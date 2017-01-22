package ch.heigvd.dao;

import ch.heigvd.models.User;
import ch.heigvd.models.UserBadge;
import ch.heigvd.models.UserBadgeId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserBadgeRepository extends CrudRepository<UserBadge, Integer> {
	UserBadge findByPk(UserBadgeId pk);

	@Query("select b from UserBadge b where b.pk.user = ?1")
	List<UserBadge> findByUser(User user);
}
