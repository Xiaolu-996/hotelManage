package com.guest.controller;


import com.guest.pojo.po.BookMsg;
import com.guest.pojo.po.CheckIn;
import com.guest.pojo.po.CostType;
import com.guest.pojo.po.Guest;
import com.guest.pojo.vo.GuestMsg;
import com.guest.pojo.vo.Response;
import com.guest.pojo.vo.ResponseMsg;
import com.guest.service.*;
import com.guest.utils.JwtUtill;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
@Api(tags={"客户"})
public class GuestController {
    @Autowired
    private BackgroundService backgroundService;
    @Autowired
    private FrontService frontService;
    @Autowired
    private GuestService guestService;
    @Autowired
    private BookMsgService bookMsgService;
    @Autowired
    private CheckInService checkInService;
    @Autowired
    private CostService costService;
    @Autowired
    private JwtUtill jwtUtill;


    @PostMapping("/addGuest")
    @ApiOperation(value="添加/修改客户")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="前/后台管理员的token",required=true),
            @ApiImplicitParam(name="idCard",value="客户的身份证号",required=true),
            @ApiImplicitParam(name="name",value="客户的姓名",required=true),
            @ApiImplicitParam(name="contact",value="客户的联系方式",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response addGuest(HttpServletRequest request, Guest guest){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null || frontService.getById(num) != null){
            guestService.saveOrUpdate(guest);
            String token = jwtUtill.updateJwt(num);
            return (new Response()).success(token);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }

    @DeleteMapping("/deleteGuest")
    @ApiOperation(value="通过身份证号删除客户")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="前/后台管理员的token",required=true),
            @ApiImplicitParam(name="idCard",value="身份证号",required=true),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response deleteGuest(HttpServletRequest request,String idCard){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null || frontService.getById(num) != null){
            bookMsgService.removeByIdCard(idCard);
            List<CheckIn> checkIns = checkInService.getByIdCard(idCard);
            if(checkIns != null && checkIns.size()>0){
                for(CheckIn checkIn:checkIns)
                    costService.removeByRoomId(checkIn.getRoomId());
            }
            checkInService.removeByIdCard(idCard);
            guestService.removeById(idCard);
            String token = jwtUtill.updateJwt(num);
            return (new Response()).success(token);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }


    @GetMapping("/getAllGuest")
    @ApiOperation(value="获取所有的客户，及其入住情况")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="后/前台管理员的token",required=true),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40002,message="数据不存在"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response getAllGuest(HttpServletRequest request){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null || frontService.getById(num) != null){
            List<Guest> guests = guestService.list();
            List<GuestMsg> guestMsgs = new ArrayList<>();
            if(guests != null && guests.size()>0){
                Map<String,Object> resultMap = new HashMap<>();
                String token = jwtUtill.updateJwt(num);
                for(Guest guest:guests){
                    List<BookMsg> bookMsgs = bookMsgService.getBookMsgByIdCard(guest.getIdCard());
                    List<CheckIn> checkIns = checkInService.getByIdCard(guest.getIdCard());
                    GuestMsg guestMsg = new GuestMsg(guest.getIdCard(),guest.getName(),guest.getContact(),-1,"");
                    if(checkIns != null && checkIns.size()>0 && checkIns.get(0).getState() == 1){
                        guestMsg.setState(1);
                        guestMsg.setRoomId(checkIns.get(0).getRoomId());
                    }else if(bookMsgs != null && bookMsgs.size()>0 && bookMsgs.get(0).getState() != 11){
                        guestMsg.setState(0);
                        guestMsg.setRoomId(bookMsgs.get(0).getResultRoom());
                    }else if(checkIns != null && checkIns.size()>0 && checkIns.get(0).getState() == 0){
                        guestMsg.setState(-1);
                        guestMsg.setRoomId(checkIns.get(0).getRoomId());
                    }
                    guestMsgs.add(guestMsg);
                }
                resultMap.put("guestMsgs",guestMsgs);
                resultMap.put("token",token);
                return (new Response()).success(resultMap);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }

    @GetMapping("/getGuestByIdCard")
    @ApiOperation(value="通过身份证号获取客户信息,包括入住情况，模糊查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="后/前台管理员的token",required=true),
            @ApiImplicitParam(name="idCard",value="客户的身份证号",required=true),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40002,message="数据不存在"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response getGuestByIdCard(HttpServletRequest request,String idCard){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null || frontService.getById(num) != null){
            List<Guest> guests = guestService.getByIdCard(idCard);
            List<GuestMsg> guestMsgs = new ArrayList<>();
            if(guests != null && guests.size()>0){
                Map<String,Object> resultMap = new HashMap<>();
                String token = jwtUtill.updateJwt(num);
                for(Guest guest:guests){
                    List<BookMsg> bookMsgs = bookMsgService.getBookMsgByIdCard(idCard);
                    List<CheckIn> checkIns = checkInService.getByIdCard(idCard);
                    GuestMsg guestMsg = new GuestMsg(guest.getIdCard(),guest.getName(),guest.getContact(),-1,"");
                    if(checkIns != null && checkIns.size()>0 && checkIns.get(0).getState() == 1){
                        guestMsg.setState(1);
                        guestMsg.setRoomId(checkIns.get(0).getRoomId());
                    }else if(bookMsgs != null && bookMsgs.size()>0 && bookMsgs.get(0).getState() != 11){
                        guestMsg.setState(0);
                        guestMsg.setRoomId(bookMsgs.get(0).getResultRoom());
                    }else if(checkIns != null && checkIns.size()>0 && checkIns.get(0).getState() == 0){
                        guestMsg.setState(-1);
                        guestMsg.setRoomId(checkIns.get(0).getRoomId());
                    }
                    guestMsgs.add(guestMsg);
                }
                resultMap.put("guestMsgs",guestMsgs);
                resultMap.put("token",token);
                return (new Response()).success(resultMap);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }

    @GetMapping("/getGuestByContact")
    @ApiOperation(value="通过联系方式获取客户信息,包括入住情况，模糊查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="后/前台管理员的token",required=true),
            @ApiImplicitParam(name="contact",value="客户的联系方式",required=true),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40002,message="数据不存在"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response getGuestByContact(HttpServletRequest request,String contact){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null || frontService.getById(num) != null){
            List<Guest> guests = guestService.getByContact(contact);
            List<GuestMsg> guestMsgs = new ArrayList<>();
            if(guests != null && guests.size()>0){
                Map<String,Object> resultMap = new HashMap<>();
                String token = jwtUtill.updateJwt(num);
                for(Guest guest:guests){
                    List<BookMsg> bookMsgs = bookMsgService.getBookMsgByIdCard(guest.getIdCard());
                    List<CheckIn> checkIns = checkInService.getByIdCard(guest.getIdCard());
                    GuestMsg guestMsg = new GuestMsg(guest.getIdCard(),guest.getName(),guest.getContact(),-1,"");
                    if(checkIns != null && checkIns.size()>0 && checkIns.get(0).getState() == 1){
                        guestMsg.setState(1);
                        guestMsg.setRoomId(checkIns.get(0).getRoomId());
                    }else if(bookMsgs != null && bookMsgs.size()>0 && bookMsgs.get(0).getState() != 11){
                        guestMsg.setState(0);
                        guestMsg.setRoomId(bookMsgs.get(0).getResultRoom());
                    }else if(checkIns != null && checkIns.size()>0 && checkIns.get(0).getState() == 0){
                        guestMsg.setState(-1);
                        guestMsg.setRoomId(checkIns.get(0).getRoomId());
                    }
                    guestMsgs.add(guestMsg);
                }
                resultMap.put("guestMsgs",guestMsgs);
                resultMap.put("token",token);
                return (new Response()).success(resultMap);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }

    @GetMapping("/getGuestByName")
    @ApiOperation(value="通过联系方式获取客户信息,包括入住情况，模糊查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="后/前台管理员的token",required=true),
            @ApiImplicitParam(name="name",value="客户的姓名",required=true),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40002,message="数据不存在"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response getGuestByName(HttpServletRequest request,String name){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null || frontService.getById(num) != null){
            List<Guest> guests = guestService.getByName(name);
            List<GuestMsg> guestMsgs = new ArrayList<>();
            if(guests != null && guests.size()>0){
                Map<String,Object> resultMap = new HashMap<>();
                String token = jwtUtill.updateJwt(num);
                for(Guest guest:guests){
                    List<BookMsg> bookMsgs = bookMsgService.getBookMsgByIdCard(guest.getIdCard());
                    List<CheckIn> checkIns = checkInService.getByIdCard(guest.getIdCard());
                    GuestMsg guestMsg = new GuestMsg(guest.getIdCard(),guest.getName(),guest.getContact(),-1,"");
                    if(checkIns != null && checkIns.size()>0 && checkIns.get(0).getState() == 1){
                        guestMsg.setState(1);
                        guestMsg.setRoomId(checkIns.get(0).getRoomId());
                    }else if(bookMsgs != null && bookMsgs.size()>0 && bookMsgs.get(0).getState() != 11){
                        guestMsg.setState(0);
                        guestMsg.setRoomId(bookMsgs.get(0).getResultRoom());
                    }else if(checkIns != null && checkIns.size()>0 && checkIns.get(0).getState() == 0){
                        guestMsg.setState(-1);
                        guestMsg.setRoomId(checkIns.get(0).getRoomId());
                    }
                    guestMsgs.add(guestMsg);
                }
                resultMap.put("guestMsgs",guestMsgs);
                resultMap.put("token",token);
                return (new Response()).success(resultMap);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }
}

