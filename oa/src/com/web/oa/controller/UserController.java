package com.web.oa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.web.oa.pojo.ActiveUser;
import com.web.oa.pojo.Employee;
import com.web.oa.pojo.EmployeeCustom;
import com.web.oa.pojo.MenuTree;
import com.web.oa.pojo.SysPermission;
import com.web.oa.pojo.SysRole;
import com.web.oa.pojo.SysUserRole;
import com.web.oa.service.EmployeeService;
import com.web.oa.service.SysService;
import com.web.oa.utils.Constants;

import cn.hutool.db.Session;

@Controller
public class UserController {

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private SysService sysService;
   
	//登录请求
	@RequestMapping("/login")
	public String login(HttpServletRequest request,Model model,String hutool,HttpSession session){	
		String exceptionName = (String) request.getAttribute("shiroLoginFailure");
					if (exceptionName!=null) {
						if (UnknownAccountException.class.getName().equals(exceptionName)) {
							model.addAttribute("errorMsg", "用户账号不存在");
						} else if (IncorrectCredentialsException.class.getName().equals(exceptionName)) {
							model.addAttribute("errorMsg", "密码不正确");
						} else if ("randomCodeError".equals(exceptionName)) {
							model.addAttribute("errorMsg", "验证码不正确");
						}
						else {
							model.addAttribute("errorMsg", "未知错误");
						}
					}
		return "login";
	}
	

	//用户管理
	@RequestMapping("/findUserList")
	public ModelAndView findUserList(String userId) {
		ModelAndView mv = new ModelAndView();
		List<SysRole> allRoles = sysService.findAllRoles();
		List<EmployeeCustom> list = employeeService.findUserAndRoleList();
		
		mv.addObject("userList", list);
		mv.addObject("allRoles", allRoles);
		
		mv.setViewName("userlist");
		return mv;
	}
	 
	//重新分配权限
	@RequestMapping("/assignRole")
	@ResponseBody
	public Map<String, String> assignRole(String roleId,String userId) {
		Map<String, String> map = new HashMap<>(); 
		try {
			employeeService.updateEmployeeRole(roleId, userId);
			map.put("msg", "分配权限成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "分配权限失败");
		}
		return map;
	}
	
	//角色添加
	@RequestMapping("/toAddRole")
	public ModelAndView toAddRole() {
		List<MenuTree> allPermissions = sysService.loadMenuTree();
		List<SysPermission> menus = sysService.findAllMenus();
		List<SysRole> permissionList = sysService.findRolesAndPermissions();		
		ModelAndView mv = new ModelAndView();
		mv.addObject("allPermissions", allPermissions);
		mv.addObject("menuTypes", menus);
		mv.addObject("roleAndPermissionsList", permissionList);
		mv.setViewName("rolelist");		
		return mv;
		
	}
	
	@RequestMapping("/saveRoleAndPermissions")
	public String saveRoleAndPermissions(SysRole role,int[] permissionIds) {
		//设置role主键，使用uuid
		String uuid = UUID.randomUUID().toString();
		role.setId(uuid);
		//默认可用
		role.setAvailable("1");
		
		sysService.addRoleAndPermissions(role, permissionIds);
		
		return "redirect:/toAddRole";
	}
	
	@RequestMapping("/saveSubmitPermission")
	public String saveSubmitPermission(SysPermission permission) {
		if (permission.getAvailable() == null) {
			permission.setAvailable("0");
		}
		sysService.addSysPermission(permission);
		return "redirect:/toAddRole";
	}
	 
	
	//角色列表
	@RequestMapping("/findRoles")  //rest
	public ModelAndView findRoles() {
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		List<SysRole> roles = sysService.findAllRoles();
		List<MenuTree> allMenuAndPermissions = sysService.getAllMenuAndPermision();
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("allRoles", roles);
		mv.addObject("activeUser",activeUser);
		mv.addObject("allMenuAndPermissions", allMenuAndPermissions);
		
		mv.setViewName("permissionlist");
		return mv;
	}
	
	@RequestMapping("/loadMyPermissions")
	@ResponseBody
	public List<SysPermission> loadMyPermissions(String roleId) {
		List<SysPermission> list = sysService.findPermissionsByRoleId(roleId);
		return list;
	}
	
	//编辑角色和权限
	@RequestMapping("/updateRoleAndPermission")
	public String updateRoleAndPermission(String roleId,int[] permissionIds) {
		sysService.updateRoleAndPermissions(roleId, permissionIds);
		return "redirect:/findRoles";		
	}
	 
	
	//查看权限，查看员工的角色和权限列表
	@RequestMapping("/viewPermissionByUser")
	@ResponseBody
	public SysRole viewPermissionByUser(String userName) {
		SysRole sysRole = sysService.findRolesAndPermissionsByUserId(userName);
		return sysRole;
	}
	 
	//新建用户
	@RequestMapping("/saveUser")
	public String saveUser(Employee user) {
		String password = user.getPassword();
		String salt = "eteokues";
		int hashIterations = 2; // 加密次数
		Md5Hash md5HASH = new Md5Hash(password, salt, hashIterations);
		user.setSalt(salt);
		user.setPassword(md5HASH.toString());
		employeeService.addEmployee(user);
		SysUserRole su=new SysUserRole();
		su.setSysUserId(user.getName());
        su.setSysRoleId(user.getRole().toString());
        employeeService.addEmployeePermission(su);
		return "redirect:/findUserList";		
	}
	
	@RequestMapping("/findNextManager")
	@ResponseBody
	public List<Employee> findNextManager(int level) {
		level++; //加一，表示下一个级别
		List<Employee> list = employeeService.findEmployeeByLevel(level);
		System.out.println(list);
		return list;
		
	}
    
	
	//删除角色
	@RequestMapping("/delRole")
	public String deleteRole(String roleId) {
		sysService.deleteRole(roleId);
		return "redirect:/findRoles";
	}
}
