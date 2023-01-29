package packman.controller.aloneList;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packman.dto.category.CategoryCreateDto;
import packman.service.aloneList.AloneListCategoryService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/list/alone/category")
public class AloneListCategoryController {
    private final AloneListCategoryService aloneListCategoryService;

    @PostMapping
    public ResponseEntity<ResponseMessage> createCategory(@RequestBody @Valid CategoryCreateDto categoryRequestDto, HttpServletRequest request) {
        Long userId = 1L;
        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_CREATE_ALONE_CATEGORY,
                aloneListCategoryService.createCategory(categoryRequestDto, userId)
        );
    }
}
