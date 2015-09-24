package cat.udl.eps.softarch.hello.repository;

import cat.udl.eps.softarch.hello.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by http://rhizomik.net/~roberto/
 */

@RepositoryRestResource
public interface UserRepository extends PagingAndSortingRepository<User, String> {

    User findUserByEmail(@Param("email") String email);
}
