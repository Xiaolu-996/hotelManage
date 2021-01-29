package com.guest.controller;


import com.guest.pojo.po.*;
import com.guest.pojo.vo.Response;
import com.guest.pojo.vo.ResponseMsg;
import com.guest.service.*;
import com.guest.utils.JwtUtill;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 张雪萍
 * @since 2020-11-27
 */
@CrossOrigin
@Transactional
@RestController
@Api(tags = {"入住信息"})
public class CheckInController {
	@Autowired
	private CheckInService checkInService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private BookMsgService bookMsgService;
	@Autowired
	private FrontService frontService;
	@Autowired
	private CostTypeService costTypeService;
	@Autowired
	private CostService costService;
	@Autowired
	private GuestService guestService;
	@Autowired
	private JwtUtill jwtUtill;

	/**
	 * 有预约的用户入住
	 *
	 * @param request
	 * @param bookMsgId
	 * @return
	 */
	@PostMapping("/bookCheckIn")
	@ApiOperation(value = "有预约的用户入住")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "填前台管理员的token", required = true),
			@ApiImplicitParam(name = "bookMsgId", value = "预定信息的id", required = true),
	})
	@ApiResponses({
			@ApiResponse(code = 200, message = "请求成功"),
			@ApiResponse(code = 40000, message = "请求失败"),
			@ApiResponse(code = 40104, message = "非法操作, 试图操作不属于自己的数据")
	})
	public Response bookCheckIn(HttpServletRequest request, int bookMsgId) {
		String num = (String) request.getAttribute("num");
		if (frontService.getById(num) != null) {
			BookMsg bookMsg = bookMsgService.getById(bookMsgId);
			CheckIn checkIn = new CheckIn(0, bookMsg.getGuestIdCard()
					, bookMsg.getResultRoom(), bookMsg.getFromTime()
					, bookMsg.getToTime(), 1);

			bookMsg.setState(11);
			checkIn.setState(1);
			String costName1 = bookMsg.getResultRoom() + "房间定金";
			String costName2 = bookMsg.getResultRoom() + "房间租金";
			List<CostType> costTypes1 = costTypeService.getCostTypeByName(costName1);
			List<CostType> costTypes2 = costTypeService.getCostTypeByName(costName2);
			if (costTypes1 != null && costTypes2 != null && costTypes1.size() > 0 && costTypes2.size() > 0) {
				CostType costType = costTypes1.get(0);
				Cost cost1 = new Cost(0, costType.getId(), bookMsg.getResultRoom(), 1, 0);
				costService.saveOrUpdate(cost1);

				int costNum = (int) (bookMsg.getToTime().getTime() - bookMsg.getFromTime().getTime()) / 1000 / 60 / 60 / 24;
				costType = costTypes2.get(0);
				cost1 = new Cost(0, costType.getId(), bookMsg.getResultRoom(), costNum, 1);
				costService.saveOrUpdate(cost1);

				bookMsgService.saveOrUpdate(bookMsg);
				checkInService.saveOrUpdate(checkIn);
				String token = jwtUtill.updateJwt(num);
				return (new Response()).success(token);
			}
			return new Response(ResponseMsg.FAIL);
		}
		return new Response(ResponseMsg.ILLEGAL_OPERATION);
	}

	/**
	 * 没预约的用户入住
	 *
	 * @param request
	 * @return
	 */
	@PostMapping("/checkIn")
	@ApiOperation(value = "没预约的用户入住")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "填前台管理员的token", required = true),
			@ApiImplicitParam(name = "fromTime", value = "预计入住时间", required = true),
			@ApiImplicitParam(name = "toTime", value = "预计退房时间", required = true),
			@ApiImplicitParam(name = "contact", value = "客户的联系方式", required = true),
			@ApiImplicitParam(name = "idCard", value = "客户的身份证号", required = true),
			@ApiImplicitParam(name = "name", value = "客户的姓名", required = true),
			@ApiImplicitParam(name = "roomId", value = "房间id", required = true)
	})
	@ApiResponses({
			@ApiResponse(code = 200, message = "请求成功"),
			@ApiResponse(code = 40000, message = "请求失败"),
			@ApiResponse(code = 40104, message = "非法操作, 试图操作不属于自己的数据")
	})
	public Response checkIn(HttpServletRequest request, Long fromTime, Long toTime, String contact, String idCard, String name, String roomId) {
		String num = (String) request.getAttribute("num");
		if (frontService.getById(num) != null) {
			CheckIn checkIn = new CheckIn(0, idCard, roomId, new Timestamp(fromTime), new Timestamp(toTime), 1);
			Guest guest = new Guest(idCard, name, contact);
			guestService.saveOrUpdate(guest);
			Room room = roomService.getById(roomId);
			int maxNum = room.getMaxNum();
			int num1 = checkInService.getNum(checkIn.getRoomId());
			if (maxNum > num1) {
				if (num1 == 0) {
					String costName1 = room.getRoomId() + "房间定金";
					String costName2 = room.getRoomId() + "房间租金";
					List<CostType> costTypes1 = costTypeService.getCostTypeByName(costName1);
					List<CostType> costTypes2 = costTypeService.getCostTypeByName(costName2);
					if (costTypes1 != null && costTypes2 != null && costTypes1.size() > 0 && costTypes2.size() > 0) {
						CostType costType = costTypes1.get(0);
						Cost cost1 = new Cost(0, costType.getId(), roomId, 1, 0);
						costService.saveOrUpdate(cost1);

						int costNum = (int) (toTime - fromTime) / 1000 / 60 / 60 / 24;
						costType = costTypes2.get(0);
						cost1 = new Cost(0, costType.getId(), roomId, costNum, 1);
						costService.saveOrUpdate(cost1);
					} else {
						return new Response(ResponseMsg.FAIL);
					}
				}
				checkInService.saveOrUpdate(checkIn);
				String token = jwtUtill.updateJwt(num);
				return (new Response()).success(token);
			}
			return new Response(ResponseMsg.FAIL);
		}
		return new Response(ResponseMsg.ILLEGAL_OPERATION);
	}

	/**
	 * 退房
	 *
	 * @param request
	 * @return
	 */
	@PostMapping("/checkOut")
	@ApiOperation(value = "退房")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "填前台管理员的token", required = true),
			@ApiImplicitParam(name = "roomId", value = "房间id", required = true)

	})
	@ApiResponses({
			@ApiResponse(code = 200, message = "请求成功"),
			@ApiResponse(code = 40000, message = "请求失败"),
			@ApiResponse(code = 40104, message = "非法操作, 试图操作不属于自己的数据")
	})
	public Response checkOut(HttpServletRequest request, String roomId) {
		String num = (String) request.getAttribute("num");
		if (frontService.getById(num) != null) {
			int costNum = costService.getNotCostNum(roomId);
			if (costNum == 0) {
				costService.settleCostByRoomId(roomId);
				Timestamp now = new Timestamp(LocalDateTime.now().toInstant(ZoneOffset.of("+0")).toEpochMilli());
				List<CheckIn> checkIns = checkInService.getValidCheckIns(now, now);
				if (checkIns != null && checkIns.size() > 0) {
					for (CheckIn checkIn : checkIns) {
						checkIn.setState(0);
						checkInService.saveOrUpdate(checkIn);
					}
					String token = jwtUtill.updateJwt(num);
					return (new Response()).success(token);
				}
			}
			return new Response(ResponseMsg.FAIL);
		}
		return new Response(ResponseMsg.ILLEGAL_OPERATION);
	}
}

