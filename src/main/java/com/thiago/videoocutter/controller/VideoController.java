package com.thiago.videoocutter.controller;

import com.thiago.videoocutter.model.VideoRequest;
import com.thiago.videoocutter.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping("/processar")
    public String processar(@RequestBody VideoRequest request) {
        videoService.baixarECortarAsync(request.getUrl(), request.getDuracaoCorte());
        return "Download e corte iniciados! Verifique a pasta de vídeos depois.";
    }
}
