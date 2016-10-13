package com.neosoft.assistant.kq.model;

public class UserInfo {
	private final Integer internalID;
	private final String username;
	private final String password;
	private final String email;

	public UserInfo(Integer internalID, String username, String password, String email) {
		super();
		this.internalID = internalID;
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public Integer getInternalID() {
		return internalID;
	}
}
