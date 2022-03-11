package com.petmily.controller.product;


import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.petmily.dto.Cart;
import com.petmily.dto.Product;
import com.petmily.service.CartService;
import com.petmily.service.ProductService;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;

@Controller
@RequestMapping(value = "/cart")
public class CartController {

	@Autowired
	CartService cartService;
	
	@Autowired
	ProductService productService;

	@Autowired
	HttpSession session;
	
	@GetMapping("")
	public String cartMain(Model model) {
		String user_id = (String) session.getAttribute("user_id");
		try {
			List<Cart> cartList = cartService.cartQueryById(user_id);
			model.addAttribute("cartList", cartList);
			
			
			List<Product> prodList = new ArrayList<Product>();
			for (int i = 0; i < cartList.size(); i++) {
				int prod_num = cartList.get(i).getProd_num();
				Product prod= cartService.prodQueryByProdNum(prod_num);
				prodList.add(i, prod);
			}
			model.addAttribute("prodList", prodList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "cart";
	}
	
	// 수량에 따른 총 금액 계산
	@ResponseBody
	@PostMapping("/gettotal")
	public int GetTotal(@RequestParam("objParams") String objParams) {
		System.out.println(objParams);
		System.out.println("진입");
		JsonParser jsonParser = new JsonParser();
		JsonObject jsonObj = jsonParser.parse(objParams).getAsJsonObject();
		JsonArray jsonNumArr = jsonObj.getAsJsonArray("numList");
		JsonArray jsonQuanArr = jsonObj.getAsJsonArray("quanList");
		
		Gson gson = new Gson();
		ArrayList numList= gson.fromJson(jsonNumArr, ArrayList.class);	
		ArrayList quanList= gson.fromJson(jsonQuanArr, ArrayList.class);
		int total = 0;
		ArrayList priceList= new ArrayList();
		
		try {
			// 상품 번호를 통해 가격을 조회<
			for(int i=0;numList.size()>i;i++) {
				int prod_num = (int) Integer.parseInt((String) numList.get(i));
				int price = productService.selectPrice(prod_num);
				priceList.add(price);
			}
			
			for(int j=0;priceList.size()>j;j++) {
				int a=  (int) priceList.get(j);
				int b =(int) Integer.parseInt((String) quanList.get(j));
				total += a * b;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 상품 수량을 조회
		return total; 
	}
	
}

/*
 * @PostMapping("/deleteProduct") public String
 */
	
//	@PostMapping(value = "/addProdInCart") // addProdInCart를 요청하면 String addProdInCart가 호출됨(prod_num가 전달됨).
//	public @ResponseBody String addProdInCart(@RequestParam("prod_num") int prod_num, HttpServletRequest request,
//			HttpServletResponse response) throws Exception {
//		// <--전송된 상품번호를 받는다!
//
//		HttpSession session = request.getSession();
//		User user = (User) session.getAttribute("userInfo");
//		String user_id = user.getUser_id();
//		Cart cart = new Cart();
//		cart.setUser_id(user_id); // 장바구니에 등록하기 전, 이미 등록되어있는 상품인지를 확인한다.
//		cart.setProd_num(prod_num); // user_id, prod_num를 속성에 세팅 boolean
//		Boolean isAreadyExisted = cartService.findCartProd(cart); // 해당 상품 번호에 대해서 Cart 테이블에 상품이 존재하느냐를 체크함.
//																	// findCartProd에서 cart(dto)를 전달 // 얘가 xml에 id문을 받음
//																	// -^-
//
//		if (isAreadyExisted == true) { // 상품 번호가 이미 Cart 테이블에 있으면(true) 메세지를 브라우저로 전송, 없으면 Cart테이블에 추가
//			return "already_existed";
//		} else {
//			cartService.addProdInCart(cart);
//			return "add_success"; // 없으면 cart테이블에 추가 '상품 상세 정보페이지'.jsp 에이쟉스로 전달
//		}
//	}
//
//	@GetMapping(value="myCartList") //조회한 장바구니 목록과 상품 정보 목록을 Map에 저장한다. public
//	ModelAndView myCartMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String viewName=(String)request.getAttribute("viewName");
//		ModelAndView mav = new ModelAndView(viewName);
//		HttpSession session=request.getSession();
//		User user=(User)session.getAttribute("userInfo");
//		String user_id=user.getUser_id();
//		Cart cart = new Cart();
//		cart.setUser_id(user_id);
//		Map<String ,List> cartMap=cartService.myCartList(cart);
//		session.setAttribute("cartMap",cartMap);// 장바구니 목록 화면에서 상품 주문 시 사용하기 위해서 장바구니 목록을 세션에 저장한다.
//		mav.addObject("cartMap", cartMap); return mav;
//		}
//	}

//  myCartList를 요청하면 myCartMain 메소드가 호출됨. //로그인 상태에서 장바구니 페이지를 요청하기에 세션에 저장된 userInfo속성을 이용해서 User(dto)를 가져와서 getUser_Id 호출해서 user_id를 얻는 방식
// user_id를 얻어서 Cart(dto)에 user_id를 세팅함.
//그   후 cartService에 myCartList로 Cart(dto)를 전달.
// cartService에서 반환한 cartMap을 받아서 세션에 cartMap으로 바인딩해서 장바구니 페이지로 포워딩?? 한다.
