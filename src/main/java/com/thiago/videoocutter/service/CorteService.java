package com.thiago.videoocutter.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class CorteService {

    // Corta o vídeo em partes de duracaoCorte segundos
    public List<String> cortarVideo(String caminhoVideo, int duracaoCorte) throws IOException, InterruptedException {
        List<String> cortes = new ArrayList<>();
        File video = new File(caminhoVideo);

        if (!video.exists()) {
            throw new RuntimeException("Vídeo não encontrado: " + caminhoVideo);
        }

        // Pasta para salvar cortes
        String pastaCortes = video.getParent() + "\\cortes";
        new File(pastaCortes).mkdirs();

        // Descobrir a duração do vídeo com ffprobe
        double duracaoTotal = obterDuracaoVideo(caminhoVideo);

        int partes = (int) Math.ceil(duracaoTotal / duracaoCorte);

        for (int i = 0; i < partes; i++) {
            String nomeCorte = pastaCortes + "\\corte_" + (i + 1) + ".mp4";

            ProcessBuilder pbCorte = new ProcessBuilder(
                    "D:\\dowload\\ffmpeg\\ffmpeg-7.1.1-essentials_build\\bin\\ffmpeg.exe",
                    "-i", caminhoVideo,
                    "-ss", String.valueOf(i * duracaoCorte),
                    "-t", String.valueOf(duracaoCorte),
                    "-c", "copy",
                    nomeCorte
            );

            pbCorte.inheritIO(); // Mostra log do ffmpeg
            Process pCorte = pbCorte.start();
            int rc = pCorte.waitFor();
            if (rc != 0) {
                throw new RuntimeException("Erro ao cortar vídeo: " + nomeCorte);
            }

            cortes.add(nomeCorte);
            System.out.println("Corte gerado: " + nomeCorte);
        }

        return cortes;
    }

    // Método auxiliar para pegar duração do vídeo com ffprobe
    private double obterDuracaoVideo(String caminhoVideo) throws IOException, InterruptedException {
        ProcessBuilder pbInfo = new ProcessBuilder(
                "D:\\dowload\\ffmpeg\\ffmpeg-7.1.1-essentials_build\\bin\\ffprobe.exe",
                "-v", "error",
                "-show_entries", "format=duration",
                "-of", "default=noprint_wrappers=1:nokey=1",
                caminhoVideo
        );

        Process pInfo = pbInfo.start();

        // Lê a saída do ffprobe
        InputStream is = pInfo.getInputStream();
        Scanner scanner = new Scanner(is);
        String duracaoStr = scanner.hasNext() ? scanner.next() : "0";
        scanner.close();

        pInfo.waitFor();
        return Double.parseDouble(duracaoStr);
    }
}
