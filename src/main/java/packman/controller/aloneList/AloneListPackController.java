package packman.controller.aloneList;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import packman.dto.pack.PackCreateDto;
import packman.dto.pack.PackUpdateDto;
import packman.service.aloneList.AloneListPackService;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/list/alone/pack")
public class AloneListPackController {
    private final AloneListPackService aloneListPackService;

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
                aloneListPackService.updatePack(packUpdateDto, userId)
        );
    }
}
