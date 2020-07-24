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
import com.bj.contract.entity.ReviewOrder;
import com.bj.contract.service.ContractService;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Autowired
    private ReviewOrder reviewOrder;
    //根据查询条件查询所有的相关合同信息
    @Override
    public PageUtils queryPage(Map<String, Object> params,Long deptId) throws Exception {
        //创建分页
        Page page=new Query<Contract>(params).getPage();
        //创建查询条件构造器
        Wrapper wrapper= SqlHelper.fillWrapper(page,new EntityWrapper<Contract>());

        String partyBId = null != params.get("partyBId")
                && StringUtils.isNotBlank(params.get("partyBId") + "") ? params.get("partyBId") + "" : "";
        //供应商id不为空则表示只需根据供应商id查询相关合同信息
        if (StringUtils.isNotBlank(partyBId)) {
            wrapper.eq("party_b_id",partyBId);
        }else {
            Contract contract= JSON.parseObject((String)params.get("contract"),Contract.class);
            //查询条件，合同当前部门节点
            String dept = null != params.get("deptId")
                    && StringUtils.isNotBlank(params.get("deptId") + "") ? params.get("deptId") + "" : "";
            if(StringUtils.isNotBlank(dept)){
                contract.setContractNode(Integer.parseInt(dept));
            }
            //如果合同对象不为空，根据注解加反射创建查询条件SQL
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
                                //坑
                                if(paymnetRange[0]==null || paymnetRange[0].equals("null")){
                                    System.out.println("合同金额范围为空："+paymnetRange[0]+","+paymnetRange[1]);
                                }else {
                                    System.out.println("合同金额范围非空："+paymnetRange[0]+","+paymnetRange[1]);
                                    wrapper.between("contract_amount",paymnetRange[0],paymnetRange[1]);
                                }
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
        }



        //sql格式化过滤
        wrapper.addFilterIfNeed(params.get(CommonEnum.SQL_FILTER)!=null,(String)params.get(CommonEnum.SQL_FILTER)).orderBy("create_date",false);
        //执行查询并将查询结果添加到page
        page.setRecords(baseMapper.queryAllContract(page,wrapper));
        return new PageUtils(page);
    }

    /**
     * 分页获取该部门需要审核的合同
     * @param params
     * @param deptId
     * @return
     */
    @Override
    public PageUtils queryReview(Map<String, Object> params, Long deptId, Long roleId) {
        Integer order=0;
        Integer order1=0;
        if (deptId!=10000 && deptId!=null){
            if (deptId==10003){
                order = 1;
            }else if(deptId==10004){
                order = 3;
            }else if (deptId==10005){
                order = 4;
            }
            else if (deptId==10006){
                order = 5;
            }
            else if (deptId==10007){
                order = 6;
            }else {
                order = 2;
            }

        }
        if (roleId!=null){
            if (roleId==3){
                order1=7;
            }else if(roleId==4){
                order1=8;
            }
        }
        System.out.println(deptId +"-" + roleId);
        EntityWrapper<Contract> wrapper = new EntityWrapper<>();
        if (deptId==10000){
            if(order1!=0){
                wrapper.eq("contract_node",order1);
            }else {
                System.out.println("+++++++");
            }
        }else {
            if (order!=2){
                wrapper.eq("contract_node",order);
            }else {
                wrapper.eq("contract_node",order).and().eq("demand_dept_id",deptId);
            }


        }
        String contractName = null != params.get("contractName")
                && StringUtils.isNotBlank(params.get("contractName") + "") ? params.get("contractName") + "" : "";
        String contractManagerName = null != params.get("contractManagerName")
                && StringUtils.isNotBlank(params.get("contractManagerName") + "") ? params.get("contractManagerName") + "" : "";

        if (StringUtils.isNotBlank(contractName)) {
            wrapper.gt("instr(contract_name,'" + contractName + "')", 0);
        }
        if (StringUtils.isNotBlank(contractManagerName)) {
            wrapper.gt("instr(real_name,'" + contractManagerName + "')", 0);
        }

        wrapper.eq("1",1);
        Page<Contract> page = null;
        try {
            page = new Query<Contract>(params).getPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        page.setRecords(baseMapper.queryAllContract(page,wrapper));

        return new PageUtils(page);//new PageUtils(page);
    }

    //根据合同id获取合同信息，仅包含合同表中的数据
    @Override
    public Contract getContractInfoById(Long id) {
        return baseMapper.selectById(id);
    }

    //合同信息导出为excel
    @Override
    public void excelExport(HttpServletResponse response, List<String> ids) throws FileNotFoundException, IOException, IllegalAccessException {

        //先根据合同id查出需要转换为excel的合同信息
        List<Contract> contractList = baseMapper.selectContractByIds(ids);

        //创建Excel导出工作对象
        HSSFWorkbook wb=new HSSFWorkbook();
        //创建Excel页
        HSSFSheet sheet=wb.createSheet("POI导出测试");
        //创建表头
        HSSFRow header=sheet.createRow(0);
        //设置表头样式
        HSSFCellStyle cellStyle=wb.createCellStyle();
        //背景色
        cellStyle.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        //水平对齐方式
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //垂直对齐方式
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置字体
        Font font=wb.createFont();
        font.setFontName("微软雅黑");
        font.setBold(true);
        cellStyle.setFont(font);
        //应用样式
        header.setRowStyle(cellStyle);
        //获取所有有@ApiModelProperty注解
        Field[] fields=Contract.class.getDeclaredFields();
        //设置表头单元格名称
        int rowNo=0;
        for(int i=0;i<fields.length;i++){
            if(fields[i].getAnnotation(ApiModelProperty.class)!=null){
                HSSFCell cell=header.createCell(rowNo++);
                cell.setCellValue(fields[i].getAnnotation(ApiModelProperty.class).value());
                cell.setCellStyle(cellStyle);
            }
        }

        //写入内容：
        for(int i=0;i<contractList.size();i++){
            //创建行
            HSSFRow row = sheet.createRow(i+1);
            System.out.println("第"+(i+1)+"条数据："+contractList.get(i).getContractName());
            Field[] lineFields=contractList.get(i).getClass().getDeclaredFields();
            //写入每一行内容
            int k=0;
            for(int j=0;j<lineFields.length;j++){
                if(lineFields[j].getAnnotation(ApiModelProperty.class)!=null){
                    lineFields[j].setAccessible(true);
                    SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
                    //如果为时间类型，转换时间格式
                    if(lineFields[j].getType()== Date.class){
                        row.createCell(k++).setCellValue(ft.format(lineFields[j].get(contractList.get(i))));
                    }else{
                        row.createCell(k++).setCellValue(String.valueOf(lineFields[j].get(contractList.get(i))));
                    }

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

    /*根据合同id获取合同详细信息，包括会将合同表中的外键信息进行转换为具体数据
     *也包括付款阶段等相关信息
     */
    @Override
    public Contract getContractDetailById(Long id) {
        return baseMapper.selectContractDetail(id);
    }

    //获取当前合同的所有年份
    @Override
    public String[] getAllYear() {
        return baseMapper.selectAllYear();
    }

    //根据年份获取各个合同类别的合同数量
    @Override
    public List<Map<String,String>>  getTypeCount(String year) {
        return baseMapper.selectTypeCount(year);
    }

    //根据年份获取各个合同类别每个月的合同数量
    @Override
    public List<Map<String, String>> getTypeCountMonth(String year, Integer type) {
        return baseMapper.selectTypeCountMonth(year,type);
    }
    //根据年份获取每个月对应的合同数量
    @Override
    public List<Map<String, String>> getMonthNumberByYear(String year) {
        return baseMapper.selectMonthNumberByYear(year);
    }

    //获取当前合同的所有年份以及对应的合同数量
    @Override
    public List<Map<String, String>> getAllYearAndCount() {
        return baseMapper.selectAllYearAndCount();
    }

    //获取当前合同详细信息
    @Override
    public Contract getById(Long contractId) {
        Contract contract = baseMapper.contractById(contractId);
        return contract;
    }
}
