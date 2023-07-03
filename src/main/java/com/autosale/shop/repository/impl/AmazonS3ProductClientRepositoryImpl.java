package com.autosale.shop.repository.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.autosale.shop.repository.AmazonS3ProductClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class AmazonS3ProductClientRepositoryImpl implements AmazonS3ProductClientRepository {

    private final AmazonS3 s3;
    @Override
    public void save(String data) {
            s3.putObject("partsshop", LocalDate.now() + ".csv", data);
    }

    @Override
    public String load(String name) {
        try(S3Object s3Object = s3.getObject("partsshop", name)) {
            return new String(s3Object.getObjectContent().readAllBytes());
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (AmazonS3Exception e)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Backup file with name "+name+" not found!");
        }
    }
}
