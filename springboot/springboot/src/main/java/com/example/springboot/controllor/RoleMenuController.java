package com.example.springboot.controllor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.springboot.mapper.RoleMenuMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.entity.roleMenu;

import java.util.List;

@RestController
@RequestMapping("/rolemenu")
public class RoleMenuController {
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/change")
    public int test(int[] roleList,int id){

        id=userMapper.get_roleid(id);
        roleMenuMapper.deleteByID1(id);

        for (Integer menuId:roleList)
        {
            roleMenu roles=new roleMenu();
            roles.setRole_id(id);
            roles.setMenu_id(menuId);
            roleMenuMapper.insert(roles);
        }
        return  id;

    }
    @GetMapping("/selectrole")
    public List<Integer> getMenus(@RequestParam Integer id)
    {
        id=userMapper.get_roleid(id);
        return roleMenuMapper.selectMenus(id);
    }
}
