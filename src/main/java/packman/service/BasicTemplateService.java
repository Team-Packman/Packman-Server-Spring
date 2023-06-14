package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.template.TemplateResponseDto;
import packman.dto.template.TemplateResponseMapping;
import packman.entity.User;
import packman.entity.template.BasicTemplate;
import packman.repository.UserRepository;
import packman.repository.basicTemplate.BasicTemplateRepository;

import static packman.validator.IdValidator.validateBasicTemplateId;
import static packman.validator.IdValidator.validateUserId;
import static packman.validator.Validator.validateUserBasicTemplate;

@Service
@Transactional
@RequiredArgsConstructor
public class BasicTemplateService {
    private final BasicTemplateRepository basicTemplateRepository;
    private final UserRepository userRepository;

    public TemplateResponseDto getBasicTemplate(Long templateId, Long userId) {
        User user = validateUserId(userRepository, userId);
        BasicTemplate template = validateBasicTemplateId(basicTemplateRepository, templateId);

        validateUserBasicTemplate(template, user);

        TemplateResponseMapping templateResponseMapping = basicTemplateRepository.findProjectionById(template.getId());

        return TemplateResponseDto.builder()
                .id(templateResponseMapping.getId())
                .title(templateResponseMapping.getTitle())
                .category(templateResponseMapping.getCategories()).build();
    }
}
