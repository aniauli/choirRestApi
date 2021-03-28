package restapi.choir;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChoirMemberRepository extends CrudRepository<ChoirMemberEntity, Integer> {
    List<ChoirMemberEntity> findAll();
    Optional<ChoirMemberEntity> findByName(String name);
}
