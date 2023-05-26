package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import packman.repository.UserRepository;
import packman.util.LogMessage;

import static packman.validator.IdValidator.validateUserId;

@Service
@RequiredArgsConstructor
public class HelpService {
    private final UserRepository userRepository;

    public void getHelp(Long userId) {
        validateUserId(userRepository, userId);

        LogMessage.setNonDataLog("엿보기 조회", userId);
    }
}
