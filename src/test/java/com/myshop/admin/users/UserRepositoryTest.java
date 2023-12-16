package com.myshop.admin.users;

import com.myshop.entity.Role;
import com.myshop.entity.User;
import com.myshop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    @Test
    public void testCreateOneUser(){
        User user=new User("nguyenva@gmail.com",
                "Nguyen",
                "A",
                "nguyenvana",
                "",
                true);
        Role role=testEntityManager.find(Role.class,1);
        user.getRoles().add(role);
        userRepository.save(user);
        assertThat(user.getId()).isGreaterThan(0);
    }
    @Test
    public void testCreateOneMoreUser(){
        Role roleOne=testEntityManager.find(Role.class,2);
        Role roleTwo=testEntityManager.find(Role.class, 3);
        User user=new User("nguyenvanb@gmail.com",
                "Nguyen",
                "B",
                "ngyenvanb",
                "",
                true);
        user.addNewRole(roleOne);
        user.addNewRole(roleTwo);
        userRepository.save(user);
    }
    @Test
    public void testListAllUser(){
        Iterator<User> iterator= userRepository.findAll().iterator();
        while(iterator.hasNext()){
            iterator.next().getAllRoles();
        }
    }
    @Test
    public void testUpdateUser(){
        User user=userRepository.findById(2).get();
        user.setEmail("nguyenvanaa@gmail.com");
        Role roleEditor=testEntityManager.find(Role.class,3);
        user.getRoles().remove(roleEditor);
        userRepository.save(user);
    }
}
