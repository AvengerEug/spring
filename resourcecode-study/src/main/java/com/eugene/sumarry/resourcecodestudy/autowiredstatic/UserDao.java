package com.eugene.sumarry.resourcecodestudy.autowiredstatic;

import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

	public static void test() {
		System.out.println("123");
	}
}
