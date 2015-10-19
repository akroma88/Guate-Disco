package com.ecys.ingenieria.usac.guate_disco;

/**
 * Created by akroma on 7/10/15.
 */
public class DTO_Comentario {
    private String text;
    private String fecha;
    private float rt;
    private String user;
    private String urlPic;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public float getRt() {
        return rt;
    }

    public void setRt(float rt) {
        this.rt = rt;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUrlPic() {
        return urlPic;
    }

    public void setUrlPic(String urlPic) {
        this.urlPic = urlPic;
    }
}
