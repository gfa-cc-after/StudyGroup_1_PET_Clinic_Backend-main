package com.greenfox.dramacsoport.petclinicbackend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PetClinicBackendApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void goodDummyTest(){
        int x = 1;
        Assertions.assertEquals(1,x);
    }

//    @Test
//    void failingDummyTest(){
//        int x = 2;
//        Assertions.assertEquals(1,x);
//    }

}
