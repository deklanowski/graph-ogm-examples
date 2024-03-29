package de.jotschi.examples.sdn;

import org.springframework.data.neo4j.repository.GraphRepository;

public interface UserRepository extends GraphRepository<User> {

	User findByName(String name);

}
