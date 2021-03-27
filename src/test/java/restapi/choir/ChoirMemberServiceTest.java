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

    ChoirMember firstMember = ChoirMember.builder().name("john").phoneNumber(555555678).build();
    ChoirMember secondMember = ChoirMember.builder().name("alex").phoneNumber(12345678).build();
    ChoirMember thirdMember = ChoirMember.builder().name("jennifer").phoneNumber(1545462).build();

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
        ChoirMember newMember = ChoirMember.builder().name("new").phoneNumber(155456).build();
        choirMemberRepository.save(newMember);
        int currentMembersNumber = choirMemberRepository.findAll().size();

        assertThat(currentMembersNumber).isGreaterThan(previousMembersNumber);
    }

    @Test
    void newMemberHasAnUniqueId() {
        List<Integer> idsOfOldMembers = choirMemberRepository.findAll().stream().map(ChoirMember::getID).collect(Collectors.toList());
        ChoirMember newMember = ChoirMember.builder().name("new").phoneNumber(155456).build();
        choirMemberRepository.save(newMember);

        assertThat(idsOfOldMembers).doesNotContain(newMember.getID());
    }
}