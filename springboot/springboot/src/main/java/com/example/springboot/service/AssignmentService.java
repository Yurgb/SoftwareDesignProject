package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Assignment;
import com.example.springboot.mapper.AssignmentMapper;
import org.springframework.stereotype.Service;

@Service
public class AssignmentService extends ServiceImpl<AssignmentMapper, Assignment> {
}
