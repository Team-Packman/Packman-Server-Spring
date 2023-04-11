package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import packman.repository.UserRepository;

import static packman.validator.IdValidator.validateUserId;

@Service
@RequiredArgsConstructor
public class HelpService {
    private final UserRepository userRepository;

    public void getHelp(Long userId) {
        validateUserId(userRepository, userId);
    }
}
