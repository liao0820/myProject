package com.web.oa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.oa.mapper.EmployeeMapper;
import com.web.oa.mapper.SysPermissionMapperCustom;
import com.web.oa.mapper.SysUserRoleMapper;
import com.web.oa.pojo.Employee;
import com.web.oa.pojo.EmployeeCustom;
import com.web.oa.pojo.EmployeeExample;
import com.web.oa.pojo.SysUserRole;
import com.web.oa.pojo.SysUserRoleExample;
import com.web.oa.service.EmployeeService;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	private EmployeeMapper employeeMapper;
	@Autowired
	private SysPermissionMapperCustom permissionMapper;
	@Autowired
	private SysUserRoleMapper userRoleMapper;

	@Override
	public Employee findEmployeeByName(String name) {
		EmployeeExample example = new EmployeeExample();
		EmployeeExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name);
		List<Employee> list = employeeMapper.selectByExample(example);
		
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public Employee findEmployeeManager(long id) {
		return employeeMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Employee> findUsers() {
		return employeeMapper.selectByExample(null);
	}

	@Override
	public List<EmployeeCustom> findUserAndRoleList() {
		return permissionMapper.findUserAndRoleList();
	}

	@Override
	public void updateEmployeeRole(String roleId, String userId) {
		SysUserRoleExample example = new SysUserRoleExample();
		SysUserRoleExample.Criteria criteria = example.createCriteria();
		criteria.andSysUserIdEqualTo(userId);
		
		SysUserRole userRole = userRoleMapper.selectByExample(example).get(0);
		userRole.setSysRoleId(roleId);
		
		userRoleMapper.updateByPrimaryKey(userRole);
	}

	//根据员工级别查找员工信息
	@Override
	public List<Employee> findEmployeeByLevel(int level) {
		EmployeeExample example = new EmployeeExample();
		EmployeeExample.Criteria criteria = example.createCriteria();
		criteria.andRoleEqualTo(level);
		List<Employee> list = employeeMapper.selectByExample(example);
		
		return list;
	}
    
	
	//新建用户
	@Override
	public void addEmployee(Employee user) {
		employeeMapper.insert(user);	
	}
	
	//新建用户，插入到关联表
	@Override
	public void addEmployeePermission(SysUserRole user) {
		
		userRoleMapper.insert(user);
	}

}
