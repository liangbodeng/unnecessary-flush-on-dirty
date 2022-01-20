package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private TransactionTemplate transactionTemplate;

	@Test
	void testOneToOneUpdate() {
		System.out.println("======== START ========");
		String userId = "user1";

		transactionTemplate.executeWithoutResult(transactionStatus -> {
			User user = new User();
			user.setId(userId);
			user.setVersion(1);
			entityManager.persist(user);
		});

		transactionTemplate.executeWithoutResult(transactionStatus -> {
			User user = entityManager.find(User.class, userId);
			Profile userStats = new Profile();
			userStats.setId("user-stats1");
			userStats.setUser(user);
			user.setProfile(userStats);
		});

		System.out.println("========= END =========");
	}
}
