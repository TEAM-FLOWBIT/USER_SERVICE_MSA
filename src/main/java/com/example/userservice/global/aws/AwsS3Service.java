package com.example.userservice.global.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@NoArgsConstructor
@Slf4j
public class AwsS3Service {
    private AmazonS3 s3Client;
//    public static final String CLOUD_FRONT_DOMAIN_NAME ="d2endhb8pwljj4.cloudfront.net";

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public String upload(String currentFilePath, MultipartFile file ,String username) throws IOException {
        // 고유한 key 값을 갖기위해 현재 시간을 postfix로 붙여줌
        SimpleDateFormat date = new SimpleDateFormat("yyyy-mm-dd-HH:mm:ss");
        String fileName = username+"-"+file.getOriginalFilename() + "-" + date.format(new Date());

        // key가 존재하면 기존 파일은 삭제
        if ("".equals(currentFilePath) == false && currentFilePath != null) {
            boolean isExistObject = s3Client.doesObjectExist(bucket, currentFilePath);

            if (isExistObject == true) {
                s3Client.deleteObject(bucket, currentFilePath);
            }
        }
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType()); // 이 부분을 추가하여 Content-Type 설정

        // 파일 업로드
        s3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(),metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return fileName;
    }

    // db에서 해당하는 파일찾아옴
    public String deleteFile(String fileName) {

        String result = "success";

        try {
            boolean isObjectExist = s3Client.doesObjectExist(bucket, fileName);
            if (isObjectExist) {
                s3Client.deleteObject(bucket, fileName);
            } else {
                result = "file not found";
            }
        } catch (Exception e) {
            log.debug("Delete File failed", e);
        }

        return result;
    }


    public String getThumbnailPath(String path) {
        return s3Client.getUrl(bucket, path).toString();
    }







}