package com.litchi.iguardian;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UserDataSource {

	// Database fields
	public SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	// private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
	// MySQLiteHelper.COLUMN_COMMENT };

	private String[] user_columns = { MySQLiteHelper.user_id,
			MySQLiteHelper.user_name, MySQLiteHelper.school_db, MySQLiteHelper.user_password };

	public UserDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public UserTable createComment(String user_name, String school_db, String user_password) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.user_name, user_name);
		values.put(MySQLiteHelper.school_db, school_db);
		values.put(MySQLiteHelper.user_password, user_password);
		long insertId = database
				.insert(MySQLiteHelper.user_table, null, values);

		Cursor cursor = database.query(MySQLiteHelper.user_table, user_columns,
				MySQLiteHelper.user_id + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		UserTable newComment = cursorToComment(cursor);
		cursor.close();
		return newComment;
	}

	public void deleteComment(UserTable comment) {
		long user_id = comment.get_user_id();
		System.out.println("Comment deleted with id: " + user_id);
		database.delete(MySQLiteHelper.user_table, MySQLiteHelper.user_id
				+ " = " + user_id, null);
		// database.delete(MySQLiteHelper.user_table,null, null);
	}

	public void deletetable() {

		System.out.println("usertable data deleted");
		database.execSQL("delete from " + MySQLiteHelper.user_table);
		database.close();
	}

	public List<String> get_school_db() {
		List<String> get_school_db = new ArrayList<String>();

		Cursor cursor = database.query(MySQLiteHelper.user_table, user_columns,
				null, null, null, null, null);
		// Cursor cursor =
		// database.rawQuery("select user_name from user_table",null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			// UserTable comment = cursorToComment(cursor);
			// String user_school = get_user_School(cursor);
			String school_db = cursor.getString(2);
			get_school_db.add(school_db.toString());
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return get_school_db;
	}

	public List<String> get_user_name() {
		List<String> get_user_name = new ArrayList<String>();

		Cursor cursor = database.query(MySQLiteHelper.user_table, user_columns,
				null, null, null, null, null);
		// Cursor cursor =
		// database.rawQuery("select user_name from user_table",null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			// UserTable comment = cursorToComment(cursor);
			// String user_school = get_user_School(cursor);
			String user_name = cursor.getString(1);
			get_user_name.add(user_name.toString());
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return get_user_name;
	}
	
	public List<String> get_user_password() {
		List<String> get_user_password = new ArrayList<String>();

		Cursor cursor = database.query(MySQLiteHelper.user_table, user_columns,
				null, null, null, null, null);
		// Cursor cursor =
		// database.rawQuery("select user_name from user_table",null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			// UserTable comment = cursorToComment(cursor);
			// String user_school = get_user_School(cursor);
			String user_password = cursor.getString(3);
			get_user_password.add(user_password.toString());
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return get_user_password;
	}

	private UserTable cursorToComment(Cursor cursor) {
		UserTable comment = new UserTable();
		comment.set_user_id(cursor.getLong(0));
		comment.set_user_name(cursor.getString(1));
		comment.set_school_db(cursor.getString(2));
		comment.set_user_password(cursor.getString(3));
		return comment;
	}
	/*
	 * private String get_user_School(Cursor cursor) { // UserTable user_school
	 * = new UserTable(); String user_school;
	 * //comment.set_user_id(cursor.getLong(0));
	 * //comment.set_user_name(cursor.getString(1));
	 * user_school.get_user_school(cursor.getString(2)); return
	 * user_school.toString(); }
	 */
}
