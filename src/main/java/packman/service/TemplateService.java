package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.template.TemplateListResponseDto;
import packman.repository.UserRepository;
import packman.repository.template.TemplateRepository;

import packman.dto.template.TemplateResponseDto;
import packman.dto.template.TemplateResponseMapping;
import packman.entity.User;
import packman.entity.template.Template;

import static packman.validator.IdValidator.validateTemplateId;
import static packman.validator.IdValidator.validateUserId;
import static packman.validator.Validator.validateUserTemplate;

@Service
@Transactional
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;
    private final UserRepository userRepository;

    public TemplateListResponseDto getAloneTemplateList(Long userId) {

        TemplateListResponseDto templateListResponseDto = TemplateListResponseDto.builder()
                .basicTemplate(templateRepository.findByUserIdAndIsAlonedAndIsDeleted(null, true, false))
                .myTemplate(templateRepository.findByUserIdAndIsAlonedAndIsDeleted(userId, true, false)).build();

        return templateListResponseDto;
    }

    public TemplateListResponseDto getTogetherTemplateList(Long userId) {

        TemplateListResponseDto templateListResponseDto = TemplateListResponseDto.builder()
                .basicTemplate(templateRepository.findByUserIdAndIsAlonedAndIsDeleted(null, false, false))
                .myTemplate(templateRepository.findByUserIdAndIsAlonedAndIsDeleted(userId, false, false)).build();

        return templateListResponseDto;
    }

    public TemplateResponseDto getTemplate(Long templateId, Long userId) {
        User user = validateUserId(userRepository, userId);
        Template template = validateTemplateId(templateRepository, templateId);

        validateUserTemplate(template, user);

        TemplateResponseMapping templateResponseMapping = templateRepository.findProjectionById(template.getId());

        return TemplateResponseDto.builder()
                .id(templateResponseMapping.getId())
                .title(templateResponseMapping.getTitle())
                .category(templateResponseMapping.getCategories()).build();
    }
}
