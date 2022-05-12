package com.guest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  swagger配置类
 *  接口地址：http://localhost:80/swagger-ui.html
 * </p>
 *
 * @author 路建彬
 * @since 2020-11-27
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    //作者信息
    Contact contact = new Contact("路建彬","","jianbin.lu@foxmail.com");
    //配置Swagger的Docket的bean实例

    @Bean
    public Docket customDocket(){
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        ticketPar.name("Authorization").description("Authorization")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build(); //header中的ticket参数非必填，传空也可以
        pars.add(ticketPar.build());    //根据每个方法名也知道当前方法在设置什么参数

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.guest.controller"))
                .build()
                .globalOperationParameters(pars)
                .apiInfo(apiInfo())
                .enable(true);
    }


    //配置Swagger信息 = apiInfo
    private ApiInfo apiInfo(){
        return new ApiInfo
                ("guest API"
                ,"酒店管理子系统"
                ,"v1.0"
                ,""
                ,contact
                ,"Apache 2.0"
                ,"http://www.apache.org/licenses/LICENSE-2.0"
                ,new ArrayList<>());
    }
}

