package restapi.choir;

import lombok.Value;
import org.springframework.lang.Nullable;

@Value
public class ChoirMemberToUpdate {
    Integer id;
    @Nullable
    String name;
    @Nullable
    String phoneNumber;
}
