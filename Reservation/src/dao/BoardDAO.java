package dao;

import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;

public class BoardDAO {
	
	private static BoardDAO instance = null;
	private BoardDAO() {}
	public static BoardDAO getInstance() {
		if(instance == null) instance = new BoardDAO();
		return instance;
	}
	
	JDBCUtil jdbc = JDBCUtil.getInstance();
	
	public int inqInsert(List<Object>param) {
		String sql = "INSERT INTO BOARD (BO_NO, BO_TYPE, BO_WRITER, BO_TITLE, BO_CONTENT, BO_DATE) "
				+ " VALUES (BO_SEQ.NEXTVAL, 1, ?, ?, ?, SYSDATE) ";
		return jdbc.update(sql,param);
	}
	
	
	public List<Map<String, Object>> getInqList(List<Object> param) {
		String sql = "SELECT * FROM BOARD WHERE BO_TYPE = 1 AND BO_WRITER = ? ORDER BY BO_DATE DESC";
		return jdbc.selectList(sql, param);
	}
	
	public List<Map<String, Object>> inqDelList(List<Object> param) {
		String sql = "SELECT * FROM BOARD WHERE BO_TYPE = 1 AND BO_NO = ? ORDER BY BO_DATE DESC";
		return jdbc.selectList(sql, param);
	}
	
	public List<Map<String, Object>> inqSimpleList(List<Object> param) {
		String sql = "SELECT * FROM BOARD WHERE BO_TYPE = 1 AND BO_WRITER = ? ORDER BY BO_NO DESC";
		return jdbc.selectList(sql, param);
	}
	
	
	public int inqModify(List<Object> param) {
		String sql = "UPDATE BOARD SET BO_TITLE = ?, BO_CONTENT = ? WHERE BO_NO = ?";
		return jdbc.update(sql,param);
	}
	
	
	public int inqDelete(List<Object> param) {
		String sql = "DELETE BOARD WHERE BO_NO = ?";
		return jdbc.update(sql, param);
	}
	
	public  List<Map<String, Object>> carRentList(List<Object> param) {
		String sql = "SELECT * FROM CAR_BOOKING WHERE MEM_ID = ? AND CB_STATE = ?";
		return jdbc.selectList(sql, param);
	}
	
	public List<Map<String, Object>> carRentRow(List<Object> param) {
		String sql = "SELECT * FROM CAR_BOOKING WHERE CB_NO = ?";
		return jdbc.selectList(sql, param);
	}
	
	public int carRRInsert(List<Object> param) {
		String sql = "INSERT INTO BOARD (BO_NO, BO_TYPE, BO_WRITER, BO_TITLE, BO_CONTENT, BO_STARS, BO_RNAME, R_NO, BO_DATE) "
				+ " VALUES (BO_SEQ.NEXTVAL, 2, ?, ?, ?, ?, ?, ?, SYSDATE) ";
		return jdbc.update(sql,param);
	}
	public List<Map<String, Object>> carRRList() {
		String sql = "SELECT * FROM BOARD WHERE BO_TYPE = 2";
		return jdbc.selectList(sql);
	}
	
	public List<Map<String, Object>> carRRList(List<Object> param) {
		String sql = "SELECT * FROM BOARD WHERE BO_TYPE = 2 AND BO_RNAME = ?";
		return jdbc.selectList(sql, param);
	}
	
	public List<Map<String, Object>> carRRDList(List<Object> param) {
		String sql = "SELECT * FROM BOARD WHERE BO_TYPE = 2 AND BO_NO = ?";
		return jdbc.selectList(sql, param);
	}
	
	public List<Map<String, Object>> carRRBMList(List<Object> param) {
		String sql = "SELECT * FROM BOARD WHERE BO_TYPE = 2 AND BO_WRITER = ?";
		return jdbc.selectList(sql,param);
	}
	
	public int carRRModify(List<Object> param) {
		String sql = "UPDATE BOARD SET BO_TITLE = ?, BO_STARS =?, BO_CONTENT = ? WHERE BO_NO = ?";
		return jdbc.update(sql,param);
	}
	
	public List<Map<String, Object>> carRList(List<Object> param) {
		String sql = "SELECT * FROM BOARD WHERE BO_TYPE = 2 AND BO_WRITER = ?";
		return jdbc.selectList(sql, param);
	}
	
	public int carRRDelete(List<Object> param) {
		String sql = "DELETE BOARD WHERE BO_NO = ?";
		return jdbc.update(sql, param);
	}
	public List<Map<String, Object>> replyList(List<Object> param) {
		String sql = "SELECT * FROM REPLY WHERE BO_NO = ? ORDER BY RE_DATE DESC";
		return jdbc.selectList(sql, param);
	}
	public List<Map<String, Object>> inqDetailOne(List<Object> param) {
		String sql = "SELECT * FROM BOARD WHERE BO_NO = ?";
		return jdbc.selectList(sql, param);
	}
	
