package com.basketball.rbgt;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.basketball.rbgt.entity.Student;
import com.basketball.rbgt.mapper.StudentMapper;
import com.basketball.rbgt.mapper.UserMapper;
import com.basketball.rbgt.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class RbgtApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StudentMapper studentMapper;

    @Test
    void contextLoads() {
        // 1. 查询用户信息
        User user = userMapper.selectById(1L);
        // 2. 修改用户信息
        user.setName("小月");
        user.setEmail("123456@qq.com");
        // 3. 执行更新操作
        userMapper.updateById(user);
    }

    // 测试乐观锁失败！多线程下
    @Test
    public void testOptimisticLocker2(){
        // 线程 1
        User user = userMapper.selectById(1L);
        user.setName("小明");
        user.setEmail("1223456@qq.com");

        // 模拟另外一个线程执行了插队操作
        User user2 = userMapper.selectById(1L);
        user2.setName("小花");
        user2.setEmail("486494@qq.com");
        userMapper.updateById(user2);

        // 自旋锁来多次尝试提交！
        userMapper.updateById(user); // 如果没有乐观锁就会覆盖插队线程的值！
    }

    // 测试查询
    @Test
    public void testSelectById(){
        User user = userMapper.selectById(1L);
        System.out.println(user);
    }

    // 测试批量查询！
    @Test
    public void testSelectByBatchId(){
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        users.forEach(System.out::println);
    }

    // 按条件查询之一使用map操作
    @Test
    public void testSelectByBatchIds(){
        HashMap<String, Object> map = new HashMap<>();
        // 自定义要查询
        map.put("name","狂神说Java");
        map.put("age",3);
        List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }

    @Test
    void testPage() {
        //参数一： 当前页
        //参数二： 页面大小
        //使用了分页插件之后，所有的分页操作都非常简单！
        Page<User> page = new Page<>(2, 5);
        userMapper.selectPage(page, null);

        page.getRecords().forEach(System.out::println);
        System.out.println(page.getTotal());
    }

    @Test
    void deleted() {
        int i = userMapper.deleteById(1l);
        System.out.println(i);
    }

    @Test
    void test1() {
        // 查询name不为空的用户，并且邮箱不为空的用户，年龄大于等于12
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper
                .isNotNull("name")
                .isNotNull("email")
                .ge("age", 12);
        userMapper.selectList(wrapper).forEach(System.out::println);
    }

    @Test
    void test2() {
        // 查询名字moon
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", "moon");
        User user = userMapper.selectOne(wrapper); // 查询一个数据，出现多个结果使用List或者 Map
        System.out.println(user);
    }

    @Test
    void test3(){
        List<Student> selectallll = studentMapper.selectallll();
        System.out.println(selectallll.size());
    }

}
