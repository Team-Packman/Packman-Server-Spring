package packman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import packman.service.TemplateService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/template")
public class TemplateController {
    private final TemplateService templateService;

    @GetMapping("/alone")
    public ResponseEntity<ResponseMessage> getAloneTemplateList(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_ALONE_TEMPLATE_LIST,
                templateService.getAloneTemplateList(userId)
        );
    }

    @GetMapping("/together")
    public ResponseEntity<ResponseMessage> getTogetherTemplateList(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_TOGETHER_TEMPLATE_LIST,
                templateService.getTogetherTemplateList(userId)
        );
    }

    @GetMapping("/{templateId}")
    public ResponseEntity<ResponseMessage> getTemplate(@PathVariable Long templateId, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_DETAILED_TEMPLATE,
                templateService.getTemplate(templateId, userId)
        );
    }
}
