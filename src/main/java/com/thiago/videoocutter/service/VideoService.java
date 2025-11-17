package com.thiago.videoocutter.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {

    private final DownloadService downloadService;
    private final CorteService cortarService;

    public VideoService(DownloadService downloadService, CorteService cortarService) {
        this.downloadService = downloadService;
        this.cortarService = cortarService;
    }


    @Async
    public void baixarECortarAsync(String url, int duracaoCorte) {
        try {
            // 1. Baixa o vídeo
            String caminhoVideo = downloadService.baixarVideo(url);

            // 2. Corta o vídeo em partes
            List<String> cortes = cortarService.cortarVideo(caminhoVideo, duracaoCorte);

            System.out.println("Processo finalizado! Cortes: " + cortes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
