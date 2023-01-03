package com.example.springboot.controllor;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.example.springboot.entity.Reset;
import com.example.springboot.mapper.ResetMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.ResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


@RestController
@RequestMapping("/reset")
public class ResetController {
    @Autowired
     private ResetService resetService;
    @Autowired
    private ResetMapper resetMapper;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    private UserMapper userMapper;

    @Value("${spring.mail.username}")
    private String from;



    @GetMapping("/email/{email}")
    public Reset sendEmailCode(@PathVariable String email) {
       Date now=new Date();
        String code = RandomUtil.randomNumbers(4); // 随机一个 4位长度的验证码
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(from);  // 发送人
        message.setTo(email);
        message.setSentDate(now);
        message.setSubject("重置密码邮箱验证");
        message.setText("您本次的验证码是：" + code + "，有效期1分钟。请妥善保管，切勿泄露");
        javaMailSender.send(message);
        resetMapper.shan(email);
        Reset c=new Reset();
        c.setEmail(email);
        c.setCode(code);
        c.setTime(DateUtil.offsetMinute(now, 5));
       // DateUtil.isIn(now,DateUtil.beginOfHour(now),c.getTime());
        resetService.save(c);
        return c;
    }
    @PostMapping ("/resetPassword")
    public boolean resetP(@RequestParam String email,@RequestParam String password,@RequestParam String code,@RequestParam Integer id)
    {
        DateTime now=new DateTime();
        String a=resetMapper.cha(email);

        if(!a.equals(code))
            return false;
        else {
            userMapper.update1(id,password);
            return true;
        }

    }


}
