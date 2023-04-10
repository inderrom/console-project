package service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import controller.Controller;
import dao.MypageDAO;
import util.ScanUtil;
import util.SpaceUtil;
import util.View;

public class MypageService {
	private static MypageService instance = null;
	private MypageService() {};
	public static MypageService getInstance() {
		if(instance == null) instance = new MypageService();
		return instance;
	}
	
	MypageDAO mypageDAO = MypageDAO.getInstance();
	
	private boolean pwCheck(String pw) {	// pw 체크
		return Pattern.matches("^[a-zA-Z]{1}[a-zA-Z0-9_]{7,16}$", pw);
	}
	
	private boolean phoneCheck(String phone) {	// 휴대폰 번호 체크
		return Pattern.matches("^(\\d{3})(\\d{4})(\\d{4})$", phone);
	}
	
	public boolean addressCheck(String address) { // 신(구)주소, 도로명 주소
		return Pattern.matches("^[가-힣0-9\\s]*$", address);
	}
	
	public boolean cardName(String card) { // 카드 이름
		return Pattern.matches("^[가-힣0-9a-zA-Z]{2,8}$", card);
	}

	public boolean cardCheck(String card) { // 신용카드
		return Pattern.matches("^((5[1-5])([0-9]{2})((-|\\s)?[0-9]{4}){3})$", card);
	}
	
	public int myInfo() { // 내정보
		if (!(boolean) Controller.sessionStorage.get("login")) { // == !true
			System.out.println("로그인 되어있지 않습니다.");
			return View.HOME;
		}
		
		System.out.println("┌────────┬────────┬────────────┬────────────┬─────────────────────────┬──────┬────────────┐");
		System.out.printf("│%s│%s│%s│%s│%s│%s│%s│", 
				SpaceUtil.format("아이디", 8, 0), 
				SpaceUtil.format("이름", 8, 0),
				SpaceUtil.format("생년월일", 12, 0), 
				SpaceUtil.format("휴대폰 번호", 12, 0), 
				SpaceUtil.format("주소", 25, 0),
				SpaceUtil.format("면허증", 6, 0),
				SpaceUtil.format("마일리지", 12, 0));
			
		System.out.println("\n├────────┼────────┼────────────┼────────────┼─────────────────────────┼──────┼────────────┤");

		List<Object> param = new ArrayList<>();
		Map<String, Object> userInfo = (Map<String, Object>)Controller.sessionStorage.get("userInfo");
		String userId = (String) userInfo.get("MEM_ID");
		param.add(userId);
		
		List<Map<String, Object>> memberList = mypageDAO.myPage(param);
		for (Map<String, Object> row : memberList) {
			System.out.printf("│%s│%s│%s│%s│%s│%s│%s│", 
					SpaceUtil.format(row.get("MEM_ID"), 8, 0),
					SpaceUtil.format(row.get("MEM_NAME"), 8, 0), 
					SpaceUtil.dateFormat(row.get("MEM_BIR"), 12, 0),
					SpaceUtil.format(row.get("MEM_PHONE"), 12, 0), 
					SpaceUtil.format(row.get("MEM_ADDR"), 25, 0),
					SpaceUtil.format(row.get("MEM_LICENSE"), 6, 0),
					SpaceUtil.format(row.get("MEM_MILE"), 12, 0));
			System.out.println();
		}
		System.out.println("└────────┴────────┴────────────┴────────────┴─────────────────────────┴──────┴────────────┘");
		System.out.println("1.내정보 수정 2.뒤로가기");
		System.out.print("입력 >> ");
		int num = ScanUtil.nextInt();
		clear();
		switch (num) {
		case 1:
			return View.UPDATE_INFO;
		case 2:
			return View.MYPAGE;
		default:
			return View.MY_INFO;
		}
	}
	
