package com.guest.controller;


import com.guest.pojo.po.*;
import com.guest.pojo.vo.Response;
import com.guest.pojo.vo.ResponseMsg;
import com.guest.service.*;
import com.guest.utils.JwtUtill;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
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
@Api(tags={"预定信息"})
public class BookMsgController {
    @Autowired
    private BookMsgService bookMsgService;
    @Autowired
    private FrontService frontService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private CheckInService checkInService;
    @Autowired
    private GuestService guestService;
    @Autowired
    private JwtUtill jwtUtill;



    @PostMapping("/addBookMsg")
    @ApiOperation(value="添加预定信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="填前台管理员的token",required=true),
            @ApiImplicitParam(name="id",value="如果是添加就填0，如果是修改就填对应的id",required=true),
            @ApiImplicitParam(name="fromTime",value="预计入住时间",required=true),
            @ApiImplicitParam(name="toTime",value="预计退房时间",required=true),
            @ApiImplicitParam(name="contact",value="客户的联系方式",required=true),
            @ApiImplicitParam(name="idCard",value="客户的身份证号",required=true),
            @ApiImplicitParam(name="name",value="客户的姓名",required=true),
            @ApiImplicitParam(name="rank",value="房间的级别",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response addBookMsg(HttpServletRequest request, Long fromTime,Long toTime,String contact,String idCard,String name,String rank){
        String num = (String) request.getAttribute("num");
        if(frontService.getById(num) != null){
            Guest guest = new Guest(idCard,name,contact);
            guestService.saveOrUpdate(guest);
            BookMsg bookMsg = new BookMsg(0,new Timestamp(fromTime),new Timestamp(toTime),idCard,rank,0,null);
            bookMsgService.saveOrUpdate(bookMsg);
            String token = jwtUtill.updateJwt(num);
            return (new Response()).success(token);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }

    @PutMapping("/updateBookMsg")
    @ApiOperation(value="修改预定信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="填前台管理员的token",required=true),
            @ApiImplicitParam(name="id",value="如果是添加就填0，如果是修改就填对应的id",required=true),
            @ApiImplicitParam(name="fromTime",value="预计入住时间",required=true),
            @ApiImplicitParam(name="toTime",value="预计退房时间",required=true),
            @ApiImplicitParam(name="rank",value="房间的级别",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response updateBookMsg(HttpServletRequest request,int id, Long fromTime,Long toTime,String rank){
        String num = (String) request.getAttribute("num");
        if(frontService.getById(num) != null){
            BookMsg bookMsg = bookMsgService.getById(id);
            bookMsg.setState(0);
            bookMsg.setResultRoom("");
            bookMsg.setFromTime(new Timestamp(fromTime));
            bookMsg.setToTime(new Timestamp(toTime));
            bookMsg.setRank(rank);
            bookMsgService.saveOrUpdate(bookMsg);
            String token = jwtUtill.updateJwt(num);
            return (new Response()).success(token);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }

    /**
     * 为预定信息分配安排房间
     * @param request
     * @param bookMsgId
     * @param roomId
     * @return
     */
    @PutMapping("/assignment")
    @ApiOperation(value="为预定分配安排房间")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="填前台管理员的token",required=true),
            @ApiImplicitParam(name="bookMsgId",value="预定信息的id",required=true),
            @ApiImplicitParam(name="roomId",value="分配的房间的id",required=true),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40000,message="请求失败"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response assignment(HttpServletRequest request, int bookMsgId,String roomId){
        String num = (String) request.getAttribute("num");
        if(frontService.getById(num) != null){
            BookMsg bookMsg = bookMsgService.getById(bookMsgId);
            //获取当前的预定信息
            List<BookMsg> bookMsgs = bookMsgService.getBookMsgByTime(bookMsg.getFromTime(),bookMsg.getToTime());
            //获取当前正在入住的信息
            List<CheckIn> checkIns = checkInService.getValidCheckIns(bookMsg.getFromTime(),bookMsg.getToTime());
            int f = 0;
            for(BookMsg bookMsg1:bookMsgs){
                if(bookMsg1.getResultRoom() == roomId){
                    f = 1;
                    break;
                }
            }
            for(CheckIn checkIn:checkIns){
                if(f == 1)
                    break;
                if(checkIn.getRoomId() == roomId){
                    f = 1;
                    break;
                }
            }
            if(f == 0){
                bookMsg.setState(1);
                bookMsg.setResultRoom(roomId);
                bookMsgService.saveOrUpdate(bookMsg);
                String token = jwtUtill.updateJwt(num);
                return (new Response()).success(token);
            }
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }

    @DeleteMapping("/deleteBookMsg")
    @ApiOperation(value="删除预定信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="填前台管理员的token",required=true),
            @ApiImplicitParam(name="id",value="要删除的预定信息的id",required=true),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response deleteBookMsg(HttpServletRequest request,int id){
        String num = (String) request.getAttribute("num");
        if(frontService.getById(num) != null){
            bookMsgService.removeById(id);
            String token = jwtUtill.updateJwt(num);
            return (new Response()).success(token);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }

    @GetMapping("/getAllBookMsgs")
    @ApiOperation(value="获取全部预定信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="填前台管理员的token",required=true),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40002,message="数据不存在"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response getAllBookMsgs (HttpServletRequest request){
        String num = (String) request.getAttribute("num");
        if(frontService.getById(num) != null){
            List<BookMsg> bookMsgs = bookMsgService.list();
            return getResponse(num, bookMsgs);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }

    private Response getResponse(String num, List<BookMsg> bookMsgs) {
        if(bookMsgs != null && bookMsgs.size()>0){
            Map<String,Object> resultMap = new HashMap<>();
            String token = jwtUtill.updateJwt(num);
            resultMap.put("bookMsgs",bookMsgs);
            resultMap.put("token",token);
            return (new Response()).success(resultMap);
        }
        return new Response(ResponseMsg.NO_TARGET);
    }


    @GetMapping("/getBookMsgByIdCard")
    @ApiOperation(value="通过身份证号获取预定信息,模糊查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="填前台管理员的token",required=true),
            @ApiImplicitParam(name="idCard",value="身份证号",required=true),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40002,message="数据不存在"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response getBookMsgByIdCard(HttpServletRequest request,String idCard){
        String num = (String) request.getAttribute("num");
        if(frontService.getById(num) != null){
            List<BookMsg> bookMsgs = bookMsgService.getBookMsgByIdCard(idCard);
            return getResponse(num, bookMsgs);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }


}

