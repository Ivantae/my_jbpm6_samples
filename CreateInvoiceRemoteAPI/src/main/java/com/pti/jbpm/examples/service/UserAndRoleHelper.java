package com.pti.jbpm.examples.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class UserAndRoleHelper {
	/**
	 * user and his password
	 */
	private static Map<String, String> users;
	/**
	 * user and his roles/groups
	 */
	private static Map<String, List<String>> userRoles;
	/**
	 * roles or groups
	 */
	private static List<String> allRoles;
	
	static {
		// user and his password
		users = new HashMap<String, String>();
		// roles or groups
		allRoles = new ArrayList<String>();
		// user and his roles/groups
		userRoles = new HashMap<String, List<String>>();
		
		Properties properties = new Properties();
		try {
			InputStream inputStream = UserAndRoleHelper.class.getClassLoader()
					.getResourceAsStream("myUserInfo.properties");
			properties.load(inputStream);
			java.util.Enumeration<Object> keys = properties.keys();
			String key, value;
			while (keys.hasMoreElements()) {
				key = (String) keys.nextElement();
				System.out.println("Value of key : " + key);
				value = (String) properties.get(key);
				System.out.println("Value of value : " + value);
				String[] strs = value.split(":");
				String pwd = strs[0];
				String[] roleArr = strs[1].split(",");
				List<String> userRoleList = new ArrayList<String>();
				users.put(key, pwd);
				for(String s: roleArr){
					if(!allRoles.contains(s)) {
						allRoles.add(s);
					}
					userRoleList.add(s);
				}
				userRoles.put(key, userRoleList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("******************users and pwds=" + users);
		System.out.println("******************allRoles=" + allRoles);
		System.out.println("******************userRoles=" + userRoles);
	}

	/**
	 * @param userName
	 * @return 1 - success; 0 - fail
	 */
	public static List<String> login(String userName, String password) {
		List<String> roles = null;
		String pwd = users.get(userName);
		if(null != pwd && pwd.equals(password)){
			roles = getGroupsForUser(userName);
		}
		return roles;
	}
	
	public static List<String> getUsers(String excludedUser) {
		List<String> result = new ArrayList<String>();
		for(String str: users.keySet()){
			if(!str.equalsIgnoreCase(excludedUser)) {
				result.add(str);
			}
		}
		return result;
	}
	
	public static List<String> getGroupsForUser(String userName){
		return userRoles.get(userName);
	}
	
	public static boolean existsUser(String userName) {
		return users.containsKey(userName);
	}

	public static boolean existsGroup(String groupId) {
		return allRoles.contains(groupId);
	}
}
