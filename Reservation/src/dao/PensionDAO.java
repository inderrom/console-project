package dao;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class PensionDAO {
	private static PensionDAO instance=null;
	private PensionDAO() {};
	public static PensionDAO getInstance() {
		if(instance ==null) instance= new PensionDAO();
		return instance;
	}
	
	JDBCUtil jdbc = JDBCUtil.getInstance();
	
	//펜션예약
	public List<Map<String, Object>> pensionResvion(int startDate, int endDate) {
		String sql=
				   " SELECT A.* "
	             + "   FROM PENSION A "  
	             + "  WHERE (SELECT COUNT(*) "  
		              + "      FROM PENSION_BOOKING B "  
		              + "     WHERE (B.PB_STARTDATE <= '"+startDate+ "' AND B.PB_ENDDATE >= '"+startDate + "') "
		              + "       AND (B.PB_STATE = 1 OR B.PB_STATE = 2) "
		              + "		AND B.P_NAME=A.P_NAME)=0 "
	             + "   	AND (SELECT COUNT(*) "  
			     + "   		   FROM PENSION_BOOKING B "  
				 + "  		  WHERE (B.PB_STARTDATE <= '"+endDate+ "' AND B.PB_ENDDATE >= '"+endDate + "')"
				 + "    		AND (B.PB_STATE = 1 OR B.PB_STATE = 2) "
				           + "	AND B.P_NAME=A.P_NAME)=0 "
	             + "   	AND "    
		              + "   (SELECT COUNT(*) "  
		              + "      FROM PENSION_BOOKING B "  
		              + "     WHERE (B.PB_STARTDATE >= '"+startDate + "'AND B.PB_ENDDATE<= '"+endDate+"')"
		              + "       AND (B.PB_STATE = 1 OR B.PB_STATE = 2)"
		              + "		AND B.P_NAME=A.P_NAME)=0  ";
		return jdbc. selectList(sql);
	}
	
	public List<Map<String, Object>> pensionResvion1(int startDate, int endDate) {
		String sql = "SELECT * FROM A LEFT JOIN B ON ( AS B, PB_TOTAL FROM PENSION_BOOKING";
		return jdbc.selectList(sql);
	}
	public List<Map<String, Object>> pensionInfo(String penName) {
		String sql = " SELECT * FROM PENSION WHERE P_NAME = '" + penName + "' ";
		return jdbc.selectList(sql);
	}
	
	// 시퀀스
	public int pension_payment(List<Object> param) {
		String sql = " INSERT INTO PENSION_BOOKING "
				+ " (PB_NO, MEM_ID, P_NAME, PB_STARTDATE, PB_ENDDATE, PB_TOTAL, PB_STATE) "
				+ " VALUES(SEQ_PB_NO.NEXTVAL, ?, ?, TO_DATE(?), TO_DATE(?), ? , ?) ";// t\순서
		return jdbc.update(sql, param);
	}
	
	public int card_insert(List<Object> param) {
		String sql = "INSERT INTO CARD "
				+ " VALUES(SEQ_CARD.NEXTVAL, ?, ? ,?)";
		return jdbc.update(sql, param);
	}
	
	public List<Map<String, Object>> card_bring(Object object) {
		String sql = "SELECT * FROM CARD WHERE MEM_ID = '" + object + "'";
		return jdbc.selectList(sql);
	}

	public List<Map<String, Object>> card_Manager(Object object) {
		String sql = "SELECT * FROM CARD WHERE MEM_ID = '" + object + "'";
		return jdbc.selectList(sql);
	}
	public Map<String, Object> cardUse(Object object, Object name) {
		String sql = "SELECT CARD_NUMBER FROM CARD WHERE CARD_NUMBER = '" + object + "' AND MEM_ID = '" + name + "'";
		return jdbc.selectOne(sql);
	}
	public Map<String, Object> getMile(Object object) {
		String sql = "SELECT MEM_MILE FROM MEMBER WHERE MEM_ID = '" + object + "'";
		return jdbc.selectOne(sql);
	}
	public int useMile(int myMile, Object object) {
		String sql = "UPDATE MEMBER SET MEM_MILE = '" + myMile + "' WHERE MEM_ID = '" + object + "'";
		return jdbc.update(sql);
	}
	
	public List<Map<String, Object>> selectPension() {
		String sql="SELECT PB_STARTDATE, PB_ENDDATE, PB_NO FROM PENSION_BOOKING WHERE (PB_STATE = 1 OR PB_STATE = 2)";
		return jdbc.selectList(sql);
	}
	
	public int udatePensionState(List<Object> param) {
		String sql="UPDATE PENSION_BOOKING SET PB_STATE = ? WHERE PB_NO = ? ";
		return jdbc.update(sql, param);
	}
}
