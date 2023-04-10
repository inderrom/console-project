package service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import controller.Controller;
import dao.PensionDAO;
import util.ScanUtil;
import util.SpaceUtil;
import util.View;

public class PensionService {
	private static PensionService instance=null;
	private PensionService() {};
	public static PensionService getInstance() {
		if(instance ==null) instance= new PensionService();
		return instance;
	}
	
	PensionDAO pensionDAO = PensionDAO.getInstance();
	String sYear = "", sMonth = "", sMD = "", sDay = "", eYear = "", eMD = "", eMonth = "", eDay = "";
	int pensionPrice = 0;
	
	public boolean cardName(String card) { // 카드 이름
		return Pattern.matches("^[가-힣0-9a-zA-Z]{2,8}$", card);
	}

	public boolean cardCheck(String card) { // 신용카드
		return Pattern.matches("^((5[1-5])([0-9]{2})((-|\\s)?[0-9]{4}){3})$", card);
	}
	
	public boolean mile(String mile) {	// 마일리지 숫자 체크
		return Pattern.matches("^[0-9]+$", mile);
	}
	
	
	private boolean dayCheck(String birth) {	// 날짜 체크
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
		
		date.setLenient(false);
		try {
			date.parse(birth);
			return true;
		}catch(ParseException e) {
			return false;
		}
	}
	
	int dd; // 상태
	public int pensionState() {
		dd = 2;
		SimpleDateFormat simple = new SimpleDateFormat("yyyyMMdd");
		String date = simple.format(new Date());
		int today = Integer.parseInt(date); // 현재날짜

		List<Map<String, Object>> statePension = pensionDAO.selectPension();	// 팬션 조회
		if(statePension.size() != 0) {
			for (int i = 0; i < statePension.size(); i++) {
				List<Object> param = new ArrayList<>();
				
				String PB_STARTDATE = simple.format(statePension.get(i).get("PB_STARTDATE"));	//시작 날짜
				String PB_ENDDATE = simple.format(statePension.get(i).get("PB_ENDDATE"));		//끝 날짜
	
				if (Integer.parseInt(PB_STARTDATE) <= today) {	// 시작날 <= 현재날
					param.add(1);
					param.add(statePension.get(i).get("PB_NO").toString());
					pensionDAO.udatePensionState(param);
				}
				
				List<Object> param1 = new ArrayList<>();
				if (Integer.parseInt(PB_ENDDATE) < today) { 	// 마지막날<현재날
					param1.add(0);
					param1.add(statePension.get(i).get("PB_NO").toString());
					pensionDAO.udatePensionState(param1);
				}
			}
			
		}
		return View.PENSION_RES;
	}
	
