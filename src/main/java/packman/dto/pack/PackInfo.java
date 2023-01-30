package packman.dto.pack;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import packman.dto.user.PackerInfo;

import java.util.Optional;

@JsonPropertyOrder({"id", "name", "isChecked", "packer"})
public interface PackInfo {
    String getId();

    String getName();

    boolean getIsChecked();

    Optional<PackerInfo> getPacker();
}
