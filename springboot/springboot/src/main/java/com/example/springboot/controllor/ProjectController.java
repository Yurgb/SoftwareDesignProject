package com.example.springboot.controllor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Assignment;
import com.example.springboot.entity.Course;
import com.example.springboot.entity.Project;
import com.example.springboot.mapper.AssignmentMapper;
import com.example.springboot.mapper.CourseMapper;
import com.example.springboot.mapper.ProjectMapper;
import com.example.springboot.mapper.TakesMapper;
import com.example.springboot.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private AssignmentMapper assignmentMapper;
    @Autowired
    private TakesMapper takesMapper;
    @GetMapping("/page")
    public IPage<Project> findPage(@RequestParam Integer pageNum,
                                  @RequestParam Integer pageSize,
                                  @RequestParam(defaultValue = "") String name,
                                  @RequestParam(defaultValue = "") String courseID
    ) {
        IPage<Project> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        queryWrapper.like("courseid",courseID);
        return projectService.page(page, queryWrapper);
    }
    @PostMapping
    public boolean save(@RequestBody Project course) {
        // 新增或者更新
        return projectService.saveOrUpdate(course);
    }
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return projectService.removeById(id);
    }
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return projectService.removeBatchByIds(ids);
    }
    @GetMapping("/getattdence")
    public Double getattdence(@RequestParam String courseID,@RequestParam Integer studentID)
    {
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("courseid",courseID);
        //选出该课程的所有实验
        List<Project> a=projectMapper.selectList(queryWrapper);
        //得到该课程的出席分占比
        Course b=courseMapper.selectById(courseID);
        //得到该学生的出席分
        Integer atten=takesMapper.getatten(studentID, courseID);
        Double score =b.getAttendenceper().doubleValue()*atten/a.size();
        return score;
    }
    @GetMapping("/getproject")
    public Double getproject(@RequestParam String courseID,@RequestParam Integer studentID)
    {
        QueryWrapper<Project> queryWrapper = new QueryWrapper<>();
        QueryWrapper<Assignment> c = new QueryWrapper<>();
        queryWrapper.like("courseid",courseID);
        //选出该课程的所有实验
        List<Project> a=projectMapper.selectList(queryWrapper);
        Double totalproject=0.0;
        Double mygrade=0.0;
        for(Project project:a)
        {
            totalproject=totalproject+project.getScore();
            c.like("studentid",studentID);
            c.like("projectid",project.getId());
            mygrade=mygrade+assignmentMapper.selectgrade(studentID,project.getId());
        }
        //得到该课程的实验分占比
        Course b=courseMapper.selectById(courseID);
        //得到该学生实验总分
        Double score =b.getProjectper().doubleValue()*mygrade/totalproject;
        return score;
    }

}
