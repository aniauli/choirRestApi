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
    int addNewMember(@RequestBody ChoirMember newChoirMember) {
        return choirMemberService.addMemberAndReturnId(newChoirMember.getName(), newChoirMember.getPhoneNumber());
    }

    @PostMapping("/updatemember")
    void updateMember(@RequestBody ChoirMemberToUpdate choirMemberToUpdate) {
        choirMemberService.updateMembersData(
                choirMemberToUpdate.getID(), choirMemberToUpdate.getName(), choirMemberToUpdate.getPhoneNumber());
    }
}
