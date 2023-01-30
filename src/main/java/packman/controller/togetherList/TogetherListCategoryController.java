package packman.controller.togetherList;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import packman.dto.category.CategoryCreateDto;
import packman.dto.category.CategoryUpdateDto;
import packman.service.togetherList.TogetherListCategoryService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/list/together/category")
public class TogetherListCategoryController {

    private final TogetherListCategoryService togetherListCategoryService;

    @PostMapping
    public ResponseEntity<ResponseMessage> createCategory(@RequestBody @Valid CategoryCreateDto categoryCreateDto, HttpServletRequest request) {
        Long userId = 1L;
        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_CREATE_TOGETHER_CATEGORY,
                togetherListCategoryService.createCategory(categoryCreateDto, userId)
        );
    }

    @PatchMapping
    public ResponseEntity<ResponseMessage> updateCategory(@RequestBody @Valid CategoryUpdateDto categoryUpdateDto, HttpServletRequest request) {
        Long userId = 1L;
        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_UPDATE_TOGETHER_CATEGORY,
                togetherListCategoryService.updateCategory(categoryUpdateDto, userId)
        );
    }
}
