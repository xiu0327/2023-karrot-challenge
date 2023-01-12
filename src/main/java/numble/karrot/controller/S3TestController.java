package numble.karrot.controller;

import lombok.RequiredArgsConstructor;
import numble.karrot.aws.S3Uploader;
import numble.karrot.member.dto.MemberUpdateRequest;
import numble.karrot.product.domain.ProductCategory;
import numble.karrot.product.dto.ProductRegisterRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Controller
public class S3TestController {

    private final S3Uploader s3Uploader;

    @GetMapping("image-test")
    public String imageTest(Model model){
        List<String> urls = new ArrayList<>();
        urls.add("test1");
        urls.add("test2");
        model.addAttribute("tableList", urls);
        model.addAttribute("form", new MemberUpdateRequest());
        return "image-test";
    }

    @PostMapping("/image-test")
    public String upload(@ModelAttribute MemberUpdateRequest form) throws IOException {
        String url = s3Uploader.getImageUrl(form.getProfile(), "static");
        System.out.println("url = " + url);
        return "start";
    }


}
