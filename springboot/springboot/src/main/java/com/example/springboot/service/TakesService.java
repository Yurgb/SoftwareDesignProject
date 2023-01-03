package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Takes;
import com.example.springboot.mapper.TakesMapper;
import org.springframework.stereotype.Service;

@Service
public class TakesService extends ServiceImpl<TakesMapper, Takes> {
}
