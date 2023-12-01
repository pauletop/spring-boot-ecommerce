package vn.com.ecommerce.springcommerce.controller;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import vn.com.ecommerce.springcommerce.domain.Brand;
import vn.com.ecommerce.springcommerce.domain.Cart;

import vn.com.ecommerce.springcommerce.domain.Brand;
import vn.com.ecommerce.springcommerce.domain.Product;
import vn.com.ecommerce.springcommerce.service.BrandService;
import vn.com.ecommerce.springcommerce.service.CategoryService;
import vn.com.ecommerce.springcommerce.service.ProductService;

import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/store")
public class StoreController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    @Autowired
    public StoreController(ProductService productService, CategoryService categoryService, BrandService brandService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }
    @GetMapping({"", "/"})
    public String store(Model model, @RequestParam(defaultValue = "1") int page, HttpServletRequest request,
                        @Nullable @SessionAttribute(value = "sCart", required = false) Cart cart,
                        @Nullable @SessionAttribute(value = "isLogin", required = false) Boolean isLogin) {
        Page<Product> products = productService.getAllProducts(page - 1);
        // add query string to model, and remove page parameter if exists
        if (request.getQueryString() != null) {
            model.addAttribute("queryStr", Base64.getEncoder().encodeToString(request.getQueryString().replace("&page=" + page, "").getBytes()));
        } else {
            model.addAttribute("queryStr", "");
        }
        if (isLogin == null || !isLogin) {
            model.addAttribute("isLogin", (boolean ) false);
        } else {
            model.addAttribute("isLogin", (boolean) true);
        }
        model.addAttribute("sCart", cart);
        model.addAttribute("products", products.getContent());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalProducts", products.getTotalElements());
        model.addAttribute("numberPerPage", 12);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("topSelling", productService.getTop5BestSellingProducts());
        model.addAttribute("breadcrumb", new  String[]{"Home", "Store"});
        return "store";
    }


    @GetMapping("/search")
    public String search(Model model, @RequestParam(defaultValue = "1") int page,
                         @RequestParam(name = "keyword") String keyword,
                         @RequestParam(name = "category", required = false) Integer categoryId,
                         @RequestParam(name = "categories", required = false) List<Integer> categoryIds,
                         @RequestParam(name = "brands", required = false) List<Integer> brandIds,
                         @RequestParam(name = "min", required = false) Double minPrice,
                         @RequestParam(name = "max", required = false) Double maxPrice,
                         @RequestParam(name = "sort", required = false) String sort,
                         @RequestParam(name = "isAdv", required = false, defaultValue = "false") boolean isAdv,
                         HttpServletRequest request) {
        System.out.println("keyword: " + keyword);
        System.out.println("categoryId: " + categoryId);
        System.out.println("categoryIds: " + categoryIds);
        System.out.println("brandIds: " + brandIds);
        System.out.println("minPrice: " + minPrice);
        System.out.println("maxPrice: " + maxPrice);
        System.out.println("isAdv: " + isAdv);
        Page<Product> products;
        if (isAdv) {
            products = productService.searchByOptions(keyword.toLowerCase(), brandIds, categoryIds, minPrice, maxPrice, page - 1, sort);
        } else {
            System.out.println("page: " + page);
            products = productService.searchByKeyword(keyword.toLowerCase(), categoryId, page - 1, sort);
        }
        if (request.getQueryString() != null) {
            model.addAttribute("queryStr", Base64.getEncoder().encodeToString((request.getQueryString().replace("&page=" + page, "")).getBytes()));
        } else {
            model.addAttribute("queryStr", "");
        }
        model.addAttribute("tab", "search");
        model.addAttribute("products", products.getContent());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalProducts", products.getTotalElements());
        model.addAttribute("numberPerPage", 12);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("topSelling", productService.getTop5BestSellingProducts());
        model.addAttribute("breadcrumb", new  String[]{"Home", "Store", "Search"});
        return "store";
    }

    @GetMapping("/brands")
    public String brands(Model model, @Nullable @SessionAttribute(value = "sCart", required = false) Cart sCart,
                         @Nullable @SessionAttribute(value = "isLogin", required = false) Boolean isLogin) {
        Iterable<Brand> brands = brandService.getAllBrands();
        if (isLogin == null || !isLogin) {
            model.addAttribute("isLogin", (boolean ) false);
        } else {
            model.addAttribute("isLogin", (boolean) true);
        }
        model.addAttribute("brands", brands);
        model.addAttribute("sCart", sCart);
        return "brands";

    }




//    import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//    public class IntersectionExample {
//        public static void main(String[] args) {
//            List<String> list1 = Arrays.asList("1", "2", "3", "4");
//            List<String> list2 = Arrays.asList("3", "4", "5", "6");
//            List<String> list3 = Arrays.asList("4", "5", "6", "7");
//
//            List<String> intersection = list1.stream()
//                    .filter(list2::contains)
//                    .filter(list3::contains)
//                    .collect(Collectors.toList());
//
//            System.out.println("Intersection List: " + intersection);

//    List<Integer> numbers = List.of(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5);
//
//    List<Integer> sortedList = numbers.stream()
//            .distinct()
//            .sorted()
//            .collect(Collectors.toList());
//
//        System.out.println("Sorted List: " + sortedList);
//        }
//    }
}