	public int updateInfo() { // 내정보 수정
		clear();
		System.out.println("수정할 내용을 선택해 주세요");
		System.out.println("┌──────────────────────────────────────────────┐");
		System.out.println("│1.비밀번호 2.전화번호 3.주소 4.면허증 0.홈으로│");
		System.out.println("└──────────────────────────────────────────────┘");
		System.out.print("입력 >> ");
		switch (ScanUtil.nextInt()) {
		case 1:
			return View.UPDATE_PW;
		case 2:
			return View.UPDATE_PHONE;
		case 3:
			return View.UPDATE_ADDR;
		case 4:
			return View.UPDATE_LICENSE;
		case 0:
			return View.MAIN;
		default:
			return View.MY_INFO;
		}
	}
	
	Map<Integer, Object> pensionNumber = new HashMap<>();
	
	public String state(int state) {
		switch (state) {
		case 1: return "대여중";
		case 2: return "예약";
		case 3: return "예약취소";
		case 0: return "반납완료";
		default: return "";
		}
	}
	
	public void pensionList() {
		Map<String, Object> userInfo = (Map<String, Object>)Controller.sessionStorage.get("userInfo");
		List<Object> param = new ArrayList<>();
		param.add(userInfo.get("MEM_ID"));
		List<Map<String, Object>> reservation = mypageDAO.pensionRecord(param);
		
		int cnt = 1;
		if(reservation.size() != 0) {
			System.out.println("===================================== 숙소 대여 현황 ===================================");
			System.out.println("┌─────┬──────────────────┬────────────┬────────────┬────────────┬────────────┬──────────┐");
			System.out.printf("│%s│%s│%s│%s│%s│%s│%s│",
					SpaceUtil.format("번호", 5, 0),
					SpaceUtil.format("아이디", 18, 0),
					SpaceUtil.format("팬션 이름", 12, 0),
					SpaceUtil.format("입실 날짜", 12, 0),
					SpaceUtil.format("퇴실 날짜", 12, 0),
					SpaceUtil.format("가격", 12, 0),
					SpaceUtil.format("상태", 10, 0));
			for(Map<String, Object> re : reservation) {
				int cost = Integer.parseInt(String.valueOf(re.get("PB_TOTAL")).toString());
				String total = String.format("%,3d", cost);
				
				int pb_state = Integer.parseInt(re.get("PB_STATE").toString());
				String state = state(pb_state);
				System.out.println("\n├─────┼──────────────────┼────────────┼────────────┼────────────┼────────────┼──────────┤");
				System.out.printf("│%s│%s│%s│%s│%s│%s│%s│",
						SpaceUtil.format(cnt++ + "", 5, 0),
						SpaceUtil.format(re.get("MEM_ID"), 18, 0),
						SpaceUtil.format(re.get("P_NAME"), 12, 0),
						SpaceUtil.dateFormat(re.get("PB_STARTDATE"), 12, 0),
						SpaceUtil.dateFormat(re.get("PB_ENDDATE"), 12, 0),
						SpaceUtil.format(total + "원", 12, 1),
						SpaceUtil.format(state, 10, 0)
				);
			}
			System.out.println("\n└─────┴──────────────────┴────────────┴────────────┴────────────┴────────────┴──────────┘");
		}else {
			System.out.println("=============================== 숙소 대여 정보가 없습니다. =============================");
		}
	}
	
