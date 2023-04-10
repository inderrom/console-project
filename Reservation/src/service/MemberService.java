package service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import controller.Controller;
import dao.MemberDAO;
import util.ScanUtil;
import util.SpaceUtil;
import util.View;

public class MemberService {
	private static MemberService instance = null;
	private MemberService() {}
	public static MemberService getInstance() {
		if (instance == null)
			instance = new MemberService();
		return instance;
	}
	
	private boolean idCheck(String id) {	// id 체크
		return Pattern.matches("^[a-zA-Z]{1}[a-zA-Z0-9_]{3,7}$", id);
	}
	
	private boolean pwCheck(String pw) {	// pw 체크
		return Pattern.matches("^[a-zA-Z]{1}[a-zA-Z0-9_]{7,15}$", pw);	// 정규식 수정 숫자 영어 혼합사용 가능 해야함?? 되는데>?
	}
	
	private boolean nameCheck(String name) {	// 이름 체크
		return Pattern.matches("^[가-힣]{1,30}$", name);
	}
	
	private boolean birthCheck(String birth) {	// 생년월일 체크
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
		
		date.setLenient(false);
		try {
			date.parse(birth);
			return true;
		}catch(ParseException e) {
			return false;
		}
	}
	
	private boolean phoneCheck(String phone) {	// 휴대폰 번호 체크
		return Pattern.matches("^(\\d{3})(\\d{4})(\\d{4})$", phone);
	}
	
	public boolean addressCheck(String address) { // 신(구)주소, 도로명 주소
		return Pattern.matches("^[가-힣0-9\\s]*$", address);
	}
	
	MemberDAO memberDAO = MemberDAO.getInstance();
	private static int loginCount = 0; // 로그인 횟수
	
	public int signup() throws InterruptedException { // 회원가입
		Date today = new Date();
		boolean b = false;	// 비밀번호 일치여부
		String  memId = "", memPass = "", memName = "", 
				memBir = "", memphone = "",memAddr = "";
		int memLicense = 0;
		
		System.out.println("-------------------- 회원가입 ----------------------");
		id:
		while(true) {
			System.out.println("아이디 입력 (4 ~ 8자리 영문시작, 영문 숫자 혼합 사용)");
			System.out.print("입력 >> ");
			memId = ScanUtil.nextLine();
			memId = memId.trim();
			
			if(!idCheck(memId)) {
				System.out.println("아이디 형식이 잘못되었습니다.");
			}else {
				int status = memberDAO.idCheck(memId);	// 아이디 중복 확인
				if(status > 0) {	// 사용가능
					System.out.println("[사용 가능한 아이디입니다]");
					break id;
				}else { // 이미 사용중
					System.out.println("[이미 사용중인 아이디입니다]");
				}
			}
		}
		
		pw:
		while(true){
			System.out.println("비밀번호 입력 (8 ~ 16자리 영문시작, 영문 숫자 혼합 사용) ");
			System.out.print("입력 >> ");
			String memPass1 = ScanUtil.nextLine();
			memPass1 = memPass1.trim();
			
			if(!pwCheck(memPass1)) {
				System.out.println("비밀번호 형식이 잘못 되었습니다.");
			}else {
				System.out.print("비밀번호를 한번 더 입력하세요 >> ");
				String memPass2 = ScanUtil.nextLine();
				memPass2 = memPass2.trim();
				b = memPass1.equals(memPass2);
				
				if(b) {
					System.out.println("비밀번호가 일치합니다.");
					memPass = memPass1;
					break pw;
				}else {
					System.out.println("비밀번호가 일치하지 않습니다.");
					System.out.println("1. 다시 입력 2. 돌아가기");
					switch (ScanUtil.nextInt()) {
					case 2: return View.HOME;
					default:
					}
				}
			}
		}
		
		name:
		while(true) {
			System.out.println("이름 (한글)");
			System.out.print("입력 >> ");
			memName = ScanUtil.nextLine();
			memName = memName.trim();
			if(!nameCheck(memName)){
				System.out.println("이름 형식이 잘못 되었습니다.");
			}else {
				break name;
			}
		}
		
		int age = 0;
		birth:
		while(true) {
			System.out.println("생년월일 ex) 19871231 >> ");
			System.out.print("입력 >> ");
			memBir = ScanUtil.nextLine();
			memBir = memBir.trim();
			
			if(!birthCheck(memBir)) {
				System.out.println("생일 형식이 잘못되었습니다.");
			}else {
				break birth;
			}
		}
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("MMdd");
		String year = sdf1.format(today);
		String month = sdf2.format(today);
		
		String year1 = memBir.substring(0, 4);
		String month1 = memBir.substring(4, 8);
		
		boolean k = Integer.parseInt(month) < Integer.parseInt(month1);
		
		if(k) {
			age = Integer.parseInt(year) - Integer.parseInt(year1) - 1;
		}else {
			age = Integer.parseInt(year) - Integer.parseInt(year1);
		}
		
		System.out.printf("만 %d세입니다.", age);
		if (age < 19) {
			System.out.println("미성년자는 회원가입이 불가능합니다.\n");
			ScanUtil.nextLine();
			return View.HOME;
		}
		
		phone:
		while(true) {
			System.out.println("휴대폰번호 (ex 01098761234) ");
			System.out.print("입력 >> ");
			memphone = ScanUtil.nextLine();
			memphone.trim();
			if(!phoneCheck(memphone)) {
				System.out.println("옳바른 휴대폰 번호를 입력해 주세요");
			}else {
				break phone;
			}
		}
		
		addr:
		while(true){
			System.out.print("주소 입력 >> ");
			memAddr = ScanUtil.nextLine();
			if(!addressCheck(memAddr)) {
				System.out.println("잘못된 주소입니다.");
			}else {
				break addr;
			}
		}
		
		license:
		while(true) {
			System.out.println("면허증 소지 ");
			System.out.println("1.1종,  2.2종  0.미소지");
			System.out.print("입력 >> ");
			String n = ScanUtil.nextLine();
			n = n.trim();
			switch (n) {
			case "1": memLicense = 1; break license;
			case "2": memLicense = 2; break license;
			case "0":
				System.out.println("\n!!주의!! 면허증 미소지자는 차량 예약이 불가능합니다.\n");
				ScanUtil.nextLine();
				memLicense = 0;
				break license;
			default:
				System.out.println("잘못 입력하셨습니다.");
			}
		}
			
		List<Object> param = new ArrayList<>();
		param.add(memId);
		param.add(memPass);
		param.add(memName);
		param.add(memBir);
		param.add(memphone);
		param.add(memAddr);
		param.add(memLicense);
		int result = memberDAO.signup(param);
		if(result > 0) {
			System.out.println("등록 성공!");
		}else {
				System.out.println("등록 실패!");
		}
		
		Thread.sleep(1000);
		
		return View.HOME;
	}
	

