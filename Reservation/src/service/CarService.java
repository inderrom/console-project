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
import dao.CarDAO;
import util.ScanUtil;
import util.SpaceUtil;
import util.View;

public class CarService {
	private static CarService instance = null;

	private CarService() {
	};

	public static CarService getInstance() {
		if (instance == null)
			instance = new CarService();
		return instance;
	}

	CarDAO carDAO = CarDAO.getInstance();
	String inputDate = ""; // 예약시작날짜(입력)
	String outputDate = ""; // 예약반납날짜(입력)
	String carName = "";// 차량명입력
	List<Map<String, Object>> carList2 = new ArrayList<>();
	Map<String, Object> selectCar = new HashMap<>();// c_no를 누르면 찾아주는거
	String carDivision = ""; // 차종 구분입력(c_no)
	int totalPrice; // 일수*대여료
	int period;// 일수
	int dd; // 차 예약을 할때마다 날짜 기준으로 STATE 바꿔주는거
	int license;
	String selectCard;

	public int card_regist() {
		String cardName = "", card = "";

		car1d: while (true) {
			System.out.println();
			System.out.println();
			System.out.println("등록할 카드명을 입력해주세요 (2~8자리)");
			cardName = ScanUtil.nextLine();
			if (!cardName(cardName)) {
				System.out.println("카드 형식을 확인해주세요");
			} else {
				break car1d;
			}
		}
		card: while (true) {
			System.out.println("카드 번호를 입력해주세요 (ex 5xxx-xxxx-xxxx-xxxx) 숫자 16자리");
			System.out.print("입력 >> ");
			card = ScanUtil.nextLine();
			if (!cardCheck(card)) {
				System.out.println("카드 형식을 확인해주세요");
			} else {
				break card;
			}
		}

		List<Object> param = new ArrayList<>();
		Map<String, Object> userInfo = (Map<String, Object>) Controller.sessionStorage.get("userInfo");

		param.add(userInfo.get("MEM_ID"));
		param.add(cardName);
		param.add(card);

		int result = carDAO.card_insert(param);
		if (result > 0) {
			System.out.println("카드 등록이 완료되었습니다.");
			System.out.println("메인 화면 창으로 다시 넘어갑니다.");
			return View.MAIN;
		} else {
			System.out.println("카드 등록에 실패하였습니다.");
		}
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return View.MAIN;
	}

	public boolean cardName(String card) { // 카드 이름
		return Pattern.matches("^[가-힣0-9a-zA-Z]{2,8}$", card);
	}

	public boolean cardCheck(String card) { // 신용카드
		return Pattern.matches("^((5[1-5])([0-9]{2})((-|\\s)?[0-9]{4}){3})$", card);
	}

	private boolean dayCheck(String birth) { // 날짜 체크
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);