	public List<Map<String, Object>> inqReList(List<Object> param) {
		String sql = "SELECT * FROM REPLY WHERE BO_NO = ? ORDER BY RE_DATE DESC";
		return jdbc.selectList(sql, param);
	}
	
	public int inqReDelete(List<Object> param) {
		String sql = "DELETE REPLY WHERE BO_NO = ?";
		return jdbc.update(sql, param);
	}
	
	public List<Map<String, Object>> carRRRList(List<Object> param) {
		String sql = "SELECT * FROM REPLY WHERE BO_NO = ? ORDER BY RE_DATE DESC";
		return jdbc.selectList(sql, param);
	}
	public int reInsert(List<Object> param) {
		String sql = "INSERT INTO REPLY (RE_NO, BO_NO, RE_WRITER, RE_CONTENT, RE_DATE) "
				+ "VALUES(RE_SEQ.NEXTVAL, ?, ?, ?, SYSDATE) ";
		return jdbc.update(sql, param);
	}
	
	public List<Map<String, Object>> carOlnyRRList(List<Object> param) {
		String sql = "SELECT B. * FROM BOARD A, REPLY B "
				+ "WHERE A.BO_NO = B.BO_NO "
				+ "AND A.BO_TYPE = 2 "
				+ "AND B.RE_WRITER = ?";
		return jdbc.selectList(sql, param);
	}
	
	public List<Map<String, Object>> carReRList(List<Object> param) {
		String sql = "SELECT A.* FROM BOARD A, REPLY B "
				+ " WHERE A.BO_TYPE = 2 "
				+ " AND A.BO_NO = B.BO_NO "
				+ " AND B.RE_NO = ?";
		return jdbc.selectList(sql, param);
	}
	
	public List<Map<String, Object>> replyOne(List<Object> param) {
		String sql = "SELECT * FROM REPLY WHERE RE_NO = ? ORDER BY RE_DATE DESC";
		return jdbc.selectList(sql, param);
	}
	
	public int modReply(List<Object> param) {
		String sql = "UPDATE REPLY SET RE_CONTENT = ? WHERE RE_NO = ?";
		return jdbc.update(sql, param);
	}
	
	public int deleteRe(List<Object> param) {
		String sql = "DELETE REPLY WHERE RE_NO = ?";
		return jdbc.update(sql, param);
	}
	
	public List<Map<String, Object>> carRRTotalList() {
		String sql = "SELECT * FROM BOARD WHERE BO_TYPE = 2";
		return jdbc.selectList(sql);
	}
	
	
	public List<Map<String, Object>> penRentList(List<Object> param) {
		String sql = "SELECT * FROM PENSION_BOOKING WHERE MEM_ID = ? AND PB_STATE = ?";
		return jdbc.selectList(sql, param);
	}
	
	public List<Map<String, Object>> penRentRow(List<Object> param) {
		String sql = "SELECT * FROM PENSION_BOOKING WHERE PB_NO = ?";
		return jdbc.selectList(sql, param);
	}
	
	public int penRRInsert(List<Object> param) {
		String sql = "INSERT INTO BOARD (BO_NO, BO_TYPE, BO_WRITER, BO_TITLE, BO_CONTENT, BO_STARS, BO_RNAME, R_NO, BO_DATE) "
				+ " VALUES (BO_SEQ.NEXTVAL, 3, ?, ?, ?, ?, ?, ?, SYSDATE) ";
		return jdbc.update(sql,param);
	}
	
	
	public List<Map<String, Object>> penRRTotalList() {
		String sql = "SELECT * FROM BOARD WHERE BO_TYPE = 3";
		return jdbc.selectList(sql);
	}
	
	public List<Map<String, Object>> penRRList(List<Object> param) {
		String sql = "SELECT * FROM BOARD WHERE BO_TYPE = 3 AND BO_RNAME = ?";
		return jdbc.selectList(sql, param);
	}
	
	public List<Map<String, Object>> penRRDList(List<Object> param) {
		String sql = "SELECT * FROM BOARD WHERE BO_TYPE = 3 AND BO_NO = ?";
		return jdbc.selectList(sql, param);
	}
	
	public List<Map<String, Object>> pentRRBMList(List<Object> param) {
		String sql = "SELECT * FROM BOARD WHERE BO_TYPE = 3 AND BO_WRITER = ?";
		return jdbc.selectList(sql,param);
	}
	
