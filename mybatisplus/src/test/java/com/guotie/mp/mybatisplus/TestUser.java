package com.guotie.mp.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guotie.mp.mybatisplus.entity.User;
import com.guotie.mp.mybatisplus.mapper.UserMapper;
import org.apache.ibatis.session.RowBounds;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.swing.Timer;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUser {
    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);
    }

    @Test
    public void testInsert(){
        for (int i = 1;i <= 5;i++){
            User user = User.builder().age(23)
                    .name("deninis"+Integer.toString(i))
                    .email("helle@guotie"+Integer.toString(i)+".com")
                    .gender(i%3)
                    .build();
            int insertRow = userMapper.insert(user);
            Assert.assertEquals(1,insertRow);
        }

    }

    @Test
    public void testUpdata(){
        User dennis = User.builder().id(7L).age(88).name("dennis").email("egg@cqq.com").build();
        int id = userMapper.updateById(dennis);
        System.out.println(id);
    }

    @Test
    public void testSelectById(){
        User user = userMapper.selectById(5);
        System.out.println(user);
    }

    @Test
    public void testSelectByEntity(){
        User tom = User.builder().age(28).build();


        User user = userMapper.selectOne(new QueryWrapper<User>(tom));
        System.out.println(user);
    }

    @Test
    public void testSelectBatch(){

        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        ids.add(3);

        List<User> users = userMapper.selectBatchIds(ids);
        System.out.println(users);
    }

    @Test
    public void testSelectPage(){
        IPage<User> userIPage = userMapper.selectPage(new Page<>(1,2), null);
        System.out.println(userIPage);
        System.out.println(userIPage.getRecords());
    }

    @Test
    public void testDeleteById(){
        int id = userMapper.deleteById(5);
        Assert.assertEquals(1,id);
    }

    @Test
    public void testAllEqual(){
//        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("id", 1);

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        HashMap<String, Object> map = new HashMap<>();

        map.put("id",2);

        List<User> users = userMapper.selectList(
                new QueryWrapper<User>()
                .eq("gender",2)
                .gt("age",20)
        );

        System.out.println(users);
    }




}

