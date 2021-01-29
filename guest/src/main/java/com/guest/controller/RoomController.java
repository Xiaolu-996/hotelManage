package com.guest.controller;


import com.guest.pojo.po.BookMsg;
import com.guest.pojo.po.CheckIn;
import com.guest.pojo.po.CostType;
import com.guest.pojo.po.Room;
import com.guest.pojo.vo.Response;
import com.guest.pojo.vo.ResponseMsg;
import com.guest.pojo.vo.RoomMsg;
import com.guest.service.*;
import com.guest.utils.JwtUtill;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
 * @since 2020-11-27
 */
@CrossOrigin
@Transactional
@RestController
@Api(tags={"房间"})
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private BackgroundService backgroundService;
    @Autowired
    private BookMsgService bookMsgService;
    @Autowired
    private CostService costService;
    @Autowired
    private CostTypeService costTypeService;
    @Autowired
    private FrontService frontService;
    @Autowired
    private CheckInService checkInService;
    @Autowired
    private JwtUtill jwtUtill;

    @PostMapping("/addRoom")
    @ApiOperation(value="添加/修改房间")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="后台管理员的token",required=true),
            @ApiImplicitParam(name="roomId",value="房间id，如果要修改，填要修改的房间id",required=true),
            @ApiImplicitParam(name="rank",value="房间级别，共A,B,C,D三个级别",required=true),
            @ApiImplicitParam(name="rent",value="租金，单位是人民币元",required=true),
            @ApiImplicitParam(name="earnest",value="入住定金，单位是人民币元",required=true),
            @ApiImplicitParam(name="maxNum",value="最大人数",required=true),
            @ApiImplicitParam(name="size",value="房间的大小，以平方米为单位",required=true),
            @ApiImplicitParam(name="position",value="房间的具体位置信息",required=true),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response addRoom(HttpServletRequest request, Room room){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null){
            roomService.saveOrUpdate(room);
            CostType costType1;
            CostType costType2;
            List<CostType> costTypes1 = costTypeService.getCostTypeByName(room.getRoomId()+"房间定金");
            List<CostType> costTypes2 = costTypeService.getCostTypeByName(room.getRoomId()+"房间租金");
            if(costTypes1 != null && costTypes1.size() > 0 && costTypes2 != null && costTypes2.size() > 0){
                costType1 = costTypes1.get(0);
                costType2 = costTypes2.get(0);
                costType1.setMoney(0-room.getEarnest());
                costType1.setMoney(room.getRent());
            }else{
                costType1 = new CostType(0,room.getRoomId()+"房间定金",0-room.getEarnest());
                costType2 = new CostType(0,room.getRoomId()+"房间租金",room.getRent());
            }
            costTypeService.saveOrUpdate(costType1);
            costTypeService.saveOrUpdate(costType2);
            String token = jwtUtill.updateJwt(num);
            return (new Response()).success(token);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }


    @DeleteMapping("/deleteRoom")
    @ApiOperation(value="删除房间")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="后台管理员的token",required=true),
            @ApiImplicitParam(name="id",value="要删除的房间的id",required=true),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response deleteRoom(HttpServletRequest request,String id){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null){
            costService.removeByRoomId(id);
            bookMsgService.removeByResultRoom(id);
            checkInService.removeByRoomId(id);
            costTypeService.removeByName(id+"房间定金");
            costTypeService.removeByName(id+"房间租金");
            roomService.removeById(id);
            String token = jwtUtill.updateJwt(num);
            return (new Response()).success(token);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }


    @GetMapping("/getAllRooms")
    @ApiOperation(value="获取所有的房间")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="后/前台管理员的token",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40002,message="数据不存在"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response getAllRooms(HttpServletRequest request){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null || frontService.getById(num) != null){
            List<Room> rooms = roomService.list();
            if(rooms != null && rooms.size()>0){
                Timestamp now = new Timestamp(System.currentTimeMillis());
                Timestamp later = new Timestamp(System.currentTimeMillis()+31536000000l);
                //获取当前的预定信息
                List<BookMsg> bookMsgs = bookMsgService.getBookMsgByTime(now,later);
                //获取当前正在入住的信息
                List<CheckIn> checkIns = checkInService.getValidCheckIns(now,now);
                List<RoomMsg> roomMsgs = new ArrayList<>();
                for(Room room:rooms){
                    RoomMsg roomMsg = new RoomMsg(room.getRoomId(),room.getSize(),room.getRank(),room.getRent(),room.getEarnest(),room.getMaxNum(),room.getPosition(),-1,"");
                    int f = 0;
                    for(CheckIn checkIn:checkIns){
                        if(f == 1)
                            break;
                        if(checkIn.getRoomId().equals(roomMsg.getRoomId())){
                            roomMsg.setState(1);
                            Timestamp fromTime = checkIn.getFromTime();
                            Timestamp toTime = checkIn.getToTime();
                            String time = fromTime.toString().substring(0,10).replace("-",".")+"-"+toTime.toString().substring(0,10).replace("-",".");
                            roomMsg.setTime(time);
                            roomMsgs.add(roomMsg);
                            f = 1;
                        }
                    }

                    for(BookMsg bookMsg:bookMsgs){
                        if(f == 1)
                            break;
                        if(bookMsg.getResultRoom().equals(roomMsg.getRoomId())){
                            roomMsg.setState(0);
                            Timestamp fromTime = bookMsg.getFromTime();
                            Timestamp toTime = bookMsg.getToTime();
                            String time = fromTime.toString().substring(0,10).replace("-",".")+"-"+toTime.toString().substring(0,10).replace("-",".");
                            roomMsg.setTime(time);
                            roomMsgs.add(roomMsg);
                            f = 1;
                        }
                    }
                    if(f != 1)
                        roomMsgs.add(roomMsg);
                }
                Map<String,Object> resultMap = new HashMap<>();
                String token = jwtUtill.updateJwt(num);
                resultMap.put("roomMsgs",roomMsgs);
                resultMap.put("token",token);
                return (new Response()).success(resultMap);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }


    @GetMapping("/getRoomById")
    @ApiOperation(value="通过id获取房间")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="后/前台管理员的token",required=true),
            @ApiImplicitParam(name="id",value="房间的id",required=true),
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40002,message="数据不存在"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response getRoomById(HttpServletRequest request,String id){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null || frontService.getById(num) != null){
            Room room = roomService.getById(id);

            if(room != null ){
                Timestamp now = new Timestamp(System.currentTimeMillis());
                Timestamp later = new Timestamp(System.currentTimeMillis()+31536000000l);
                //获取当前的预定信息
                List<BookMsg> bookMsgs = bookMsgService.getBookMsgByTime(now,later);
                //获取当前正在入住的信息
                List<CheckIn> checkIns = checkInService.getValidCheckIns(now,now);
                RoomMsg roomMsg = new RoomMsg(room.getRoomId(),room.getSize(),room.getRank(),room.getRent(),room.getEarnest(),room.getMaxNum(),room.getPosition(),-1,"");
                int f = 0;
                for(CheckIn checkIn:checkIns){
                    if(f == 1)
                        break;
                    if(checkIn.getRoomId().equals(roomMsg.getRoomId())){
                        roomMsg.setState(1);
                        Timestamp fromTime = checkIn.getFromTime();
                        Timestamp toTime = checkIn.getToTime();
                        String time = fromTime.toString().substring(0,10).replace("-",".")+"-"+toTime.toString().substring(0,10).replace("-",".");
                        roomMsg.setTime(time);
                        f = 1;
                    }
                }

                for(BookMsg bookMsg:bookMsgs){
                    if(f == 1)
                        break;
                    if(bookMsg.getResultRoom().equals(roomMsg.getRoomId())){
                        roomMsg.setState(0);
                        Timestamp fromTime = bookMsg.getFromTime();
                        Timestamp toTime = bookMsg.getToTime();
                        String time = fromTime.toString().substring(0,10).replace("-",".")+"-"+toTime.toString().substring(0,10).replace("-",".");
                        roomMsg.setTime(time);
                        f = 1;
                    }
                }
                Map<String,Object> resultMap = new HashMap<>();
                String token = jwtUtill.updateJwt(num);
                resultMap.put("roomMsg",roomMsg);
                resultMap.put("token",token);
                return (new Response()).success(resultMap);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }

    /**
     * 获取所有的空房间
     * @param request
     * @return
     */
    @GetMapping("/getNullRooms")
    @ApiOperation(value="获取现在所有的空房间")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="后/前台管理员的token",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40002,message="数据不存在"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response getNullRooms(HttpServletRequest request){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null || frontService.getById(num) != null){
            Timestamp now = new Timestamp(System.currentTimeMillis());
            List<Room> rooms = roomService.list();
            //获取当前的预定信息
            List<BookMsg> bookMsgs = bookMsgService.getBookMsgByTime(now,now);
            //获取当前正在入住的信息
            List<CheckIn> checkIns = checkInService.getValidCheckIns(now,now);
            List<Room> nullRooms = new ArrayList<>();
            for(Room room:rooms){
                String roomId = room.getRoomId();
                int f = 0;
                for(BookMsg bookMsg:bookMsgs){
                    if(roomId.equals(bookMsg.getResultRoom()) ){
                        f = 1;
                        break;
                    }
                }
                for(CheckIn checkIn:checkIns){
                    if(f == 1)
                        break;
                    if(roomId.equals(checkIn.getRoomId()) ){
                        f = 1;
                        break;
                    }
                }
                if(f == 0){
                   nullRooms.add(room);
                }
            }
            if(rooms != null && rooms.size()>0){
                Map<String,Object> resultMap = new HashMap<>();
                String token = jwtUtill.updateJwt(num);
                resultMap.put("nullRooms",nullRooms);
                resultMap.put("token",token);
                return (new Response()).success(resultMap);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }

    /**
     * 获取所有已经入住的房间
     * @param request
     * @return
     */
    @GetMapping("/getHasCheckedRoom")
    @ApiOperation(value="获取现在所有已经入住的房间")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="后/前台管理员的token",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40002,message="数据不存在"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response getHasCheckedRoom(HttpServletRequest request){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null || frontService.getById(num) != null){
            Timestamp now = new Timestamp(System.currentTimeMillis());
            //获取当前正在入住的信息
            List<CheckIn> checkIns = checkInService.getValidCheckIns(now,now);
            List<RoomMsg> roomMsgs = new ArrayList<>();
            for(CheckIn checkIn:checkIns){
                Room room = roomService.getById(checkIn.getRoomId());
                RoomMsg roomMsg = new RoomMsg(room.getRoomId(),room.getSize(),room.getRank(),room.getRent(),room.getEarnest(),room.getMaxNum(),room.getPosition(),1,"");
                Timestamp fromTime = checkIn.getFromTime();
                Timestamp toTime = checkIn.getToTime();
                String time = fromTime.toString().substring(0,10).replace("-",".")+"-"+toTime.toString().substring(0,10).replace("-",".");
                roomMsg.setTime(time);
                roomMsgs.add(roomMsg);
            }
            if(roomMsgs != null && roomMsgs.size()>0){
                Map<String,Object> resultMap = new HashMap<>();
                String token = jwtUtill.updateJwt(num);
                resultMap.put("roomMsgs",roomMsgs);
                resultMap.put("token",token);
                return (new Response()).success(resultMap);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }

    /**
     * 获取所有已经被预定的房间
     * @param request
     * @return
     */
    @GetMapping("/getHasBookedRoom")
    @ApiOperation(value="获取现在所有已经被预定的房间")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="后/前台管理员的token",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40002,message="数据不存在"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response getHasBookedRoom(HttpServletRequest request){
        String num = (String) request.getAttribute("num");
        if(backgroundService.getById(num) != null || frontService.getById(num) != null){
            Timestamp now = new Timestamp(System.currentTimeMillis());
            List<RoomMsg> roomMsgs = new ArrayList<>();
            //获取当前的预定信息
            List<BookMsg> bookMsgs = bookMsgService.getBookMsgByTime(now,now);
            for(BookMsg bookMsg:bookMsgs){
                Room room = roomService.getById(bookMsg.getResultRoom());
                RoomMsg roomMsg = new RoomMsg(room.getRoomId(),room.getSize(),room.getRank(),room.getRent(),room.getEarnest(),room.getMaxNum(),room.getPosition(),0,"");
                Timestamp fromTime = bookMsg.getFromTime();
                Timestamp toTime = bookMsg.getToTime();
                String time = fromTime.toString().substring(0,10).replace("-",".")+"-"+toTime.toString().substring(0,10).replace("-",".");
                roomMsg.setTime(time);
                roomMsgs.add(roomMsg);
            }
            if(roomMsgs != null && roomMsgs.size()>0){
                Map<String,Object> resultMap = new HashMap<>();
                String token = jwtUtill.updateJwt(num);
                resultMap.put("roomMsgs",roomMsgs);
                resultMap.put("token",token);
                return (new Response()).success(resultMap);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }


    /**
     * 按等级获取指定时间内可以安排给预定客户或入住客户的房间
     * @param request
     * @return
     */
    @GetMapping("/getNullRoomsByRank")
    @ApiOperation(value="按等级获取指定时间内可以安排给预定客户或入住客户的房间")
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization",value="后/前台管理员的token",required=true)
    })
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=40002,message="数据不存在"),
            @ApiResponse(code=40104,message="非法操作, 试图操作不属于自己的数据")
    })
    public Response getNullRoomsByRank(HttpServletRequest request,long fromTime,long toTime,String rank) {
        String num = (String) request.getAttribute("num");
        if (backgroundService.getById(num) != null || frontService.getById(num) != null) {
            Timestamp fromTimeT = new Timestamp(fromTime);
            Timestamp toTimeT = new Timestamp(toTime);
            List<Room> rooms = roomService.list();
            //获取当前的预定信息
            List<BookMsg> bookMsgs = bookMsgService.getBookMsgByTime1(fromTimeT, toTimeT);
            System.out.println(bookMsgs);
            //获取当前正在入住的信息
            List<CheckIn> checkIns = checkInService.getValidCheckIns1(fromTimeT, toTimeT);
            List<Room> nullRooms = new ArrayList<>();
            for (Room room : rooms) {
                if(rank.equals(room.getRank())){
                    String roomId = room.getRoomId();
                    int f = 0;
                    for (BookMsg bookMsg : bookMsgs) {
                        if (roomId.equals(bookMsg.getResultRoom())) {
                            f = 1;
                            break;
                        }
                    }
                    for (CheckIn checkIn : checkIns) {
                        if (f == 1)
                            break;
                        if (roomId.equals(checkIn.getRoomId())) {
                            f = 1;
                            break;
                        }
                    }
                    if (f == 0) {
                        nullRooms.add(room);
                    }
                }
            }
            if (rooms != null && rooms.size() > 0) {
                Map<String, Object> resultMap = new HashMap<>();
                String token = jwtUtill.updateJwt(num);
                resultMap.put("nullRooms", nullRooms);
                resultMap.put("token", token);
                return (new Response()).success(resultMap);
            }
            return new Response(ResponseMsg.NO_TARGET);
        }
        return new Response(ResponseMsg.ILLEGAL_OPERATION);
    }
}

