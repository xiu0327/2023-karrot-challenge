package numble.karrot.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.karrot.aws.exception.ImageConvertFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@PropertySource("classpath:aws.properties")
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${bucket}")
    private String bucket;

    @Value("${removeUrl}")
    private String removeUrl;

    public String getImageUrl(MultipartFile multipartFile, String dirName) throws IOException{
        File file = convertImageFile(multipartFile).orElseThrow(() -> new ImageConvertFileException());
        return uploadImageFile(file, dirName);
    }

    private Optional<File> convertImageFile(MultipartFile multipartFile) throws IOException{
        File convertFile = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());
        if(convertFile.createNewFile()){
            try(FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    private void removeNewFile(File file){
        if (file.delete()){
            log.info("New file delete success");
            return;
        }
        log.info("New file delete fail");
    }

    public String uploadImageFile(File uploadImage, String dirName){
        String imageName = dirName + "/" + UUID.randomUUID() + "/" + uploadImage.getName();
        String imageUrl = putImageFile(uploadImage, imageName);
        removeNewFile(uploadImage);
        return imageUrl;
    }
    
    public String putImageFile(File image, String imageName){
        amazonS3Client.putObject(new PutObjectRequest(bucket, imageName, image).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, imageName).toString();
    }

    public void deleteImageFile(String imageUrl){
        String imageName = getFileName(imageUrl);
        amazonS3Client.deleteObject(bucket, imageName);
        log.info("old image delete success");
    }

    public String getFileName(String imageUrl){
        return imageUrl.replace(removeUrl, "");
    }
}