	int diffDay2 = 0;
	public int pensionRes() throws InterruptedException {
		Date day = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		int today = Integer.parseInt(sdf.format(day));
		int startDate = 1, endDate = 0;
		
		System.out.println("=======================================");
		System.out.println("        여기갈까? 옆에 탈래? ");
		System.out.println("=======================================");
		System.out.println("        대여 날짜를 입력해주세요");
		System.out.println("=======================================");
		
		start:
		while(true) {
			System.out.println("[뒤로가기 : B]");
			System.out.print("날짜 입력(ex 20201212) >> ");
			String input = ScanUtil.nextLine();
			input = (input.toUpperCase()).trim();
			if(input.equals("B")) {
				return View.MAIN;
			}
			if(!dayCheck(input)) {
				System.out.println("!!! 날짜 형식이 잘못 되었습니다. !!!");
				System.out.println("다시 입력해주세요 ");
				continue;
			}
			startDate = Integer.parseInt(input);
			if(startDate < today) {
				System.out.println("오늘 날짜보다 작습니다.");
				System.out.println("다시 입력해주세요");
				continue;
			}
				break start;
		}
		System.out.println("=======================================");
		System.out.println("        반납 날짜를 입력해주세요");
		System.out.println("=======================================");
		
		end:
		while(true) {
			System.out.println("[뒤로가기 : B]");
			System.out.print("날짜 입력(ex 20201212) >> ");
			String input = ScanUtil.nextLine();
			input = (input.toUpperCase()).trim();
			if(input.equals("B")) {
				return View.MAIN;
			}
			if(!dayCheck(input)) {
				System.out.println("!!! 날짜 형식이 잘못 되었습니다. !!!");
				System.out.println("다시 입력해주세요 ");
				continue;
			}
			endDate = Integer.parseInt(input);
			if(startDate > endDate) {
				System.out.println("대여날짜보다 작습니다.");
				System.out.println("다시 입력해주세요");
				continue;
			}
			break end;
		}
		
		String sDate = startDate + "";
		sYear = sDate.substring(0, 4);
		sMD = sDate.substring(4, 8);
		sMonth = sDate.substring(4, 6);
		sDay = sDate.substring(6, 8);
		
		String eDate = endDate + "";
		eMD = eDate.substring(4, 8);
		eYear = eDate.substring(0, 4);
		eMonth = eDate.substring(4, 6);
		eDay = eDate.substring(6, 8);
		
		List<Map<String, Object>> pensionRes = pensionDAO.pensionResvion(startDate, endDate);
		
		if(pensionRes.size() == 0) {
			System.out.println("예약 가능한 숙소가 없습니다.");
			System.out.println("날짜를 다시 입력해주세요");
			Thread.sleep(1000);
			clear();
			return View.PENSION_RES;
		}
		
		Map<String , Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		clear();
		System.out.println("====================================");
		System.out.println("┌──────────┬──────────┬────────────┐");
		System.out.printf("│%s│%s│%s│",
				SpaceUtil.format("팬션이름", 10, 0),
				SpaceUtil.format("수용 인원", 10, 0),
				SpaceUtil.format("가격", 12, 0)
				);
		System.out.println("\n├──────────┼──────────┼────────────┤");
		
		for(Map<String, Object> pen : pensionRes) {
			int num=Integer.parseInt(String.valueOf(pen.get("P_PRICE")).toString());
			String numm = String.format("%,3d",num);
			
			System.out.printf("│%s│%s│%s│",
					SpaceUtil.format(pen.get("P_NAME"), 10, 0),			
					SpaceUtil.format(pen.get("P_MEN") + "명   ", 10, 1),
					SpaceUtil.format(numm + "원", 12, 1)
					);
			System.out.println();
		}
		System.out.println("└──────────┴──────────┴────────────┘");
		
		String strFormat = "yyyyMMdd"; // strStartDate 와 strEndDate 의 format
		// SimpleDateFormat 을 이용하여 startDate와 endDate의 Date 객체를 생성한다.
		SimpleDateFormat sdfff = new SimpleDateFormat(strFormat);
		long diffDay = 0;
		
		try {
			Date startDate1 = sdf.parse(startDate + "");
			Date endDate1 = sdf.parse(endDate + "");
			// 두날짜 사이의 시간 차이(ms)를 하루 동안의 ms(24시*60분*60초*1000밀리초) 로 나눈다.
			diffDay = (endDate1.getTime() - startDate1.getTime()) / (24 * 60 * 60 * 1000);
			diffDay2 = (int) diffDay;
		}catch(Exception e) {
		}
		
		
		System.out.printf("%s년 %s월 %s일 ~ %s년 %s월 %s일\n               (%d일)\n"
				,sYear, sMonth, sDay,eYear, eMonth, eDay, diffDay2);
		
		System.out.println("1.숙소 선택 2. 날짜 재입력 3. 홈으로 ");
		System.out.print("입력 >> ");
		switch (ScanUtil.nextInt()) {
		case 1: return View.PENSION_SELECT;
		case 3: return View.MAIN;
		default : return View.PENSION_RES;
		}
	}
	
	List<Object> pensionInfo = new ArrayList<>();
	String p_name = "", p_people = "";
	
