package com.telusko.SpringEcom.Controller;

import com.telusko.SpringEcom.Model.Product;
import com.telusko.SpringEcom.Service.ProductService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductService service;
    @GetMapping({"/products", "/product"})
    public ResponseEntity<List<Product>>getProducts(){
        return new ResponseEntity<>(service.getAllProducts(),HttpStatus.ACCEPTED);

    }
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id){
        Product product =service.getProductById(id);
        if (product.getId() > 0) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,@RequestPart MultipartFile imageFile ) throws IOException {
        Product savedProduct=null;
        try{
             savedProduct= service.addOrUpdateProduct(product,imageFile);
            return  new ResponseEntity<>(savedProduct,HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>( e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable int productId) {
        Product product = service.getProductById(productId);
        if (product.getId() > 0) {
            return new ResponseEntity<>(product.getImageData(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
 @PutMapping("product/{productId}")
 public ResponseEntity<String> updateProduct(@RequestPart Product product,@RequestPart MultipartFile imageFile){
        Product updatedProduct=null;
        try{
            updatedProduct=service.addOrUpdateProduct(product,imageFile);
            return new ResponseEntity<>("Updated",HttpStatus.OK);
        }
   catch(Exception e ){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
   }

 }
    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        Product product = service.getProductById(id);
        if (product != null) {
            service.deleteProduct(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        }
 }
 @GetMapping("products/search")
    public ResponseEntity<List<Product>> searchAllProducts(@RequestParam String keyword){
       List<Product>  product=service.searchAllProducts(keyword);
        return new ResponseEntity<>(product,HttpStatus.OK);
 }
}
