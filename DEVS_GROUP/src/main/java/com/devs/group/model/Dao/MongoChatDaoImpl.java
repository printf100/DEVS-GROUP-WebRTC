package com.devs.group.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.devs.group.model.repository.SequenceGenerator;
import com.devs.group.model.vo.ChatVo;
import com.devs.group.model.vo.RtcVo;
import com.mongodb.client.result.UpdateResult;

@Repository
public class MongoChatDaoImpl implements MongoChatDao {

	private static final String GROUP_COLLECTION_NAME = "GROUP";
	private static final String RTC_COLLECTION_NAME = "RTC";

	@Autowired
	private MongoTemplate mongo;

	@Autowired
	private SequenceGenerator seqGenerator;

	/*
	 * Chatting
	 */

	@Override
	public ChatVo insertChatRoom(ChatVo newRoom) {
		newRoom.setRoom_code((int) seqGenerator.getNextSequenceId(newRoom.ROOM_CODE_SEQ_NAME));
		return mongo.insert(newRoom, GROUP_COLLECTION_NAME);
	}

	@Override
	public List<ChatVo> findGroupChanelChatRoomList(int channelcode) {

		// 1. 어그리게이트, 같은 room_code에서 최대 chat_code를 가져오기
		Aggregation aggre = Aggregation.newAggregation(//
				Aggregation.sort(Sort.Direction.DESC, "chat_code"), //
				Aggregation.group("room_code").first("chat_code").as("chat_code")//
		);

		Map<String, Object> map = new HashMap<>();
		AggregationResults<? extends Map> result = mongo.aggregate(aggre, GROUP_COLLECTION_NAME, map.getClass());

		Iterator<? extends Map> iter = result.iterator();
		List<Integer> chat_code_list = new ArrayList<Integer>();
		while (iter.hasNext()) {
			Map<String, Object> resultMap = iter.next();
			chat_code_list.add((Integer) resultMap.get("chat_code"));
		}

		// 2. 최종, 해당 채널, 채팅방 별 최근 채팅 메시지 document list 추출 -> 채팅방리스트
		Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("chat_code").in(chat_code_list),
				Criteria.where("channel_code").is(channelcode));

		Query finalQuery = new Query(criteria).with(Sort.by(Sort.Direction.DESC, "chat_code"));

		return mongo.find(finalQuery, ChatVo.class, GROUP_COLLECTION_NAME);
	}

	@Override
	public ChatVo findRecentChat(int room_code) {
		Query query = new Query(Criteria.where("room_code").is(room_code))
				.with(Sort.by(Sort.Direction.DESC, "chat_code")).limit(1);

		return mongo.findOne(query, ChatVo.class, GROUP_COLLECTION_NAME);
	}

	@Override
	public ChatVo insertChat(ChatVo newChat) {
		newChat.setChat_code((int) seqGenerator.getNextSequenceId(newChat.CHAT_CODE_SEQ_NAME));
		return mongo.insert(newChat, GROUP_COLLECTION_NAME);
	}

	@Override
	public List<ChatVo> selectChatList(int room_code) {

		Query query = new Query();

		Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("room_code").is(room_code), Criteria.where("chat_code").ne(0));

		query.addCriteria(criteria).with(Sort.by(Sort.Direction.ASC, "chat_code"));

		return mongo.find(query, ChatVo.class, GROUP_COLLECTION_NAME);
	}

	@Override
	public void removeUnreadMemberCodeList(int room_code, int member_code) {
		Query query = new Query(Criteria.where("room_code").is(room_code));
		Update update = new Update().pull("unread_member_code_list", member_code);

		UpdateResult result = mongo.updateMulti(query, update, ChatVo.class, GROUP_COLLECTION_NAME);
	}

	/*
	 * WebRTC
	 */

	@Override
	public RtcVo insertRtcRoom(RtcVo newRtcRoom) {
		newRtcRoom.setRoom_code((int) seqGenerator.getNextSequenceId(newRtcRoom.RTC_CODE_SEQ_NAME));
		return mongo.insert(newRtcRoom, RTC_COLLECTION_NAME);
	}

	@Override
	public List<RtcVo> selectRtcList(int channel_code) {

		Query query = new Query();

		Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where("channel_code").is(channel_code), Criteria.where("room_code").ne(0));

		query.addCriteria(criteria).with(Sort.by(Sort.Direction.ASC, "room_code"));

		return mongo.find(query, RtcVo.class, RTC_COLLECTION_NAME);
	}

	@Override
	public int deleteRtcRoom(int room_code) {

		Query query = new Query(Criteria.where("room_code").is(room_code));

		mongo.remove(query, RTC_COLLECTION_NAME);

		return 0;
	}

	@Override
	public RtcVo selectOneRtcRoom(int room_code) {

		Query query = new Query(Criteria.where("room_code").is(room_code));

		return mongo.findOne(query, RtcVo.class, RTC_COLLECTION_NAME);
	}

}
