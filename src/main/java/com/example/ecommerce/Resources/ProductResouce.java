package com.example.ecommerce.Resources;

import com.example.ecommerce.model.*;
import com.example.ecommerce.request.AddProductRequest;
import com.example.ecommerce.request.CategoryProductService;
import com.example.ecommerce.request.UpdateProductRequest;
import com.example.ecommerce.response.BaseResponse;
import com.example.ecommerce.response.CategoryProductResponse;
import com.example.ecommerce.service.*;
import com.example.ecommerce.utils.Define;
import com.example.ecommerce.utils.Utils;
import org.hibernate.mapping.Any;
import org.slf4j.helpers.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/product")
public class ProductResouce {
    @Autowired
    ProdcutService prodcutService;


    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryProductService categoryProductService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @PostMapping(path = "/insert")
    ResponseEntity<BaseResponse> insertProduct(@RequestBody AddProductRequest request){
        //kiem tra user co ton tai khong
        User seller = userService.findUserByIdAndRole(request.getIdSeller(), Define.ROLE_SELLER);
        if (seller == null){
            return Utils.getResponse(HttpStatus.NOT_FOUND.value(), new String[]{"Cant find seller"}, null);
        }
        Product addedProduct = new Product();
        addedProduct.setSellerid(seller);
        addedProduct.setName(request.getName());
        addedProduct.setDescription(request.getDescription());
        addedProduct.setQuantity(request.getQuantity());
        addedProduct.setPrice(request.getPrice());
        addedProduct.setDiscount(request.getDiscount());
        addedProduct.setReviewNumber("0");
        addedProduct.setRate(0.0);
        addedProduct.setImage(request.getImage());
        try {
            Product product = prodcutService.insertProduct(addedProduct);
            Category category = categoryService.getCategoryById(request.getIdCategory());
            CategoryProduct categoryProduct = new CategoryProduct(category, product);
            categoryProductService.addCategoryProduct(categoryProduct);
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, product);
        } catch (Exception e){
            return Utils.getResponse(HttpStatus.NO_CONTENT.value(), new String[] {e.getMessage()}, null);
        }
    }

    @GetMapping(path = "/seller/{id}")
    ResponseEntity<BaseResponse> getProductsBySellerId(@PathVariable int id){
        return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, prodcutService.getProductBySellerId(id));
    }

    @GetMapping(path = "/category")
    ResponseEntity<BaseResponse> getProductByCategoryId(@RequestParam int id, @RequestParam int filter){
        List<CategoryProduct> categoryProducts = categoryProductService.getCategoryProductByIdCategory(id);
        if (categoryProducts.isEmpty()){
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{"The category is empty"}, null);
        }
        ArrayList<Product> products = new ArrayList<>();
        for(CategoryProduct categoryProduct: categoryProducts){
            products.add(prodcutService.getProductById(categoryProduct.getProduct().getId()));
        }
        if(products.isEmpty()){
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{"The category is empty"}, null);
        } else {
            if (filter == Define.FilferType.CUSTOMER_REVIEW){
                Collections.sort(products, new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        if (o1.getRate() == null || o2.getRate() == null){
                            return 0;
                        }
                        return o1.getRate() > o2.getRate()? 1 : -1;
                    }
                });
                Collections.reverse(products);
            } else if (filter == Define.FilferType.PRICE_HIGHEST_TO_LOW){
                Collections.sort(products, new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        if (o1.getPriceProduct() == null || o2.getPriceProduct() == null){
                            return 0;
                        }
                        return o1.getPriceProduct() > o2.getPriceProduct()? 1 : -1;
                    }
                });
            } else if (filter == Define.FilferType.PRICE_LOWEST_TO_HIGH){
                Collections.sort(products, new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        if (o1.getPriceProduct() == null || o2.getPriceProduct() == null){
                            return 0;
                        }
                        return -o1.getPriceProduct().compareTo(o2.getPriceProduct());
                    }
                });
            }
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, products);
        }
    }



    @GetMapping(path = "/all")
    ResponseEntity<BaseResponse> getAllProduct(){
        List<Product> products = prodcutService.getAllProduct();
        return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, products);
    }

    @PutMapping(path = "/{id}")
    ResponseEntity<BaseResponse> updateProduct(@RequestBody UpdateProductRequest request, @PathVariable int id){
        Product updatedProduct = prodcutService.getProductById(id);
        if (updatedProduct == null){
            return Utils.getResponse(HttpStatus.NOT_FOUND.value(), new String[]{"Product not exist"}, null);
        }
        if (request.getDescription()!=null){
            updatedProduct.setDescription(request.getDescription());
        }
        if (request.getDiscount() != null){
            updatedProduct.setDiscount(request.getDiscount());
        }
        if (request.getImage() != null){
            updatedProduct.setImage(request.getImage());
        }
        if (request.getName() != null){
            updatedProduct.setName(request.getName());
        }
        if (request.getPrice() != null){
            updatedProduct.setPrice(request.getPrice());
        }
        if (request.getQuantity() != null){
            updatedProduct.setQuantity(request.getQuantity());
        }
        if (request.getRate() != null){
            updatedProduct.setRate(request.getRate());
        }
        if (request.getReviewNumber() != null) {
            updatedProduct.setReviewNumber(request.getReviewNumber());
        }
        try{
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, prodcutService.updateProduct(updatedProduct));
        } catch (Exception e){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{e.getMessage()}, null);
        }
    }

    @GetMapping(path = "/category_product")
    ResponseEntity<BaseResponse> getCategoryAndProduct(){
        List<Category> categories = categoryService.getListCategory();
        ArrayList<CategoryProductResponse> response = new ArrayList<>();
        for(Category category: categories){
            CategoryProductResponse categoryProductResponse = new CategoryProductResponse();
            categoryProductResponse.setIdCategory(category.getId());
            categoryProductResponse.setTitleCategory(category.getTitle());
            categoryProductResponse.setDescriptionCategory(category.getDescription());

            List<CategoryProduct> categoryProducts = categoryProductService.getCategoryProductByIdCategory(category.getId());
            if (categoryProducts.isEmpty()){
                continue;
            }
            ArrayList<Product> products = new ArrayList<>();
            for(CategoryProduct categoryProduct: categoryProducts){
                products.add(prodcutService.getProductById(categoryProduct.getProduct().getId()));
            }
            if(products.size()<=5){
                categoryProductResponse.setProducts(products);
                response.add(categoryProductResponse);
            }
        }
        return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, response);
    }

    @PostMapping(path = "/purchase")
    ResponseEntity<BaseResponse> addToCart(@RequestBody MultiValueMap<String, String> request){
        Integer idUser = Integer.parseInt(Objects.requireNonNull(request.getFirst("idUser")));
        Integer quantity = Integer.parseInt(Objects.requireNonNull(request.getFirst("quantity")));
        Integer idProduct = Integer.parseInt(Objects.requireNonNull(request.getFirst("idProduct")));
        Cart cart = cartService.findCartByUserId(idUser);
        Product product = prodcutService.getProductById(idProduct);
        if (cart == null){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{Define.USER_CART_IS_NOT_EXIST}, null);
        } else if (product == null){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{"Product is not exist"}, null);
        }

        try{
            // Check sản phẩm đã tồn tại trong cart chưa, nếu tồn tại thì sửa đổi quantity, chưa thì thêm vào
            CartItem existCartItem = cartItemService.findCartItemByProductId(idProduct, cart.getId());
            if (existCartItem == null){
                CartItem cartItem = new CartItem(quantity, quantity*product.getPriceProduct(), product, cart);
                return  Utils.getResponse( HttpStatus.OK.value(), new String[]{}, cartItemService.addCartItem(cartItem));
            } else {
                return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, cartItemService.changeQuantityProduct(existCartItem, quantity));
            }
        } catch (Exception e){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{e.getMessage()}, null);
        }
    }
}