package com.thiago.videoocutter.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class DownloadService {

    // Baixa o vídeo com yt-dlp; retorna o caminho do arquivo baixado
    public String baixarVideo(String url) throws IOException, InterruptedException {
        String pastaBaixada = "C:\\Users\\thiago\\Desktop\\videos";
        String output = pastaBaixada + "\\" + UUID.randomUUID() + ".mp4";

        ProcessBuilder pb = new ProcessBuilder(
                "D:\\dowload\\ffmpeg\\ffmpeg-7.1.1-essentials_build\\bin\\yt-dlp.exe",
                "-f", "bestvideo[ext=mp4]+bestaudio[ext=m4a]/mp4",
                "--merge-output-format", "mp4",
                "--retries", "10",
                "--fragment-retries", "10",
                "-o", output,
                url
        );

        pb.inheritIO(); // mostra logs no console
        Process p = pb.start();
        int rc = p.waitFor();
        if (rc != 0) {
            throw new RuntimeException("Erro ao baixar com yt-dlp. Código: " + rc);
        }

        System.out.println("Vídeo baixado em: " + output);
        return output;
    }
}
