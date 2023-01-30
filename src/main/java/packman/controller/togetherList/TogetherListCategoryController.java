package packman.controller.togetherList;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packman.dto.category.CategoryCreateDto;
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
}
