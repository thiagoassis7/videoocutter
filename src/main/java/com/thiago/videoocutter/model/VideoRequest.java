package com.thiago.videoocutter.model;

public class VideoRequest {
    private  String url;
    private  int duracaoCorte;

    public  VideoRequest(){}

    public  String getUrl(){return url;}
    public  void  setUrl(String url) {this.url= url;}

    public  int getDuracaoCorte() {return duracaoCorte;}
    public void setDuracaoCorte(int duracaoCorte) {this.duracaoCorte=duracaoCorte;}


}
