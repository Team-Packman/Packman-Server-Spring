package packman.controller.aloneList;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import packman.dto.pack.PackCreateDto;
import packman.dto.pack.PackUpdateDto;
import packman.util.ResponseCode;
import packman.util.ResponseMessage;
import packman.util.ResponseNonDataMessage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequiredArgsConstructor
@RequestMapping("/list/alone/pack")
@Validated
public class AloneListPackController {
    private final PackService packService;

    @PostMapping
    public ResponseEntity<ResponseMessage> createPack(@RequestBody @Valid PackCreateDto packCreateDto, HttpServletRequest request) {
        Long userId = 1L;
        return ResponseMessage.toResponseEntity(
                ResponseCode.SUCCESS_CREATE_ALONE_PACK,
                packService.createAlonePack(packCreateDto, userId)
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

    @DeleteMapping("/{listId}/{categoryId}/{packId}")
    public ResponseEntity<ResponseNonDataMessage> deletePack(
            @PathVariable @NotBlank String listId, @PathVariable @NotBlank String categoryId, @PathVariable @NotBlank String packId, HttpServletRequest request) {
        Long userId = 1L;
        aloneListPackService.deletePack(Long.valueOf(listId), Long.valueOf(categoryId), Long.valueOf(packId), userId);

        return ResponseNonDataMessage.toResponseEntity(
                ResponseCode.SUCCESS_DELETE_ALONE_PACK
        );
    }
}
