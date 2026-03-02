package com.thiago.videoocutter.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.*;

@Service
public class CorteService {

    private final String FFPROBE = "D:\\dowload\\ffmpeg\\ffmpeg-7.1.1-essentials_build\\bin\\ffprobe.exe";
    private final String FFMPEG = "D:\\dowload\\ffmpeg\\ffmpeg-7.1.1-essentials_build\\bin\\ffmpeg.exe";

    // 🔹 Normaliza (remove acentos e caracteres inválidos)
    private String normalizarTitulo(String titulo) {

        // Remove acentos
        String texto = Normalizer.normalize(titulo, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Remove caracteres proibidos no Windows
        texto = texto.replaceAll("[\\\\/:*?\"<>|]", "");

        // Remove espaços em excesso
        texto = texto.trim().replaceAll(" +", " ");

        return texto;
    }

    // 🔹 Corta o vídeo usando ffmpeg
    public List<String> cortarVideo(String caminhoVideo, String titulo, int duracaoCorte)
            throws IOException, InterruptedException {

        List<String> cortes = new ArrayList<>();
        File video = new File(caminhoVideo);

        if (!video.exists()) {
            throw new RuntimeException("Vídeo não encontrado: " + caminhoVideo);
        }

        titulo = normalizarTitulo(titulo);

        String pastaCortes = video.getParent() + "\\cortes\\";
        new File(pastaCortes).mkdirs();

        double duracaoTotal = obterDuracaoVideo(caminhoVideo);
        int partes = (int) Math.ceil(duracaoTotal / duracaoCorte);

        for (int i = 0; i < partes; i++) {

            String nomeCorte = pastaCortes + titulo + " - parte_" + (i + 1) + ".mp4";
            String inicio = String.valueOf(i * duracaoCorte);

            ProcessBuilder pbCorte = new ProcessBuilder(
                    FFMPEG,
                    "-ss", inicio,                   // Busca rápida (mais rápido)
                    "-i", caminhoVideo,
                    "-t", String.valueOf(duracaoCorte),
                    "-c:v", "libx264",               // Recodifica só o trecho (preciso)
                    "-preset", "ultrafast",          // MUITO rápido
                    "-c:a", "aac",
                    "-movflags", "+faststart",
                    nomeCorte
            );

            pbCorte.inheritIO();
            Process p = pbCorte.start();
            p.waitFor();

            cortes.add(nomeCorte);
        }

        return cortes;
    }


    // 🔹 Obtém duração com ffprobe
    private double obterDuracaoVideo(String caminhoVideo) throws IOException, InterruptedException {

        ProcessBuilder pbInfo = new ProcessBuilder(
                FFPROBE,
                "-v", "error",
                "-show_entries", "format=duration",
                "-of", "default=noprint_wrappers=1:nokey=1",
                caminhoVideo
        );

        Process pInfo = pbInfo.start();
        Scanner scanner = new Scanner(pInfo.getInputStream());
        String duracaoStr = scanner.hasNext() ? scanner.next() : "0";
        scanner.close();
        pInfo.waitFor();

        return Double.parseDouble(duracaoStr);
    }

    // 🔹 Comprime o corte
    public String comprimirVideo(String caminhoEntrada) throws IOException, InterruptedException {

        String saida = caminhoEntrada.replace(".mp4", "_.mp4");

        ProcessBuilder pb = new ProcessBuilder(
                FFMPEG,
                "-i", caminhoEntrada,
                "-vf", "scale=1080:-1",
                "-vcodec", "libx264",
                "-crf", "23",
                "-preset", "slow",
                "-acodec", "aac",
                "-b:a", "128k",
                saida
        );

        pb.inheritIO();
        Process p = pb.start();
        p.waitFor();

        File original = new File(caminhoEntrada);
        if (original.exists()) {
            original.delete();
        }

        return saida;
    }
}