	public int pension_select() throws InterruptedException {
		pensionInfo = new ArrayList<>();
		String penName = "";

		pension:
		while(true) {
			System.out.print("숙소 이름을 입력하세요 >> ");
			penName = ScanUtil.nextLine();
			//겹침
			Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
			List<Map<String, Object>> penInfo = pensionDAO.pensionInfo(penName);
			if(penInfo.size() != 0) {
				break pension;
			}else {
				System.out.println("팬션 정보가 없습니다.");
			}
		}
		int day = Integer.parseInt(eMD) - Integer.parseInt(sMD) + 1;
		
		// 겹침
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		List<Map<String, Object>> penInfo = pensionDAO.pensionInfo(penName);
		
		int num=Integer.parseInt(String.valueOf(penInfo.get(0).get("P_PRICE")).toString());
		String price = String.format("%,3d",num * diffDay2);
		pensionPrice = num * diffDay2;
		
		System.out.println("================================");
		System.out.println("숙소 이름 : " + penInfo.get(0).get("P_NAME"));
		System.out.println("사용 인원 : " + penInfo.get(0).get("P_MEN"));
		System.out.printf("대여 날짜 : %s년 %s월%s일\n", sYear, sMonth, sDay);
		System.out.printf("반납 날짜 : %s년 %s월%s일\n", eYear, eMonth, eDay);
		System.out.println("가격  : " + price);
		System.out.println("================================");
		System.out.println("결제 하시겠습니까 ?");
		System.out.println("1.결제 2.날짜 재입력 3.홈으로");
		System.out.print("입력 >> ");
		int ch = ScanUtil.nextInt();
		
		p_name = penInfo.get(0).get("P_NAME").toString();
		p_people = 	penInfo.get(0).get("P_MEN").toString();
		
		switch (ch) {
		case 1: 
			pensionInfo.add((String)userInfo.get("MEM_ID"));
			pensionInfo.add(penInfo.get(0).get("P_NAME"));
			pensionInfo.add(sYear + sMonth + sDay);
			pensionInfo.add(eYear + eMonth + eDay);
			return View.PENSION_MILE;
		case 2: return View.PENSION_RES;
		default: return View.MAIN;
		}
	}
	
	public void receipt() {
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		Map<String, Object> mileage = pensionDAO.getMile(userInfo.get("MEM_ID"));
		int myMile = Integer.parseInt(mileage.get("MEM_MILE").toString());	
		String myMileS = String.format("%,3d", myMile);
		String price = String.format("%,3d", pensionPrice);
		
		System.out.println("================================");
		System.out.println("숙소 이름 : " + p_name);
		System.out.println("사용 인원 : " + p_people);
		System.out.printf("대여 날짜 : %s년 %s월%s일\n", sYear, sMonth, sDay);
		System.out.printf("반납 날짜 : %s년 %s월%s일\n", eYear, eMonth, eDay);
		System.out.println("가격  : " + price);
		System.out.println("보유 마일리지 : " + myMileS);
		System.out.println("================================");
	}
	
	public void ima() {
		System.out.println("┌──────────────── 마일리지 사용시 주의 사항 ────────────────┐");
		System.out.println("│                                                           │");
		System.out.println("│마일리지는 총대여료의 5%만큼 적립이 됩니다                 │");
		System.out.println("│마일리지로 대여료를 계산을 하시더라도                      │ ");
		System.out.println("│여기갈까? 옆에 탈래? 는 차감된 대여료의 5%가 아닌          │");
		System.out.println("│기존 총대여료의 5%만큼 적립해드립니다                      │");
		System.out.println("│여기갈까? 옆에 탈래? 는  여러분들의                        │");
		System.out.println("│기분 좋은 여행을 위해 최선을 다하겠습니다.                 │");
		System.out.println("│감사합니다.                                                │");
		System.out.println("│                                                           │");
		System.out.println("│                                                           │");
		System.out.println("│                                 (주)여기갈까? 옆에 탈래?  │");
		System.out.println("│                                                     대표  │");
		System.out.println("│                                                   박승배  │");
		System.out.println("└───────────────────────────────────────────────────────────┘");
	}
	
