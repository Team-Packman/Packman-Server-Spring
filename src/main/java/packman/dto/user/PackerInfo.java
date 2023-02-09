package packman.dto.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "nickname"})
public interface PackerInfo {
    String getId();

    String getNickname();
}
