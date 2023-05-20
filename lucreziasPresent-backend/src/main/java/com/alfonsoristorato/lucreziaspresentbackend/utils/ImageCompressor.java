package com.alfonsoristorato.lucreziaspresentbackend.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public interface ImageCompressor {
    byte[] compressImage(MultipartFile file) throws IOException;
}
