package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.user.UserCreateDto;
import packman.dto.user.UserResponseDto;
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

    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        String refreshToken = "2222"; //임시
        String accessToken = "123"; //임시

        //닉네임 글자수 제한
        if (userCreateDto.getNickname().length() > 4) {
            throw new CustomException(ResponseCode.EXCEED_LENGTH);
        }
        User user = new User(userCreateDto, refreshToken);

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
