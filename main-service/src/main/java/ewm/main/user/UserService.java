package ewm.main.user;

import ewm.main.exception.ConflictException;
import ewm.main.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    public List<User> findUsersByIds(List<Long> ids) {
        return userRepository.findAllById(ids);
    }

    public List<User> findAllUsers(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        // from-количество пропускаемых в начале списка элементов
        //size-общее количество элементов
        return userRepository.findAll(pageable).getContent();
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("Такой email уже используется!");
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User с таким id не найден");
        } else {
            userRepository.deleteById(id);
        }
    }
}
