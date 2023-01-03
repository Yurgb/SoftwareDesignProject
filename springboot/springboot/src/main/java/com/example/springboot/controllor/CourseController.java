package com.example.springboot.controllor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Course;
import com.example.springboot.entity.Files;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.CourseMapper;
import com.example.springboot.mapper.TakesMapper;
import com.example.springboot.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private TakesMapper takesMapper;
    @Autowired
    private CourseMapper courseMapper;
    @GetMapping("/page1")
    public IPage<Course> findPage(@RequestParam Integer pageNum,
                                  @RequestParam Integer pageSize,
                                  @RequestParam(defaultValue = "") String name,
                                  @RequestParam(defaultValue = "") String teacherID
                               ) {
        IPage<Course> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        queryWrapper.like("teacherid",teacherID);
        return courseService.page(page, queryWrapper);
    }
    @GetMapping("/page2")
    public Map<String,Object> findPage2(@RequestParam Integer pageNum,
                                  @RequestParam Integer pageSize,
                                  @RequestParam(defaultValue = "") String name,
                                  @RequestParam(defaultValue = "") String studentID
    ) {
        List<Integer> courseIDs= takesMapper.selectCourseID(studentID);
        List<Course> a=courseMapper.selectBatchIds(courseIDs);
        Map<String,Object> res= new HashMap<>();
        res.put("records",a);
        res.put("total",a.size());
        return res;
    }
    @PostMapping
    public boolean save(@RequestBody Course course) {
        // 新增或者更新
        return courseService.saveOrUpdate(course);
    }
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return courseService.removeById(id);
    }
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return courseService.removeBatchByIds(ids);
    }
    @GetMapping("/setper")
    public Integer setper(@RequestParam(defaultValue = "") String courseID,@RequestParam(defaultValue = "") Integer per)
    {
        Course a=new Course();
        a.setId(courseID);
        a.setProjectper(per);
        a.setAttendenceper(100-per);
        return courseMapper.updateById(a);
    }
}
