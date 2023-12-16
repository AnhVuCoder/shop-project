package com.myshop.service.impl;

import com.myshop.config.Common;
import com.myshop.entity.Role;
import com.myshop.entity.User;
import com.myshop.repository.RoleRepository;
import com.myshop.repository.UserRepository;
import com.myshop.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public List<User> getListAllUser(){
        return userRepository.findAll();
    }
    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("User do not exist"));
    }
    @Override
    public void delete(Integer id)  {
        User user=userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("User not exist with id = "+id));
        Long count=userRepository.countById(id);
        if(count==null || count<=0){
            throw new UsernameNotFoundException("User not exists with id = "+id);
        }
        userRepository.delete(user) ;
    }
    @Override
    public void editEnabled(Integer id, boolean enabled) {
        userRepository.editStatusUser(id,enabled);
    }

    @Override
    public Page<User> pageNoUser(Integer pageNo, String sortField, String sortDir, String keyword) {
        Sort sort=Sort.by(sortField);
        sort=sortDir.equals("asc")?sort.ascending():sort.descending();
        Pageable pageable= PageRequest.of(pageNo-1,Common.PAGE_SIZE, sort);
        if(keyword==null){
            return userRepository.findAll(pageable);
        }
        return userRepository.findAll(keyword, pageable);
    }

    public void saveUser(User user) {
        boolean isUpdatingUser=user.getId()!=null;
        if(isUpdatingUser){
            User existingUser=userRepository.findById(user.getId()).orElseThrow(()->new UsernameNotFoundException("User not found with id = "+user.getId()));
            if(user.getPassword().isEmpty()){
                user.setPassword(existingUser.getPassword());
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }
    public boolean checkDuplicatedEmail(Integer id, String email){
        User user=userRepository.findByEmail(email);
        if(user==null) return true;
        boolean isCreatingUser=id==null;
        if(isCreatingUser){
            return false;
        }
        if(!user.getId().equals(id)){
            return false;
        }
        return true;
    }

}
