package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.auth.JwtTokenProvider;
import packman.dto.log.UserLogDto;
import packman.dto.user.UserCreateDto;
import packman.dto.user.UserInfoResponseDto;
import packman.dto.user.UserResponseDto;
import packman.dto.user.UserUpdateDto;
import packman.entity.Folder;
import packman.entity.User;
import packman.repository.UserRepository;
import packman.util.CustomException;
import packman.util.LogMessage;
import packman.util.ResponseCode;

import java.util.List;

import static packman.validator.IdValidator.validateUserId;
import static packman.validator.LengthValidator.validateUserNicknameLength;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final FolderService folderService;

    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        //닉네임 글자수 제한
        validateUserNicknameLength(userCreateDto.getNickname());

        String refreshToken = jwtTokenProvider.createRefreshToken();

        User user = User.builder()
                .email(userCreateDto.getEmail())
                .nickname(userCreateDto.getNickname())
                .profileImage(userCreateDto.getProfileImage())
                .name(userCreateDto.getName())
                .path(userCreateDto.getPath())
                .gender(userCreateDto.getGender())
                .ageRange(userCreateDto.getAgeRange())
                .refreshToken(refreshToken).build();

        User createdUser = userRepository.save(user);

        String accessToken = jwtTokenProvider.createAccessToken(createdUser.getId().toString());

        UserLogDto userLogDto = UserLogDto.builder()
                .email(userCreateDto.getEmail())
                .name(userCreateDto.getName())
                .gender(userCreateDto.getGender())
                .ageRange(userCreateDto.getAgeRange())
                .nickname(userCreateDto.getNickname())
                .profileImage(userCreateDto.getProfileImage())
                .path(userCreateDto.getPath())
                .build();

        LogMessage.setDataLog("닉네임/프로필 등록", userLogDto, createdUser.getId());

        return UserResponseDto.builder()
                .isAlreadyUser(true)
                .id(createdUser.getId().toString())
                .email(createdUser.getEmail())
                .name(createdUser.getName())
                .gender(createdUser.getGender())
                .ageRange(createdUser.getAgeRange())
                .nickname(createdUser.getNickname())
                .profileImage(createdUser.getProfileImage())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .path(createdUser.getPath()).build();
    }

    public UserInfoResponseDto getUser(Long userId) {
        User user = userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(
                () -> new CustomException(ResponseCode.NO_USER)
        );
        return new UserInfoResponseDto(userId.toString(), user.getEmail(), user.getNickname(), user.getProfileImage());
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findByIdAndIsDeleted(userId, false).orElseThrow(() -> new CustomException(ResponseCode.NO_USER));

        List<Folder> folders = user.getFolders();

        for (Folder folder : folders) {
            folderService.deleteFolder(folder.getId(), userId);
        }

        userRepository.setUserIsDeletedByUserId(userId);
    }


    public UserInfoResponseDto updateUser(UserUpdateDto userUpdateDto, Long userId) {
        User user = validateUserId(userRepository, userId);
        validateUserNicknameLength(userUpdateDto.getNickname());
        user.setNickname(userUpdateDto.getNickname());
        user.setProfileImage(userUpdateDto.getProfileImage());
        return new UserInfoResponseDto(userId.toString(), user.getEmail(), user.getNickname(), user.getProfileImage());
    }
}
