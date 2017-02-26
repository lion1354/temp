package com.popular.rest.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.popular.model.ClientUser;
import com.popular.model.Friend;
import com.popular.model.GoodWill;
import com.popular.model.Refuse;
import com.popular.responseutil.ResponseUtils;
import com.popular.service.ClientUserService;
import com.popular.service.FriendService;
import com.popular.service.GoodWillService;
import com.popular.service.RefuseService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import net.sf.json.JSONObject;

/**
 * 
 * @author Aidan
 *
 * @Date 2016年7月28日
 */
@Api("friend")
@RestController
@RequestMapping("/1/friend")
public class FriendController {

	private static Logger log = LoggerFactory.getLogger(FriendController.class);
	@Autowired
	private GoodWillService goodWillService;

	@Autowired
	private FriendService friendService;

	@Autowired
	private RefuseService refuseService;

	@Autowired
	private ClientUserService userService;

	/**
	 * 示好
	 * 
	 * @param goodWill
	 * @return
	 */
	@ApiOperation(value = "show good to some body", notes = "show good to some body", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/showGood", method = RequestMethod.POST)
	public ResponseEntity<?> showGood(
			@ApiParam(value = "goodWill", required = true) @RequestBody(required = true) String goodWill) {
		log.debug(goodWill);
		try {
			JSONObject info = JSONObject.fromObject(goodWill);
			if (!info.containsKey("ownerId"))
				return ResponseUtils.fail("Owner id is necessary!");
			if (!info.containsKey("targetId"))
				return ResponseUtils.fail("Target id is necessary!");
			ClientUser owner = new ClientUser(info.getInt("ownerId"));
			ClientUser target = new ClientUser(info.getInt("targetId"));
			GoodWill gw = goodWillService.getGoodWillByOwnerAndTarget(new GoodWill(target, owner));
			if (gw == null) {
				goodWillService.add(new GoodWill(owner, target, new Date()));
			} else {
				goodWillService.showGood(new GoodWill(target, owner));
			}
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/**
	 * 查询我的好友
	 * 
	 * @param ownerId
	 * @return
	 */
	@ApiOperation(value = "get frends by owner", notes = "get frends by owner", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/frends/{ownerId}", method = RequestMethod.GET)
	public ResponseEntity<?> getFrendsByOwner(
			@ApiParam(value = "ownerId", required = true) @PathVariable(value = "ownerId") int ownerId) {
		try {
			List<Friend> friends = friendService.getFriendByOwnerId(ownerId);
			List<ClientUser> users = new ArrayList<ClientUser>();
			for (Friend friend : friends) {
				users.add(friend.getFriend());
			}
			return ResponseUtils.successWithValues(users);
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/**
	 * 双删
	 * 
	 * @param friend
	 * @return
	 */
	@ApiOperation(value = "delete friend", notes = "delete friend", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<?> deleteFriend(
			@ApiParam(value = "friend", required = true) @RequestBody(required = true) String friend) {
		try {
			JSONObject info = JSONObject.fromObject(friend);
			if (!info.containsKey("ownerId"))
				return ResponseUtils.fail("Owner id is necessary!");
			if (!info.containsKey("friendId"))
				return ResponseUtils.fail("Friend id is necessary!");
			friendService.deleteFriend(info);
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/**
	 * 拒绝
	 * 
	 * @param refuse
	 * @return
	 */
	@ApiOperation(value = "refuse friend", notes = "refuse friend", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/refuse", method = RequestMethod.POST)
	public ResponseEntity<?> refuseFriend(
			@ApiParam(value = "refuse", required = true) @RequestBody(required = true) String refuse) {
		try {
			JSONObject info = JSONObject.fromObject(refuse);
			if (!info.containsKey("ownerId"))
				return ResponseUtils.fail("Owner id is necessary!");
			if (!info.containsKey("targetId"))
				return ResponseUtils.fail("Target id is necessary!");
			ClientUser owner = new ClientUser(info.getInt("ownerId"));
			ClientUser target = new ClientUser(info.getInt("targetId"));
			Refuse ref = refuseService.getRefuseByOwnerAndTarget(new Refuse(owner, target));
			if (ref == null) {
				refuseService.add(new Refuse(owner, target, 1, new Date()));
			} else {
				refuseService.updateByOwnerAndTarget(
						new Refuse(owner, target, ref.getRefuseCount() + 1, ref.getCreateTime()));
			}
			return ResponseUtils.success();
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}

	/**
	 * 推荐
	 * 
	 * @param ownerId
	 * @return
	 */
	@ApiOperation(value = "recommend friends", notes = "recommend friends", consumes = "application/json", produces = "application/json")
	@RequestMapping(value = "/recommend/{ownerId}", method = RequestMethod.GET)
	public ResponseEntity<?> findFriends(
			@ApiParam(value = "ownerId", required = true) @PathVariable(value = "ownerId") int ownerId) {
		try {
			ClientUser owner = userService.getClientUserById(ownerId);
			if (owner == null)
				return ResponseUtils.fail("No user!");
			int sexualOrientation = owner.getSexualOrientation();
			int age = owner.getAge();
			String city = owner.getCity();
			String province = owner.getProvince();

			// 拒绝记录过滤
			List<Refuse> refuses = refuseService.getRefuseByOwner(ownerId);
			List<Integer> userIds = new ArrayList<Integer>();
			for (Refuse refuse : refuses) {
				if(!userIds.contains(refuse.getTarget().getId()))
					userIds.add(refuse.getTarget().getId());
			}
			
			// 好友记录过滤
			List<Friend> friends = friendService.getFriendByOwnerId(ownerId);
			for (Friend friend : friends) {
				if(!userIds.contains(friend.getFriend().getId()))
					userIds.add(friend.getFriend().getId());
			}

			List<ClientUser> users = new ArrayList<ClientUser>();

			// 同组，同市，年龄差3
			List<ClientUser> users1 = userService.getClientUserByMap(
					getParams(ownerId, sexualOrientation, 1, age, city, 1, province, -1, userIds));
			getList(users, users1);
			// 同组，同市，年龄差6
			List<ClientUser> users2 = userService.getClientUserByMap(
					getParams(ownerId, sexualOrientation, 2, age, city, 1, province, -1, userIds));
			getList(users, users2);
			// 同组，同市，年龄差10
			List<ClientUser> users3 = userService.getClientUserByMap(
					getParams(ownerId, sexualOrientation, 3, age, city, 1, province, -1, userIds));
			getList(users, users3);
			// 同组，同市，年龄其他
			List<ClientUser> users4 = userService.getClientUserByMap(
					getParams(ownerId, sexualOrientation, 4, age, city, 1, province, -1, userIds));
			getList(users, users4);
			// 同组，同省，不同市，年龄差3
			List<ClientUser> users5 = userService.getClientUserByMap(
					getParams(ownerId, sexualOrientation, 1, age, city, 0, province, 1, userIds));
			getList(users, users5);
			// 同组，同省，不同市，年龄差6
			List<ClientUser> users6 = userService.getClientUserByMap(
					getParams(ownerId, sexualOrientation, 2, age, city, 0, province, 1, userIds));
			getList(users, users6);
			// 同组，同省，不同市，年龄差10
			List<ClientUser> users7 = userService.getClientUserByMap(
					getParams(ownerId, sexualOrientation, 3, age, city, 0, province, 1, userIds));
			getList(users, users7);
			// 同组，同省，不同市，年龄其他
			List<ClientUser> users8 = userService.getClientUserByMap(
					getParams(ownerId, sexualOrientation, 4, age, city, 0, province, 1, userIds));
			getList(users, users8);
			// 同组，外省，年龄差3
			List<ClientUser> users9 = userService.getClientUserByMap(
					getParams(ownerId, sexualOrientation, 1, age, city, -1, province, 0, userIds));
			getList(users, users9);
			// 同组，外省，年龄差6
			List<ClientUser> users10 = userService.getClientUserByMap(
					getParams(ownerId, sexualOrientation, 2, age, city, -1, province, 0, userIds));
			getList(users, users10);
			// 同组，外省，年龄差10
			List<ClientUser> users11 = userService.getClientUserByMap(
					getParams(ownerId, sexualOrientation, 3, age, city, -1, province, 0, userIds));
			getList(users, users11);
			// 同组，外省，年龄其他
			List<ClientUser> users12 = userService.getClientUserByMap(
					getParams(ownerId, sexualOrientation, 4, age, city, -1, province, 0, userIds));
			getList(users, users12);

			// 查询拒绝过一次又没有被对方拒绝的
			for (Refuse refuse : refuses) {
				if (refuse.getRefuseCount() > 1)
					continue;
				Refuse turn = refuseService
						.getRefuseByOwnerAndTarget(new Refuse(refuse.getTarget(), refuse.getOwner()));
				if (turn != null)
					continue;
				users.add(refuse.getTarget());
			}

			return ResponseUtils.successWithValues(users);
		} catch (Exception e) {
			log.error("error: " + e);
			return ResponseUtils.fail(e.getMessage());
		}
	}
	
	private List<ClientUser> getList(List<ClientUser> many, List<ClientUser> one){
		for (ClientUser clientUser : one) {
			if(!many.contains(clientUser))
				many.add(clientUser);
		}
		return many;
	}

	private Map<String, Object> getParams(int ownerId, int sex, int ageType, int age, String city, int cityType,
			String province, int provinceType, List<Integer> ids) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ownerId", ownerId);
		params.put("sexualOrientation", sex);
		params.put("cityType", cityType);
		params.put("city", city);
		params.put("provinceType", provinceType);
		params.put("province", province);
		params.put("ageType", ageType);
		params.put("age", age);
		params.put("refuseIds", ids);
		return params;
	}
}
