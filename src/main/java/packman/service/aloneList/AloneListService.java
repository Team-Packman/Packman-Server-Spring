package packman.service.aloneList;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.alonelist.AloneListResponseDto;
import packman.dto.list.ListRequestDto;
import packman.repository.FolderRepository;
import packman.repository.UserRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

@Service
@Transactional
@RequiredArgsConstructor
public class AloneListService {
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    public AloneListResponseDto createAloneList(ListRequestDto listRequestDto, Long userId) {
    }
}
