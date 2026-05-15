package com.museum.artist.domain.daocontracts;

import java.io.InputStream;

public interface ImageStorage {
    /**
     * Uploads a file to the storage system.
     * @return The path/URL where the file is stored.
     */
    String uploadFile(String fileName, InputStream inputStream, String contentType);

    void deleteFile(String fileName);
}
