package com.example.ecommerce.utils;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;

import java.io.File;
import java.io.FileWriter;

public class FileHandle {
    public static void addProductInfo(Product product, Category category) {
        try {
            String fileName = String.format(Define.FILE_NAME, product.getId());
            String filePath = Define.FILE_PATH;
            File folder = new File(filePath);
            if (!folder.exists()) {
                folder.mkdir();
            }
            File file = new File(folder, fileName);
            FileWriter fw = new FileWriter(file);
            fw.write("name: " + product.getName() + "\n");
            fw.write("price: " + product.getPrice() + "\n");
            fw.write("description: " + product.getDescription() + "\n");
            fw.write("discount: " + product.getDiscount() + "\n");
            fw.write("seller: " + product.getSellerid().getName() + "\n");
            fw.write("type: " + category.getTitle() + "\n");
            fw.write("Buy in: Fashion shop");
            fw.close();
            System.out.println("Add file sucess");
        } catch (Exception e) {
            System.out.println("Add file fail: " + e.getMessage());
        }
    }

    public static void changeFileProductInfo(Product product, Category category) {
        try {
            String filePath = String.format(Define.PRODUCT_PATH, product.getId());
            File fileTrain = new File(filePath);
            if (fileTrain.delete()){
                System.out.println("File deleted");
                FileHandle.addProductInfo(product, category);
            } else {
                System.out.println("File not deleted");
            }
            System.out.println("Change file sucess");
        } catch (Exception e) {
            System.out.println("Change file fail: " + e.getMessage());
        }
    }
}
