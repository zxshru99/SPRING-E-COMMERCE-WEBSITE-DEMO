package com.telusko.SpringEcom.Service;

import com.telusko.SpringEcom.Model.Product;
import com.telusko.SpringEcom.Repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepo repo;
    public List<Product> getAllProducts(){
        return repo.findAll();

    }


    public Product getProductById(int id) {
       return repo.findById(id).orElse(new Product(-1));
    }

    public Product addOrUpdateProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());

        return repo.save(product);
    }


    public void deleteProduct(int id) {

            repo.deleteById(id);


    }

    public List<Product> searchAllProducts(String keyword) {
        return repo.searchAllProducts(keyword);
    }
}
