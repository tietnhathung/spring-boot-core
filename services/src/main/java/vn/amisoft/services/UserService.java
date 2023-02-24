package vn.amisoft.services;

import vn.amisoft.common.models.User;

public interface UserService {
    User findByUsername(String username);
}
