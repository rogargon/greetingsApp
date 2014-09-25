package cat.udl.eps.softarch.hello.repository;

import cat.udl.eps.softarch.hello.model.Greeting;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by http://rhizomik.net/~roberto/
 */

public interface GreetingRepository extends CrudRepository<Greeting, Long> {

    // CrudRepository provides:
    // delete(T entity), findAll(), findOne(ID id) and save(T entity)
}
