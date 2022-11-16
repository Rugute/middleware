package ke.or.karp.middleware.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SMTPServer")
public class SMTPServer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "host")
    private String host;

    @Column(name = "port")
    private String port;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "protocals")
    private String protocals;

    @Column(name = "auth")
    private String auth;

    @Column(name = "sslenabled")
    private String sslenabled;

    @Column(name = "debug")
    private String debug;

    @Column(name = "sectionid")
    private String sectionid;

    @Column(name="created_by")
    private int created_by;

    @Column(name="created_on")
    private Date created_on;

    @Column(name="modified_by", nullable=true)
    private int modified_by;

    @Column(name="modified_on", nullable=true)
    private Date modified_on;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProtocals() {
        return protocals;
    }

    public void setProtocals(String protocals) {
        this.protocals = protocals;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getSslenabled() {
        return sslenabled;
    }

    public void setSslenabled(String sslenabled) {
        this.sslenabled = sslenabled;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public String getSectionid() {
        return sectionid;
    }

    public void setSectionid(String sectionid) {
        this.sectionid = sectionid;
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
}
