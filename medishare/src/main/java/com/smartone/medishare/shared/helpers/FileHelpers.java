package com.smartone.medishare.shared.helpers;

import org.apache.commons.io.FilenameUtils;

public class FileHelpers {
    public static String appendTimestamp(String originalFilename) {
        String baseName = FilenameUtils.getBaseName(originalFilename);
        String extension = FilenameUtils.getExtension(originalFilename);
        long unixEpochTime = System.currentTimeMillis() / 1000;

        return extension.isEmpty()
            ? baseName + "_" + unixEpochTime
            : baseName + "_" + unixEpochTime + "." + extension;
    }
}
