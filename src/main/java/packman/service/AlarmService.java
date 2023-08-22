package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.repository.UserRepository;
import packman.util.LogMessage;

import static packman.validator.IdValidator.validateUserId;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlarmService {
    private final UserRepository userRepository;

    public void getAlarm(Long userId) {
        validateUserId(userRepository, userId);

        LogMessage.setNonDataLog("알림 조회 테스트", userId, "click");
    }
}
