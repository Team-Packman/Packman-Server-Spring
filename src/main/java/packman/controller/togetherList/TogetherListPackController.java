package packman.controller.togetherList;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import packman.dto.pack.PackCreateDto;
import packman.dto.pack.PackUpdateDto;
import packman.service.PackService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/list/together/pack")
public class TogetherListPackController {
    private final PackService packService;

    @PostMapping
    public ResponseEntity<ResponseMessage> createPack(@RequestBody @Valid PackCreateDto packCreateDto, HttpServletRequest request) {
        Long userId = 1L;

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_CREATE_TOGETHER_PACK,
                packService.createTogetherPack(packCreateDto, userId)
        );
    }
    
    @PatchMapping
    public ResponseEntity<ResponseMessage> updatePack(@RequestBody @Valid PackUpdateDto packUpdateDto, HttpServletRequest request) {
        Long userId = 1L;

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_UPDATE_TOGETHER_PACK,
                packService.updateTogetherPack(packUpdateDto, userId)
        );
    }
}
