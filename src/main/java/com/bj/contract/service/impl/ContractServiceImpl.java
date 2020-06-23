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



      /*  //状态
        String status=params.get("status")!=null &&StringUtils.isNotBlank(params.get("status")+"") ? (String)params.get("status") : "-1";
        //排序列
        String sidx=params.get("sidx")!=null && StringUtils.isNoneBlank(params.get("sidx")+"") ? (String)params.get("sidx") : "";
        //排序方式
        String order=params.get("order")!=null && StringUtils.isNoneBlank(params.get("order")+"") ? (String)params.get("order") : "";

*/


       /* if(userId!=null){
            wrapper.and().eq("user_id",userId);
        }
        if(Integer.parseInt(status)!=-1){
            wrapper.and().eq("status",status);
        }
        if(StringUtils.isNotBlank(sidx)){
            wrapper.orderBy(sidx,order.equals("asc"));
        }*/
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
    public void excelExport(HttpServletResponse response, List<Long> ids) throws FileNotFoundException, IOException {

        //先根据合同id查出需要转换为excel的合同信息
        List<Contract> contractList = baseMapper.selectBatchIds(ids);

        //创建Excel导出工作对象
        HSSFWorkbook wb=new HSSFWorkbook();
        //创建Excel页
        HSSFSheet sheet=wb.createSheet("POI导出测试");
        //创建表头
        HSSFRow header=sheet.createRow(0);
        //创建表头的单元格名称-------------------------------
        HSSFCell cell1_1 = header.createCell(0);
        cell1_1.setCellValue("学号");
        HSSFCell cell1_2 = header.createCell(1);
        cell1_2.setCellValue("姓名");
        HSSFCell cell1_3 = header.createCell(2);
        cell1_3.setCellValue("年级");
        HSSFCell cell1_4 = header.createCell(3);
        cell1_4.setCellValue("年龄");
        HSSFCell cell1_5 = header.createCell(4);
        cell1_5.setCellValue("性别");
        //--------------------------------------------
        //写入一行内容：
        HSSFRow row2 = sheet.createRow(1);
        HSSFCell cell2_1 = row2.createCell(0);
        cell2_1.setCellValue(1);
        HSSFCell cell2_2 = row2.createCell(1);
        cell2_2.setCellValue("阿荣");
        HSSFCell cell2_3 = row2.createCell(2);
        cell2_3.setCellValue("17(3)");
        HSSFCell cell2_4 = row2.createCell(3);
        cell2_4.setCellValue(20);
        HSSFCell cell2_5 = row2.createCell(4);


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
