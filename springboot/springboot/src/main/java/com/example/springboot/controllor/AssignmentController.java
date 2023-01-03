package com.example.springboot.controllor;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Assignment;
import com.example.springboot.entity.Files;
import com.example.springboot.mapper.AssignmentMapper;
import com.example.springboot.mapper.ProjectMapper;
import com.example.springboot.mapper.TakesMapper;
import com.example.springboot.service.AssignmentService;
import com.example.springboot.service.TakesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

@RestController
@RequestMapping("/assignment")
public class AssignmentController {
    @Value("${assignments.upload.path}")
    private String fileUploadPath;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private AssignmentMapper assignmentMapper;
    @Autowired
    private TakesMapper takesMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @PostMapping("/upload")
    public Boolean upload(@RequestParam MultipartFile file,@RequestParam(defaultValue = "") String projectID,@RequestParam(defaultValue = "") Integer studentID,@RequestParam(defaultValue = "") String courseID) throws IOException {

        Date a=projectMapper.selectById(projectID).getTime();
        Date now=new Date();
        Boolean b=DateUtil.isIn(now,DateUtil.beginOfHour(now),a);
        if(b==false)
            return false;
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        // 定义一个文件唯一的标识码
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;

        File uploadFile = new File(fileUploadPath + fileUUID);
        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
        File parentFile = uploadFile.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }



        String url;


        // 上传文件到磁盘
        file.transferTo(uploadFile);
        // 数据库若不存在重复文件，则不删除刚才上传的文件
        url = "http://1.116.163.212:9091/assignment/" + fileUUID;


        takesMapper.SetAttendence(studentID,courseID);
        // 存储数据库
        Assignment saveFile = new Assignment();
        saveFile.setStudentid(studentID);
        saveFile.setProjectid(projectID); // 单位 kb
        saveFile.setFile(url);
        saveFile.setTime(now);
        assignmentMapper.insert(saveFile);

        return true;
    }

    /**
     * 文件下载接口   http://localhost:9090/file/{fileUUID}
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }
    @GetMapping("/page")
    public IPage<Assignment> findPage(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam(defaultValue = "") String projectID
    ) {
        IPage<Assignment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Assignment> queryWrapper = new QueryWrapper<>();

        if (!"".equals(projectID)) {
            queryWrapper.like("projectid", projectID);
        }

        return assignmentService.page(page, queryWrapper);
    }
    @GetMapping("/page2")
    public IPage<Assignment> findPage2(@RequestParam Integer pageNum,
                                      @RequestParam Integer pageSize,
                                      @RequestParam(defaultValue = "") String studentID
    ) {
        IPage<Assignment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Assignment> queryWrapper = new QueryWrapper<>();

        if (!"".equals(studentID)) {
            queryWrapper.like("studentid", studentID);
        }

        return assignmentService.page(page, queryWrapper);
    }
    @GetMapping("/setgrade")
    public void setgrade(@RequestParam Float grade,@RequestParam String id)
    {
        assignmentMapper.setgrade(grade,id);
    }
}
