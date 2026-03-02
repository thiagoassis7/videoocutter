package com.thiago.videoocutter.service;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

@Service
public class DownloadService {

    private final String YTDLP = "D:\\dowload\\ffmpeg\\ffmpeg-7.1.1-essentials_build\\bin\\yt-dlp.exe";
    private final String PASTA_VIDEOS = "C:\\Users\\thiago\\Desktop\\videos\\";

    // 🔹 Obtém o título EXATO do YouTube sempre em UTF-8
    public String obterTitulo(String url) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                YTDLP,
                "--encoding", "utf-8",
                "--get-title",
                url
        );

        Process p = pb.start();
        Scanner sc = new Scanner(p.getInputStream(), "UTF-8");

        String titulo = sc.hasNextLine() ? sc.nextLine() : "video";
        sc.close();
        p.waitFor();

        // Remove caracteres proibidos no Windows
        return titulo.replaceAll("[\\\\/:*?\"<>|]", "").trim();
    }

    // 🔹 Baixa o vídeo com o título original
    public String baixarVideo(String url, String titulo)
            throws IOException, InterruptedException {

        File pasta = new File(PASTA_VIDEOS);
        if (!pasta.exists()) pasta.mkdirs();

        String caminhoFinal = PASTA_VIDEOS + titulo + ".mp4";

        ProcessBuilder pb = new ProcessBuilder(
                "yt-dlp",
                "--cookies", "C:\\Users\\thiago\\Downloads\\cookies.txt",
                "-f", "18",
                "-o", caminhoFinal,
                url
        );


        pb.inheritIO();
        Process p = pb.start();
        int exitCode = p.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Falha no download (yt-dlp retornou erro)");
        }

        return caminhoFinal;
    }
}