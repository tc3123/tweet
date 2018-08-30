package com.tc3.sp_mvc.tweet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

//配置文件转对象
@ConfigurationProperties(prefix = "upload.pictures")
public class PictureUploadProperties {
    private Resource uploadResource;
    private Resource anonymousResource;

    public void setUploadPath(String uploadPath) {
        this.uploadResource =new DefaultResourceLoader().getResource(uploadPath) ;
    }
    
    public void setAnonymousPicture(String anonymousPicture) {
        this.anonymousResource =new DefaultResourceLoader().getResource(anonymousPicture);
    }

    public Resource getUploadResource() {
        return uploadResource;
    }

    public Resource getAnonymousResource() {
        return anonymousResource;
    }
}
