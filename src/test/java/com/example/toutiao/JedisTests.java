package com.example.toutiao;

import com.example.toutiao.model.User;
import com.example.toutiao.util.JedisAdapter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
//@Sql("/init-schema.sql")
public class JedisTests {
    @Autowired
    JedisAdapter jedisAdapter;


    @Test
    public void intidata() {
        User user = new User();
        user.setHeadUrl("http://image.nowcoder.com/head/100t.png");
        user.setPassword("pwd");
        user.setName("user1");
        user.setSalt("salt");

        jedisAdapter.setObject("user1xx", user);

        User u = jedisAdapter.getObject("user1xx", User.class);
        System.out.println(ToStringBuilder.reflectionToString(u));
    }

}
