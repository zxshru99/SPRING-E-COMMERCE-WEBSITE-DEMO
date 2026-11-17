package com.telusko.SpringEcom.Service;

import com.telusko.SpringEcom.Model.Product;
import com.telusko.SpringEcom.Repository.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1);
        product.setName("Laptop");
        product.setPrice(BigDecimal.valueOf(999.99));
        product.setStockQuantity(5);
    }

    @Test
    void testGetAllProducts() {
        when(productRepo.findAll()).thenReturn(List.of(product));
        List<Product> products = productService.getAllProducts();
        assertEquals(1, products.size());
        assertEquals("Laptop", products.get(0).getName());
    }

    @Test
    void testGetProductByIdFound() {
        when(productRepo.findById(1)).thenReturn(Optional.of(product));
        Product found = productService.getProductById(1);
        assertEquals(1, found.getId());
        assertEquals("Laptop", found.getName());
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepo.findById(999)).thenReturn(Optional.empty());
        Product found = productService.getProductById(999);
        assertEquals(-1, found.getId());
    }

    @Test
    void testAddOrUpdateProduct() throws IOException {
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile", "test.png", "image/png", "fake-image-bytes".getBytes()
        );

        when(productRepo.save(any(Product.class))).thenReturn(product);

        Product saved = productService.addOrUpdateProduct(product, imageFile);

        assertNotNull(saved);
        assertEquals("test.png", product.getImageName());
        assertEquals("image/png", product.getImageType());
        assertNotNull(product.getImageData());
        verify(productRepo).save(product);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepo).deleteById(1);
        productService.deleteProduct(1);
        verify(productRepo).deleteById(1);
    }

    @Test
    void testSearchAllProducts() {
        when(productRepo.searchAllProducts("lap")).thenReturn(List.of(product));
        List<Product> results = productService.searchAllProducts("lap");
        assertEquals(1, results.size());
        assertEquals("Laptop", results.get(0).getName());
    }
}
