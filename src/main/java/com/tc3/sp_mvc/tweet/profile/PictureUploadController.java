package com.tc3.sp_mvc.tweet.profile;

import com.tc3.sp_mvc.tweet.config.PictureUploadProperties;
import com.tc3.sp_mvc.tweet.profile.UserProfileSession;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.util.Locale;

@Controller
public class PictureUploadController {
    private final Resource uploadResource;
    private final Resource anonymousResource;
    private final MessageSource messageSource;
    private final UserProfileSession userProfileSession;

    @Autowired
    public PictureUploadController(PictureUploadProperties pictureUploadProperties,
                                   MessageSource messageSource, UserProfileSession userProfileSession) {
        this.uploadResource = pictureUploadProperties.getUploadResource();
        this.anonymousResource = pictureUploadProperties.getAnonymousResource();
        this.messageSource = messageSource;
        this.userProfileSession=userProfileSession;
    }

    /**
     * 上传图片
     * @param file
     * @param redAttributes
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/profile",params = {"upload"}, method = RequestMethod.POST)
    public String onUpload(MultipartFile file, RedirectAttributes redAttributes)
            throws IOException {
        if (file.isEmpty() || !isImage(file)) {
            redAttributes.addFlashAttribute("error",
                    "Incorrect file. Please upload a picture.");
            return "redirect:profile";
        }
        Resource picturePath = copyFileToPictures(file);
        userProfileSession.setPicturePath(picturePath);
        return "redirect:profile";
    }

    /**
     * 获取图片
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/uploadedPicture")
    public void getUploadedPicture(HttpServletResponse response) throws IOException {
        Resource picturePath = userProfileSession.getPicturePath();
        if(picturePath==null){
            picturePath=anonymousResource;
        }
        response.setHeader("Content-Type",
                URLConnection.guessContentTypeFromName(picturePath.getFilename()));
        IOUtils.copy(picturePath.getInputStream(), response.getOutputStream());
    }


    @ExceptionHandler(IOException.class)
//    public ModelAndView handleIOException(IOException ioException) {
    public ModelAndView handleIOException(Locale locale) {
        ModelAndView modelAndView = new ModelAndView("profile/profilePage");
//        modelAndView.addObject("error", ioException.getMessage());
        modelAndView.addObject("error",
                messageSource.getMessage("upload.io.exception", null, locale));
        return modelAndView;
    }

    //全局异常处理
    @RequestMapping("uploadError")
//    public ModelAndView onUploadError(HttpServletRequest request) {
    public ModelAndView onUploadError(Locale locale) {
        ModelAndView modelAndView = new ModelAndView("profile/profilePage");
//        modelAndView.addObject("error", request.getAttribute(WebUtils.ERROR_MESSAGE_ATTRIBUTE));
        modelAndView.addObject("error",
                messageSource.getMessage("upload.file.too.big", null, locale));
        return modelAndView;
    }


    private Resource copyFileToPictures(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("pic",
                getFileExtension(file.getOriginalFilename()), uploadResource.getFile());
        try (InputStream in = file.getInputStream();
             OutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return new FileSystemResource(tempFile);
    }

    private boolean isImage(MultipartFile file) {
        return file.getContentType().startsWith("image");//file.getContentType() 它将会是 image/png、image/jpg 等
    }

    private static String getFileExtension(String name) {
        return name.substring(name.lastIndexOf("."));
    }

}