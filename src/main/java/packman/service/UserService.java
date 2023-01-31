package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.user.UserInfoResponseDto;
import packman.dto.user.UserUpdateDto;
import packman.entity.User;
import packman.repository.UserRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

import static packman.validator.IdValidator.validateUserId;
import static packman.validator.LengthValidator.validateUserNicknameLength;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserInfoResponseDto getUser(Long userId) {
        User user = userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );
        return new UserInfoResponseDto(userId.toString(), user.getEmail(), user.getNickname(), user.getProfileImage());
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );

        user.setDeleted(true);
    }

    public UserInfoResponseDto updateUser(UserUpdateDto userUpdateDto, Long userId) {
        User user = validateUserId(userRepository, userId);
        validateUserNicknameLength(userUpdateDto.getNickname());
        user.setNickname(userUpdateDto.getNickname());
        user.setProfileImage(userUpdateDto.getProfileImage());
        return new UserInfoResponseDto(userId.toString(), user.getEmail(), user.getNickname(), user.getProfileImage());
    }
}
