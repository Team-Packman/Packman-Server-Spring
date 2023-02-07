package packman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packman.service.TemplateService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/template")
public class TemplateController {
    private final TemplateService templateService;

    @GetMapping("/{templateId}")
    public ResponseEntity<ResponseMessage> getTemplate(@PathVariable Long templateId, HttpServletRequest request) {
        Long userId = 1L;
        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_DETAILED_TEMPLATE,
                templateService.getTemplate(templateId, userId)
        );
    }
}
