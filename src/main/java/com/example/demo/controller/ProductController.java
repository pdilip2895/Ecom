package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {
    @Autowired
    ProductService productService;
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<>(productService.getAllProducts(),HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id){
        Product product = productService.getProductById(id);
        if(product != null)
             return new ResponseEntity<>(product, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product , @RequestPart MultipartFile imageFile){
        try{
            Product product1 = productService.addProduct(product, imageFile);
            return new ResponseEntity<>(product1, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable int productId){
        return new ResponseEntity<>(productService.getImageById(productId),HttpStatus.OK);
    }
    @PutMapping("product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestPart Product product,
                                                @RequestPart MultipartFile imageFile) throws IOException {
        Product product1 = null;
        try{
            product1 = productService.updateProduct(id, product, imageFile);
        }catch (IOException e){
            return new ResponseEntity<>("Failed To Update", HttpStatus.BAD_REQUEST);
        }
        if(product1 != null){
            return new ResponseEntity<>("Product Updated", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Product Not Found", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Product product = productService.getProductById(id);
        if(product != null){
            productService.deleteProductById(id);
            return new ResponseEntity<>("Product Deleted Successfully",HttpStatus.OK );
        }
        else{
            return new ResponseEntity<>(" Product Not Found", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProductsByKeyword(@RequestParam String keyword){
        List<Product> products = productService.searchProductsByKeyword(keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
}
