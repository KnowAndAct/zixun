package com.example.toutiao;


import com.example.toutiao.service.LikeService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class LikeServiceTests {

    @Autowired
    LikeService likeService;

    @Test
    public void testLike() {
        //System.out.println("testLike");
        likeService.like(123, 1, 1);
        Assert.assertEquals(1, likeService.getLikeStatus(123, 1, 1));
    }

    @Test
    public void testDislike() {
        //System.out.println("testLikeB");
        likeService.disLike(123, 1, 1);
        Assert.assertEquals(-1, likeService.getLikeStatus(123, 1, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException() {
        throw new IllegalArgumentException("xxxxx");
    }

    @Before
    public void setUp() {
        System.out.println("setUp");
    }

    @After
    public void tearDown() {
        System.out.println("tearDown");
    }

    @BeforeClass
    public static void beforeClass() {
        System.out.println("beforeClass");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("afterClass");
    }

}
