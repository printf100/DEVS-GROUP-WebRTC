package com.devs.group.model.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.devs.group.model.vo.MongoSequence;

@Repository
public class SequenceGenerator {

	@Autowired
	private MongoOperations mongoOperations;

	public long getNextSequenceId(String seqName) {

		Query query = new Query(Criteria.where("_id").is(seqName));

		Update update = new Update();
		update.inc("seq", 1);

		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true).upsert(true);

		MongoSequence mongoSequence = mongoOperations.findAndModify(query, update, options, MongoSequence.class);

		return mongoSequence.getSeq();
	}
}
