package restapi.choir;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoirMemberRepository extends CrudRepository<ChoirMember, Integer> {
    List<ChoirMember> findAll();
}
