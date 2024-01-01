package com.example.userservice.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

public class ImageUtil {
    /**
     * @return MockMultipartFile
     * @description 테스트용 이미지 생성
     */
    public static MockMultipartFile generateMockImageFile(String fileName) throws IOException {
        return new MockMultipartFile(
                fileName,
                "test_image.png",
                "image/png",
                new ClassPathResource("/static/image/test_image.png").getInputStream()
        );
    }
}
