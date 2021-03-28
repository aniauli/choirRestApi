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
    ChoirMemberEntity firstMember = ChoirMemberEntity.builder().name("john").phoneNumber("phoneNumberForTest1").build();
    ChoirMemberEntity secondMember = ChoirMemberEntity.builder().name("alex").phoneNumber("phoneNumberForTest2").build();
    ChoirMemberEntity thirdMember = ChoirMemberEntity.builder().name("jennifer").phoneNumber("phoneNumberForTest3").build();

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
        List<ChoirMemberEntity> choirMembersSortedEntity = choirMemberService.listOfAllMembersSortedByName();
        assertThat(choirMembersSortedEntity).containsExactly(secondMember, thirdMember, firstMember);
    }

    @Test
    void newMemberIsAddedToChoir() {
        int previousMembersNumber = choirMemberRepository.findAll().size();
        choirMemberService.addMemberAndReturnId("newMember", "testPhoneNumber");
        assertThat(choirMemberRepository.findAll().size()).isGreaterThan(previousMembersNumber);
        assertThat(choirMemberRepository.findAll().stream().map(ChoirMemberEntity::getName).collect(Collectors.toList())).contains("newMember");
    }

    @Test
    void newMemberHasAnUniqueId() {
        List<Integer> idsOfOldMembers = choirMemberRepository.findAll().stream().map(ChoirMemberEntity::getId).collect(Collectors.toList());
        Integer newMemberId = choirMemberService.addMemberAndReturnId("new", "testPhone");
        assertThat(idsOfOldMembers).doesNotContain(newMemberId);
    }

    @Test
    void choirMembersPhoneNumberIsUpdated() {
        Integer idMemberToUpdate = choirMemberRepository.findByName("alex").get().getId();
        String newPhone = "newPhoneNumberForTest";
        choirMemberService.updateMembersData(idMemberToUpdate, Optional.empty(), Optional.of(newPhone));
        assertThat(choirMemberRepository.findById(idMemberToUpdate).get().getPhoneNumber()).isEqualTo(newPhone);
    }

    @Test
    void choirMembersNameIsUpdated() {
        Integer idMemberToUpdate = choirMemberRepository.findByName("alex").get().getId();
        String newName = "newUpdatedName";
        choirMemberService.updateMembersData(idMemberToUpdate, Optional.of(newName), Optional.empty());
        assertThat(choirMemberRepository.findById(idMemberToUpdate).get().getName()).isEqualTo(newName);
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
        Integer idMemberToDelete = choirMemberRepository.findByName("alex").get().getId();
        choirMemberService.deleteMember(idMemberToDelete);
        assertThat(choirMemberRepository.findAll().stream().map(ChoirMemberEntity::getId).collect(Collectors.toList()))
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