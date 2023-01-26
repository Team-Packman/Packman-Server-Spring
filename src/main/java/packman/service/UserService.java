package packman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packman.dto.user.UserRequestDto;
import packman.dto.user.UserResponseDto;
import packman.entity.User;
import packman.repository.UserRepository;
import packman.util.CustomException;
import packman.util.ResponseCode;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        String refreshToken = "2222"; //임시
        String accessToken = "123"; //임시

        //닉네임 글자수 제한
        if(userRequestDto.getNickname().length() > 4){
            throw new CustomException(ResponseCode.EXCEED_LENGTH);
        }
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
