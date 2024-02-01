package com.autosale.shop.repository.impl

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.AmazonS3Exception
import com.autosale.shop.repository.AmazonS3ProductClientRepository
import org.springframework.stereotype.Repository
import java.io.IOException
import java.time.LocalDate

@Repository
class AmazonS3ProductClientRepositoryImpl(private val s3: AmazonS3) : AmazonS3ProductClientRepository {

    override fun save(data: String) {
        s3.putObject("partsshop", LocalDate.now().toString() + ".csv", data)
    }

    override fun load(name: String): String {
        try {
            s3.getObject("partsshop", name).use { s3Object -> return String(s3Object.objectContent.readAllBytes()) }
        } catch (e: AmazonS3Exception) {
            throw RuntimeException(e)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}