	public void pensionResList() {
		Map<String, Object> userInfo = (Map<String, Object>)Controller.sessionStorage.get("userInfo");
		List<Object> param = new ArrayList<>();
		param.add(userInfo.get("MEM_ID"));
		param.add(2);
		List<Map<String, Object>> reservation = mypageDAO.pensionResInfo(param);
		
		int cnt = 0;
		if(reservation.size() != 0) {
			System.out.println("===================================== 숙소 예약 현황 ===================================");
			System.out.println("┌─────┬──────────────────┬────────────┬────────────┬────────────┬────────────┬──────────┐");
			System.out.printf("│%s│%s│%s│%s│%s│%s│%s│",
					SpaceUtil.format("번호", 5, 0),
					SpaceUtil.format("아이디", 18, 0),
					SpaceUtil.format("팬션 이름", 12, 0),
					SpaceUtil.format("입실 날짜", 12, 0),
					SpaceUtil.format("퇴실 날짜", 12, 0),
					SpaceUtil.format("가격", 12, 0),
					SpaceUtil.format("상태", 10, 0));
			for(Map<String, Object> re : reservation) {
				pensionNumber.put(cnt++, re.get("PB_NO"));
				int cost = Integer.parseInt(String.valueOf(re.get("PB_TOTAL")).toString());
				String total = String.format("%,3d", cost);
				
				int pb_state = Integer.parseInt(re.get("PB_STATE").toString());
				String state = state(pb_state);
				System.out.println("\n├─────┼──────────────────┼────────────┼────────────┼────────────┼────────────┼──────────┤");
				System.out.printf("│%s│%s│%s│%s│%s│%s│%s│",
						SpaceUtil.format(cnt + "", 5, 0),
						SpaceUtil.format(re.get("MEM_ID"), 18, 0),
						SpaceUtil.format(re.get("P_NAME"), 12, 0),
						SpaceUtil.dateFormat(re.get("PB_STARTDATE"), 12, 0),
						SpaceUtil.dateFormat(re.get("PB_ENDDATE"), 12, 0),
						SpaceUtil.format(total + "원", 12, 1),
						SpaceUtil.format(state, 10, 0)
				);
			}
			System.out.println("\n└─────┴──────────────────┴────────────┴────────────┴────────────┴────────────┴──────────┘");
		}else {
			System.out.println("=============================== 숙소 예약 정보가 없습니다. =============================");
		}
	}
	
	Map<Integer, Object> carNumber = new HashMap<>();
	
	public void carList() {
		Map<String, Object> userInfo = (Map<String, Object>)Controller.sessionStorage.get("userInfo");
		List<Object> param = new ArrayList<>();
		param.add(userInfo.get("MEM_ID"));
		List<Map<String, Object>> carReservation = mypageDAO.carRecord(param);
		if(carReservation.size() != 0) {
			System.out.println("======================================================= 차량 대여 기록 =======================================================");
			System.out.println("┌─────────┬─────────┬──────────────────┬────────────┬────────────┬────────────┬────────────┬────────────┬──────────┬──────────┐");
			System.out.printf("│%s│%s│%s│%s│%s│%s│%s│%s│%s│%s│", 
					SpaceUtil.format("번호", 9, 0), 
					SpaceUtil.format("구분 번호", 9, 0),
					SpaceUtil.format("아이디", 18, 0), 
					SpaceUtil.format("차량 이름", 12, 0), 
					SpaceUtil.format("대여 날짜", 12, 0), 
					SpaceUtil.format("반납 날짜", 12, 0), 
					SpaceUtil.format("금액", 12, 0), 
					SpaceUtil.format("예약 날짜", 12, 0), 
					SpaceUtil.format("면허증", 10, 0),
					SpaceUtil.format("상태", 10, 0)
					);
			int cnt = 1;
			for(Map<String, Object> car : carReservation) {
				int cb_state = Integer.parseInt(car.get("CB_STATE").toString());
				String state = state(cb_state);
				System.out.println("\n├─────────┼─────────┼──────────────────┼────────────┼────────────┼────────────┼────────────┼────────────┼──────────┼──────────┤");
				int cost = Integer.parseInt(String.valueOf(car.get("CB_TOTAL")).toString());
				String total = String.format("%,3d", cost);
				System.out.printf("│%s│%s│%s│%s│%s│%s│%s│%s│%s│%s│",
						SpaceUtil.format(cnt++ + "", 9, 0),
						SpaceUtil.format(car.get("C_NO"), 9, 0),
						SpaceUtil.format(car.get("MEM_ID"), 18, 0),
						SpaceUtil.format(car.get("C_NAME"), 12, 0),
						SpaceUtil.dateFormat(car.get("CB_STARTDATE"), 12, 0),
						SpaceUtil.dateFormat(car.get("CB_ENDDATE"), 12, 0),
						SpaceUtil.format(total + "원", 12, 1),
						SpaceUtil.dateFormat(car.get("CB_DATE"), 12, 0),
						SpaceUtil.format(car.get("C_LICENCE") + "종", 10, 0),
						SpaceUtil.format(state, 10, 0)
				);
				}
			System.out.println("\n└─────────┴─────────┴──────────────────┴────────────┴────────────┴────────────┴────────────┴────────────┴──────────┴──────────┘");
		}else {
			System.out.println("=============================== 차량 대여 정보가 없습니다. ==============================\n");
		}
	}
	
