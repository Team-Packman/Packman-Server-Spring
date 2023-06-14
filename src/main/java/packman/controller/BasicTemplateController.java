package packman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packman.service.BasicTemplateService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/basicTemplate")
public class BasicTemplateController {
    private final BasicTemplateService basicTemplateService;

    @GetMapping("/{templateId}")
    public ResponseEntity<ResponseMessage> getBasicTemplate(@PathVariable Long templateId, HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_BASIC_DETAILED_TEMPLATE,
                basicTemplateService.getBasicTemplate(templateId, userId)
        );
    }
}
