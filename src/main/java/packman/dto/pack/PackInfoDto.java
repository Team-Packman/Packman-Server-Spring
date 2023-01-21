package packman.dto.pack;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
@Data
@AllArgsConstructor
public class PackInfoDto {
    private String id;
    private String name;
    private Boolean isChecked;
    private ArrayList<Packer> packer;

    public class Packer{
        private String id;
        private String nickname;
    }
}