	public List<Map<String, Object>> pentRRDList(List<Object> param) {
		String sql = "SELECT * FROM BOARD WHERE BO_TYPE = 3 AND BO_NO = ?";
		return jdbc.selectList(sql, param);
	}
	public List<Map<String, Object>> penOlnyRRList(List<Object> param) {
		String sql = "SELECT B. * FROM BOARD A, REPLY B "
				+ "WHERE A.BO_NO = B.BO_NO "
				+ "AND A.BO_TYPE = 3 "
				+ "AND B.RE_WRITER = ?";
		return jdbc.selectList(sql, param);
	}
	public List<Map<String, Object>> allList() {
		String sql = "SELECT * FROM BOARD";
		return jdbc.selectList(sql);
	}
	public List<Map<String, Object>> rNOList(List<Object> param) {
		String sql = "SELECT * FROM BOARD WHERE R_NO = ?";
		return jdbc.selectList(sql, param);
	}
	
	public int milePlus(List<Object> param) {
		String sql = "UPDATE MEMBER SET MEM_MILE = ? WHERE MEM_ID = ?";
		return jdbc.update(sql,param);
	}
	
	public List<Map<String, Object>> memMile(List<Object> param) {
		String sql = "SELECT * FROM MEMBER WHERE MEM_ID = ?";
		return jdbc.selectList(sql, param);
	}
	
	public List<Map<String, Object>> carNameList() {
		String sql = "SELECT DISTINCT C_NAME FROM CAR";
		return jdbc.selectList(sql);
	}
	
	
	public List<Map<String, Object>> torf(List<Object> param) {
		String sql = "SELECT CASE WHEN (BO_DATE +30 > SYSDATE) THEN 'A' ELSE 'B' END AS V_DATE FROM BOARD WHERE BO_NO =?";
		return jdbc.selectList(sql, param);
	}
	
	public List<Map<String, Object>> penNameList() {
		String sql = "SELECT DISTINCT P_NAME FROM PENSION";
		return jdbc.selectList(sql);
	}
	
	public List<Map<String, Object>> pagee(List<Object> param) {
		String sql="SELECT B. * "
				+ "FROM (SELECT ROWNUM AS RN, A.* "
				+ "FROM (SELECT * FROM BOARD A WHERE A.BO_TYPE = 1 AND A.BO_WRITER = ? ORDER BY A.BO_DATE DESC)A) B "
				+ "WHERE B.RN <= ? AND B.RN > ?";
		return jdbc.selectList(sql,param);
	}

	
	public List<Map<String, Object>> pagee1(List<Object> param) {
		String sql=" SELECT B. * "
				+ "FROM (SELECT ROWNUM AS RN, A. * "
				+ "FROM (SELECT * FROM CAR_BOOKING A WHERE A.MEM_ID = ? AND A.CB_STATE = ? ORDER BY A.CB_NO DESC)A) B  "
				+ "WHERE B.RN <= ? AND B.RN > ?";
		return jdbc.selectList(sql,param);
	}
	
	public List<Map<String, Object>> page2(List<Object> param) {
		String sql=" SELECT B. * "
				+ "FROM (SELECT ROWNUM AS RN, A. * "
				+ "FROM (SELECT * FROM BOARD A WHERE A.BO_TYPE = 2 ORDER BY A.BO_DATE DESC)A) B  "
				+ "WHERE B.RN <= ? AND B.RN > ?";
		return jdbc.selectList(sql,param);
	}
	
	public List<Map<String, Object>> pagee2(List<Object> param) {
		String sql=" SELECT B. * "
				+ "FROM (SELECT ROWNUM AS RN, A. *  "
				+ "FROM (SELECT * FROM PENSION_BOOKING A WHERE A.MEM_ID = ? AND A.PB_STATE = ? ORDER BY A.PB_NO)A) B  "
				+ "WHERE B.RN <= ? AND B.RN > ?";
		return jdbc.selectList(sql,param);
	}
	
	public List<Map<String, Object>> page3(List<Object> param) {
		String sql=" SELECT B. * "
				+ "FROM (SELECT ROWNUM AS RN, A. * "
				+ "FROM (SELECT * FROM BOARD A WHERE A.BO_TYPE = 3 ORDER BY A.BO_DATE DESC)A) B  "
				+ "WHERE B.RN <= ? AND B.RN > ?";
		return jdbc.selectList(sql,param);
	}

	
	
	
						public List<Map<String, Object>> page(List<Object> param) {
					//		String sql = " SELECT B. * "
					//				+ " FROM "
					//				+ " (SELECT ROWNUM AS RN, A. * FROM CAR_BOOKING A ORDER BY A.MEM_ID ASC) B "
					//				+ " WHERE B.RN <=5 AND B.RN > 0 ";
							
							String sql=" SELECT B. * FROM "
									+ "(SELECT ROWNUM AS RN, A. *  "
									+ "FROM CAR_BOOKING A  ORDER BY A.CB_NO DESC) B  "
									+ "WHERE B.RN <= ? AND B.RN > ?";
							return jdbc.selectList(sql,param);
						}
						
						
						
						
					
						
	
	
	
	
	
	
	


	
	}
