# Demo of unnecessary onFlushDirty() invoked on the parent entity in a @OneToOne relationship

This project was created to demonstrate a behavior change in Hibernate 5.6 comparing to Hibernate 5.4:
if a @OneToOne field in a parent entity is changed, it will trigger an unnecessary onFlushDirty() to
the parent entity, even though none of its columns was changed.

## Description

For example, assuming we have two entities with @OneToOne relationship: User and Profile, and User is
the parent entity.

| Entity                     | Table                |
|----------------------------|----------------------|
| User(id, version, profile) | users(id, version)   |
| Profile(id, user)          | profile(id, user_id) |


**Scenario**: user1 previously had profile=null, and now we create profile1 and link it to user1 and save profile1.

**Expected**: onFlushDirty() should not be invoked on user1.

**Actual**: onFlushDirty() was invoked on user1.

## Run the test case

* To see the Hibernate 5.6.x behavior, run following command, and check the output

    `./gradlew clean test --info`

    ```text
    ======== START ========
    Hibernate: insert into users (version, id) values (?, ?)
    Hibernate: select user0_.id as id1_1_0_, user0_.version as version2_1_0_, profile1_.id as id1_0_1_, profile1_.user_id as user_id2_0_1_ from users user0_ left outer join profile profile1_ on user0_.id=profile1_.user_id where user0_.id=?
    onFlushDirty invoked on a User
    Hibernate: insert into profile (user_id, id) values (?, ?)
    ========= END =========
    ```

* To see the Hibernate 5.4.x behavior (with a local org.hibernate.type.OneToOneType class)

    `./gradlew clean test --info -Phibernate54=true`

    ```text
    ======== START ========
    Hibernate: insert into users (version, id) values (?, ?)
    Hibernate: select user0_.id as id1_1_0_, user0_.version as version2_1_0_, profile1_.id as id1_0_1_, profile1_.user_id as user_id2_0_1_ from users user0_ left outer join profile profile1_ on user0_.id=profile1_.user_id where user0_.id=?
    Hibernate: insert into profile (user_id, id) values (?, ?)
    ========= END =========
    ```
