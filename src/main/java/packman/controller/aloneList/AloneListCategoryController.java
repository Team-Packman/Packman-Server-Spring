package packman.controller.aloneList;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import packman.dto.category.CategoryCreateDto;
import packman.dto.category.CategoryUpdateDto;
import packman.service.aloneList.AloneListCategoryService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;
import packman.util.ResponseNonDataMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/list/alone/category")
public class AloneListCategoryController {
    private final AloneListCategoryService aloneListCategoryService;

    @PostMapping
    public ResponseEntity<ResponseMessage> createCategory(@RequestBody @Valid CategoryCreateDto categoryRequestDto, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_CREATE_ALONE_CATEGORY,
                aloneListCategoryService.createCategory(categoryRequestDto, userId)
        );
    }

    @PatchMapping
    public ResponseEntity<ResponseMessage> updateCategory(@RequestBody @Valid CategoryUpdateDto categoryUpdateDto, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_UPDATE_ALONE_CATEGORY,
                aloneListCategoryService.updateCategory(categoryUpdateDto, userId)
        );
    }

    @DeleteMapping("/{listId}/{categoryId}")
    public ResponseEntity<ResponseNonDataMessage> deleteCategory(@PathVariable("listId") Long listId, @PathVariable("categoryId") Long categoryId, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        aloneListCategoryService.deleteCategory(listId, categoryId, userId);
        return ResponseNonDataMessage.toResponseEntity(
                ResponseCode.SUCCESS_DELETE_ALONE_CATEGORY
        );
    }
}
