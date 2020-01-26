package com.yjs.bridge.controller;


import com.yjs.bridge.service.ITransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;

@RestController
@RequestMapping("/transfer/image")
public class TransferImageController {
    @Autowired
    private ITransferService transferService;

    @PostMapping("/url")
    public BufferedImage recordActShare(String url) {
        return transferService.transferImage(url);
    }

}
