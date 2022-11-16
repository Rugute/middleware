package ke.or.karp.middleware.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_role")
public class UserRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    @Column(name = "user_id")
    private String userid;

    @Column(name = "role_id")
    private String roleid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

}
