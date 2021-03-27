package restapi.choir;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

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
}
