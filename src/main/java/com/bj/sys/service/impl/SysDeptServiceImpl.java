
package com.bj.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bj.common.annotation.DataFilter;
import com.bj.common.enums.CommonEnum;
import com.bj.sys.dao.SysDeptDao;
import com.bj.sys.entity.SysDeptEntity;
import com.bj.sys.service.SysDeptService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao, SysDeptEntity> implements SysDeptService {
	
	@Override
	@DataFilter(subDept = true, user = false)
	public List<SysDeptEntity> queryList(Map<String, Object> params){
		Wrapper wrapper = new EntityWrapper<SysDeptEntity>().addFilterIfNeed(params.get(CommonEnum.SQL_FILTER) != null, (String)params.get(CommonEnum.SQL_FILTER));
		wrapper.addFilterIfNeed(params.containsKey("delete1"),"dept_id<>10000");
		wrapper.orderBy("sortNum");
		List<SysDeptEntity> deptList =
			this.selectList(wrapper);

		for(SysDeptEntity sysDeptEntity : deptList){
			sysDeptEntity.setName(sysDeptEntity.getDeptName());
			SysDeptEntity parentDeptEntity =  this.selectById(sysDeptEntity.getParentId());
			if(parentDeptEntity != null){
				sysDeptEntity.setParentName(parentDeptEntity.getDeptName());
			}
		}
		return deptList;
	}

	@Override
	public List<Long> queryDeptIdList(Long parentId) {

		return baseMapper.queryDeptIdList(parentId);
	}

	@Override
	public List<Long> queryUserDeptIdList() throws Exception {
		return baseMapper.queryUserDeptIdList();
	}

	@Override
	public List<Long> getSubDeptIdList(Long deptId){
		//部门及子部门ID列表
		List<Long> deptIdList = new ArrayList<>();

		//获取子部门ID
		List<Long> subIdList = queryDeptIdList(deptId);
		getDeptTreeList(subIdList, deptIdList);

		return deptIdList;
	}

	/**
	 * 递归
	 */
	private void getDeptTreeList(List<Long> subIdList, List<Long> deptIdList){
		for(Long deptId : subIdList){
			List<Long> list = queryDeptIdList(deptId);
			if(list.size() > 0){
				getDeptTreeList(list, deptIdList);
			}

			deptIdList.add(deptId);
		}
	}

	@Override
	public void autoUpdate() {
		baseMapper.deleteAll();
		baseMapper.insertAll();
	}
}
