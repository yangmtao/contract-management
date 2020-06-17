
package com.bj.sys.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bj.common.enums.CommonEnum;
import com.bj.common.util.MapUtils;
import com.bj.sys.dao.SysMenuDao;
import com.bj.sys.entity.SysMenuEntity;
import com.bj.sys.service.SysMenuService;
import com.bj.sys.service.SysRoleMenuService;
import com.bj.sys.service.SysUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	
	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
		List<SysMenuEntity> menuList = queryListParentId(parentId);
		if(menuIdList == null){
			return menuList;
		}
		
		List<SysMenuEntity> userMenuList = new ArrayList<>();
		for(SysMenuEntity menu : menuList){
			if(menuIdList.contains(menu.getMenuId())){
				userMenuList.add(menu);
			}
		}
		return userMenuList;
	}

	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId) {
		return baseMapper.queryListParentId(parentId);
	}

	@Override
	public List<SysMenuEntity> queryNotButtonList() {
		return baseMapper.queryNotButtonList();
	}

	@Override
	@Cacheable(value="menuList",key="#userId")
	public List<SysMenuEntity> getUserMenuList(Long userId) {
		logger.info("获取 用户菜单列表");
		//系统管理员，拥有最高权限
		if(userId == CommonEnum.SUPER_ADMIN){
			return getAllMenuList(null);
		}
		
		//用户菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
		return getAllMenuList(menuIdList);
	}

	@Override
	public void delete(Long menuId){
		//删除菜单
		this.deleteById(menuId);
		//删除菜单与角色关联
		sysRoleMenuService.deleteByMap(new MapUtils().put("menu_id", menuId));
	}

	/**
	 * 获取所有菜单列表
	 */
	private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList){
		//查询根菜单列表
		List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
		if(CollectionUtils.isNotEmpty(menuList)){
			menuList.get(0).setOpen(true);
			//递归获取子菜单
			getMenuTreeList(menuList, menuIdList);
		}
		return menuList;
	}

	/**
	 * 递归
	 */
	private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList){
		List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();
		//排除菜单项--“定时任务”，定时任务已经转移到timing_task项目
		String targetName="定时任务";
		for(SysMenuEntity entity : menuList){
			//目录
			if(entity.getMenuType() == CommonEnum.MenuType.CATALOG.getValue()){
				List<SysMenuEntity> children = queryListParentId(entity.getMenuId());
				List<SysMenuEntity> childrenNew = new ArrayList<>();
				if(null!=children && !children.isEmpty()){
					for(SysMenuEntity menu: children){
						String name = menu.getMenuName();
						if(!targetName.equals(name)){
							if(CollectionUtils.isNotEmpty(menuIdList)){
								boolean flag = false;
								for(Long menuId:menuIdList){
									if(menuId.longValue() == menu.getMenuId().longValue()){
										flag = true;
										break;
									}
								}
								if(flag){
									childrenNew.add(menu);
								}
							}else{
								childrenNew.add(menu);
							}
						}
					}
				}
				entity.setList(getMenuTreeList(childrenNew, menuIdList));
			}
			subMenuList.add(entity);
		}
		
		return subMenuList;
	}
}
