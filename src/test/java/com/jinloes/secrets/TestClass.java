package com.jinloes.secrets;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by jinloes on 4/18/15.
 */
public class TestClass {

    @Test
    public void test() {
        System.out.println(new BCryptPasswordEncoder(15).encode("password"));
    }
}
