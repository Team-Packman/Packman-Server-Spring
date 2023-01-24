package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.user.UserRequestDto;
import packman.dto.user.UserResponseDto;
import packman.entity.User;
import packman.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        String refreshToken = "12345"; //임시
        String accessToken = "123"; //임시
        User user = new User(userRequestDto, refreshToken);

        User createdUser = userRepository.save(user);

        return new UserResponseDto(
                true,
                createdUser.getId().toString(),
                createdUser.getEmail(),
                createdUser.getName(),
                createdUser.getGender(),
                createdUser.getAgeRange(),
                createdUser.getNickname(),
                createdUser.getProfileImage(),
                accessToken,
                createdUser.getPath()
                );
    }
}
