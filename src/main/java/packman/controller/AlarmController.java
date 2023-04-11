package packman.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import packman.service.AlarmService;
import packman.util.ResponseCode;
import packman.util.ResponseNonDataMessage;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarm")
public class AlarmController {
    private final AlarmService alarmService;

    @GetMapping
    public ResponseEntity<ResponseNonDataMessage> getAlarm(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getUserPrincipal().getName());

        alarmService.getAlarm(userId);

        return ResponseNonDataMessage.toResponseEntity(
                ResponseCode.SUCCESS_GET_ALARM
        );
    }
}
