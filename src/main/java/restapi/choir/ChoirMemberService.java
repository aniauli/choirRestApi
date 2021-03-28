package restapi.choir;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChoirMemberService {

    private final ChoirMemberRepository choirMemberRepository;

    List<ChoirMemberEntity> listOfAllMembersSortedByName(){
        List<ChoirMemberEntity> listOfAllChoirMemberEntities = choirMemberRepository.findAll();
        listOfAllChoirMemberEntities.sort(Comparator.comparing(ChoirMemberEntity::getName));
        return listOfAllChoirMemberEntities;
    }

    int addMemberAndReturnId(String name, String phoneNumber){
        ChoirMemberEntity newMember = ChoirMemberEntity.builder().name(name).phoneNumber(phoneNumber).build();
        choirMemberRepository.save(newMember);
        return newMember.getId();
    }

    void updateMembersData(Integer id, Optional<String> probableName, Optional<String> probablePhoneNumber){
        ChoirMemberEntity memberToUpdate = choirMemberRepository.findById(id).orElseThrow(() -> new NoSuchElementException("There is no member with id " + id));
        probableName.ifPresent(memberToUpdate::setName);
        probablePhoneNumber.ifPresent(memberToUpdate::setPhoneNumber);
        choirMemberRepository.save(memberToUpdate);
    }

    void deleteMember(Integer id){
        ChoirMemberEntity memberToDelete = choirMemberRepository.findById(id).orElseThrow(() -> new NoSuchElementException("There is no member with id " + id));
        choirMemberRepository.delete(memberToDelete);
    }
}
