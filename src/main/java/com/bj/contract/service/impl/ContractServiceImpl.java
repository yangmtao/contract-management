package com.bj.contract.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bj.common.enums.CommonEnum;
import com.bj.common.util.PageUtils;
import com.bj.common.util.Query;
import com.bj.contract.dao.ContractMapper;
import com.bj.contract.entity.Contract;
import com.bj.contract.service.ContractService;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 合同表 服务实现类
 * </p>
 *
 * @author yangmingtao
 * @since 2020-06-09
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) throws Exception {
        Page page=new Query<Contract>(params).getPage();
        Wrapper wrapper= SqlHelper.fillWrapper(page,new EntityWrapper<Contract>());
        Contract contract= JSON.parseObject((String)params.get("contract"),Contract.class);

        if (contract!=null){
            Field[] contractFields = contract.getClass().getDeclaredFields();

            for(int i=0;i<contractFields.length;i++){
                //获取属性名
                String propertie=contractFields[i].getName();
                //排除serialVersionUID
                if(!propertie.equals("serialVersionUID")){
                    //获取字段名
                    String columName=contractFields[i].getAnnotation(TableField.class)!=null ? contractFields[i].getAnnotation(TableField.class).value() : "";
                    //获取该属性在当前对象中的值
                    contractFields[i].setAccessible(true);
                    Object value=contractFields[i].get(contract);
                    //构造SQL
                    if(value!=null){
                        if(propertie.equals("contractName")){
                            wrapper.gt("instr(contract_name,'"+contract.getContractName()+"')",0);
                        }else if(propertie.equals("paymentRange")){
                            String[] paymnetRange=((String)value).split(",");
                            wrapper.between("contract_amount",paymnetRange[0],paymnetRange[1]);
                        } else{
                            //未知字段名时先判空
                            if(StringUtils.isNotBlank(columName)){
                                wrapper.eq(columName,value);
                            }
                        }
                    }
                }

            }

        }
        wrapper.eq("1",1);

        //sql格式化过滤
        wrapper.addFilterIfNeed(params.get(CommonEnum.SQL_FILTER)!=null,(String)params.get(CommonEnum.SQL_FILTER)).orderBy("create_date",false);
        //执行查询并将查询结果添加到page
        page.setRecords(baseMapper.queryAllContract(page,wrapper));
        return new PageUtils(page);
    }

    @Override
    public Contract getContractInfoById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public void excelExport(HttpServletResponse response, List<String> ids) throws FileNotFoundException, IOException, IllegalAccessException {

        //先根据合同id查出需要转换为excel的合同信息
        List<Contract> contractList = baseMapper.selectBatchIds(ids);

        //创建Excel导出工作对象
        HSSFWorkbook wb=new HSSFWorkbook();
        //创建Excel页
        HSSFSheet sheet=wb.createSheet("POI导出测试");
        //创建表头
        HSSFRow header=sheet.createRow(0);
        //获取所有有@ApiModelProperty注解
        ApiModelProperty[] annos= Contract.class.getDeclaredAnnotationsByType(ApiModelProperty.class);
        //设置表头单元格名称
        for(int i=0;i<annos.length;i++){
           HSSFCell cell=header.createCell(i);
           cell.setCellValue(annos[i].value());
        }

        //--------------------------------------------
        //写入内容：
        for(int i=0;i<contractList.size();i++){
            //创建行
            HSSFRow row = sheet.createRow(i+1);
            Field[] fields=contractList.get(i).getClass().getDeclaredFields();
            //写入每一行内容
            for(int j=0;j<fields.length;j++){
                if(fields[j].getAnnotation(ApiModelProperty.class)!=null){
                    fields[j].setAccessible(true);
                    row.createCell(j).setCellValue(String.valueOf(fields[j].get(contractList.get(i))));
                }
            }
        }

        //导出excel到客户端
        response.setContentType("application/ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition","attachment;filename="+new String(("合同信息").getBytes("gb2312"), "ISO-8859-1")+".xls");
        ServletOutputStream out = response.getOutputStream();
        wb.write(out);
        out.flush();
        out.close();

    }
}
