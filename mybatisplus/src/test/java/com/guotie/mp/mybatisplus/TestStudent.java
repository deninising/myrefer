package com.guotie.mp.mybatisplus;

import com.guotie.mp.mybatisplus.sso.entity.Student;
import com.guotie.mp.mybatisplus.sso.service.StudentService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestStudent {
    @Resource
    private StudentService studentService;

    @Test
    public void testSaveStudent(){
        Student student = Student.builder().id(255L).code("9527").name("deninis").useStatus("1").build();
        boolean save = studentService.save(student);
        Assert.assertFalse(!save);
    }

}
