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

import freemarker.template.utility.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
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

        //--------------------------------------------
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

    @Override
    public Contract getContractDetailById(Long id) {
        return baseMapper.selectContractDetail(id);
    }

    //获取当前合同详细信息
    @Override
    public Contract getById(Long contractId) {
        Contract contract = baseMapper.contractById(contractId);
        return contract;
    }
}
