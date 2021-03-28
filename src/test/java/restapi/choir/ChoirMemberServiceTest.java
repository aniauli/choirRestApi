package restapi.choir;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ChoirMemberServiceTest {
    ChoirMember firstMember = ChoirMember.builder().name("john").phoneNumber("phoneNumberForTest1").build();
    ChoirMember secondMember = ChoirMember.builder().name("alex").phoneNumber("phoneNumberForTest2").build();
    ChoirMember thirdMember = ChoirMember.builder().name("jennifer").phoneNumber("phoneNumberForTest3").build();

    ChoirMemberService choirMemberService;

    @Autowired
    ChoirMemberRepository choirMemberRepository;

    @BeforeEach
    void setUp(){
        choirMemberRepository.deleteAll();
        choirMemberRepository.saveAll(List.of(firstMember, secondMember, thirdMember));
        choirMemberService = new ChoirMemberService(choirMemberRepository);
    }

    @Test
    void listOfAllMembersIsSorted() {
        List<ChoirMember> choirMembersSorted = choirMemberService.listOfAllMembersSortedByName();
        assertThat(choirMembersSorted).containsExactly(secondMember, thirdMember, firstMember);
    }

    @Test
    void newMemberIsAddedToChoir() {
        int previousMembersNumber = choirMemberRepository.findAll().size();
        choirMemberService.addMemberAndReturnId("newMember", "testPhoneNumber");
        assertThat(choirMemberRepository.findAll().size()).isGreaterThan(previousMembersNumber);
    }

    @Test
    void newMemberHasAnUniqueId() {
        List<Integer> idsOfOldMembers = choirMemberRepository.findAll().stream().map(ChoirMember::getID).collect(Collectors.toList());
        Integer newMemberId = choirMemberService.addMemberAndReturnId("new", "testPhone");
        assertThat(idsOfOldMembers).doesNotContain(newMemberId);
    }

    @Test
    void choirMemberIsUpdated() {
        Integer idMemberToUpdate = choirMemberRepository.findByName("alex").get().getID();
        String newPhone = "newPhoneNumberForTest";
        choirMemberService.updateMembersData(idMemberToUpdate, Optional.empty(), Optional.of(newPhone));
        assertThat(choirMemberRepository.findById(idMemberToUpdate).get().getPhoneNumber()).isEqualTo(newPhone);
    }


    @Test
    void anExceptionIsThrownWhenUpdatingNonChoirMember() {
        Integer idMemberToUpdate = 0;
        String newName = "newNameForTest";
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> choirMemberService.updateMembersData(idMemberToUpdate, Optional.of(newName), Optional.empty()))
                .withMessage("There is no member with id " + idMemberToUpdate);
    }

    @Test
    void choirMemberIsDeleted() {
        Integer idMemberToDelete = choirMemberRepository.findByName("alex").get().getID();
        choirMemberService.deleteMember(idMemberToDelete);
        assertThat(choirMemberRepository.findAll().stream().map(ChoirMember::getID).collect(Collectors.toList()))
                .doesNotContain(idMemberToDelete);
    }

    @Test
    void anExceptionIsThrownWhenDeletingNonChoirMember() {
        Integer idMemberToDelete = 0;
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> choirMemberService.deleteMember(idMemberToDelete))
                .withMessage("There is no member with id " + idMemberToDelete);
    }
}