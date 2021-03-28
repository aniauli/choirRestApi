package restapi.choir;

import lombok.Value;

import java.util.Optional;

@Value
public class ChoirMemberToUpdate {

    Integer ID;
    Optional<String> name;
    Optional<String> phoneNumber;
}
