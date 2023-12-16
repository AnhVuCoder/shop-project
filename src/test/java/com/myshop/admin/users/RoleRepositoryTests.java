package com.myshop.admin.users;

import com.myshop.entity.Role;
import com.myshop.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTests {
    @Autowired
    private RoleRepository roleRepository;
    @Test
    public void testCreateFirstRole(){
        Role roleAdmin=new Role("Admin","Manage everything");
        Role saveRole=roleRepository.save(roleAdmin);
        assertThat(saveRole.getId()).isGreaterThan(0);
    }
    @Test
    public void testCreateRestRole(){
        Role roleSalesPerson=new Role("Salesperson","Manage products price, "+
                "customers, shipping, orders and sales report");
        Role roleEditor=new Role("Editor","Manage categories, brands, "+
                "products, menus and articles");
        Role roleShipper=new Role("Shipper","View products, view orders "+
                "and update orders status");
        Role roleAssistant=new Role("Assistant","Manage questions and reviews");
        roleRepository.saveAll(List.of(roleSalesPerson,roleEditor,roleShipper,roleAssistant));
    }
}
