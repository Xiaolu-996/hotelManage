package com.guest.controller;


import com.guest.pojo.po.Front;
import com.guest.pojo.vo.Response;
import com.guest.pojo.vo.ResponseMsg;
import com.guest.service.BackgroundService;
import com.guest.service.FrontService;
import com.guest.utils.JwtUtill;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 张雪萍
 * @since 2020-11-27
 */
@CrossOrigin
@Transactional
@RestController
@Api(tags={"前台管理员"})
public class FrontController {
    @Autowired
    private FrontService frontService;
    @Autowired
    private BackgroundService backgroundService;
    @Autowired
    private JwtUtill jwtUtill;

    @PostMapping("/addFront")
    @ApiOperation(value="添加前台账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="填后台管理员的token",required=true),
            @ApiImplicitParam(name="frontId",value="前台管理员的工号",required=true),
            @ApiImplicitParam(name="name",value="前台管理员的姓名",required=false),
            @ApiImplicitParam(name="password",value="前台管理员的密码",required=true),
            @ApiImplicitParam(name="phone",value="前台管理员的电话",required=false)
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response addFront(HttpServletRequest request, Front front){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null){
            frontService.saveOrUpdate(front);
            String token = jwtUtill.updateJwt(num);
            return (new Response()).success(token);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }


    @DeleteMapping("/deleteFront")
    @ApiOperation(value="删除前台账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="填后台管理员的token",required=true),
            @ApiImplicitParam(name="id",value="要删除的前台员工的账号id，",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response deleteFront(HttpServletRequest request,int id){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null){
            frontService.removeById(id);
            String token = jwtUtill.updateJwt(num);
            return (new Response()).success(token);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }


    @PostMapping("/frontLogin")
    @ApiOperation(value="前台登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="token，不填",required=false),
            @ApiImplicitParam(name="frontId",value="前台员工的账号id，",required=true),
            @ApiImplicitParam(name="password",value="密码",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40105,message="密码错误,请核对后重新输入"),
            @ApiResponse(code=40005,message="该用户不存在")
    })
    public Response frontLogin(String frontId,String password){
        Front front1 = frontService.getById(frontId);
        if(front1 != null){
            if(front1.getPassword().equals(password)){
                String token = jwtUtill.updateJwt(frontId);
                return (new Response()).success(token);
            }
            return new Response(ResponseMsg.PASSWORD_WRONG);
        }
        return new Response(ResponseMsg.NO_SUCH_USER);
    }


    @GetMapping("/getAllFront")
    @ApiOperation(value="获取所有的前台账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="token，填后台管理员的token",required=true),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40002,message="数据不存在"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response getAllFront(HttpServletRequest request){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null){
            List<Front> fronts = frontService.list();
            if(fronts != null && fronts.size()>0){
                Map<String,Object> resultMap = new HashMap<>();
                String token = jwtUtill.updateJwt(num);
                resultMap.put("fronts",fronts);
                resultMap.put("token",token);
                return (new Response()).success(resultMap);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }

    @GetMapping("/getFrontById")
    @ApiOperation(value="通过职工id获取前台账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="token，填后台管理员的token",required=true),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40002,message="数据不存在"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response getFrontById(HttpServletRequest request,String frontId){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null){
            Front front = frontService.getById(frontId);
            if(front != null ){
                Map<String,Object> resultMap = new HashMap<>();
                String token = jwtUtill.updateJwt(num);
                resultMap.put("front",front);
                resultMap.put("token",token);
                return (new Response()).success(resultMap);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }
}

