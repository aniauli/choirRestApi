package restapi.choir;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor

class ChoirMemberService {

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

    void updateMembersData(Integer id, String probableName, String probablePhoneNumber){
        ChoirMemberEntity memberToUpdate = choirMemberRepository.findById(id).orElseThrow(() -> new InvalidIdException("There is no member with id " + id));
        if(probableName != null){
            memberToUpdate.setName(probableName);
        }
        if(probablePhoneNumber != null) {
            memberToUpdate.setPhoneNumber(probablePhoneNumber);
        }
        choirMemberRepository.save(memberToUpdate);
    }

    void deleteMember(Integer id){
        ChoirMemberEntity memberToDelete = choirMemberRepository.findById(id).orElseThrow(() -> new InvalidIdException("There is no member with id " + id));
        choirMemberRepository.delete(memberToDelete);
    }
}
