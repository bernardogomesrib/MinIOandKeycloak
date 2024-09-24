package com.quizz.cal.minio;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.MinioException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@RestController
@RequestMapping("/files")
public class MinIOComs {
    @Autowired
    private MinioClient minioClient;

    @GetMapping("{bucketName}/{objectName}")
    public String getFileUrl(@PathVariable String bucketName, @PathVariable String objectName) throws Exception {
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(objectName)
                .expiry(60 * 60)
                .build()
        );
    }

    @PostMapping(value = "{bucketName}/{fileName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@PathVariable String bucketName, @PathVariable String fileName, @RequestParam("file") MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully!");

        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
        
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while uploading the file: " + e.getMessage());
        }
    }

    @GetMapping()
    public List<String> getBuckets() throws ServerException, XmlParserException, ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException {
        ArrayList<String> bucketNames = new ArrayList<String>();
        List<Bucket> buckets = minioClient.listBuckets();
        for (Bucket bucket : buckets) {
            bucketNames.add(bucket.creationDate() + ", " + bucket.name());
        }
        return bucketNames.subList(0, bucketNames.size());
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("{bucketName}")
    @PreAuthorize("hasRole('admin')")
    public void createBucket(@PathVariable String bucketName) throws ServerException, XmlParserException, ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException {
        minioClient.makeBucket(MakeBucketArgs.builder()
                .bucket(bucketName)
                .build()
        );
    }

    @PostMapping(value = "temp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postMethodName(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println(file.getOriginalFilename());
            InputStream inputStream = file.getInputStream();

            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket("temp")
                    .object(file.getOriginalFilename())
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(file.getContentType())
                    .build();

            String etag = minioClient.putObject(putObjectArgs).etag();

            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully! ETag: " + etag);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while uploading the file: " + e.getMessage());
        }
    }
 
    @GetMapping("etag/{etag}")
    public ResponseEntity<?> getPegarObjetoViaEtag(@PathVariable String etag) {
        try {
                etag = "\""+etag+"\"";
                Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket("temp").build());
                for (Result<Item> result : results) {
                    Item item = result.get();
                    if (item.etag().equals(etag)) {
                        return ResponseEntity.status(200).body(minioClient.getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket("temp")
                                .object(item.objectName())
                                .expiry(60 * 60)
                                .build()
                        ));
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(404).body("File not found");
    }
    @DeleteMapping("delete/{bucketName}/{etag}")
    public ResponseEntity<?> deleteFile(@PathVariable String bucketName, @PathVariable String etag) {
        try {
            etag = "\""+etag+"\"";
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                if (item.etag().equals(etag)) {
                    minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(item.objectName()).build());
                    return ResponseEntity.status(200).body("File deleted successfully!");
                }
            }
            return ResponseEntity.status(404).body("File not found");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while deleting the file: " + e.getMessage());
        }
    }
    @DeleteMapping("delete/{bucketName}")
    public ResponseEntity<?> deleteBucket(@PathVariable String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            return ResponseEntity.status(200).body("Bucket deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while deleting the bucket: " + e.getMessage());
        }
    }
    @DeleteMapping("delete")
    public ResponseEntity<?> deleteAllEntity() {
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket("temp").build());
            for (Result<Item> result : results) {
                Item item = result.get();
                minioClient.removeObject(RemoveObjectArgs.builder().bucket("temp").object(item.objectName()).build());
            }
            return ResponseEntity.status(200).body("All files deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while deleting the files: " + e.getMessage());
        }
    }
}


