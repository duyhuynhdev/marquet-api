package com.marqet.WebServer.util;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaViewer;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import org.apache.log4j.Logger;
import org.junit.Test;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

/**
 * Created by hpduy17 on 6/17/15.
 */
public class VideoProcessor {
    Logger logger = LoggerFactory.createLogger(this.getClass());

    public String convertVideoFromMOVToMP4(String fileName, String path, Part filePart) throws IOException {
        String inputFileName = new UploadImageUtil().upload(fileName, path, filePart);
        if (!inputFileName.contains("mp4") &&
                convertVideoFromMOVToMP4(inputFileName, path + File.separator + fileName + ".mp4")) {
            File file = new File(inputFileName);
            if (file.exists())
                file.delete();
            return path + File.separator + fileName + ".mp4";
        }
        return inputFileName;
    }

    public boolean convertVideoFromMOVToMP4(String inputFilename, String outputFilename) {
        try {
            Long st = System.currentTimeMillis();
            // create a media reader
            IMediaReader mediaReader = ToolFactory.makeReader(inputFilename);
            // create a media writer
            IMediaWriter mediaWriter = ToolFactory.makeWriter(outputFilename, mediaReader);
            // add a writer to the reader, to create the output file
            mediaReader.addListener(mediaWriter);
            // create a media viewer with stats enabled
            IMediaViewer mediaViewer = ToolFactory.makeViewer(true);
            // add a viewer to the reader, to see the decoded media
            // add a viewer to the reader, to see the decoded media
            mediaReader.addListener(mediaViewer);
            // read and decode packets from the source file and
            // and dispatch decoded audio and video to the writer
            while (mediaReader.readPacket() == null) ;

            Long end = System.currentTimeMillis();
            System.out.println("Time Taken In Milli Seconds: " + (end - st));
            logger.info("Convert video complete - Time Taken In Milli Seconds:"+(end - st));
            return true;

        } catch (Exception ex) {
            logger.error(ex.getStackTrace());
            return false;
        }
    }

    @Test
    public String test() {
        try {
            String input = "/marqet/production/images/products/duy.MOV";
            String ouput = "/marqet/production/images/products/duy.mp4";
            System.out.print(convertVideoFromMOVToMP4(input, ouput));
            return "convert completed";
        }catch (Exception ex){
            ex.printStackTrace();
            return "StackTrace:"+ex.getStackTrace()+"\n Message:"+ex.getMessage()+"\n Cause:"+ex.getCause();
        }

    }

}
