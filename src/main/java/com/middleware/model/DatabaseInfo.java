package com.middleware.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "databaseinfo")
public class DatabaseInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="url")
    private String url;

    @Column(name="lastencounterdate")
    private Date lastencounterdate;

    @Column(name="status")
    private String status;

    @Column(name="mflcode")
    private String mflcode;

    @Column(name="reuploaded")
    private String reuploaded;

    @Column(name="dbsize")
    private String dbsize;

    @Column(name="dbname")
    private String dbname;

    @Column(name="created_by")
    private int created_by;

    @Column(name="created_on")
    private Date created_on;

    @Column(name="modified_by", nullable=true)
    private int modified_by;

    @Column(name="modified_on", nullable=true)
    private Date modified_on;

  /*  public DatabaseInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }*/

   public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getLastencounterdate() {
        return lastencounterdate;
    }

    public void setLastencounterdate(Date lastencounterdate) {
        this.lastencounterdate = lastencounterdate;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public Date getCreated_on() {
        return created_on;
    }

    public void setCreated_on(Date created_on) {
        this.created_on = created_on;
    }

    public int getModified_by() {
        return modified_by;
    }

    public void setModified_by(int modified_by) {
        this.modified_by = modified_by;
    }

    public Date getModified_on() {
        return modified_on;
    }

    public void setModified_on(Date modified_on) {
        this.modified_on = modified_on;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReuploaded() {
        return reuploaded;
    }

    public void setReuploaded(String reuploaded) {
        this.reuploaded = reuploaded;
    }

    public String getMflcode() {
        return mflcode;
    }

    public void setMflcode(String mflcode) {
        this.mflcode = mflcode;
    }

    public String getDbsize() {
        return dbsize;
    }

    public void setDbsize(String dbsize) {
        this.dbsize = dbsize;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }
}
