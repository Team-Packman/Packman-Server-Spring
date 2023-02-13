package packman.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import packman.repository.UserRepository;
import packman.util.ResponseCode;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return userRepository.findByIdAndIsDeleted(Long.valueOf(id), false)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseCode.NO_USER.getMessage()));
    }
}
