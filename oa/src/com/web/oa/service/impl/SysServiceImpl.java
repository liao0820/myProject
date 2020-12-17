package com.web.oa.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.oa.mapper.EmployeeMapper;
import com.web.oa.mapper.SysPermissionMapper;
import com.web.oa.mapper.SysPermissionMapperCustom;
import com.web.oa.mapper.SysRoleMapper;
import com.web.oa.mapper.SysRolePermissionMapper;
import com.web.oa.pojo.Employee;
import com.web.oa.pojo.MenuTree;
import com.web.oa.pojo.SysPermission;
import com.web.oa.pojo.SysPermissionExample;
import com.web.oa.pojo.SysRole;
import com.web.oa.pojo.SysRolePermission;
import com.web.oa.pojo.SysRolePermissionExample;
import com.web.oa.service.SysService;

@Service
public class SysServiceImpl implements SysService {
	
	@Autowired
	private SysPermissionMapperCustom sysPermissionMapperCustom;
	@Autowired
	private SysRoleMapper roleMapper;
	@Autowired
	private SysRolePermissionMapper rolePermissionMapper;
	@Autowired
	private SysPermissionMapper sysPermissionMapper;

	@Override
	public List<SysPermission> findMenuListByUserId(String userid)
			throws Exception {
		return sysPermissionMapperCustom.findMenuListByUserId(userid);
	}

	@Override
	public List<SysPermission> findPermissionListByUserId(String userid)
			throws Exception {
		return sysPermissionMapperCustom.findPermissionListByUserId(userid);
	}

	@Override
	public List<MenuTree> loadMenuTree() {
		return sysPermissionMapperCustom.getMenuTree();
	}

	@Override
	public List<SysRole> findAllRoles() {
		return roleMapper.selectByExample(null);
	}

	//根据用户帐号，查询所有角色和其权限列表
	@Override
	public SysRole findRolesAndPermissionsByUserId(String userId) {
		return sysPermissionMapperCustom.findRoleAndPermissionListByUserId(userId);
	}

	@Override
	public void addRoleAndPermissions(SysRole role, int[] permissionIds) {
		//添加角色
		roleMapper.insert(role);
		//添加角色和权限关系表
		for (int i = 0; i < permissionIds.length; i++) {
			SysRolePermission rolePermission = new SysRolePermission();
			//16进制随机码
			String uuid = UUID.randomUUID().toString();
			rolePermission.setId(uuid);
			rolePermission.setSysRoleId(role.getId());
			rolePermission.setSysPermissionId(permissionIds[i]+"");
			rolePermissionMapper.insert(rolePermission);
		}
	}

	@Override
	public List<SysPermission> findAllMenus() {
		SysPermissionExample example = new SysPermissionExample();
		SysPermissionExample.Criteria criteria = example.createCriteria();
		//criteria.andTypeLike("%menu%");
		criteria.andTypeEqualTo("menu");
		return sysPermissionMapper.selectByExample(example);
	}

	@Override
	public void addSysPermission(SysPermission permission) {
		sysPermissionMapper.insert(permission);
	}

	@Override
	public List<SysPermission> findMenuAndPermissionByUserId(String userId) {
		return sysPermissionMapperCustom.findMenuAndPermissionByUserId(userId);
	}

	@Override
	public List<MenuTree> getAllMenuAndPermision() {
		return sysPermissionMapperCustom.getAllMenuAndPermision();
	}

	@Override
	public List<SysPermission> findPermissionsByRoleId(String roleId) {
		return sysPermissionMapperCustom.findPermissionsByRoleId(roleId);
	}
    
	//编辑角色和权限
	@Override
	public void updateRoleAndPermissions(String roleId, int[] permissionIds) {
		//先删除角色权限关系表中角色的权限关系
		SysRolePermissionExample example = new SysRolePermissionExample();
		SysRolePermissionExample.Criteria criteria = example.createCriteria();
		criteria.andSysRoleIdEqualTo(roleId);
		rolePermissionMapper.deleteByExample(example);
		//重新创建角色权限关系
		for (Integer pid : permissionIds) {
			SysRolePermission rolePermission = new SysRolePermission();
			String uuid = UUID.randomUUID().toString();
			rolePermission.setId(uuid);
			rolePermission.setSysRoleId(roleId);
			rolePermission.setSysPermissionId(pid.toString());
			
			rolePermissionMapper.insert(rolePermission);
		}
	}

	//查询所有角色和其权限列表
	@Override
	public List<SysRole> findRolesAndPermissions() {
		return sysPermissionMapperCustom.findRoleAndPermissionList();
	}
    
	
	//删除角色
	@Override
//	@Transactional(readOnly = false)
	public void deleteRole(String roleId) {
		roleMapper.deleteByPrimaryKey(roleId);
		//删除角色权限关系
		rolePermissionMapper.deleteByPrimaryKey(roleId);
	}

}
