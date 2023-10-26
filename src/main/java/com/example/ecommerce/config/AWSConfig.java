package com.example.ecommerce.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AWSConfig {
    public AWSCredentials credentials(){
        AWSCredentials credentials = new BasicAWSCredentials(
                "AKIA2ZDGOEBHFAN2WXK6","OjSAk88WWMENlWvz3EwuRgohZeSsXtsrkmzfUSTA"
        );
        return credentials;
    }
    @Bean
    public AmazonS3 amazonS3(){
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials()))
                .withRegion(Regions.AP_SOUTHEAST_1)
                .build();
        return s3Client;
    }

    public List<Bucket> listBuckets(){
        return amazonS3().listBuckets();
    }
}
