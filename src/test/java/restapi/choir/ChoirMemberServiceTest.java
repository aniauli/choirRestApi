package restapi.choir;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ChoirMemberServiceTest {

    ChoirMember firstMember = ChoirMember.builder().name("john").phoneNumber("testPhoneNumber1").build();
    ChoirMember secondMember = ChoirMember.builder().name("alex").phoneNumber("testPhoneNumber2").build();
    ChoirMember thirdMember = ChoirMember.builder().name("jennifer").phoneNumber("testPhoneNumber3").build();

    ChoirMemberService choirMemberService;

    @Autowired
    ChoirMemberRepository choirMemberRepository;

    @BeforeEach
    void setUp(){
        choirMemberRepository.saveAll(List.of(firstMember, secondMember, thirdMember));
        choirMemberService = new ChoirMemberService(choirMemberRepository);
    }

    @Test
    void listOfAllMembersIsSorted() {
        List<ChoirMember> choirMembersSorted = choirMemberService.listOfAllChoirMembersSortedByName();
        assertThat(choirMembersSorted).containsExactly(secondMember, thirdMember, firstMember);
    }

    @Test
    void newMemberIsAddedToChoir() {
        int previousMembersNumber = choirMemberRepository.findAll().size();
        choirMemberService.addMemberToChoirAndReturnId("newMember", "testPhoneNumber");

        assertThat(choirMemberRepository.findAll().size()).isGreaterThan(previousMembersNumber);
    }

    @Test
    void newMemberHasAnUniqueId() {
        List<Integer> idsOfOldMembers = choirMemberRepository.findAll().stream().map(ChoirMember::getID).collect(Collectors.toList());
        Integer newMemberId = choirMemberService.addMemberToChoirAndReturnId("newMember", "testPhoneNumber");

        assertThat(idsOfOldMembers).doesNotContain(newMemberId);
    }
}