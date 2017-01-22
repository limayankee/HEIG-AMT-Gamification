package ch.heigvd.dao;

import ch.heigvd.models.Application;
import ch.heigvd.models.Level;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LevelRepository extends CrudRepository<Level, Integer> {

	List<Level> findByApplication(Application application);
	Level findByNameAndApplication(String name, Application application);
	@Query("select l from Level l where l.threshold  = (select MAX(l2.threshold) from Level l2 where l2.threshold <= ?1)")
	Level findByMaxThreshold(int threshold);
}