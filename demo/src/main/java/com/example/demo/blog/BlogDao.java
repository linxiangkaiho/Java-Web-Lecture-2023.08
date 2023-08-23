package com.example.demo.blog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@Component
public class BlogDao {
//	@Value("${spring.datasource.url}") private String url;
//	@Value("${spring.datasource.username}") private String username;
//	@Value("${spring.datasource.password}") private String password;
	
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String username = "hmuser";
	private String password = "hmpass";
	
	Connection myConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public Blog getBlog(int bid) {
		Connection conn = myConnection();
		String sql = "select * from blog where bid=?";
		Blog blog = null;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bid);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bid = rs.getInt(1);
				String penName = rs.getString(2);
				String title = rs.getString(3);
				String content = rs.getString(4);
				String modTime = rs.getString(5);
				int viewCount = rs.getInt(6);
				int isDeleted = rs.getInt(7);
				blog = new Blog(bid, penName, title, content,
						LocalDateTime.parse(modTime.substring(2, 16).replace(" ", "T")),
						viewCount, isDeleted);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return blog;
	}
	
	public void insertBlog(Blog blog) {
		Connection conn = myConnection();
		String sql = "insert into blog(penName, title, content) values (?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, blog.getPenName());
			pstmt.setString(2, blog.getTitle());
			pstmt.setString(3, blog.getContent());
			pstmt.executeUpdate();			
			pstmt.close(); conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Blog> getBlogList(String field, String query) {
		Connection conn = myConnection();
		String sql = "select * from blog where " + field + " like ? and isDeleted=0 "
				+ " order by bid ";
		List<Blog> list = new ArrayList<>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + query + "%");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int bid = rs.getInt(1);
				String penName = rs.getString(2);
				String title = rs.getString(3);
				String content = rs.getString(4);
				String modTime = rs.getString(5);
				int viewCount = rs.getInt(6);
				int isDeleted = rs.getInt(7);
				Blog blog = new Blog(bid, penName, title, content,
						LocalDateTime.parse(modTime.substring(0, 19).replace(" ", "T")),
						viewCount, isDeleted);
				list.add(blog);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
		
	}
	
	public void updateBlog(Blog blog) {
		Connection conn = myConnection();
		String sql = "update blog set penName=?, title=?, content=?, modTime=current_timestamp "
				+ " where bid=?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, blog.getPenName());
			pstmt.setString(2, blog.getTitle());
			pstmt.setString(3, blog.getContent());
			pstmt.setInt(4, blog.getBid());
			pstmt.executeUpdate();			
			pstmt.close(); conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteBlog(int bid) {
		Connection conn = myConnection();
		String sql = "update blog set isDeleted=1 where bid=?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bid);
			pstmt.executeUpdate();			
			pstmt.close(); conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void increaseViewCount(int bid) {
		Connection conn = myConnection();
		String sql = "update blog set viewCount= viewCount + 1 where bid=?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bid);
			pstmt.executeUpdate();			
			pstmt.close(); conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
}