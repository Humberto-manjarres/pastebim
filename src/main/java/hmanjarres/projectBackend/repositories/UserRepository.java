package hmanjarres.projectBackend.repositories;

import hmanjarres.projectBackend.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
}
