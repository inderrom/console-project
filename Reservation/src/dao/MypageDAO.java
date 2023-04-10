package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class MypageDAO {
	private static MypageDAO instance = null;
	private MypageDAO () {};
	public static MypageDAO getInstance() {
		if(instance == null) instance = new MypageDAO();
		return instance;
	}
	
	JDBCUtil jdbc = JDBCUtil.getInstance();
	

	public List<Map<String, Object>> myPage(List<Object> param) {
		String sql = "SELECT MEM_ID, MEM_NAME, MEM_BIR, MEM_PHONE,"
				+ "MEM_ADDR, MEM_LICENSE, MEM_MILE FROM MEMBER WHERE MEM_ID = ?";
		return jdbc.selectList(sql, param);
	}
	
	public int updateInfo(List<Object> param) {	// 내정보 수정
		String sql = " UPDATE MEMBER SET MEM_PW = ?, MEM_PHONE = ?, "
				+ "MEM_ADDR = ?, MEM_LICENSE = ? WHERE MEM_ID = ?";
		return jdbc.update(sql, param);
	}
	
	public List<Map<String, Object>> pensionRecord(List<Object> param) {	// 팬션 대여 정보
		String sql = "SELECT * FROM PENSION_BOOKING WHERE MEM_ID = ? ORDER BY PB_STARTDATE ,PB_ENDDATE";
		return jdbc.selectList(sql, param);
	}
	
	public List<Map<String, Object>> carRecord(List<Object> param) {	// 차량 대여 정보
		String sql = "SELECT * FROM CAR_BOOKING WHERE MEM_ID = ?  ORDER BY CB_STARTDATE, CB_ENDDATE" ;
		return jdbc.selectList(sql, param);
	}
	
	public List<Map<String, Object>> pensionResInfo(List<Object> param) {	// 팬션 예약 정보
		String sql = "SELECT * FROM PENSION_BOOKING WHERE MEM_ID = ? AND PB_STATE = ? ORDER BY PB_STARTDATE ,PB_ENDDATE";
		return jdbc.selectList(sql, param);
	}
	
	public List<Map<String, Object>> carResInfo(List<Object> param) {	// 차량 예약 정보
		String sql = "SELECT * FROM CAR_BOOKING WHERE MEM_ID = ? AND CB_STATE = ? ORDER BY CB_STARTDATE, CB_ENDDATE" ;
		return jdbc.selectList(sql, param);
	}
	
	public int pensionCancle(Object object) {	// 팬션 예약 취소
		String sql = "UPDATE PENSION_BOOKING SET PB_STATE = " + 3 +" WHERE PB_NO = " + object + "";
		return jdbc.update(sql);
	}
//	UPDATE PENSION_BOOKING SET PB_STATE = 3 where PB_NO = 14;
	public int carCancle(Object object) {	// 차량 예약 취소
		String sql = "UPDATE CAR_BOOKING SET CB_STATE = " + 3 +" WHERE CB_NO = " + object + "";
		return jdbc.update(sql);
	}
	
	public List<Map<String, Object>> cardManage(Object object) {
		String sql = "SELECT * FROM CARD WHERE MEM_ID = '" + object + "'";
		return jdbc.selectList(sql);
	}
	
	public int card_add(List<Object> param) {	// 카드 추가
		String sql = "INSERT INTO CARD "
				+ " VALUES(SEQ_CARD.NEXTVAL, ?, ? ,?)";
		return jdbc.update(sql, param);
	}
	public int card_delete(Object object) {	// 카드 삭제
		String sql = "DELETE CARD WHERE CARD_NO = '" + object + "'";
		return jdbc.update(sql);
	}
//	public int cardCheck(String card) {
//		String sql = "SELECT CARD_NUMBER WHERE CARD_NO = '" + card + "'";
//		Map<String ,Object> card = jdbc.selectList(sql);
//		
//		
//		return jdbc.selectList(sql);
//		
//		
//	}
	
}
