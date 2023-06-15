package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.template.TemplateListResponseDto;
import packman.dto.template.TemplateResponseDto;
import packman.dto.template.TemplateResponseMapping;
import packman.entity.User;
import packman.entity.template.BasicTemplate;
import packman.entity.template.Template;
import packman.repository.UserRepository;
import packman.repository.basicTemplate.BasicTemplateRepository;
import packman.repository.template.TemplateRepository;
import packman.util.LogMessage;

import static packman.validator.IdValidator.*;
import static packman.validator.Validator.validateUserBasicTemplate;
import static packman.validator.Validator.validateUserTemplate;

@Service
@Transactional
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;
    private final BasicTemplateRepository basicTemplateRepository;
    private final UserRepository userRepository;

    public TemplateListResponseDto getAloneTemplateList(Long userId) {

        TemplateListResponseDto templateListResponseDto = TemplateListResponseDto.builder()
                .basicTemplate(basicTemplateRepository.findByUserIdAndIsAlonedAndIsDeletedOrderByCreatedAt(null, true, false))
                .myTemplate(templateRepository.findByUserIdAndIsAlonedAndIsDeletedOrderByCreatedAt(userId, true, false)).build();

        LogMessage.setNonDataLog("혼자 패킹 템플릿 리스트 조회", userId);

        return templateListResponseDto;
    }

    public TemplateListResponseDto getTogetherTemplateList(Long userId) {

        TemplateListResponseDto templateListResponseDto = TemplateListResponseDto.builder()
                .basicTemplate(basicTemplateRepository.findByUserIdAndIsAlonedAndIsDeletedOrderByCreatedAt(null, false, false))
                .myTemplate(templateRepository.findByUserIdAndIsAlonedAndIsDeletedOrderByCreatedAt(userId, false, false)).build();

        LogMessage.setNonDataLog("함께 패킹 템플릿 리스트 조회", userId);

        return templateListResponseDto;
    }

    public TemplateResponseDto getTemplate(Long templateId, boolean isBasic, Long userId) {
        User user = validateUserId(userRepository, userId);

        if (isBasic) {
            BasicTemplate template = validateBasicTemplateId(basicTemplateRepository, templateId);

            validateUserBasicTemplate(template, user);

            TemplateResponseMapping templateResponseMapping = basicTemplateRepository.findProjectionById(template.getId());

            return TemplateResponseDto.builder()
                    .id(templateResponseMapping.getId())
                    .title(templateResponseMapping.getTitle())
                    .category(templateResponseMapping.getCategories()).build();
        } else {

            Template template = validateTemplateId(templateRepository, templateId);

            validateUserTemplate(template, user);

            TemplateResponseMapping templateResponseMapping = templateRepository.findProjectionById(template.getId());

            return TemplateResponseDto.builder()
                    .id(templateResponseMapping.getId())
                    .title(templateResponseMapping.getTitle())
                    .category(templateResponseMapping.getCategories()).build();
        }
    }
}
