package com.quizz.cal.minio;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.quizz.cal.minio.lib.MinIOAnswer;

import io.minio.BucketExistsArgs;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
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
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;




@RestController
@RequestMapping("/files")
@SecurityRequirement(name = "bearerAuth")
public class MinIOComs {
    @Autowired
    private MinioClient minioClient;

    
    
    
    @PreAuthorize("hasRole('admin')")
    @PostMapping(value = "{bucketName}/{fileName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "envia um arquivo para o minio com base no nome do bucket e do arquivo")
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
            
            return ResponseEntity.status(HttpStatus.OK).body(new MinIOAnswer(fileName, "File uploaded successfully!").toString());

        } catch (Exception e) {
        
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MinIOAnswer(null, "An error occurred while uploading the file: " + e.getMessage()).toString());
        }
    }

    @GetMapping()
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "pega o nome de todos os buckets")
    public List<String> getBuckets() throws ServerException, XmlParserException, ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException {
        ArrayList<String> bucketNames = new ArrayList<String>();
        List<Bucket> buckets = minioClient.listBuckets();
        for (Bucket bucket : buckets) {
            bucketNames.add(bucket.creationDate() + ", " + bucket.name());
        }
        return bucketNames.subList(0, bucketNames.size());
    }

    @Operation(summary = "cria um bucket com base no nome entregue")
    @PostMapping("{bucketName}")
    @PreAuthorize("hasRole('admin')")
    public void createBucket(@PathVariable String bucketName) throws ServerException, XmlParserException, ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException {
        minioClient.makeBucket(MakeBucketArgs.builder()
                .bucket(bucketName)
                .build()
        );
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping(value = "temp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "envia um arquivo para o minio no bucket temp")
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
    
    @PreAuthorize("hasRole('admin')")
    @GetMapping("etag/{etag}")
    @Operation(summary = "pega o url de um arquivo que está no bucket temp com base no etag")
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
    
    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("delete/{bucketName}/{etag}")
    @Operation(summary = "deleta um arquivo com base no nome do bucket e do etag")
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

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("delete/{bucketName}")
    @Operation(summary = "deleta um bucket com base no nome do bucket")
    public ResponseEntity<?> deleteBucket(@PathVariable String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            return ResponseEntity.status(200).body("Bucket deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while deleting the bucket: " + e.getMessage());
        }
    }
    
    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("delete")
    @Operation(summary = "deleta todos os arquivos do bucket temp")
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

    @Operation(summary = "envia um arquivo para o minio em um bucket temporario com o nome do id do usuário")
    @PreAuthorize("hasRole('professor')")
    @PostMapping(value = "professor", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postFilesForQuestions(@RequestParam MultipartFile file,@AuthenticationPrincipal Jwt jwt) {
        try {
            String dateTime = java.time.LocalDateTime.now().toString();
            String nome = UUID.randomUUID().toString();
            String fileName = nome+"_"+dateTime;
            if(minioClient.bucketExists(BucketExistsArgs.builder().bucket(jwt.getClaim("sub")).build())){
                return uploadFile(jwt.getClaim("sub"), fileName, file);
            }else{
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(jwt.getClaim("sub")).build());
                return uploadFile(jwt.getClaim("sub"), fileName, file);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    @PreAuthorize("hasRole('aluno')")
    @GetMapping("{bucketName}/{objectName}")
    @Operation(summary = "pega o url de um arquivo com base no nome do bucket e do arquivo")
    public ResponseEntity<?> getFileUrl(@PathVariable String bucketName, @PathVariable String objectName) throws Exception {
        try {
            if(!bucketName.contains("eternal")){
                return ResponseEntity.status(403).body("Você não tem permissão para acessar esse arquivo!");
            }
            return ResponseEntity.status(200).body( minioClient.getPresignedObjectUrl(
                                                        GetPresignedObjectUrlArgs.builder()
                                                            .method(Method.GET)
                                                            .bucket(bucketName)
                                                            .object(objectName)
                                                            .expiry(60 * 60)
                                                            .build()
                                                        ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
        
    }
    @Operation(summary = "envia um arquivo do bucket temporario para um bucket eterno com o nome do id do usuário")
    @PreAuthorize("hasRole('professor')")
    @PutMapping("professor/{fileName}")
    public ResponseEntity<?> putMethodName(@PathVariable String fileName, @AuthenticationPrincipal Jwt jwt) {
        try {
            boolean objectExists = false;
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(jwt.getClaim("sub")).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                if (item.objectName().equals(fileName)) {
                    objectExists = true;
                    break;
                }
            }
            if(objectExists){
                minioClient.copyObject(
                    CopyObjectArgs.builder()
                        .bucket("eternal"+jwt.getClaim("sub"))
                        .object(fileName)
                        .source(
                            CopySource.builder()
                                .bucket(jwt.getClaim("sub"))
                                .object(fileName)
                                .build())
                        .build());
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(jwt.getClaim("sub")).object(fileName).build());
                return ResponseEntity.status(200).body("O arquivo foi salvo com sucesso!");
            }else{
                return ResponseEntity.status(404).body("O arquivo não foi encontrado!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('professor')")
    @DeleteMapping("professor/")
    @Operation(summary = "deleta todos os arquivos do bucket temporario com o id do usuário")
    public ResponseEntity<?> deleteAllFiles(@AuthenticationPrincipal Jwt jwt) {
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(jwt.getClaim("sub")).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(jwt.getClaim("sub")).object(item.objectName()).build());
            }
            return ResponseEntity.status(200).body("Todos os arquivos foram deletados com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}


