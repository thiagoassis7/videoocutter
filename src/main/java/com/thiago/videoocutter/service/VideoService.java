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
    public void baixarAsync(String url ) {
        try {
            String titulo = downloadService.obterTitulo(url);
             downloadService.baixarVideo( url,titulo);
            System.out.println("Download concluído!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    public void baixarECortarAsync(String url, int duracaoCorte) {
        try {
            // 1. Pega título e baixa
            String titulo = downloadService.obterTitulo(url);
            String caminhoVideo = downloadService.baixarVideo(url,titulo);

            // 2. Corta vídeo
            List<String> cortes = cortarService.cortarVideo(caminhoVideo, titulo, duracaoCorte);

            // 3. Comprime cortes
            for (String corte : cortes) {
                cortarService.comprimirVideo(corte);
            }

            System.out.println("\n🎉 Processo concluído!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
