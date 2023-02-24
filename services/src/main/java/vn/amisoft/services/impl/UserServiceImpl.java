package vn.amisoft.services.impl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.amisoft.common.models.User;
import vn.amisoft.dao.entities.UserEntity;
import vn.amisoft.dao.repositories.UserRepository;
import vn.amisoft.services.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userEntityRepository;

    public UserServiceImpl(UserRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public User findByUsername(String username) {
        Optional<UserEntity> user = userEntityRepository.findByUsername(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("Can't find user with name " + username);
        }
        return user.get().toModel();
    }
}
