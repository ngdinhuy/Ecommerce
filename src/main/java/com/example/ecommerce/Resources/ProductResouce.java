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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.mapping.Any;
import org.slf4j.helpers.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private S3Service s3Service;

    @Autowired
    private OrderItemService orderItemService;

    @PostMapping(path = "/insert")
    ResponseEntity<BaseResponse> insertProduct(@RequestParam String requestProduct, List<MultipartFile> multipartFiles) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            AddProductRequest request = mapper.readValue(requestProduct, AddProductRequest.class);

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

            Product product = prodcutService.insertProduct(addedProduct);
            Category category = categoryService.getCategoryById(request.getIdCategory());
            CategoryProduct categoryProduct = new CategoryProduct(category, product);
            categoryProductService.addCategoryProduct(categoryProduct);

            List<String> urls = new ArrayList<>();

            for(int i=0;i<multipartFiles.size();i++){
                urls.add(s3Service.uploadProductImage(
                        String.format(Define.PATH_PRODUCT_URL, product.getId(), i),
                        Utils.convertMultipartToFile(multipartFiles.get(i))));
            }

            product.setImage(urls);
            product = prodcutService.insertProduct(product);
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
        for(Product product: products){
            CategoryProduct categoryProduct = categoryProductService.getCategoryProductByProduct(product);
            product.setCategoryName(categoryProduct.getCategory().getTitle());
        }
        return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, products);
    }

    @GetMapping(path = "/detail")
    ResponseEntity<BaseResponse> getDetailProduct(@RequestParam(name = "id") Integer id){
        Product product = prodcutService.getProductById(id);
        if (product == null){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{"Product is not exist"}, null);
        }
        CategoryProduct categoryProduct = categoryProductService.getCategoryProductByProduct(product);
        if (categoryProduct == null){
            return Utils.getResponse(HttpStatus.NOT_FOUND.value(), new String[]{"Not find category"}, null);
        }
        product.setCategoryName(categoryProduct.getCategory().getTitle());
        product.setIdCategory(categoryProduct.getCategory().getId());
        return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, product);
    }

    @PostMapping(path = "/update")
    ResponseEntity<BaseResponse> updateProduct(@RequestParam String requestProduct, List<MultipartFile> multipartFiles ){
        try{
            ObjectMapper mapper = new ObjectMapper();
            UpdateProductRequest request = mapper.readValue(requestProduct, UpdateProductRequest.class);
            Product updatedProduct = prodcutService.getProductById(request.getId());
            CategoryProduct categoryProduct = categoryProductService.getCategoryProductByProduct(updatedProduct);
            if (updatedProduct == null){
                return Utils.getResponse(HttpStatus.NOT_FOUND.value(), new String[]{"Product not exist"}, null);
            }
            if (request.getDescription()!=null){
                updatedProduct.setDescription(request.getDescription());
            }
            if (request.getDiscount() != null){
                updatedProduct.setDiscount(request.getDiscount());
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
            if (request.getIdCategory() != categoryProduct.getCategory().getId()){
                Category newCategory = categoryService.getCategoryById(request.getIdCategory());
                categoryProduct.setCategory(newCategory);
                categoryProductService.addCategoryProduct(categoryProduct);
            }
            for (String url: updatedProduct.getImage()){
                s3Service.deleteObject(Define.BUCKET_NAME, url.replace(Define.SCHEME_PRODUCT_URL,""));
            }

            List<String> urls = new ArrayList<>();
            for(int i=0;i<multipartFiles.size();i++){
                urls.add(s3Service.uploadProductImage(
                        String.format(Define.PATH_PRODUCT_URL, updatedProduct.getId(), i),
                        Utils.convertMultipartToFile(multipartFiles.get(i))));
            }
            updatedProduct.setImage(urls);
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
            } else {
                categoryProductResponse.setProducts(products.subList(0,5));
                response.add(categoryProductResponse);
            }
        }
        return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, response);
    }

    @GetMapping(path = "/seller_category_product")
    ResponseEntity<BaseResponse> getCategoryAndProductByIdSeller(@RequestParam Integer idUser){
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
                if (categoryProduct.getProduct().getSellerid().getId() == idUser){
                    products.add(prodcutService.getProductById(categoryProduct.getProduct().getId()));
                }
            }
            categoryProductResponse.setProducts(products);
            response.add(categoryProductResponse);
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

    @PostMapping("/upload_image")
    public ResponseEntity<BaseResponse> uploadImage(MultipartFile multipartFile){
        try {
//            return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, s3Service.uploadFile("1", multipartFile));
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, s3Service.uploadProductImage(Utils.getFileName("1"),Utils.convertMultipartToFile(multipartFile)));
        } catch (Exception e){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{e.getMessage()}, null);
        }
    }

    @PostMapping("/rate")
    public ResponseEntity<BaseResponse> rateProduct(@RequestBody MultiValueMap<String, String> request){
        Integer idOrderItem = Integer.parseInt(Objects.requireNonNull(request.getFirst("id_order_item")));
        int rate = Integer.parseInt(Objects.requireNonNull(request.getFirst("rate")));
        OrderItem orderItem = orderItemService.getDetailOrderItem(idOrderItem);
        if (orderItem.getIsRated()){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{"You rated this order!"}, null);
        }
        Product product = orderItem.getProduct();
        Integer reviewNumber = Integer.parseInt(product.getReviewNumber());
        product.setRate((product.getRate() * reviewNumber  + rate)/(reviewNumber + 1) );
        product.setReviewNumber(String.valueOf(reviewNumber + 1));
        try {
            prodcutService.updateProduct(product);
            orderItem.setIsRated(true);
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, orderItemService.updateOrderItem(orderItem));
        } catch (Exception e){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{e.getMessage()}, null);
        }
    }
}