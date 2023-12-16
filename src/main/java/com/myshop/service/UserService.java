package com.myshop.service;

import com.myshop.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    void saveUser(User user);
    boolean checkDuplicatedEmail(Integer id, String email);
    List<User> getListAllUser();
    User findById(Integer id);
    void delete(Integer id);
    void editEnabled(Integer id, boolean enabled);
    Page<User> pageNoUser(Integer pageNo, String sortFile, String sortDir, String keyword);
}
