package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.entity.Category;
import packman.entity.Pack;
import packman.repository.CategoryRepository;

import static packman.validator.IdValidator.validateCategoryId;
import static packman.validator.LengthValidator.validatePackLength;
import static packman.validator.Validator.validateListCategory;

@Service
@Transactional
@RequiredArgsConstructor
public class ListPackService {
    private final CategoryRepository categoryRepository;

    public void addPackInCategory(Long listId, Long categoryId, String packName) {
        Category category = validateCategoryId(categoryRepository, categoryId);

        validatePackLength(packName);
        validateListCategory(listId, category);

        Pack pack = new Pack(category, packName);
        category.addPack(pack);
    }
}
