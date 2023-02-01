package packman.controller.aloneList;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packman.dto.pack.PackUpdateDto;
import packman.service.PackService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/list/alone/pack")
public class AloneListPackController {
    private final PackService packService;

    @PostMapping
    public ResponseEntity<ResponseMessage> createPack(@RequestBody @Valid PackCreateDto packCreateDto, HttpServletRequest request) {
        Long userId = 1L;
        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_CREATE_ALONE_PACK,
                aloneListPackService.createPack(packCreateDto, userId)
        );
    }

    @PatchMapping
    public ResponseEntity<ResponseMessage> updatePack(@RequestBody @Valid PackUpdateDto packUpdateDto, HttpServletRequest request) {
        Long userId = 1L;

        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_UPDATE_ALONE_PACK,
                packService.updateAlonePack(packUpdateDto, userId)
        );
    }
}
