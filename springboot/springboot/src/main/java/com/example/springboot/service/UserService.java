package com.example.springboot.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.ResetMapper;
import com.example.springboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper,User> {


    @Autowired
   private UserMapper userMapper;



    @Autowired
    JavaMailSender javaMailSender;


    public boolean login(User user) {
        String a=user.getUsername();
        String b=userMapper.selectpassword(a);
        String c=user.getPassword();
        if(b==null)
            return false;
        if (b.equals(c)) {
            return true;
        } else{
            return false;
        }
    }

    public User selectinfo(User user) {
        String a=user.getUsername();
        return userMapper.selectinfo(a);
    }


//
//    public int save(User user){
//
//        if(user.getId()==null)//user没有ID为新增
//        {
//            return  userMapper.insert(user);
//        }
//        else
//        {
//            return userMapper.update(user);
//        }
//    }
//    public List<User> findAll(){
//        return userMapper.findAll();
//    }
//    public int deleteByID(Integer id)
//    {
//        return userMapper.deleteByID(id);
//    }
//
//    public List<User> selectPage(Integer pageNum, Integer pageSize, String username) {
//        return userMapper.selectPage(pageNum,pageSize,username);
//    }

}
