package com.marqet.WebServer.util;

import javax.servlet.http.Part;
import java.io.*;

/**
 * Created by hpduy17 on 3/28/15.
 */
public class UploadImageUtil {
    public String upload(String fileName, String path, Part filePart) throws IOException {
        OutputStream out = null;
        InputStream fileContent = null;
        try {
            fileName +="."+getExtension(filePart);
            out = new FileOutputStream(new File(path + File.separator
                    + fileName));
            fileContent = filePart.getInputStream();
            int read = 0;
            final byte[] bytes = new byte[1024];
            while ((read = fileContent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
        } catch (Exception fne) {
            return "";
        } finally {
            if (out != null) {
                out.close();
            }
            if (fileContent != null) {
                fileContent.close();
            }
        }
        return path + File.separator + fileName;
    }
    //TODO scale image to thumbnail
    public String uploadThumbnail(String fileName, Part filePart) throws IOException {
        InputStream fileContent = null;
        try {
            fileName +="."+getExtension(filePart);
            fileContent = filePart.getInputStream();
            ImageProcessUtil.smartCropAndWriteFile(fileContent,340,340,getExtension(filePart),new File(Path.getThumbnailPath() + File.separator
                    + fileName));
        } catch (Exception fne) {
            return "";
        } finally {
            if (fileContent != null) {
                fileContent.close();
            }
        }
        return Path.getThumbnailPath() + File.separator+fileName;
    }
    private String getExtension(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        System.out.println("Part Header = " + partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                String fileName = content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
                if (fileName.contains("."))
                    return fileName.split("\\.")[1];
                return "";
            }
        }
        return null;
    }
}