	public void carResList() {
		Map<String, Object> userInfo = (Map<String, Object>)Controller.sessionStorage.get("userInfo");
		List<Object> param = new ArrayList<>();
		param.add(userInfo.get("MEM_ID"));
		param.add(2);
		List<Map<String, Object>> carReservation = mypageDAO.carResInfo(param);
		if(carReservation.size() != 0) {
			System.out.println("======================================================= 차량 예약 목록 =======================================================");
			System.out.println("┌─────────┬─────────┬──────────────────┬────────────┬────────────┬────────────┬────────────┬────────────┬──────────┬──────────┐");
			System.out.printf("│%s│%s│%s│%s│%s│%s│%s│%s│%s│%s│", 
					SpaceUtil.format("번호", 9, 0), 
					SpaceUtil.format("구분 번호", 9, 0),
					SpaceUtil.format("아이디", 18, 0), 
					SpaceUtil.format("차량 이름", 12, 0), 
					SpaceUtil.format("대여 날짜", 12, 0), 
					SpaceUtil.format("반납 날짜", 12, 0), 
					SpaceUtil.format("금액", 12, 0), 
					SpaceUtil.format("예약 날짜", 12, 0), 
					SpaceUtil.format("면허증", 10, 0),
					SpaceUtil.format("상태", 10, 0)
					);
			int cnt = 0;
			for(Map<String, Object> car : carReservation) {
				carNumber.put(cnt++, car.get("CB_NO"));
				
				int cb_state = Integer.parseInt(car.get("CB_STATE").toString());
				String state = state(cb_state);
				System.out.println("\n├─────────┼─────────┼──────────────────┼────────────┼────────────┼────────────┼────────────┼────────────┼──────────┼──────────┤");
				int cost = Integer.parseInt(String.valueOf(car.get("CB_TOTAL")).toString());
				String total = String.format("%,3d", cost);
				System.out.printf("│%s│%s│%s│%s│%s│%s│%s│%s│%s│%s│",
						SpaceUtil.format(cnt, 9, 0),
						SpaceUtil.format(car.get("C_NO"), 9, 0),
						SpaceUtil.format(car.get("MEM_ID"), 18, 0),
						SpaceUtil.format(car.get("C_NAME"), 12, 0),
						SpaceUtil.dateFormat(car.get("CB_STARTDATE"), 12, 0),
						SpaceUtil.dateFormat(car.get("CB_ENDDATE"), 12, 0),
						SpaceUtil.format(total + "원", 12, 1),
						SpaceUtil.dateFormat(car.get("CB_DATE"), 12, 0),
						SpaceUtil.format(car.get("C_LICENCE") + "종", 10, 0),
						SpaceUtil.format(state, 10, 0)
				);
				}
			System.out.println("\n└─────────┴─────────┴──────────────────┴────────────┴────────────┴────────────┴────────────┴────────────┴──────────┴──────────┘");
		}else {
			System.out.println("=============================== 차량 대여 정보가 없습니다. ==============================\n");
		}
	}
	