	int useMile = 0; // 사용 마일리지
	public int pension_mile() {
		clear();
		String mile = "";
		
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		Map<String, Object> mileage = pensionDAO.getMile(userInfo.get("MEM_ID"));
		int myMile = Integer.parseInt(mileage.get("MEM_MILE").toString());	
		receipt();
		ima();
		
		use:
		while(true) {
			System.out.println("마일리지를 사용하시겠습니까? (Y/N) [결제 취소 : B]");
			System.out.print("입력 >> ");
			mile = ScanUtil.nextLine();
			mile = (mile.toUpperCase()).trim();
			
			if(mile.equals("Y") || mile.equals("N")) break use;
			else System.out.println("잘못 입력하셨습니다.");
			if(mile.equals("B")) {
				return View.MAIN;
			}
		}
		clear();
		receipt();
		
		if(mile.equals("Y") && myMile <= 0) {
			System.out.println("보유하신 마일리지가 없습니다.");
		}else if(mile.equals("Y") && myMile != 0) {
			
			String mileChoose = "";
			choose:
			while(true) {
				System.out.println("1. 전부사용 2. 일부사용 (뒤로가기 : B) ");
				mileChoose = ScanUtil.nextLine();
				mileChoose = (mileChoose.toUpperCase()).trim();
				
				switch(mileChoose) {
				case "1": 
					useMile = myMile;
					break choose;
				case "2": 
					mile:
					while(true) {
						System.out.println("사용하실 마일리지를 입력해주세요 [결제 취소 : B]");
						System.out.print("입력 >> ");
						String uMile = ScanUtil.nextLine();	
						uMile = (uMile.toUpperCase()).trim();
						
						if(uMile.equals("B")) {
							return View.MAIN;
						}
						
						if(!mile(uMile)) {
							System.out.println("숫자만 입력해주세요");
							continue;
						}else {
							useMile = Integer.parseInt(uMile);
						}
						
						if(myMile < useMile) {
							System.out.println("보유 마일리지를 확인해주세요.");
						}else {
							break mile;
						}
					}
					break choose;
				case "B": return View.HOME;
				default: 
					System.out.println("잘못된 명령어 입니다.");
					System.out.println("다시 입력해주시기 바랍니다.");
				}
			}
			pensionPrice -= useMile;
			myMile -= useMile;
			int result = pensionDAO.useMile(myMile, pensionInfo.get(0));
			if(result > 0) {
				System.out.println("마일리지가 사용되었습니다.");
			}else {
				System.out.println("마일리지 사용에 실패하였습니다.");
			}
		}else {
			System.out.println("마일리지를 사용하지 않습니다.");
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		pensionInfo.add(pensionPrice);
		return View.PENSION_PAYMENT;
	}
	
	public void receipt2() {
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		Map<String, Object> mileage = pensionDAO.getMile(userInfo.get("MEM_ID"));
		int myMile = Integer.parseInt(mileage.get("MEM_MILE").toString());	
		
		String price = String.format("%,3d", pensionPrice);
		
		String useMileage = String.format("%,3d", useMile);
		String myMileS = String.format("%,3d", myMile);
		
		System.out.println("================================");
		System.out.println("숙소 이름 : " + p_name);
		System.out.println("사용 인원 : " + p_people);
		System.out.printf("대여 날짜 : %s년 %s월%s일\n", sYear, sMonth, sDay);
		System.out.printf("반납 날짜 : %s년 %s월%s일\n", eYear, eMonth, eDay);
		System.out.println("가격  : " + price);
		System.out.println("사용 마일리지 : " + useMileage);
		System.out.println("보유 마일리지 : " + myMileS);
		System.out.println("================================");
	}
	
	public int pension_payment() throws InterruptedException {
		clear();
		// 목록 저장
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");
		List<Map<String, Object>> card = pensionDAO.card_bring(userInfo.get("MEM_ID"));
		Map<Integer, Object> cardInfo = new HashMap<>();
		// 마일리지
		Map<String, Object> mileage = pensionDAO.getMile(userInfo.get("MEM_ID"));
		int myMile = Integer.parseInt(mileage.get("MEM_MILE").toString());
		
		
		receipt2();
		if(card.size() != 0) {
			System.out.println("====================== 내 카드 정보 =====================");
			Map<String, Object> userInfo1 = (Map<String, Object>)Controller.sessionStorage.get("userInfo");
			List<Map<String, Object>> card1 = pensionDAO.card_Manager(userInfo1.get("MEM_ID"));
			System.out.println("┌──────────┬──────────┬──────────┬──────────────────────┐");
			System.out.printf("│%s│%s│%s│%s│"
					,SpaceUtil.format("구분", 10, 0)
					,SpaceUtil.format(("아이디"), 10, 0)
					,SpaceUtil.format(("카드이름"), 10, 0)
					,SpaceUtil.format(("카드번호"), 22, 0));
			int no = 1;
			for(Map<String, Object> c : card) {
				cardInfo.put(no, c.get("CARD_NO"));
				System.out.println("\n├──────────┼──────────┼──────────┼──────────────────────┤");
				System.out.printf("│%s│%s│%s│%s│"
						,SpaceUtil.format(no++ + "", 10, 0)
						,SpaceUtil.format(c.get("MEM_ID"), 10, 0)
						,SpaceUtil.format(c.get("CARD_NAME"), 10, 0)
						,SpaceUtil.format(c.get("CARD_NUMBER"), 22, 0));
			}
			System.out.println("\n└──────────┴──────────┴──────────┴──────────────────────┘");
			
			
			choose:
			while(true) {
				System.out.println("(카드 추가 A, 종료 B)");
				System.out.print("결제하실 카드번호를 입력해주세요 >> ");
				String cardNo = ScanUtil.nextLine();
				cardNo = (cardNo.toUpperCase()).trim();
				String cardNo1 = "";
				Map<String, Object> cardNumber = pensionDAO.cardUse(cardNo, userInfo.get("MEM_ID"));
				switch (cardNo) {
				case "B": 
					Mile_restore();
					return View.MAIN;
				case "A": return View.PENSION_CARD_REGIST;
				default:
				}
				
				if(cardNumber != null) {
					cardNo1 = cardNumber.get("CARD_NUMBER").toString();
				}else {
					System.out.println("카드번호가 일치하지 않습니다.");
					continue;
				}
				
				if(cardNo.equals(cardNo1)) {
					break choose;
				}else {
					System.out.println("카드번호가 일치하지 않습니다. 다시 입력해주세요");
				}
			}
			Date today = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			int todayInt = Integer.parseInt(sdf.format(today));
			int start = Integer.parseInt(sYear + sMonth + sDay);
			if(start == todayInt) {
				pensionInfo.add(1);
			}else {
				pensionInfo.add(2);
			}
			
			int pension = pensionDAO.pension_payment(pensionInfo);
			if(pension > 0) {
				addMile();
				receipt2();
				System.out.println("결제가 완료 되었습니다.");
				ScanUtil.nextLine();
			}else {
				System.out.println("오류 발생!");
				Mile_restore();
			}
		}else {
			System.out.println("등록 된 카드가 없습니다.");
			System.out.println("1.카드등록  2.메인으로");
			System.out.print("입력 >> ");
			switch(ScanUtil.nextLine()) {
			case "1": return View.PENSION_CARD_REGIST;
			case "2": 
				Mile_restore();
				return View.MAIN;
			default: 
				System.out.println("잘못 입력하셨습니다.");
				return pension_payment();
			}
		}
		Thread.sleep(1000);
		return View.MAIN;
	}
	
	public void addMile() { 
		// 마일리지 적립
		// 마일리지 가져오기
		Map<String, Object> userInfo = (Map<String, Object>)Controller.sessionStorage.get("userInfo");
		Map<String, Object> mileage = pensionDAO.getMile(userInfo.get("MEM_ID"));
		int myMile = Integer.parseInt(mileage.get("MEM_MILE").toString());
		myMile += pensionPrice * 0.05;
		int mileResult = pensionDAO.useMile(myMile, pensionInfo.get(0));
		int use = (int) (pensionPrice * 0.05);
		String addMile = String.format("%,3d", use);
		
		if(mileResult > 0) {
			System.out.println("마일리지 적립 : " + addMile);
		}else {
			System.out.println("마일리지 적립에 실패했습니다");
		}
		
	}
	
	public void Mile_restore() { // 사용 마일리지 복구
		// 마일리지 가져오기
		Map<String, Object> userInfo = (Map<String, Object>)Controller.sessionStorage.get("userInfo");
		Map<String, Object> mileage = pensionDAO.getMile(userInfo.get("MEM_ID"));
		int myMile = Integer.parseInt(mileage.get("MEM_MILE").toString());
		
		myMile += useMile;
		int mileResult = pensionDAO.useMile(myMile, pensionInfo.get(0));
		if(mileResult > 0) {
			System.out.println("마일리지가 사용이 취소되었습니다.");
		}else {
			System.out.println("마일리지 적립에 실패했습니다??");
		}
	}
	
	public int card_regist() {
		String cardName = "", card = "";
		
		car1d:
		while(true) {
			System.out.println("등록할 카드명을 입력해주세요 (2~8자리) [취소 : B]");
			cardName = ScanUtil.nextLine();
			cardName = (cardName.toUpperCase()).trim();
			
			if(!cardName(cardName)) {
				System.out.println("카드 형식을 확인해주세요");
			}else {
				break car1d;
			}
			if(cardName.equals("B")) {
				return View.MAIN;
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
		
		int result = pensionDAO.card_insert(param);
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
		return View.PENSION_PAYMENT;
	}
	
	private void clear() {
		for(int i = 0; i < 50; i++) {
			System.out.println();
		}
	}
	
	
	
	
	
}
