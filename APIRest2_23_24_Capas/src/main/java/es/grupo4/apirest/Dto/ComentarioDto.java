package es.grupo4.apirest.Dto;

import es.grupo4.apirest.constant.Utils;
import es.grupo4.apirest.model.Comentario;
import es.grupo4.apirest.model.Incidencia;
import es.grupo4.apirest.model.Personal;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

public class ComentarioDto {
    private int id;

    private int incidenciaNum;
    private String texto;
    private LocalDateTime fechahora;
    private Incidencia incidencia;
    private Personal personal;
    private String adjuntoUrl;
    private String fichero;
    private String extension;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIncidenciaNum() {
        return incidenciaNum;
    }

    public void setIncidenciaNum(int incidenciaNum) {
        this.incidenciaNum = incidenciaNum;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getFechahora() {
        return fechahora;
    }

    public void setFechahora(LocalDateTime fechahora) {
        this.fechahora = fechahora;
    }

    public Incidencia getIncidencia() {
        return incidencia;
    }

    public void setIncidencia(Incidencia incidencia) {
        this.incidencia = incidencia;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    public String getAdjuntoUrl() {
        return adjuntoUrl;
    }

    public void setAdjuntoUrl(String adjuntoUrl) {
        this.adjuntoUrl = adjuntoUrl;
    }

    public String getFichero() {
        return fichero;
    }

    public void setFichero(String fichero) {
        this.fichero = fichero;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public static ComentarioDto fromEntity(Comentario comentario){
        ComentarioDto dto = new ComentarioDto();
        dto.setId(comentario.getId());

        if (comentario.getFechahora() != null){
            dto.setFechahora(comentario.getFechahora()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .atStartOfDay());
        }else{
           dto.setFechahora(null);
        }
        if (comentario.getTexto() != null){
            dto.setTexto(comentario.getTexto());
        }
        if (comentario.getIncidencia() != null){
            dto.setIncidencia(comentario.getIncidencia());
        }
        if (comentario.getPersonal() != null){
            dto.setPersonal(comentario.getPersonal());
        }
        if (comentario.getAdjuntoUrl() != null){
            File file=new File(Utils.path+comentario.getAdjuntoUrl());
            dto.setFichero(encodeFileToBase64(file));
            dto.setExtension(FilenameUtils.getExtension(file.getName()));
        }
        return dto;
    }

    public static Comentario toEntity(ComentarioDto dto) {
        Comentario comentario = new Comentario();
        comentario.setId(dto.getId());

        if (dto.getFechahora() != null) {
            comentario.setFechahora(Timestamp.valueOf(dto.getFechahora())); // Convert LocalDateTime to Timestamp
        } else {
            comentario.setFechahora(null);
        }

        if (dto.getIncidenciaNum() > 0) {
            Incidencia incidencia = new Incidencia();
            incidencia.setNum(dto.getIncidenciaNum());
            comentario.setIncidencia(incidencia);
        } else {
            throw new IllegalArgumentException("IncidenciaNum must be greater than 0");
        }

        if (dto.getPersonal() != null) {
            comentario.setPersonal(dto.getPersonal());
        } else {
            comentario.setPersonal(null);
        }

        comentario.setTexto(dto.getTexto() != null ? dto.getTexto() : "");

        if (dto.getAdjuntoUrl() != null) {

        } else {

        }

        return comentario;
    }
    private static String encodeFileToBase64(File file) {
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new IllegalStateException("Fallo al codificar el archivo " + file, e);
        }
    }

    private static File decodeFileFromBase64(String fic, String url){
        try{
            File file=new File(Utils.path+url);
            byte[] fileContent=Base64.getDecoder().decode(fic);
            FileUtils.writeByteArrayToFile(file, fileContent);
            return file;
        }catch (IOException e) {
            throw new IllegalStateException("Fallo al codificar el archivo " + fic, e);
        }
    }

}
