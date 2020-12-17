package com.web.oa.service;

import java.util.List;

import com.web.oa.pojo.Employee;
import com.web.oa.pojo.EmployeeCustom;
import com.web.oa.pojo.SysUserRole;

public interface EmployeeService {

	//根据员工帐号查找员工
	Employee findEmployeeByName(String name);
	
	//根据主键查找员工
	Employee findEmployeeManager(long id);
	
	List<Employee> findUsers();
	
	List<EmployeeCustom> findUserAndRoleList();
	
	void updateEmployeeRole(String roleId,String userId);
	
	List<Employee> findEmployeeByLevel(int level);
     
	//新建用户
	void addEmployee(Employee user);
    //新建用户，插入到关联表
	void addEmployeePermission(SysUserRole su);
}
