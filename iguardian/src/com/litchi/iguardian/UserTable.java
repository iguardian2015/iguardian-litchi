package com.litchi.iguardian;

public class UserTable {
	// private long id;
	private long user_id;
	// private String comment;
	private String user_name;
	private String school_db;
	private String user_password;

	/*
	 * public long getId() { return id; }
	 */
	public long get_user_id() {
		return user_id;
	}

	/*
	 * public void setId(long id) { this.id = id; }
	 */
	public void set_user_id(long user_id) {
		this.user_id = user_id;
	}

	/*
	 * public String getComment() { return comment; }
	 */
	public String get_user_name() {
		return user_name;
	}

	public String get_school_db() {
		return school_db;
	}
	
	public String get_user_password() {
		return user_password;
	}

	/*
	 * public void setComment(String comment) { this.comment = comment; }
	 */
	public void set_user_name(String user_name) {
		this.user_name = user_name;
	}

	public void set_school_db(String school_db) {
		this.school_db = school_db;
	}
	
	public void set_user_password(String user_password) {
		this.user_password = user_password;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return user_name;
	}
}
