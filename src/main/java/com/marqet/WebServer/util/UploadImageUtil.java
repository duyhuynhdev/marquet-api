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
            fileName += "." + getExtension(filePart);
            String filePath = AccentRemoveAlgorithm.removeAccent(path + File.separator + fileName).replaceAll(" ", "");
            out = new FileOutputStream(new File(filePath));
            fileContent = filePart.getInputStream();
            int read = 0;
            final byte[] bytes = new byte[1024];
            while ((read = fileContent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            return filePath;
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

    }

    public String uploadProduct(String fileName, String path, Part filePart) throws IOException {
        OutputStream out = null;
        InputStream fileContent = null;

        try {
            fileName += "." + getExtension(filePart);
            String filePath = AccentRemoveAlgorithm.removeAccent(path + File.separator + fileName).replaceAll(" ", "");
            out = new FileOutputStream(new File(filePath));
            fileContent = filePart.getInputStream();
//            ImageProcessUtil.smartCropAndWriteFile(fileContent,800,800,getExtension(filePart),new File(path + File.separator
//                    + fileName));
            int read = 0;
            final byte[] bytes = new byte[1024];
            while ((read = fileContent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            return filePath;
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

    }

    //TODO scale image to thumbnail
    public String uploadThumbnail(String fileName, Part filePart) throws IOException {
        InputStream fileContent = null;
        try {
            fileName += "." + getExtension(filePart);
            String filePath = AccentRemoveAlgorithm.removeAccent(Path.getThumbnailPath() + File.separator + fileName).replaceAll(" ", "");
            fileContent = filePart.getInputStream();
            ImageProcessUtil.smartCropAndWriteFile(fileContent, 340, 340, getExtension(filePart), new File(filePath));
            return filePath;
        } catch (Exception fne) {
            return "";
        } finally {
            if (fileContent != null) {
                fileContent.close();
            }
        }

    }

    private String getExtension(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        System.out.println("Part Header = " + partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                String fileName = content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
                if (fileName.contains(".")) {
                    String[] temp = fileName.split("\\.");
                    return temp[temp.length-1];
                }
                return "";
            }
        }
        return null;
    }
}
