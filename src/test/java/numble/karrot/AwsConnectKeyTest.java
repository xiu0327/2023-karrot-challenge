package numble.karrot;

import numble.karrot.aws.AmazonS3Config;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AwsConnectKeyTest {

    @Test
    void 접근값_확인(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AmazonS3Config.class);
        AmazonS3Config bean = ac.getBean(AmazonS3Config.class);
        System.out.println("amazonS3Config = " + bean.getAccessKey());
        System.out.println("amazonS3Config = " + bean.getSecretKey());
    }

    @Test
    void url_분리(){
        String[] splitUrl = "https://karrot-challenge-bucket.s3.ap-northeast-2.amazonaws.com/static/67889cc5-f643-4b22-ab24-197397a19b9a/P6.jpg".split("/");
        for (String s : splitUrl) {
            System.out.println("s = " + s);
        }
    }
}
