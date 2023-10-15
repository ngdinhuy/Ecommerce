package com.example.ecommerce.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.ecommerce.utils.Define;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class S3Service {
    private final AmazonS3 amazonS3;

    @Autowired
    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public List<Bucket> getAllBucket(){
        return amazonS3.listBuckets();
    }

    public String uploadProductImage(String fileName, File file){
       amazonS3.putObject(new PutObjectRequest(Define.BUCKET_NAME,fileName, file)
               .withCannedAcl(CannedAccessControlList.PublicRead));
       return String.format(Define.END_POINT_URL, Define.BUCKET_NAME, fileName);
    }

    public void deleteAllObjectInFolder(
            String bucketName,
            String folderPath
    ){
        for (S3ObjectSummary file: amazonS3.listObjects(bucketName, folderPath).getObjectSummaries()){
            amazonS3.deleteObject(bucketName, file.getKey());
        }
    }

    public void deleteObject(
            String bucketName,
            String imagePath
    ){
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, imagePath);
        amazonS3.deleteObject(deleteObjectRequest);
    }
}
