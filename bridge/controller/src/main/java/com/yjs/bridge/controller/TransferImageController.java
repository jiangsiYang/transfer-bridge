package com.yjs.bridge.controller;


import com.yjs.bridge.service.ITransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

@RestController
@RequestMapping("/transfer/image")
public class TransferImageController {
    @Autowired
    private ITransferService transferService;

    @GetMapping("/url")
    public void recordActShare(HttpServletRequest request, HttpServletResponse response, String url) {
        try {
            response.setContentType("image/png");
            ImageIO.write(transferService.transferImage(url), "JPEG", response.getOutputStream());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