		date.setLenient(false);
		try {
			date.parse(birth);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public int carre() {
		dd = 2;
		List<Map<String, Object>> reservationCar = carDAO.reservationCar();
		
		SimpleDateFormat simple = new SimpleDateFormat("yyyyMMdd");
		String date = simple.format(new Date());
		int today = Integer.parseInt(date); // 현재날짜
		
		if(reservationCar.size() != 0) {
			for (int i = 0; i < reservationCar.size(); i++) {
				List<Object> param = new ArrayList<>();
				
				String PB_STARTDATE = simple.format(reservationCar.get(i).get("CB_STARTDATE"));	//시작 날짜
				String PB_ENDDATE = simple.format(reservationCar.get(i).get("CB_ENDDATE"));		//끝 날짜
				
				if (Integer.parseInt(PB_STARTDATE) <= today) {	// 시작날 <= 현재날
					param.add(1);
					param.add(reservationCar.get(i).get("CB_NO").toString());
					carDAO.carState(param);
				}
				
				List<Object> param1 = new ArrayList<>();
				if (Integer.parseInt(PB_ENDDATE) < today) { 	// 마지막날<현재날
					param1.add(0);
					param1.add(reservationCar.get(i).get("CB_NO").toString());
					carDAO.carState(param1);
				}
			}
			
		}
		
		return View.RESERVATIONSTART;
	}
		
		public int reservationstart() throws InterruptedException {
			List<Map<String, Object>> reservationCar = carDAO.reservationCar();
			String pattern = "yyyyMMdd";
			SimpleDateFormat simple = new SimpleDateFormat(pattern);
			String date = simple.format(new Date());

			int today = Integer.parseInt(date); // 현재날짜
			int result = 0;
			List<Object> param0 = new ArrayList<>();
			param0.add(Controller.sessionStorage.get("MEM_ID"));
			Map<String, Object> selectLicence = carDAO.selectLicence(param0);

			if (selectLicence.get("MEM_LICENSE") == null
					|| Integer.parseInt(String.valueOf(selectLicence.get("MEM_LICENSE"))) == 0) {
				System.out.println(Controller.sessionStorage.get("MEM_ID") + "님은");
				System.out.println("면허 인증이 되지 않은 계정입니다");
				System.out.println("면허 인증 하신 후 이용 부탁드립니다");
				System.out.println("감사합니다");
				System.out.println();
				System.out.println();
				System.out.println("메인화면으로 돌아가겠습니다.");
				System.out.println();
				System.out.println();
				Thread.sleep(1000);
				
				return View.MAIN;
			}

			start: while (true) {
				System.out.println("대여  날짜를 입력해주세요(EX 20220908) (뒤로가기 B)>> ");
				inputDate = ScanUtil.nextLine();
				inputDate=(inputDate.toUpperCase()).trim();
				if(inputDate.equals("B")) {
					return View.MAIN;
				}
				 else if (!dayCheck(inputDate)) {
						System.out.println("날짜 형식을 확인해주세요 ");
					}
				else if (Integer.parseInt(inputDate) < today) {
					System.out.println("현재시간보다 과거입니다 ");
				} else {
					break start;
				}
			}
			end: while (true) {
				System.out.println("반납 날짜를 입력해주세요 (EX 20220908) (뒤로가기 B) >>");
				outputDate = ScanUtil.nextLine();
				outputDate=(outputDate.toUpperCase()).trim();
				if(outputDate.equals("B")) {
					return View.MAIN;
				}
				else if (!dayCheck(outputDate)) {
					System.out.println("날짜 형식을 확인해주세요 ");
				}
				else if (Integer.parseInt(outputDate) < today) {
					System.out.println("현재시간보다 과거입니다 ");
				} else {
					break end;
				}
			}

			int start = Integer.parseInt(inputDate); // 예약시작날짜 정수형으로 변환
			int end = Integer.parseInt(outputDate); // 예약반납날짜 정수형으로 변환

			/*
			 * if (start < today) { // 마지막날<현재날 dd = 0; }
			 * 
			 * if (start <= today && end >= today) { dd = 1; }
			 * 
			 * // 현재날짜<시작날짜 if (today < start) { dd = 2; }
			 */
			while (true) {
				if (Integer.parseInt(inputDate) > Integer.parseInt(outputDate)) {
					System.out.println("입력일자가 반납일자 보다 큽니다 ");
					System.out.println("다시 입력해주세요!");
					result = View.CARRE;
					return result;
				}
				break;
			}

			int mem_license = Integer.parseInt(String.valueOf(selectLicence.get("MEM_LICENSE")));
			license = mem_license;
			if (license == 1) {
				result = View.LICENSE1;
				return result;
			}

			else if (license == 2) {
				result = View.LICENSE2;
				return result;
			}

			return result;
		}
	


	public void carAllList() {
		System.out.println("┌──────────────────────────────────────────────────────────────────────────────────┐");
		System.out.printf("│   %s │\n", SpaceUtil.format("예약 가능 차량", 78, 0));
		System.out.printf("├──────────────────────┬────────────────────┬────────────────────┬─────────────────┤\n");
		System.out.printf("│%s│%s│%s│%s│\n", SpaceUtil.format("차량명", 22, 0), SpaceUtil.format("가능면허", 20, 0),
				SpaceUtil.format("차량인승", 20, 0), SpaceUtil.format("차량가격", 17, 0));

		List<Map<String, Object>> carNameList = carDAO.carNameList(inputDate, outputDate); // 이거 씀 상세보기
		for (Map<String, Object> u : carNameList) {
			System.out.printf("├──────────────────────┼────────────────────┼────────────────────┼─────────────────┤\n");
			System.out.printf("│%s│%s│%s│%s│\n", SpaceUtil.format(u.get("C_NAME"), 22, 0),
					SpaceUtil.format(u.get("C_LICENCE") + "종", 20, 0), SpaceUtil.format(u.get("C_MEN") + "인승", 20, 0),
					SpaceUtil.format(u.get("C_PRICE") + "원", 17, 0));
		}
		System.out.printf("└──────────────────────┴────────────────────┴────────────────────┴─────────────────┘\n");

	}

	public void carAllList2() {
		List<Object> param0 = new ArrayList<>();
		param0.add(Controller.sessionStorage.get("MEM_ID"));
		Map<String, Object> selectLicence = carDAO.selectLicence(param0);

		System.out.println(Controller.sessionStorage.get("MEM_ID") + "님의 면허 종류는 " + selectLicence.get("MEM_LICENSE")
				+ "종 이므로 해당 차량만 출력합니다");

		int result = 0;

		System.out.println("┌──────────────────────────────────────────────────────────────────────────────────┐");
		System.out.printf("│   %s │\n", SpaceUtil.format("2종 예약 가능 차량", 78, 0));
		System.out.printf("├──────────────────────┬────────────────────┬────────────────────┬─────────────────┤\n");
		System.out.printf("│%s│%s│%s│%s│\n", SpaceUtil.format("차량명", 22, 0), SpaceUtil.format("가능면허", 20, 0),
				SpaceUtil.format("차량인승", 20, 0), SpaceUtil.format("차량가격", 17, 0));
		List<Map<String, Object>> carNameList = carDAO.carNameList2(inputDate, outputDate,
				selectLicence.get("MEM_LICENSE")); // 이거 씀 상세보기
		for (Map<String, Object> u : carNameList) {
			System.out.printf("├──────────────────────┼────────────────────┼────────────────────┼─────────────────┤\n");
			System.out.printf("│%s│%s│%s│%s│\n", SpaceUtil.format(u.get("C_NAME"), 22, 0),
					SpaceUtil.format(u.get("C_LICENCE") + "종", 20, 0), SpaceUtil.format(u.get("C_MEN") + "인승", 20, 0),
					SpaceUtil.format(u.get("C_PRICE") + "원", 17, 0));

		}
		System.out.printf("└──────────────────────┴────────────────────┴────────────────────┴─────────────────┘\n");
		System.out.println();
	}

	public int license1() {
		int result = 0;

		carAllList();
		System.out.println();

		while (true) {
			System.out.println("1. 차량예약 2. 뒤로가기(날짜입력)");
			int num = ScanUtil.nextInt();
			
			switch (num) {
			case 1:
				clear();
				result = View.RESERVATION;
				return result;
			case 2:
				result = View.RESERVATIONSTART; // 메인화면으로
				return result;
			default:
				System.out.println("잘못 입력하셨습니다!");
				System.out.println("다시 입력해주세요!");
				continue;
			}

		}

//		return result;
	}

	private void clear() {
		for (int i = 0; i < 50; i++) {
			System.out.println();
		}
	}

	public int reservation() {
		int result = 0;

		if (license == 1) {
			while (true) {
				carAllList();
				System.out.println();
				System.out.print("대여할 차량명을 입력해주세요(뒤로가기 : B) >> ");
				carName = ScanUtil.nextLine();
				carName = (carName.toUpperCase()).trim();

				if (carName.equals("B")) {
					result = View.LICENSE1;
					return result;
				}
				carList2 = carDAO.carList(inputDate, outputDate, carName); // 이거씀 차량명 입력후 그것만 나오게

				if (carList2.size() == 0) {
					System.out.println();
					System.out.println();
					System.out.println("[" + carName + "] 차량은 존재하지 않습니다.");
					System.out.println("다시 입력 바랍니다.");
					result = View.RESERVATION;

				} else {
					result = View.CARDETAIL;
				}
				break;
			}
		}

		else if (license == 2) {
			while (true) {
				carAllList2();
				System.out.println();
				System.out.print("대여할 차량명을 입력해주세요(뒤로가기 : B) >> ");
				carName = ScanUtil.nextLine();
				carName = (carName.toUpperCase()).trim();

				if (carName.equals("B")) {
					result = View.LICENSE2;
					return result;
				}

				carList2 = carDAO.carList(inputDate, outputDate, carName); // 이거씀 차량명 입력후 그것만 나오게

				if (carList2.size() == 0) {
					System.out.println();
					System.out.println();
					System.out.println("[" + carName + "] 차량은 존재하지 않습니다.");
					System.out.println("다시 입력 바랍니다.");
					result = View.RESERVATION;

				} else {
					result = View.CARDETAIL;
				}
				break;
			}
		}

		return result;
	}
	
	public int license2() {
		List<Object> param0 = new ArrayList<>();
		param0.add(Controller.sessionStorage.get("MEM_ID"));
		Map<String, Object> selectLicence = carDAO.selectLicence(param0);

		System.out.println(Controller.sessionStorage.get("MEM_ID") + "님의 면허 종류는 " + selectLicence.get("MEM_LICENSE")
				+ "종 이므로 해당 차량만 출력합니다");

		int result = 0;

		System.out.println("┌──────────────────────────────────────────────────────────────────────────────────┐");
		System.out.printf("│   %s │\n", SpaceUtil.format("2종 예약 가능 차량", 78, 0));
		System.out.printf("├──────────────────────┬────────────────────┬────────────────────┬─────────────────┤\n");
		System.out.printf("│%s│%s│%s│%s│\n", SpaceUtil.format("차량명", 22, 0), SpaceUtil.format("가능면허", 20, 0),
				SpaceUtil.format("차량인승", 20, 0), SpaceUtil.format("차량가격", 17, 0));
		List<Map<String, Object>> carNameList = carDAO.carNameList2(inputDate, outputDate,
				selectLicence.get("MEM_LICENSE")); // 이거 씀 상세보기
		for (Map<String, Object> u : carNameList) {
			System.out.printf("├──────────────────────┼────────────────────┼────────────────────┼─────────────────┤\n");
			System.out.printf("│%s│%s│%s│%s│\n", SpaceUtil.format(u.get("C_NAME"), 22, 0),
					SpaceUtil.format(u.get("C_LICENCE") + "종", 20, 0), SpaceUtil.format(u.get("C_MEN") + "인승", 20, 0),
					SpaceUtil.format(u.get("C_PRICE") + "원", 17, 0));

		}
		System.out.printf("└──────────────────────┴────────────────────┴────────────────────┴─────────────────┘\n");
		System.out.println();

		while (true) {
			System.out.println("1. 차량예약 2. 뒤로가기(메인화면)");
			int num = ScanUtil.nextInt();

			switch (num) {
			case 1:
				result = View.RESERVATION;
				return result;
			case 2:
				result = View.MAIN; // 날짜입력부터 다시가기
				return result;
			default:
				System.out.println("잘못 입력하셨습니다!");
				System.out.println("다시 입력해주세요!");
				continue;
			}

		}
	}

	public void carList2() {
		System.out
				.printf("┌───────────────┬──────────────┬────────────┬───────────────┬──────────────┬─────────────┐\n");
		System.out.printf("│%s│%s│%s│%s│%s│%s│\n", SpaceUtil.format(("차량구분"), 15, 0), SpaceUtil.format(("차량명"), 14, 0),
				SpaceUtil.format(("대여가능면허"), 10, 0), SpaceUtil.format(("탑승인수"), 15, 0),
				SpaceUtil.format(("번호판"), 14, 0), SpaceUtil.format(("1일대여가격"), 13, 0));

		for (Map<String, Object> u : carList2) {
			System.out.printf(
					"├───────────────┼──────────────┼────────────┼───────────────┼──────────────┼─────────────┤\n");
			System.out.printf("│%s│%s│%s│%s│%s│%s│\n", SpaceUtil.format(u.get("C_NO"), 15, 0),
					SpaceUtil.format(u.get("C_NAME"), 14, 0), SpaceUtil.format(u.get("C_LICENCE") + "종", 12, 0),
					SpaceUtil.format(u.get("C_MEN") + "인승", 15, 0), SpaceUtil.format(u.get("C_NUM"), 14, 0),
					SpaceUtil.format(u.get("C_PRICE") + "원", 13, 0));
		}
		System.out
				.printf("└───────────────┴──────────────┴────────────┴───────────────┴──────────────┴─────────────┘\n");
	}

	public int carDetail() {
		int result = 0;

		System.out.println();
		System.out.println(carName + "의 대여 가능한 차량 상세 현황입니다.");
		carList2();

		System.out.println("대여하실 차량의 차종 구분을 입력해주세요(EX K002) (뒤로가기 : B) ");
		carDivision = ScanUtil.nextLine();
		carDivision = (carDivision.toUpperCase()).trim();

		if (carDivision.equals("B")) {
			result = View.RESERVATION;
			return result;
		}

		else {
			result = View.INPUTCNO;
		}

		return result;
	}

	public int inputCno() {
		int result = 0;

		List<Object> param3 = new ArrayList<>();
		param3.add(carDivision);
		selectCar = carDAO.selectCar(param3);

		carList2();

		if (selectCar == null) {
			System.out.println();
			System.out.println();
			System.out.println("죄송합니다.");
			System.out.println("차량구분 [" + carDivision + "] 은 존재하지 않습니다");
			result = View.CARDETAIL;
			return result;
		} else {
			int start = Integer.parseInt(inputDate); // 예약시작날짜 정수형으로 변환
			int end = Integer.parseInt(outputDate); // 예약반납날짜 정수형으로 변환

			// 입력날짜처리
			int year = start / 10000;
			start = start % 10000;
			int month = start / 100;
			int day = start % 100;

			// 반납날짜처리
			int year2 = end / 10000;
			end = end % 10000;
			int month2 = end / 100;
			int day2 = end % 100;

			String s_price = String.valueOf(selectCar.get("C_PRICE"));
			int price = Integer.parseInt(s_price);
			for (int i = 0; i < 100; i++) {
				System.out.println();
			}
			System.out.println("            예약 상세 정보           ");
			System.out.println("                                     ");
			System.out.println("차량구분번호 : " + selectCar.get("C_NO"));
			System.out.println("차량이름 : " + selectCar.get("C_NAME"));
			System.out.println("차량필요면허 : " + selectCar.get("C_LICENCE"));
			System.out.println("차량탑승인승 : " + selectCar.get("C_MEN"));
			System.out.println("차량번호판 : " + selectCar.get("C_NUM"));
			System.out.printf("차량대여가격(1일) : %,3d원\n", price);
			System.out.printf("대여시작일 : %d/%2d/%2d\n", year, month, day);
			System.out.printf("대여반납일 : %d/%2d/%2d\n", year2, month2, day2);
			while (true) {
				System.out.println();
				System.out.print("마일리지를 사용하시겠습니까? (Y/N) (뒤로가기 : B)");
				String answer = ScanUtil.nextLine();
				answer = (answer.toUpperCase()).trim();
				if (answer.equals("Y")) {
					return View.MILEAGE;
				} else if (answer.equals("N")) {
					return View.PAYMENT;
				} else if (answer.equals("B")) {
					return View.CARDETAIL;
				} else {
					System.out.println("잘못된 명령어 입니다");
					continue;
				}
			}
		}
	}

	public int payment() {

		int result = 0;
		String strFormat = "yyyyMMdd"; // strStartDate 와 strEndDate 의 format
		// SimpleDateFormat 을 이용하여 startDate와 endDate의 Date 객체를 생성한다.
		SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
		int c_price = Integer.parseInt(String.valueOf(selectCar.get("C_PRICE")));

		try {
			Date startDate = sdf.parse(inputDate);
			Date endDate = sdf.parse(outputDate);
			// 두날짜 사이의 시간 차이(ms)를 하루 동안의 ms(24시*60분*60초*1000밀리초) 로 나눈다.
			long diffDay = (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
			int diffDay2 = (int) diffDay;

			totalPrice = (diffDay2) * (c_price);
			period = diffDay2;
			String price3 = String.valueOf(selectCar.get("C_PRICE"));
			int carPay = Integer.parseInt(price3);
			int mileage = ((int) (carPay * 0.05)) * period;
			int totalMileage = 0;

			Map<String, Object> membergetMileage = carDAO.membergetMileage(Controller.sessionStorage.get("MEM_ID"));

			// 카드생성
			Map<String, Object> card = carDAO.card(Controller.sessionStorage.get("MEM_ID"));
			if (card == null) {
				System.out.println(Controller.sessionStorage.get("MEM_ID") + "님");
				System.out.println("결제 카드 등록이 되지 않았습니다");
				System.out.println("카드 등록 창으로 이동하겠습니다.");
				card_regist();
				return View.PAYMENT;
			} else { // 카드가 있을 시
				card: while (true) {
					System.out.println("현재 등록된 카드가 존재합니다");
					System.out.println("1. 등록카드결제 2. 신규카드결제 3. 뒤로가기");
					int num = ScanUtil.nextInt();

					switch (num) {
					case 1:
						List<Object> param2 = new ArrayList<>();
						param2.add(Controller.sessionStorage.get("MEM_ID"));
						List<Map<String, Object>> findCard = carDAO.findCard(param2);
						System.out.println(Controller.sessionStorage.get("MEM_ID") + "님 보유카드");
						int cnt = 0;
						System.out.println("┌───────────────────┬─────────────────────────────────┐");
						for (Map<String, Object> u : findCard) {
							cnt++;
							System.out.printf("│카드명 : %s│카드번호 : %s│"
									, SpaceUtil.format(u.get("CARD_NAME"), 10, 0)
									, SpaceUtil.format(u.get("CARD_NUMBER"), 22, 0));
							if(findCard.size()==cnt) {
								System.out.println("\n└───────────────────┴─────────────────────────────────┘");
							}else {
								System.out.printf("\n├───────────────────┼─────────────────────────────────┤\n");
							}
						}
						System.out.print("선택하실 카드번호를 입력해주세요 : ");
						selectCard = ScanUtil.nextLine();

						break card;
					case 2:
						card_regist();
						return View.MAIN;
					case 3:
						return View.CARDETAIL;
					default:
						System.out.println("잘못된 명령어입니다");
						System.out.println("다시 입력해주세요");
						continue;
					}
				}
				List<Object> param = new ArrayList<>();
				param.add(carDivision);
				param.add(carDivision);
				param.add(Controller.sessionStorage.get("MEM_ID"));
				param.add(inputDate);
				param.add(outputDate);
				param.add(totalPrice);
				param.add(dd);
				param.add(license);
				result = carDAO.carReservation(param);
				System.out.println(Controller.sessionStorage.get("MEM_ID") + "님");
				System.out.printf("예약차량 : [%s]\n", selectCar.get("C_NAME"));
				System.out.printf("예약금액 : %,3d원\n", totalPrice);
				System.out.printf("예약일수 : %d일\n", period);
				System.out.println("");

				while (true) {
					System.out.print("결제를 진행하시겠습니까?? (y/n)");
					String answer = ScanUtil.nextLine();
					answer = (answer.toUpperCase()).trim();
					System.out.println();
					switch (answer) {
					case "Y":
						Map<String, Object> cardSearch = carDAO.cardSearch(selectCard);
						int mem_mile=Integer.parseInt(String.valueOf(membergetMileage.get("MEM_MILE"))); //기존 회원 마일리지
						System.out.println(Controller.sessionStorage.get("MEM_ID") + "님");
						System.out.printf("카드이름 : %s카드\n", (String) cardSearch.get("CARD_NAME"));
						System.out.printf("카드번호 : %s\n", (String) cardSearch.get("CARD_NUMBER"));
						System.out.printf("예약금액 : %,3d원\n", totalPrice);
						System.out.println("적립마일리지 : " + mileage + "점");
						System.out.println("예약이 완료되었습니다!!");
						System.out.println();
						System.out.println();
						totalMileage = totalMileage + mileage+mem_mile;
						List<Object> memMilage = new ArrayList<>();

						memMilage.add(totalMileage);
						memMilage.add(Controller.sessionStorage.get("MEM_ID"));
						carDAO.carMileage(memMilage);
						result = View.MAIN;
						return result;
					case "N":
						System.out.println("결제창으로 이동하겠습니다.");
						System.out.println();
						result = View.ALLMILEAGE;
						return result;
					default:
						System.out.println("잘못된 명령어입니다.");
						System.out.println("다시 입력해주세요.");
						continue;
					}
				}
			}
		} catch (Exception e) {

		} finally {

		}
		return result;
	}
//			param=new ArrayList<>();
//			System.out.println(Controller.sessionStorage.get("MEM_ID") + "님");
//			System.out.println();
//			System.out.println();
//			param.add(carDivision);
//			param.add(carDivision);
//			param.add(Controller.sessionStorage.get("MEM_ID"));
//			param.add(inputDate);
//			param.add(outputDate);
//			param.add(totalPrice);
//			param.add(dd);
//			param.add(license);
//			result = carDAO.carReservation(param);
//
//			System.out.println();
//			System.out.println();
//			System.out.println(Controller.sessionStorage.get("MEM_ID") + "님");
//			System.out.printf("예약차량 : [%s]\n", selectCar.get("C_NAME"));
//			System.out.printf("예약금액 : %,3d원\n", totalPrice);
//			System.out.printf("예약일수 : %d일\n", period);
//			System.out.println("예약이 완료되었습니다!!");
//			String mile = String.valueOf(membergetMileage.get("MEM_MILE"));
//			int mile2 = Integer.parseInt(mile);
//			totalMileage = totalMileage + mileage + mile2;
//			List<Object> memMilage = new ArrayList<>();
//			memMilage.add(totalMileage);
//			memMilage.add(Controller.sessionStorage.get("MEM_ID"));
//			carDAO.carMileage(memMilage);
//
//			result = View.MAIN;
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}

	public int mileage() {
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

		int result = 0;
		String strFormat = "yyyyMMdd"; // strStartDate 와 strEndDate 의 format
		// SimpleDateFormat 을 이용하여 startDate와 endDate의 Date 객체를 생성한다.
		SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
		int c_price = Integer.parseInt(String.valueOf(selectCar.get("C_PRICE")));
		int start = Integer.parseInt(inputDate); // 예약시작날짜 정수형으로 변환
		int end = Integer.parseInt(outputDate); // 예약반납날짜 정수형으로 변환

		// 입력날짜처리
		int year = start / 10000;
		start = start % 10000;
		int month = start / 100;
		int day = start % 100;

		// 반납날짜처리
		int year2 = end / 10000;
		end = end % 10000;
		int month2 = end / 100;
		int day2 = end % 100;
		try {
			Date startDate = sdf.parse(inputDate);
			Date endDate = sdf.parse(outputDate);
			// 두날짜 사이의 시간 차이(ms)를 하루 동안의 ms(24시*60분*60초*1000밀리초) 로 나눈다.
			long diffDay = (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
			int diffDay2 = (int) diffDay;
			System.out.println();
			System.out.println();
			System.out.printf("대여료 합계 : %,3d원(%d일 대여)\n", (diffDay2) * (c_price), diffDay2);
			System.out.println();
			totalPrice = (diffDay2) * (c_price);
			period = diffDay2;
			while (true) {
				System.out.println("1. 전부사용 2. 일부사용 (뒤로가기 : B) ");
				String mileAnswer = ScanUtil.nextLine();
				mileAnswer = (mileAnswer.toUpperCase()).trim();

				if (mileAnswer.equals("B")) {
					result = View.INPUTCNO;
					return result;
				} else if (mileAnswer.equals("1")) {
					result = View.ALLMILEAGE;
					return result;
				} else if (mileAnswer.equals("2")) {
					result = View.PARTITIONMILEAGE;
					return result;
				} else {
					System.out.println("잘못된 명령어입니다.");
					System.out.println("다시 입력해주시기 바랍니다.");
					continue;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int allmileage() throws InterruptedException {
		int result = 0;
		String price3 = String.valueOf(selectCar.get("C_PRICE"));
		int carPay = Integer.parseInt(price3);
		int mileage = ((int) (carPay * 0.05)) * period;
		int totalMileage = 0;
		String selectCard = "";
		Map<String, Object> membergetMileage = carDAO.membergetMileage(Controller.sessionStorage.get("MEM_ID"));
		String memMile = String.valueOf(membergetMileage.get("MEM_MILE"));
		int memMile2 = Integer.parseInt(memMile); // 회원의 마일리지 정수형으로 바꾼거
//		System.out.println(memMile2);
		
		if (memMile2 == 0) { // 마일리지가 없을때
			memMile2 = 0;
			System.out.println("내 보유 마일리지 : " + memMile2 + "점");
			System.out.println(Controller.sessionStorage.get("MEM_ID") + "님");
			System.out.println("보유중인 마일리지가 없습니다.");

			for (int i = 1; i <= 3; i++) {
				Thread.sleep(1000);
			}

			for (int i = 0; i < 100; i++) {
				System.out.println();
			}

			// 카드생성
			Map<String, Object> card = carDAO.card(Controller.sessionStorage.get("MEM_ID"));
			if (card == null) {
				System.out.println(Controller.sessionStorage.get("MEM_ID") + "님");
				System.out.println("결제 카드 등록이 되지 않았습니다");
				System.out.println("카드 등록 창으로 이동하겠습니다.");
				card_regist();
				return View.ALLMILEAGE;
			} else { // 카드가 있을 시
				card: while (true) {
					System.out.println("현재 등록된 카드가 존재합니다");
					System.out.println("1. 등록카드결제 2. 신규카드결제 3. 뒤로가기");
					int num = ScanUtil.nextInt();

					switch (num) {
					case 1:
						List<Object> param = new ArrayList<>();
						param.add(Controller.sessionStorage.get("MEM_ID"));
						List<Map<String, Object>> findCard = carDAO.findCard(param);
						System.out.println(Controller.sessionStorage.get("MEM_ID") + "님 보유카드");
						
						int cnt = 0;
						System.out.println("┌───────────────────┬─────────────────────────────────┐");
						for (Map<String, Object> u : findCard) {
							cnt++;
							System.out.printf("│카드명 : %s│카드번호 : %s│"
									, SpaceUtil.format(u.get("CARD_NAME"), 10, 0)
									, SpaceUtil.format(u.get("CARD_NUMBER"), 22, 0));
							if(findCard.size()==cnt) {
								System.out.println("\n└───────────────────┴─────────────────────────────────┘");
							}else {
								System.out.printf("\n├───────────────────┼─────────────────────────────────┤\n");
							}
						}
						System.out.print("선택하실 카드번호를 입력해주세요 : ");
						selectCard = ScanUtil.nextLine();
						param.remove(0);
						break card;
					case 2:
						card_regist();
						return View.ALLMILEAGE;
					case 3:
						return View.CARDETAIL;
					default:
						System.out.println("잘못된 명령어입니다");
						System.out.println("다시 입력해주세요");
						continue;
					}
				}
				List<Object> param = new ArrayList<>();
			
				
				System.out.println(Controller.sessionStorage.get("MEM_ID") + "님");
				System.out.printf("예약차량 : [%s]\n", selectCar.get("C_NAME"));
				System.out.printf("예약금액 : %,3d원\n", totalPrice);
				System.out.printf("예약일수 : %d일\n", period);
				System.out.println("");
				while (true) {
					System.out.print("결제를 진행하시겠습니까?? (y/n)");
					String answer = ScanUtil.nextLine();
					answer = (answer.toUpperCase()).trim();
					System.out.println();
					switch (answer) {
					case "Y":
						Map<String, Object> cardSearch = carDAO.cardSearch(selectCard);

						System.out.println(Controller.sessionStorage.get("MEM_ID") + "님");
						System.out.printf("카드이름 : %s카드\n", (String) cardSearch.get("CARD_NAME"));
						System.out.printf("카드번호 : %s\n", (String) cardSearch.get("CARD_NUMBER"));
						System.out.printf("예약금액 : %,3d원\n", totalPrice);
						System.out.println("적립마일리지 : " + mileage + "점");
						System.out.println("예약이 완료되었습니다!!");
						
						ScanUtil.nextLine();
						
						param.add(carDivision);
						param.add(carDivision);
						param.add(Controller.sessionStorage.get("MEM_ID"));
						param.add(inputDate);
						param.add(outputDate);
						param.add(totalPrice);
						param.add(dd);
						param.add(license);
						
						result = carDAO.carReservation(param);
						
						
						System.out.println();
						System.out.println();
						totalMileage = totalMileage + mileage;
						List<Object> memMilage = new ArrayList<>();

						memMilage.add(totalMileage);
						memMilage.add(Controller.sessionStorage.get("MEM_ID"));
						carDAO.carMileage(memMilage);
						result = View.MAIN;
						System.out.println("enter 키를 입력해주세요");
						
						Thread.sleep(1000);
						
						return result;
					case "N":
						System.out.println("결제창으로 이동하겠습니다.");
						System.out.println();
						result = View.ALLMILEAGE;
						return result;
					default:
						System.out.println("잘못된 명령어입니다.");
						System.out.println("다시 입력해주세요.");
						continue;
					}
				}
			}
		}

		else {// 마일리지가 있고 전부 다 써버릴때 여기서 결제까지 한번에
			List<Object> mileageUpdate = new ArrayList<>();
			System.out.printf("대여료 %d원에서 %d점 마일리지 차감합니다.\n", totalPrice, memMile2);
			totalPrice = totalPrice - memMile2;
		
			System.out.println();
			System.out.println();
			// 카드생성
			Map<String, Object> card = carDAO.card(Controller.sessionStorage.get("MEM_ID"));
			if (card == null) {
				System.out.println(Controller.sessionStorage.get("MEM_ID") + "님");
				System.out.println("결제 카드 등록이 되지 않았습니다");
				System.out.println("카드 등록 창으로 이동하겠습니다.");
				card_regist();
				return View.ALLMILEAGE;
			} else { // 카드가 있을 시
				card: while (true) {
					System.out.println("현재 등록된 카드가 존재합니다");
					System.out.println("1. 등록카드결제 2. 신규카드결제 3. 뒤로가기");
					int num = ScanUtil.nextInt();

					switch (num) {
					case 1:
						List<Object> param2 = new ArrayList<>();
						//왜 첫번째 때는 잘 되는데 똑같이 2번째 할려고하면 널값이 터질까...
						param2.add(Controller.sessionStorage.get("MEM_ID"));
						List<Map<String, Object>> findCard = carDAO.findCard(param2);
						System.out.println(Controller.sessionStorage.get("MEM_ID") + "님 보유카드");
						int cnt = 0;
						System.out.println("┌───────────────────┬─────────────────────────────────┐");
						for (Map<String, Object> u : findCard) {
							cnt++;
							System.out.printf("│카드명 : %s│카드번호 : %s│"
									, SpaceUtil.format(u.get("CARD_NAME"), 10, 0)
									, SpaceUtil.format(u.get("CARD_NUMBER"), 22, 0));
							if(findCard.size()==cnt) {
								System.out.println("\n└───────────────────┴─────────────────────────────────┘");
							}else {
								System.out.printf("\n├───────────────────┼─────────────────────────────────┤\n");
							}
						}
						System.out.print("선택하실 카드번호를 입력해주세요 : ");
						selectCard = ScanUtil.nextLine();
						param2.remove(0);
						break card;
					case 2:
						card_regist();
						return View.ALLMILEAGE;
					case 3:
						return View.CARDETAIL;
					default:
						System.out.println("잘못된 명령어입니다");
						System.out.println("다시 입력해주세요");
						continue;
					}
					
					
				}
			List<Object> param = new ArrayList<>();
			
				param.add(carDivision);
				param.add(carDivision);
				param.add(Controller.sessionStorage.get("MEM_ID"));
				param.add(inputDate);
				param.add(outputDate);
				param.add(totalPrice);
				param.add(dd);
				param.add(license);
				result = carDAO.carReservation(param);
				System.out.println(Controller.sessionStorage.get("MEM_ID") + "님");
				System.out.printf("예약차량 : [%s]\n", selectCar.get("C_NAME"));
				System.out.printf("예약금액 : %,3d원\n", totalPrice);
				System.out.printf("예약일수 : %d일\n", period);
				System.out.println("");

				while (true) {
					System.out.print("결제를 진행하시겠습니까?? (y/n)");
					String answer = ScanUtil.nextLine();
					answer = (answer.toUpperCase()).trim();
					System.out.println();
					switch (answer) {
					case "Y":
						Map<String, Object> cardSearch = carDAO.cardSearch(selectCard);
						totalPrice = totalPrice - memMile2;
						mileageUpdate.add(0);
						mileageUpdate.add(Controller.sessionStorage.get("MEM_ID"));
						carDAO.carMileage(mileageUpdate);

						System.out.println(Controller.sessionStorage.get("MEM_ID") + "님");
						System.out.printf("카드이름 : %s카드\n", (String) cardSearch.get("CARD_NAME"));
						System.out.printf("카드번호 : %s\n", (String) cardSearch.get("CARD_NUMBER"));
						System.out.printf("예약금액 : %,3d원\n", totalPrice);
						System.out.println("적립마일리지 : " + mileage + "점");
						System.out.println("예약이 완료되었습니다!!");
						System.out.println();
						System.out.println();
						totalMileage = totalMileage + mileage;
						List<Object> memMilage = new ArrayList<>();

						memMilage.add(totalMileage);
						memMilage.add(Controller.sessionStorage.get("MEM_ID"));
						carDAO.carMileage(memMilage);
						ScanUtil.nextLine();
						result = View.MAIN;
						return result;
					case "N":
						System.out.println("결제창으로 이동하겠습니다.");
						System.out.println();
						result = View.ALLMILEAGE;
						return result;
					default:
						System.out.println("잘못된 명령어입니다.");
						System.out.println("다시 입력해주세요.");
						continue;
					}

				}

			}
		}
	}

	public int partitionmileage() {
		int result = 0;
		String price3 = String.valueOf(selectCar.get("C_PRICE"));
		int carPay = Integer.parseInt(price3);

		int mileage = ((int) (carPay * 0.05)) * period;
		int totalMileage = 0;

		Map<String, Object> membergetMileage = carDAO.membergetMileage(Controller.sessionStorage.get("MEM_ID"));
		String memMile = String.valueOf(membergetMileage.get("MEM_MILE"));
		int memMile2 = Integer.parseInt(memMile); // 회원의 마일리지 정수형으로 바꾼거

		if (memMile2 == 0) { // 마일리지가 없을때
			System.out.println("내 보유 마일리지 : [" + memMile2 + "]점");
			System.out.println(Controller.sessionStorage.get("MEM_ID") + "님");
			System.out.println("보유중인 마일리지가 없습니다.");
			System.out.println("결제란으로 이동하겠습니다.");
			System.out.println();
			System.out.println();

			// 카드가 없을때 자동 생성
			Map<String, Object> card = carDAO.card(Controller.sessionStorage.get("MEM_ID"));
			if (card == null) {
				System.out.println(Controller.sessionStorage.get("MEM_ID") + "님");
				System.out.println("결제 카드 등록이 되지 않았습니다");
				System.out.println("카드 등록 창으로 이동하겠습니다.");
				card_regist();
				return View.PARTITIONMILEAGE;
			} else { // 카드가 있을 시
				card: while (true) {
					System.out.println("현재 등록된 카드가 존재합니다");
					System.out.println("1. 등록카드결제 2. 신규카드결제 3. 뒤로가기");
					int num = ScanUtil.nextInt();

					switch (num) {
					case 1:
						List<Object> param = new ArrayList<>();
						param.add(Controller.sessionStorage.get("MEM_ID"));
						List<Map<String, Object>> findCard = carDAO.findCard(param);
						System.out.println(Controller.sessionStorage.get("MEM_ID") + "님 보유카드");

						int cnt = 0;
						System.out.println("┌───────────────────┬─────────────────────────────────┐");
						for (Map<String, Object> u : findCard) {
							cnt++;
							System.out.printf("│카드명 : %s│카드번호 : %s│"
									, SpaceUtil.format(u.get("CARD_NAME"), 10, 0)
									, SpaceUtil.format(u.get("CARD_NUMBER"), 22, 0));
							if(findCard.size()==cnt) {
								System.out.println("\n└───────────────────┴─────────────────────────────────┘");
							}else {
								System.out.printf("\n├───────────────────┼─────────────────────────────────┤\n");
							}
						}
						System.out.print("선택하실 카드번호를 입력해주세요 : ");
						selectCard = ScanUtil.nextLine();
						param.remove(0);
						break card;
					case 2:
						card_regist();
						return View.PARTITIONMILEAGE;
					case 3:
						return View.CARDETAIL;
					default:
						System.out.println("잘못된 명령어입니다");
						System.out.println("다시 입력해주세요");
						continue;
					}
				}

				List<Object> param = new ArrayList<>();

				

				System.out.println(Controller.sessionStorage.get("MEM_ID") + "님");
				System.out.printf("예약차량 : [%s]\n", selectCar.get("C_NAME"));
				System.out.printf("예약금액 : %,3d원\n", totalPrice);
				System.out.printf("예약일수 : %d일\n", period);
				System.out.println("");
				
				while (true) {
					System.out.print("결제를 진행하시겠습니까?? (y/n)");
					String answer = ScanUtil.nextLine();
					answer = (answer.toUpperCase()).trim();
					System.out.println();
					switch (answer) {
					case "Y":
						Map<String, Object> cardSearch = carDAO.cardSearch(selectCard);

						System.out.println(Controller.sessionStorage.get("MEM_ID") + "님");
						System.out.printf("카드이름 : %s카드\n", (String) cardSearch.get("CARD_NAME"));
						System.out.printf("카드번호 : %s\n", (String) cardSearch.get("CARD_NUMBER"));
						System.out.printf("예약금액 : %,3d원\n", totalPrice);
						System.out.println("적립마일리지 : " + mileage + "점");
						System.out.println("예약이 완료되었습니다!!");
						System.out.println();
						System.out.println();
						
						System.out.println();
						System.out.println();
						param.add(carDivision);
						param.add(carDivision);
						param.add(Controller.sessionStorage.get("MEM_ID"));
						param.add(inputDate);
						param.add(outputDate);
						param.add(totalPrice);
						param.add(dd);
						param.add(license);
						result = carDAO.carReservation(param);
						
						
						totalMileage = totalMileage + mileage;
						List<Object> memMilage = new ArrayList<>();

						memMilage.add(totalMileage);
						memMilage.add(Controller.sessionStorage.get("MEM_ID"));
						carDAO.carMileage(memMilage);
						result = View.MAIN;
						return result;
					case "N":
						System.out.println("결제창으로 이동하겠습니다.");
						System.out.println();
						result = View.PARTITIONMILEAGE;
						return result;
					default:
						System.out.println("잘못된 명령어입니다.");
						System.out.println("다시 입력해주세요.");
						continue;
					}
				}
			}
		}

		else {// 마일리지 일부만 결제할때
			List<Object> mileageUpdate = new ArrayList<>();
			while (true) {
				System.out.printf("[현재 보유 마일리지 : %d점]사용하실 마일리지를 입력해주세요 : ", memMile2);
				String inputMileage = ScanUtil.nextLine();

//				boolean reservationFlag = false;
				// 정규식으로 숫자만 입력되게 필터를 적용
				// inputMileage 정규식으로 숫자만 되게
//				try {
//					memMile2 = Integer.parseInt(inputMileage);
//					reservationFlag = true;
//				} catch (NumberFormatException e) {
//					System.out.println("잘못된 명령어 입니다.");
//					System.out.println("다시 입력해주세요");
//					continue;
//				}
//				if (reservationFlag) {
				if (memMile2 < Integer.parseInt(inputMileage)) {
					System.out.println("보유하고 계신 마일리지보다 입력값이 큽니다 다시 입력해주세요");
					System.out.println();
					continue;
				} else if (Integer.parseInt(inputMileage) <= memMile2) {
					String selectCard = "";
					System.out.printf("대여료 %d원에서 %d점 마일리지 차감합니다.\n", totalPrice, Integer.parseInt(inputMileage));
					totalPrice = totalPrice - Integer.parseInt(inputMileage);

					// 카드생성
					// 카드가 없을때 자동 생성
					Map<String, Object> card = carDAO.card(Controller.sessionStorage.get("MEM_ID"));
					if (card == null) {
						System.out.println(Controller.sessionStorage.get("MEM_ID") + "님");
						System.out.println("결제 카드 등록이 되지 않았습니다");
						System.out.println("카드 등록 창으로 이동하겠습니다.");
						card_regist();
						return View.PARTITIONMILEAGE;
					} else { // 카드가 있을 시
						card: while (true) {
							System.out.println("현재 등록된 카드가 존재합니다");
							System.out.println("1. 등록카드결제 2. 신규카드결제 3. 뒤로가기");
							int num = ScanUtil.nextInt();

							switch (num) {
							case 1:
								List<Object> param = new ArrayList<>();
								param.add(Controller.sessionStorage.get("MEM_ID"));
								List<Map<String, Object>> findCard = carDAO.findCard(param);
								System.out.println(Controller.sessionStorage.get("MEM_ID") + "님 보유카드");
								
								int cnt = 0;
								System.out.println("┌───────────────────┬─────────────────────────────────┐");
								for (Map<String, Object> u : findCard) {
									cnt++;
									System.out.printf("│카드명 : %s│카드번호 : %s│"
											, SpaceUtil.format(u.get("CARD_NAME"), 10, 0)
											, SpaceUtil.format(u.get("CARD_NUMBER"), 22, 0));
									if(findCard.size()==cnt) {
										System.out.println("\n└───────────────────┴─────────────────────────────────┘");
									}else {
										System.out.printf("\n├───────────────────┼─────────────────────────────────┤\n");
									}
								}
								System.out.print("선택하실 카드번호를 입력해주세요 : ");
								selectCard = ScanUtil.nextLine();
								param.remove(0);
								break card;
							case 2:
								card_regist();
								return View.PARTITIONMILEAGE;
							case 3:
								return View.CARDETAIL;
							default:
								System.out.println("잘못된 명령어입니다");
								System.out.println("다시 입력해주세요");
								continue;
							}
						}
						List<Object> param = new ArrayList<>();
						param.add(carDivision);
						param.add(carDivision);
						param.add(Controller.sessionStorage.get("MEM_ID"));
						param.add(inputDate);
						param.add(outputDate);
						param.add(totalPrice);
						param.add(dd);
						param.add(license);
						result = carDAO.carReservation(param);
						
						System.out.println();
						System.out.println();
						System.out.println(Controller.sessionStorage.get("MEM_ID") + "님");
						System.out.printf("예약차량 : [%s]\n", selectCar.get("C_NAME"));
						System.out.printf("예약금액 : %,3d원(마일리지 차감 금액)\n", totalPrice);
						System.out.printf("예약일수 : %d일\n", period);
						System.out.println("");

						while (true) {
							System.out.print("결제를 진행하시겠습니까?? (y/n)");
							String answer = ScanUtil.nextLine();
							answer = (answer.toUpperCase()).trim();
							System.out.println();
							switch (answer) {
							case "Y":
								Map<String, Object> cardSearch = carDAO.cardSearch(selectCard);

								System.out.println(Controller.sessionStorage.get("MEM_ID") + "님");
								System.out.printf("카드이름 : %s카드\n", (String) cardSearch.get("CARD_NAME"));
								System.out.printf("카드번호 : %s\n", (String) cardSearch.get("CARD_NUMBER"));
								System.out.printf("예약금액 : %,3d원\n", totalPrice);
								System.out.println("적립마일리지 : " + mileage + "점");
								System.out.println("예약이 완료되었습니다!!");
								System.out.println();
								System.out.println();
								
								totalPrice = totalPrice - Integer.parseInt(inputMileage);
								mileageUpdate.add(memMile2 - Integer.parseInt(inputMileage));
								mileageUpdate.add(Controller.sessionStorage.get("MEM_ID"));

								carDAO.carMileage(mileageUpdate);
								System.out.println();
								System.out.println();
								
								totalMileage = totalMileage + mileage;
								totalMileage = mileage + (memMile2 - Integer.parseInt(inputMileage));
								List<Object> memMilage = new ArrayList<>();

								memMilage.add(totalMileage);
								memMilage.add(Controller.sessionStorage.get("MEM_ID"));
								carDAO.carMileage(memMilage);
								result = View.MAIN;
								return result;
							case "N":
								System.out.println("결제창으로 이동하겠습니다.");
								System.out.println();
								result = View.ALLMILEAGE;
								return result;
							default:
								System.out.println("잘못된 명령어입니다.");
								System.out.println("다시 입력해주세요.");
								continue;
							}

						}

					}
				}
			}
		}
	}

	
}
