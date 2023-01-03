package com.example.springboot.controllor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.example.springboot.entity.Takes;
import com.example.springboot.entity.User;
import com.example.springboot.service.TakesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/takes")
public class TakesController {
    @Autowired
    private TakesService takesService;

    @PostMapping ("/import")
    public Boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);
        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<Takes> users = CollUtil.newArrayList();
        for (List<Object> row : list) {
            Takes user = new Takes();
            user.setStudentid(Integer.parseInt(row.get(0).toString()));

            user.setCourseid(row.get(1).toString());

            users.add(user);
        }
        takesService.saveBatch(users);
        return true;
    }

}
