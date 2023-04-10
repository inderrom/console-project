package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class MemberDAO {
	private static MemberDAO instance = null;
	private MemberDAO() {}
	public static MemberDAO getInstance() {
		if(instance == null) instance = new MemberDAO();
		return instance;
	}
	
	JDBCUtil jdbc = JDBCUtil.getInstance();
	
	public int signup(List<Object> param) {
		String sql = "INSERT INTO MEMBER (MEM_ID, MEM_PW, MEM_NAME, MEM_BIR,"
					+ " MEM_PHONE, MEM_ADDR, MEM_LICENSE, MEM_STATE, MEM_MILE) "
					+ " VALUES(?, ?, ?, TO_DATE(?), ?, ?, ?, 0, 0)";
		return jdbc.update(sql, param);
	}
	
	public Map<String, Object> userLogin(List<Object> param) {
		String sql = "SELECT * FROM MEMBER WHERE MEM_ID=? AND MEM_PW=?";
		return jdbc.selectOne(sql, param);
	}
	
	public Map<String, Object> adminLogin(List<Object> param) {
		String sql = "SELECT * FROM MEMBER WHERE MEM_ID=? AND MEM_PW=? AND MEM_STATE=?";
		return jdbc.selectOne(sql, param);
	}
	
	public int idCheck(String id) {// 아이디 중복 체크
		int status = 1;
		String sql = "SELECT MEM_ID FROM MEMBER WHERE MEM_ID = '" + id + "'";
		Map<String, Object> member = jdbc.selectOne(sql);
		
		if(member != null && member.get("MEM_ID").equals(id)){
			status = 0;
		}
		return status;
	}
	public Map<String, Object> admin(List<Object> param) {// 관리자, 회원 체크
		String sql = "SELECT MEM_STATE FROM MEMBER WHERE MEM_ID = ? AND MEM_PW = ?";
		return jdbc.selectOne(sql, param);
	}
	
	public Map<String, Object> findId(String name, String phone) {// 아이디 찾기
		String sql = "SELECT MEM_ID FROM MEMBER "
				+ "WHERE MEM_NAME = '" + name + "' AND MEM_PHONE = '" + phone + "'";
		return jdbc.selectOne(sql);
	}
	
	public Map<String, Object> findPw(String id, String name, String phone){// 비밀번호 찾기
		String sql = "SELECT MEM_PW FROM MEMBER "
				+ "WHERE MEM_ID = '" + id + "'" 
			+	"AND MEM_NAME = '" + name + "'"
			+	"AND MEM_PHONE = '" + phone + "'";
		return jdbc.selectOne(sql);
	}
	
	public int updatePw(String id, String pw) {// 패스워드 확인
		String sql = "UPDATE MEMBER "
				+	 "SET MEM_PW = '" + pw + "'" + 
					"WHERE MEM_ID = '" + id + "'";
		return jdbc.update(sql);
	}
	
}
