package vn.com.ecommerce.springcommerce.controller;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.com.ecommerce.springcommerce.DTO.ResponseMessage;
import vn.com.ecommerce.springcommerce.domain.Account;
import vn.com.ecommerce.springcommerce.domain.Cart;
import vn.com.ecommerce.springcommerce.domain.Order;
import vn.com.ecommerce.springcommerce.service.AccountService;
import vn.com.ecommerce.springcommerce.service.CartService;
import vn.com.ecommerce.springcommerce.service.OrderService;

import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {
    final CartService cartService;
    final AccountService accountService;
    final OrderService orderService;
    final String[] paymentMethods = {"COD", "Bank transfer"};
    @Autowired
    public OrderController(CartService cartService, AccountService accountService, OrderService orderService) {
        this.cartService = cartService;
        this.accountService = accountService;
        this.orderService = orderService;
    }
    @GetMapping("/success")
    String success(@Nullable @SessionAttribute(value = "accEmail", required = false) String email,

                   Model model, HttpServletRequest request){
        if (email == null) {
            return "redirect:/account/login";
        }
        Account account = accountService.getAccount(email);
        Order requestOrder = orderService.getOrder((Long) request.getSession().getAttribute("orderId"));
        if (requestOrder == null) {
            return "redirect:/";
        } else {
//            request.getSession().removeAttribute("order");
        }
        model.addAttribute("user", account);
        model.addAttribute("order", requestOrder);
        return "order-detail";
    }
    @PostMapping("/checkout")
    ResponseEntity<ResponseMessage<String>> checkout(@Nullable @SessionAttribute(value = "accEmail", required = false) String email,
                                                     @RequestBody Map<String, Object> body,
                                                     @Nullable @SessionAttribute(value = "sCart", required = false) Cart sCart,
                                                     HttpServletRequest request){
        if (email == null) {
            return ResponseEntity.ok((new ResponseMessage<>(Response.SC_UNAUTHORIZED, "Please reload and try again")));
        }
        Account account = accountService.getAccount(email);
        Cart cart = sCart;
        Order order = cart.moveCartToOrder();
        String name = (String) body.get("name");
        String phone = (String) body.get("phone");
        String address = (String) body.get("address") + ", " + ((String) body.get("city") == null ? "" : (String) body.get("city")) + ", " + ((String) body.get("country") == null ? "" : (String) body.get("country"));
        String note = body.get("note") == null ? "" : (String) body.get("note");
        if (name == null || phone == null || address.length() < 10) {
            return ResponseEntity.ok((new ResponseMessage<>(Response.SC_BAD_REQUEST, "Please fill in all required fields")));
        }
        String paymentMethod = paymentMethods[Integer.parseInt(body.get("paymentMethod").toString())-1];
        order.setData(name, phone, address, note, paymentMethod, "pending");
        Order newOrder = orderService.createOrder(order);
        request.getSession().setAttribute("orderId", newOrder.getId());
        request.getSession().setAttribute("sCart", cartService.getCart(email));
        return ResponseEntity.ok((new ResponseMessage<>(Response.SC_OK, "Checkout successfully")));
    }
}
