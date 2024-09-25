package com.example.my_parc.service;

import com.azure.storage.blob.BlobClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class AzureBlobStorageService {

    @Value("${spring.cloud.azure.storage.blob.account-name}")
    private String accountName;

    @Value("${spring.cloud.azure.storage.blob.account-key}")
    private String accountKey;

    @Value("${spring.cloud.azure.storage.blob.endpoint}")
    private String endpoint;

    public String uploadImage(MultipartFile file, String containerName) {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String blobUrl = "";

        try {
            BlobClientBuilder blobClientBuilder = new BlobClientBuilder()
                    .endpoint(endpoint)
                    .containerName(containerName)
                    .blobName(fileName)
                    .credential(new com.azure.storage.common.StorageSharedKeyCredential(accountName, accountKey));

            blobClientBuilder.buildClient().upload(file.getInputStream(), file.getSize(), true);

            // Construct the correct blob URL
            blobUrl = endpoint + "" + containerName + "/" + fileName;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while uploading file.");
        }

        return blobUrl;
    }
}
