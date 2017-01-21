package ch.heigvd.dao;

import ch.heigvd.models.Level;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LevelRepository extends CrudRepository<Level, Integer> {

	List<Level> findByApplication(Application application);
	Level findByNameAndApplication(String name, Application application);
	@Query("select l.name, MAX(l.threshold) from Level l where l.threshold <= ?1")
	Level findByMaxThreshold(int threshold);
}