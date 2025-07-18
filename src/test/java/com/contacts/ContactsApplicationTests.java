package com.contacts;

import com.contacts.mapper.EmpMapper;
import com.contacts.pojo.Emp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ContactsApplicationTests {

@Autowired
   private EmpMapper empMapper;

    @Test
    public void testList(){
        List<Emp> empList = empMapper.list();
        empList.forEach(System.out::println);
    }

}
