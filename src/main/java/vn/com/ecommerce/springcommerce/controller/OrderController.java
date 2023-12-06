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
import vn.com.ecommerce.springcommerce.domain.*;
import vn.com.ecommerce.springcommerce.service.AccountService;
import vn.com.ecommerce.springcommerce.service.CartService;
import vn.com.ecommerce.springcommerce.service.OrderService;
import vn.com.ecommerce.springcommerce.service.ProductService;

import java.util.ArrayList;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {
    final CartService cartService;
    final AccountService accountService;
    final OrderService orderService;
    final ProductService productService;
    final String[] paymentMethods = {"COD", "Bank transfer"};
    @Autowired
    public OrderController(CartService cartService, AccountService accountService, OrderService orderService, ProductService productService) {
        this.cartService = cartService;
        this.accountService = accountService;
        this.orderService = orderService;
        this.productService = productService;
    }
    @GetMapping("/success")
    String success(@Nullable @SessionAttribute(value = "accEmail", required = false) String email,
                   @SessionAttribute(value = "isLogin", required = false) Boolean isLogin,
                   @Nullable @SessionAttribute(value = "sCart", required = false) Cart sCart,
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
        if (isLogin == null || !isLogin) {
            model.addAttribute("isLogin", false);
        } else {
            model.addAttribute("isLogin", true);
        }
        model.addAttribute("user", account);
        model.addAttribute("order", requestOrder);
        model.addAttribute("sCart", sCart);
        model.addAttribute("navActive", "order");
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
        Cart cart = sCart;
        Order order = cart.moveCartToOrder();
        String name = (String) body.get("name");
        String phone = (String) body.get("phone");
        String address = body.get("address") + ", " + (body.get("city") == null ? "" : (String) body.get("city")) + ", " + ((String) body.get("country") == null ? "" : (String) body.get("country"));
        String note = body.get("note") == null ? "" : (String) body.get("note");
        if (name == null || phone == null || address.length() < 10) {
            return ResponseEntity.ok((new ResponseMessage<>(Response.SC_BAD_REQUEST, "Please fill in all required fields")));
        }
        if (cart.getItems().isEmpty()) {
            return ResponseEntity.ok((new ResponseMessage<>(Response.SC_BAD_REQUEST, "Your cart is empty, please try again")));
        }
        // check if quantity is enough with stock
        for (SelectedItem item : cart.getItems()) {
            if (item.getQuantity() > item.getProduct().getStock()) {
                return ResponseEntity.ok((new ResponseMessage<>(Response.SC_BAD_REQUEST, "Sorry, " + item.getProduct().getName() + " is not enough in stock, please try again")));
            }
        }
        /* ----------------- ORDER SUCCESS PROCESS ----------------- */
        // update product quantity
        for (SelectedItem item : cart.getItems()) {
            Product product = item.getProduct();
            int qty = item.getQuantity();
            product.setStock(product.getStock() - qty);
            product.setSold(product.getSold() + qty);
            productService.saveProduct(product);
        }
        accountService.updateAddress(email, address);
        String paymentMethod = paymentMethods[Integer.parseInt(body.get("paymentMethod").toString())-1];
        order.setData(name, phone, address, note, paymentMethod, "pending");
        Order newOrder = orderService.saveOrder(order);

        // set with new items list
        newOrder.setItems(new ArrayList<>(cart.getItems()));
        cart.getItems().forEach(item -> item.setSelectedList(newOrder));
        orderService.saveOrder(newOrder);
        cart.clearCart();
        cartService.saveCart(cart);
        request.getSession().setAttribute("orderId", newOrder.getId());
        request.getSession().setAttribute("sCart", cart);
        return ResponseEntity.ok((new ResponseMessage<>(Response.SC_OK, "Checkout successfully")));
    }
}
