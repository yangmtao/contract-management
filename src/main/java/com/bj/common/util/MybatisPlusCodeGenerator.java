package com.bj.common.util;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class MybatisPlusCodeGenerator {
    public static void main(String[] args) {
        AutoGenerator autoGenerator=new AutoGenerator();

        //全局配置
        GlobalConfig gc=new GlobalConfig();
        //获取项目路径
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("yangmingtao");
        gc.setOpen(false);
        autoGenerator.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://192.168.1.104:3306/dev_contract_db?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("admin2019");
        autoGenerator.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("contract");
        pc.setParent("com.bj");
        pc.setMapper("dao");
        pc.setController("controller");
        autoGenerator.setPackageInfo(pc);



        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
        strategy.setSuperControllerClass("com.bj.sys.controller.AbstractController");
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns("id");
        //表
        strategy.setInclude("contract_payment_stage");
        strategy.setControllerMappingHyphenStyle(true);

        //strategy.setTablePrefix(pc.getModuleName() + "_");
        autoGenerator.setStrategy(strategy);
        autoGenerator.execute();
    }
}
