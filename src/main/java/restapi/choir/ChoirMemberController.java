package restapi.choir;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/choirmembers")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ChoirMemberController {

    ChoirMemberService choirMemberService;

    @GetMapping("/showall")
    List<ChoirMemberEntity> showAllMembers() {
        return choirMemberService.listOfAllMembersSortedByName();
    }

    @PostMapping("/addmember")
    int addNewMember(@RequestBody ChoirMemberToAdd newChoirMemberToAdd) {
        return choirMemberService.addMemberAndReturnId(newChoirMemberToAdd.getName(), newChoirMemberToAdd.getPhoneNumber());
    }

    @PutMapping("/updatemember")
    void updateMember(@RequestBody ChoirMemberToUpdate choirMemberToUpdate) {
        choirMemberService.updateMembersData(
                choirMemberToUpdate.getId(), choirMemberToUpdate.getName(), choirMemberToUpdate.getPhoneNumber());
    }

    @DeleteMapping("/deletemember/{id}")
    void deleteMember(@PathVariable Integer id) {
        choirMemberService.deleteMember(id);
    }
}
