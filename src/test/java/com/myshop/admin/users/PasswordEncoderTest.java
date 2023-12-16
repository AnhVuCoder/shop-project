package com.myshop.admin.users;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
    @Test
    public void testEncoderPassword(){
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        String name="nguyenleanhvu";
        String nameEncoder=bCryptPasswordEncoder.encode(name);
        System.out.println(nameEncoder);
    }
}
