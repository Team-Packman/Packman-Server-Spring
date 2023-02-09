package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.template.TemplateListResponseDto;
import packman.repository.UserRepository;
import packman.repository.template.TemplateRepository;

import static packman.validator.IdValidator.*;

@Service
@Transactional
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;
    private final UserRepository userRepository;
    public TemplateListResponseDto getAloneTemplateList(Long userId){
        // 유저 검증
        validateUserId(userRepository, userId);

        TemplateListResponseDto templateListResponseDto = TemplateListResponseDto.builder()
                .basicTemplate(templateRepository.findByUserIdAndIsAlonedAndIsDeleted(null,true, false))
                .myTemplate(templateRepository.findByUserIdAndIsAlonedAndIsDeleted(userId, true,false)).build();

        return templateListResponseDto;
    }
}
