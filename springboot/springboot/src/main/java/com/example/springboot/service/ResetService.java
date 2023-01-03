package com.example.springboot.service;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Reset;
import com.example.springboot.mapper.ResetMapper;
import com.example.springboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Date;


@Service
public class ResetService extends ServiceImpl<ResetMapper,Reset>{

    @Autowired
    private ResetMapper resetMapper;

    @Autowired
    JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    public void sendEmailCode(String email) {
        DateTime now = new DateTime();
        String code = RandomUtil.randomNumbers(4); // 随机一个 4位长度的验证码
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(from);  // 发送人
        message.setTo(email);
        message.setSentDate(now);
        message.setSubject("重置密码邮箱验证");
        message.setText("您本次的验证码是：" + code + "，有效期5分钟。请妥善保管，切勿泄露");
        javaMailSender.send(message);
        if(resetMapper.cha(email)==null)
            resetMapper.save1(email,code, DateUtil.offsetMinute(now, 5));
        else
            resetMapper.update1(email,code,DateUtil.offsetMinute(now, 5));
    }

}
