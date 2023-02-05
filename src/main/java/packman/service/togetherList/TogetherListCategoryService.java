package packman.service.togetherList;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.category.CategoryCreateDto;
import packman.dto.list.ListResponseMapping;
import packman.entity.Category;
import packman.entity.UserGroup;
import packman.entity.packingList.PackingList;
import packman.repository.packingList.PackingListRepository;

import java.util.List;

import static packman.validator.DuplicatedValidator.validateDuplicatedCategory;
import static packman.validator.IdValidator.validatePackingListId;
import static packman.validator.IdValidator.validateUserMemberId;
import static packman.validator.LengthValidator.validateCategoryLength;

@Service
@Transactional
@RequiredArgsConstructor
public class TogetherListCategoryService {
    private final PackingListRepository packingListRepository;

    public ListResponseMapping createCategory(CategoryCreateDto categoryCreateDto, Long userId) {

        // 카테고리 exceed_len
        validateCategoryLength(categoryCreateDto.getName());

        // no_list
        PackingList packingList = validatePackingListId(packingListRepository, Long.parseLong(categoryCreateDto.getListId()));

        // no_member_user
        List<UserGroup> userGroups = packingList.getTogetherPackingList().getGroup().getUserGroups();
        validateUserMemberId(userGroups, userId);

        // duplicate_category
        validateDuplicatedCategory(packingList, categoryCreateDto.getName());

        // insert
        Category category = new Category(packingList, categoryCreateDto.getName());
        packingList.addCategory(category);
        packingListRepository.save(packingList);


        // response
        ListResponseMapping categoryResponseDto = packingListRepository.findByIdAndTitle(Long.parseLong(categoryCreateDto.getListId()), packingList.getTitle());
        return categoryResponseDto;
    }

}
