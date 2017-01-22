package ch.heigvd.dao;

import ch.heigvd.models.Application;
import ch.heigvd.models.Rule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RuleRepository extends CrudRepository<Rule, Integer> {
	List<Rule> findByApplication(Application application);
	Rule findByNameAndApplication(String name, Application application);

	@Query("select r from Rule r where r.application = ?1 and r.eventType = ?2")
	List<Rule> findMatching(Application application, String type);
}
