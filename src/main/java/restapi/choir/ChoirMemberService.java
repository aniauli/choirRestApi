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

    List<ChoirMember> listOfAllChoirMembersSortedByName(){
        List<ChoirMember> listOfAllChoirMembers = choirMemberRepository.findAll();
        listOfAllChoirMembers.sort(Comparator.comparing(ChoirMember::getName));
        return listOfAllChoirMembers;
    }

    int addMemberToChoirAndReturnId(String name, String phoneNumber){
        ChoirMember newMember = ChoirMember.builder().name(name).phoneNumber(phoneNumber).build();
        choirMemberRepository.save(newMember);
        return newMember.getID();
    }

    void updateMembersData(Integer id, Optional<String> probableName, Optional<String> probablePhoneNumber){
        ChoirMember memberToUpdate = choirMemberRepository
                .findById(id).orElseThrow(() -> new NoSuchElementException("There is no member with id " + id));
        probableName.ifPresent(memberToUpdate::setName);
        probablePhoneNumber.ifPresent(memberToUpdate::setPhoneNumber);
    }
}
