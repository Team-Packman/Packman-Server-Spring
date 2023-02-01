package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.pack.PackUpdateDto;
import packman.entity.Category;
import packman.entity.Pack;
import packman.repository.CategoryRepository;
import packman.repository.PackRepository;

import static packman.validator.IdValidator.validateCategoryId;
import static packman.validator.IdValidator.validatePackId;
import static packman.validator.LengthValidator.validatePackLength;
import static packman.validator.Validator.validateCategoryPack;
import static packman.validator.Validator.validateListCategory;

@Service
@Transactional
@RequiredArgsConstructor
public class PackService {
    private final CategoryRepository categoryRepository;
    private final PackRepository packRepository;

    public void updatePackInCategory(PackUpdateDto packUpdateDto) {
        Long aloneId = Long.valueOf(packUpdateDto.getListId());
        Long categoryId = Long.valueOf(packUpdateDto.getCategoryId());
        String packName = packUpdateDto.getName();
        Long packId = Long.valueOf(packUpdateDto.getId());

        Category category = validateCategoryId(categoryRepository, categoryId);
        Pack pack = validatePackId(packRepository, packId);

        validatePackLength(packName);

        validateListCategory(aloneId, category);
        validateCategoryPack(categoryId, pack);

        pack.setChecked(packUpdateDto.getIsChecked());
        pack.setName(packName);
    }
}
