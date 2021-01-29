package com.guest.controller;


import com.guest.pojo.po.CostType;
import com.guest.pojo.vo.Response;
import com.guest.pojo.vo.ResponseMsg;
import com.guest.service.*;
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
 * @since 2020-12-02
 */
@CrossOrigin
@Transactional
@RestController
@Api(tags={"消费类型"})
public class CostTypeController {
    @Autowired
    private CostTypeService costTypeService;
    @Autowired
    private BackgroundService backgroundService;
    @Autowired
    private FrontService frontService;
    @Autowired
    private CostService costService;
    @Autowired
    private JwtUtill jwtUtill;


    @PostMapping("/addCostType")
    @ApiOperation(value="添加/修改消费类别")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="后台管理员的token",required=true),
            @ApiImplicitParam(name="id",value="消费类别编号，添加时直接填0，如果要修改，填要修改的类别的id",required=true),
            @ApiImplicitParam(name="name",value="消费项目的名称",required=true),
            @ApiImplicitParam(name="money",value="金额",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response addCostType(HttpServletRequest request, CostType costType){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null){
            costTypeService.saveOrUpdate(costType);
            String token = jwtUtill.updateJwt(num);
            return (new Response()).success(token);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }

    @DeleteMapping("/deleteCostType")
    @ApiOperation(value="通过id删除消费类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="后台管理员的token",required=true),
            @ApiImplicitParam(name="id",value="消费类型的id",required=true),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response deleteCostType(HttpServletRequest request,Integer id){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null){
            costService.removeByCostTypeId(id);
            costTypeService.removeById(id);
            String token = jwtUtill.updateJwt(num);
            return (new Response()).success(token);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }


    @GetMapping("/getAllCostType")
    @ApiOperation(value="获取所有的消费类型,不包括房间的租金和定金")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="后/前台管理员的token",required=true),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40002,message="数据不存在"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response getAllCostType(HttpServletRequest request){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null || frontService.getById(num) != null){
            List<CostType> costTypes = costTypeService.getAllCostType();
            if(costTypes != null && costTypes.size()>0){
                Map<String,Object> resultMap = new HashMap<>();
                String token = jwtUtill.updateJwt(num);
                resultMap.put("costTypes",costTypes);
                resultMap.put("token",token);
                return (new Response()).success(resultMap);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }

    @GetMapping("/getCostTypeById")
    @ApiOperation(value="通过id获取消费类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="后/前台管理员的token",required=true),
            @ApiImplicitParam(name="id",value="消费类型的id",required=true),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40002,message="数据不存在"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response getCostTypeById(HttpServletRequest request,Integer id){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null || frontService.getById(num) != null){
            CostType costType = costTypeService.getById(id);
            if(costType != null ){
                Map<String,Object> resultMap = new HashMap<>();
                String token = jwtUtill.updateJwt(num);
                resultMap.put("costType",costType);
                resultMap.put("token",token);
                return (new Response()).success(resultMap);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }


    @GetMapping("/getCostTypeByName")
    @ApiOperation(value="通过名称获取消费类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="后/前台管理员的token",required=true),
            @ApiImplicitParam(name="name",value="消费类型的名称",required=true),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40002,message="数据不存在"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response getCostTypeByName(HttpServletRequest request,String name){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null || frontService.getById(num) != null){
            List<CostType> costTypes = costTypeService.getCostTypeByName(name);
            if(costTypes != null ){
                Map<String,Object> resultMap = new HashMap<>();
                String token = jwtUtill.updateJwt(num);
                resultMap.put("costTypes",costTypes);
                resultMap.put("token",token);
                return (new Response()).success(resultMap);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }


}