	public int userLogin() throws InterruptedException { //유저 로그인
		int pageNo = 0;
		if ((boolean) Controller.sessionStorage.get("login")) { // == true
			System.out.println("이미 로그인되어있습니다.");
			return View.MAIN;
		}
		
		System.out.print("아이디 입력 >> ");
		String memId = ScanUtil.nextLine();
		System.out.print("비밀번호 입력 >> ");
		String memPass = ScanUtil.nextLine();
		
		List<Object> param = new ArrayList<>();			
		param.add(memId);
		param.add(memPass);
		
		int no = 0;
		Map<String, Object> admin = memberDAO.admin(param);
		if(admin != null) {
			no = Integer.parseInt(admin.get("MEM_STATE").toString());
		}
		
		Map<String, Object> userInfo = memberDAO.userLogin(param);
		
		if (userInfo != null && userInfo.get("MEM_ID").equals(memId)) {
			Controller.sessionStorage.put("userInfo", userInfo);
			Controller.sessionStorage.put("login", true);
			if(no == 1) {
				System.out.println("관리자로 로그인 되었습니다.");
				ScanUtil.nextLine();
				return View.ADMIN;
			}else {
				System.out.println(userInfo.get("MEM_NAME") + " 님 환영합니다!!!");
				Thread.sleep(1000);
				Controller.sessionStorage.put("MEM_ID", userInfo.get("MEM_ID"));
			}
			Controller.sessionStorage.put("MEM_NAME", userInfo.get("MEM_NAME"));
			loginCount = 0;
			return View.MAIN;
		} else {
			System.out.println("아이디 ,비밀번호를 잘못 입력했습니다.");
			loginCount++;
			clear();
			try {				
				System.out.print(SpaceUtil.format("로그인 실패 : " + loginCount + "회",10,0));		
				Thread.sleep(1200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(loginCount >= 3) {
				System.out.println(SpaceUtil.format("실패하셨습니다", 10, 0));
				System.out.println(SpaceUtil.format("보안문자를 정확하게 입력해야 로그인이 가능합니다.", 30, 0));
				while(true) {
					String securityText = UUID.randomUUID().toString().substring(0,6);
					System.out.printf("보안문자 : [%s]",
									SpaceUtil.format(securityText, 8, 0));
					System.out.print("\n입력 >> ");
					String input = ScanUtil.nextLine();
					if(input.equals(securityText)){
						System.out.println(SpaceUtil.format("정확하게 입력하셨습니다.",20,0));
						try {
							System.out.println("로그인 화면으로 돌아갑니다");
							Thread.sleep(1200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						loginCount = 0;
						break;
					}else {
						System.out.println(SpaceUtil.format("다시 입력해주세요!.",20,0));
					}
					pageNo = View.HOME;
				}	
			}
		}
		return pageNo;
	}
	
	public int findId() {
		clear();
		System.out.println("====== 아이디찾기 ======");
		
		String name = "";
		name:
			while(true) {
				System.out.println("이름 (한글) ");
				System.out.print("입력>> ");
				name = ScanUtil.nextLine();
				if(!nameCheck(name)){
					System.out.println("이름 형식이 잘못 되었습니다.");
				}else {
					break name;
				}
			}
		
		String phone = "";
		phone:
		while(true) {
			System.out.println("휴대폰 번호를 입력해주세요 ex) 01099997777 ");
			System.out.print("입력 >> ");
			phone = ScanUtil.nextLine();
			if(!phoneCheck(phone)) {
				System.out.println("옳바른 휴대폰 번호를 입력해 주세요");
			}else {
				break phone;
			}
		}
		
		Map<String, Object> idMap = memberDAO.findId(name, phone);
		
		if(idMap != null) {
			System.out.println(name + "님의 아이디는 \"" + idMap.get("MEM_ID") + "\" 입니다!");
		}else {
			System.out.println("존재하지 않는 회원입니다!");
		}
		System.out.println("enter 를 눌러주세요");
		ScanUtil.nextLine();
		return View.HOME;
	}
	
	public int findPw() {
		clear();
		int pageNo = 0;
		String name = "", id = "", phone = "";

		System.out.println("================= 비밀번호찾기 =================");
		id:
		while(true) {
			System.out.println("아이디 (4 ~ 8자리 영문시작, 영문 숫자 혼합 사용)");
			System.out.print("입력 >> ");
			id = ScanUtil.nextLine();
			if(!idCheck(id)) {
				System.out.println("아이디 형식이 잘못되었습니다.");
			}else {
				break id;
			}
		}
		
		name:
		while(true) {
			System.out.println("이름 (한글) ");
			System.out.print("입력>> ");
			name = ScanUtil.nextLine();
			if(!nameCheck(name)){
				System.out.println("이름 형식이 잘못 되었습니다.");
			}else {
				break name;
			}
		}
		
		phone:
		while(true) {
			System.out.println("휴대폰 번호 ex) 01099997777 ");
			System.out.print("입력 >> ");
			phone = ScanUtil.nextLine();
			if(!phoneCheck(phone)) {
				System.out.println("옳바른 휴대폰 번호를 입력해 주세요");
			}else {
				break phone;
			}
		}
		
		clear();
		Map<String, Object> resultMap = memberDAO.findPw(id, name, phone);
		if(resultMap != null) {
			System.out.println(name + "님 인증 완료!");
			String pw1 = "";
			
			pwCheck:
			while(true) {
				System.out.println("새로운 비밀번호를 입력해 주세요 (8 ~ 16자리 영문, 숫자)");
				System.out.print("입력 >> ");
				pw1 = ScanUtil.nextLine();
				if(!pwCheck(pw1)) {
					System.out.println("비밀번호 형식을 확인해주세요");
				}else {
					break pwCheck;
				}
			}
			System.out.println("다시 비밀번호 입력 >> ");
			String pw2 = ScanUtil.nextLine();
			if(pw1.equals(pw2)) {
				System.out.println("입력하신 비밀번호가 일치합니다!");
				int status = memberDAO.updatePw(id, pw1);
				if(status > 0) {
					System.out.println("비밀번호 수정 완료!");
					pageNo = View.HOME;
				}else {
					System.out.println("비밀번호 수정 실패!");
					pageNo = View.FIND_PW;
				}
			}else {
				System.out.println("비밀번호가 일치하지 않습니다!");
				pageNo = View.FIND_PW;
			}
		}else {
			System.out.println("존재하지 않는 회원입니다!");
			pageNo = View.HOME;
		}
		
		System.out.println("enter 키를 눌러주세요");
		ScanUtil.nextLine();
		return pageNo;
	}
	
	public int userLogout() { // 로그아웃 완료
		Controller.sessionStorage.put("login", false);
		Controller.sessionStorage.put("loginInfo", null);
		try {
			System.out.println("------로그아웃------");
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return View.HOME;
	}
	
	private void clear() {
		for (int i = 0; i < 50; i++) {
			System.out.println();
		}
	}
}	