	public int resInfo() { // 예약정보 확인
		clear();
		pensionList();
		System.out.println();
		carList();
		
		System.out.println("1.숙소예약 취소, 2.차량예약 취소 3.돌아가기");
		System.out.print("입력 >> ");
		switch(ScanUtil.nextInt()) {
		case 1: return View.PENSHON_CANCLE; 
		case 2: return View.CAR_CANCLE;
		case 3: return View.MYPAGE;
		default: return View.RES_INFO;
		}
	}
	
	
	// 팬션예약 목록의 구분 번호가 0 ,1 반납완료 대여 중이면 예약 취소 불가능 2 -> 3 변경
	public int pensionCancel() {
		clear();
		pensionResList();
		if(pensionNumber.size() != 0) {
			System.out.println("취소하실 번호를 선택하세요");
			System.out.print("입력 >> ");
			int num = ScanUtil.nextInt();
			int result = mypageDAO.pensionCancle(pensionNumber.get(num - 1));
			if(result > 0) {
				try {
					System.out.println("예약이 취소되었습니다.");
					Thread.sleep(1000); 
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else {
				System.out.println("예약 취소에 실패하였습니다.");
				System.out.println("잠시 후 다시 시도하세요");
			}
		}else {
			try {
				System.out.println("예약정보가 없습니다");
				Thread.sleep(1000); 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("아무키나 입력하세요");
		ScanUtil.nextLine();
		return View.RES_INFO;
	}
	
	public int carCancel() {
		clear();
		carResList();
		if(carNumber.size() != 0) {
			System.out.println("취소하실 번호를 선택하세요");
			System.out.print("입력 >> ");
			int num = ScanUtil.nextInt();
			int result = mypageDAO.carCancle((carNumber.get(num - 1)));
			if(result > 0) {
				try {Thread.sleep(1000); } catch (InterruptedException e) {
					System.out.println("예약이 취소되었습니다.");
					e.printStackTrace();
				}
			}else {
				System.out.println("예약 취소에 실패하였습니다.");
				System.out.println("다시 시도하세요");
			}
		}else {
			try {
				Thread.sleep(1000); 
			} catch (InterruptedException e) {
				System.out.println("예약정보가 없습니다");
				e.printStackTrace();
			}
		}
		return View.RES_INFO;
	}

	public int updatePw() { // 비밀번호 수정
		boolean b = false;
		String memPass = "";
		System.out.println("현재 비밀번호를 입력해주세요");
		System.out.print("입력 >> ");
		String password = ScanUtil.nextLine();
		
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		if(!password.equals(userInfo.get("MEM_PW").toString())) {
			System.out.println("비밀번호가 일치하지 않습니다.");
			return View.UPDATE_INFO;
		}
		
		pass:
		while(true){
			System.out.println("새 비밀번호를 입력하세요 (8 ~ 16자리 영문, 숫자)");
			System.out.print("입력 >> ");
			String newPass1 = ScanUtil.nextLine();
			if(!pwCheck(newPass1)) {
				System.out.println("비밀번호 형식을 확인하세요");
			}else {
				System.out.print("새 비밀번호를 한번 더 입력하세요 >> ");
				String newPass2 = ScanUtil.nextLine();
				b = newPass1.equals(newPass2);
				
				if(b) {
					System.out.println("비밀번호가 일치합니다.");
					memPass = newPass1;
					break pass;
				}else {
					System.out.println("비밀번호가 일치하지 않습니다.");
					System.out.println("1. 다시 입력 2. 돌아가기");
					switch (ScanUtil.nextInt()) {
					case 2: return View.UPDATE_INFO;
					default:
					}
				}
			}
		}
		
		List<Object> param = new ArrayList<>();
		String userId = (String) userInfo.get("MEM_ID");
		param.add(userId);
		List<Map<String, Object>> memberList = mypageDAO.myPage(param);
		
		List<Object> param1 = new ArrayList<>();
		param1.add(memPass);
		param1.add(memberList.get(0).get("MEM_PHONE"));
		param1.add(memberList.get(0).get("MEM_ADDR"));
		param1.add(memberList.get(0).get("MEM_LICENSE"));
		param1.add(memberList.get(0).get("MEM_ID"));
		
		int result = mypageDAO.updateInfo(param1);
		if(result > 0) {
			System.out.println("비밀번호가 변경되었습니다.");
		}else {
			System.out.println("비밀번호 변경에 실패했습니다.");
		}
		
		return View.UPDATE_INFO;
	}
	
	public int updatePN() { // 핸드폰 번호 수정
		String newPhone = "";
		
		List<Object> param = new ArrayList<>();
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		String userId = (String) userInfo.get("MEM_ID");
		param.add(userId);
		List<Map<String, Object>> memberList = mypageDAO.myPage(param);
		
		phone:
		while(true) {
			System.out.println("변경하실 전화번호를 입력하세요 ex ) 01012345678  ");
			System.out.print("입력 >>");
			newPhone = ScanUtil.nextLine();
			if(!phoneCheck(newPhone)) {
				System.out.println("형식이 일치하지 않습니다.");
			} else {
				break phone;
			}
		}
		
		List<Object> param1 = new ArrayList<>();
		param1.add(userInfo.get("MEM_PW"));
		param1.add(newPhone);
		param1.add(memberList.get(0).get("MEM_ADDR"));
		param1.add(memberList.get(0).get("MEM_LICENSE"));
		param1.add(memberList.get(0).get("MEM_ID"));
		
		int infoPassword = mypageDAO.updateInfo(param1);
		if(infoPassword > 0) {
			System.out.println("전화번호가 변경되었습니다.");
		}else {
			System.out.println("전화번호 변경에 실패했습니다.");
		}
		
		return View.UPDATE_INFO;
	}
	
	public int updateAddr() {	// 주소 수정
		String newAddrs = "";
		List<Object> param = new ArrayList<>();
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		String userId = (String) userInfo.get("MEM_ID");
		param.add(userId);
		List<Map<String, Object>> memberList = mypageDAO.myPage(param);
		
		address:
		while(true) {
			System.out.println("변경하실 주소를 입력하세요");
			System.out.print("입력 >>");
			newAddrs = ScanUtil.nextLine();
			if(!addressCheck(newAddrs)) {
				System.out.println("다시 입력해주세요 ");
			}else {
				break address;
			}
		}
		List<Object> param1 = new ArrayList<>();
		param1.add(userInfo.get("MEM_PW"));
		param1.add(memberList.get(0).get("MEM_PHONE"));
		param1.add(newAddrs);
		param1.add(memberList.get(0).get("MEM_LICENSE"));
		param1.add(memberList.get(0).get("MEM_ID"));
		
		int infoPassword = mypageDAO.updateInfo(param1);
		if(infoPassword > 0) {
			System.out.println("주소가 변경되었습니다.");
		}else {
			System.out.println("주소 변경에 실패했습니다.");
		}
		
		return View.UPDATE_INFO;
	}
	
	public int updateLicense() { // 면허증 수정
		int newLicense = -1;
		List<Object> param = new ArrayList<>();
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		String userId = (String) userInfo.get("MEM_ID");
		param.add(userId);
		List<Map<String, Object>> memberList = mypageDAO.myPage(param);
		
		license:
		while(true) {
			System.out.println("변경할 항목을 선택해 주세요");
			System.out.println("1 : 1종  2 : 2종 0: 무면허 ");
			System.out.print("입력 >> ");
			newLicense = ScanUtil.nextInt();
			switch(newLicense) {
			case 1: case 2: case 0: break license;
			default:
			}
		}
			
		List<Object> param1 = new ArrayList<>();
		param1.add(userInfo.get("MEM_PW"));
		param1.add(memberList.get(0).get("MEM_PHONE"));
		param1.add(memberList.get(0).get("MEM_ADDR"));
		param1.add(newLicense);
		param1.add(memberList.get(0).get("MEM_ID"));
		
		int result = mypageDAO.updateInfo(param1);
		if(result > 0) {
			System.out.println("변경 완료");
		}else {
			System.out.println("변경 실패");
		}
		return View.MY_INFO;
	}
	
	
	Map<Integer ,Object> myCard = new HashMap<>();
	public int cardManage() {
		clear();
		System.out.println("===================== 내 카드 정보 ====================");
		Map<String, Object> userInfo = (Map<String, Object>)Controller.sessionStorage.get("userInfo");
		List<Map<String, Object>> card = mypageDAO.cardManage(userInfo.get("MEM_ID"));
		if(card.size() != 0) {
			System.out.println("┌──────────┬──────────┬──────────┬────────────────────┐");
			System.out.printf("│%s│%s│%s│%s│"
					,SpaceUtil.format("구분", 10, 0)
					,SpaceUtil.format("ID", 10, 0)
					,SpaceUtil.format("카드 이름", 10, 0)
					,SpaceUtil.format("카드 번호", 20, 0)
					);
		
		int no = 1;
			for(Map<String, Object> c : card) {
				myCard.put(no, c.get("CARD_NO"));
				System.out.println("\n├──────────┼──────────┼──────────┼────────────────────┤");
				System.out.printf("│%s│%s│%s│%s│"
						,SpaceUtil.format(no++ + "", 10, 0)
						,SpaceUtil.format(c.get("MEM_ID"), 10, 0)
						,SpaceUtil.format(c.get("CARD_NAME"), 10, 0)
						,SpaceUtil.format(c.get("CARD_NUMBER"), 20, 0)
						);
				
			}
			System.out.println("\n└──────────┴──────────┴──────────┴────────────────────┘");
		}else {
			System.out.println("등록 된 카드가 없습니다");
		}
		
		System.out.println("1.카드추가  2.카드삭제  3.  0.뒤로가기");
		System.out.print("입력 >> ");
		int no = ScanUtil.nextInt();
		switch (no) {
		case 1: return View.CARD_ADD;
		case 2: return View.CARD_DELETE;
		case 3:	
		case 0: return  View.MYPAGE;
		default: 
			System.out.println("잘못 입력하셨습니다.");
			return View.CARD_MANAGE;
		}
	}
	
	public int cardAdd() {
		String cardName = "", card = "";
		
		car1d:
		while(true) {
			System.out.println("등록할 카드명을 입력해주세요 (2~8자리)");
			cardName = ScanUtil.nextLine();
			if(!cardName(cardName)) {
				System.out.println("카드 형식을 확인해주세요");
			}else {
				
//				int result = mypageDAO.cardCheck(card);
				break car1d;
			}
		}
		card:
		while(true) {
			System.out.println("카드 번호를 입력해주세요 (ex 5xxx-xxxx-xxxx-xxxx) 숫자 16자리");
			System.out.print("입력 >> ");
			card = ScanUtil.nextLine();
			if(!cardCheck(card)) {
				System.out.println("카드 형식을 확인해주세요");
			}else {
				break card;
			}
		}
		
		List<Object> param = new ArrayList<>();
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		
		param.add(userInfo.get("MEM_ID"));
		param.add(cardName);
		param.add(card);
		
		int result = mypageDAO.card_add(param);
		if(result > 0) {
			System.out.println("카드 등록이 완료되었습니다.");
		}else {
			System.out.println("카드 등록에 실패하였습니다.");
		}
		for(int i = 0; i < 10; i++) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return View.CARD_MANAGE;
	}
	
	public int cardDelete() {
		System.out.println("삭제 할 카드를 선택해주세요");
		System.out.print("입력 >> ");
		int no = ScanUtil.nextInt();
		
		int result = mypageDAO.card_delete(myCard.get(no));
		if(result > 0) {
			System.out.println("카드 삭제가 완료되었습니다.");
		}else {
			System.out.println("카드 삭제에 실패하였습니다.");
		}
		for(int i = 0; i < 10; i++) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return View.CARD_MANAGE;
	}
	
	private void clear() {
		for (int i = 0; i < 50; i++) {
			System.out.println();
		}
	}
	
	
	
	
	
}
