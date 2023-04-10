package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class AdminDAO {
	private static AdminDAO instance = null;
	private AdminDAO() {}
	public static AdminDAO getInstance() {
		if(instance == null) instance = new AdminDAO();
		return instance;
	}
	
	JDBCUtil jdbc = JDBCUtil.getInstance();
	
	public List<Map<String, Object>> getc_salist() {
		String sql = "SELECT CB_NO,C_NAME,(CB_ENDDATE - CB_STARTDATE) AS 일수,CB_TOTAL,CB_STATE FROM CAR_BOOKING  ORDER BY CB_NO"; 
		return jdbc.selectList(sql);
	}
	
	public List<Map<String, Object>> getp_salist() {
		String sql = "SELECT PB_NO,P_NAME, (PB_ENDDATE - PB_STARTDATE) AS 일수,PB_TOTAL,PB_STATE FROM PENSION_BOOKING ";
		return jdbc.selectList(sql);
	}
	
	
	public  Map<String, Object> getc_sales() {
		String sql = "SELECT SUM(CB_TOTAL) AS 차량매출액 FROM CAR_BOOKING WHERE CB_STATE !=3 ";
		return jdbc.selectOne(sql);
	}
	
	public  Map<String, Object> getp_sales() {
		String sql = "SELECT SUM(PB_TOTAL) AS 숙소매출액 FROM PENSION_BOOKING WHERE PB_STATE !=3";
		return jdbc.selectOne(sql);
	}
	
	public List<Map<String, Object>> getiq_bolist(List<Object> param) {
		String sql = "SELECT BO_NO,BO_WRITER,BO_TITLE,BO_DATE FROM BOARD WHERE BO_TYPE = ? ORDER BY BO_NO DESC";
		return jdbc.selectList(sql,param);
	}
	
//	public List<Map<String, Object>> getiq_bolist(List<Object> param) {
//		String sql = "select  BO_NO,BO_WRITER,BO_TITLE,BO_DATE "
//					+ "FROM ( SELECT ROWNUM NUM, B.* "
//							 + "FROM (SELECT *"
//							 		 + "FROM BOARD ORDER BY BO_DATE DESC)B)"
//					+ "WHERE NUM BETWEEN ? AND ?"
//					 + "AND BO_TYPE = 1";
//		return jdbc.selectList(sql,param);
//	}
//	public List<Map<String, Object>> getiq_bolist(List<Object> param) {
//	String sql=" SELECT B. * FROM "
//            + "(SELECT ROWNUM AS RN, A. *  "
//            + "FROM BOARD A "
//            + "WHERE BO_TYPE =1 ORDER BY A.BO_NO DESC) B  "
//            + "WHERE B.RN <= ? AND B.RN > ?";
//	return jdbc.selectList(sql,param);
//	}
	
	public List<Map<String, Object>> getrs_info(List<Object> param) {
		String sql = "SELECT * FROM BOARD WHERE BO_TYPE = 1 AND BO_NO = ?";
		return jdbc.selectList(sql,param);
	}
	
	public int getiq_answer(List<Object> param) {
		String sql = "INSERT INTO REPLY(RE_NO,BO_NO,RE_WRITER,RE_CONTENT,RE_DATE) "
					+ "VALUES(SEQ_REPLY.NEXTVAL, ?, 'admin', ?, SYSDATE)";
		return jdbc.update(sql, param);
	}
	public List<Map<String, Object>> getiq_relist(List<Object>param) {
		String sql = "SELECT RE_NO, RE_WRITER,RE_CONTENT,RE_DATE FROM REPLY WHERE BO_NO =? ORDER BY RE_NO DESC";
		return jdbc.selectList(sql,param);
	}
	public int getiq_anupdate(List<Object> param) {
		String sql = "UPDATE REPLY SET RE_CONTENT  = ? WHERE RE_NO = ? ";
		return jdbc.update(sql, param);
	}
	public int getiq_andelete(List<Object> param) {
		String sql = "DELETE FROM REPLY WHERE RE_NO = ?";
		return jdbc.update(sql, param);
	}
	public int getiq_delete(List<Object> param) {
		String sql = "DELETE FROM BOARD WHERE BO_NO = ?";
		return jdbc.update(sql, param);
	}

	public List<Map<String, Object>> getrvc_bolist(List<Object> param) {
		String sql = "SELECT BO_NO,BO_WRITER,BO_TITLE,BO_DATE FROM BOARD WHERE BO_TYPE = ? ORDER BY BO_NO DESC";
		return jdbc.selectList(sql,param);
	}
	public List<Map<String, Object>> getrsc_info(List<Object> param) {
		String sql = "SELECT * FROM BOARD WHERE BO_TYPE = 2 AND BO_NO = ?";
		return jdbc.selectList(sql,param);
	}
	public List<Map<String, Object>> getrvc_relist(List<Object> param) {
		String sql = "SELECT RE_NO, RE_WRITER,RE_CONTENT,RE_DATE FROM REPLY WHERE BO_NO =? ORDER BY RE_NO DESC ";
		return jdbc.selectList(sql,param);
		
	}
	public int getrvc_answer(List<Object> param) {
		String sql = "INSERT INTO REPLY(RE_NO,BO_NO,RE_WRITER,RE_CONTENT,RE_DATE) "
				+ "VALUES(SEQ_REPLY.NEXTVAL, ?, 'admin', ?, SYSDATE)";
	return jdbc.update(sql, param);
	}
	public int getrvc_anupdate(List<Object> param) {
		String sql = "UPDATE REPLY SET RE_CONTENT  = ? WHERE RE_NO = ? ";
		return jdbc.update(sql, param);
	
	}
	public int getrvc_andelete(List<Object> param) {
		String sql = "DELETE FROM REPLY WHERE RE_NO = ?";
		return jdbc.update(sql, param);
	}
	public int getrvc_delete(List<Object> param) {
		String sql = "DELETE FROM BOARD WHERE BO_NO = ?";
		return jdbc.update(sql, param);
	}
	public List<Map<String, Object>> getrvp_bolist(List<Object> param) {
		String sql = "SELECT BO_NO,BO_WRITER,BO_TITLE,BO_DATE FROM BOARD WHERE BO_TYPE = ? ORDER BY BO_NO DESC";
		return jdbc.selectList(sql,param);
	}
	public List<Map<String, Object>> getrsp_info(List<Object>param) {
		String sql = "SELECT * FROM BOARD WHERE BO_TYPE = 3 AND BO_NO = ?";
		return jdbc.selectList(sql,param);
	}
	public List<Map<String, Object>> getrvp_relist(List<Object> param) {
		String sql = "SELECT RE_NO, RE_WRITER,RE_CONTENT,RE_DATE FROM REPLY WHERE BO_NO =? ORDER BY RE_NO DESC ";
		return jdbc.selectList(sql,param);
	
	}
	public int getrvp_answer(List<Object> param) {
		String sql = "INSERT INTO REPLY(RE_NO,BO_NO,RE_WRITER,RE_CONTENT,RE_DATE) "
				+ "VALUES(SEQ_REPLY.NEXTVAL, ?, 'admin', ?, SYSDATE)";
	return jdbc.update(sql, param);
	}
	public int getrvp_anupdate(List<Object> param) {
		String sql = "UPDATE REPLY SET RE_CONTENT  = ? WHERE RE_NO = ? ";
		return jdbc.update(sql, param);
	}
	public int getrvp_andelete(List<Object> param) {
		String sql = "DELETE FROM REPLY WHERE RE_NO = ?";
		return jdbc.update(sql, param);
	}
	public int getrvp_delete(List<Object> param) {
		String sql = "DELETE FROM BOARD WHERE BO_NO = ?";
		return jdbc.update(sql, param);
	}
	public List<Map<String, Object>> getiq_bolist0() {
		String sql="SELECT ROWNUM AS RN, A. *  "
                   +"FROM BOARD A  ORDER BY A.BO_NO DESC";
		return jdbc.selectList(sql);
	}
	
}
