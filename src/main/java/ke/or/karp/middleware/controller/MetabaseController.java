package ke.or.karp.middleware.controller;

import com.google.api.client.json.webtoken.JsonWebSignature;
import ke.or.karp.middleware.model.User;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Date;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;

@Controller
@RequestMapping("/metabase")
public class MetabaseController {
    @Value("${app.metabase.key}")
    public String METABASE_SECRET_KEY;
    @Value("${app.metabase.url}")
    public String METABASE_SITE_URL ;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView posts(HttpSession session) {
       if(session.getAttribute("user")!=null) {
           ModelAndView modelAndView = new ModelAndView();
         //  Jwt token = JwtHelper.encode("{\"resource\": {\"dashboard\": 2}, \"params\": {}, \"exp\":Math.round(Date.now() / 1000) + (10 * 60)}", new MacSigner(METABASE_SECRET_KEY));
           Jwt token = JwtHelper.encode("{\"resource\": {\"dashboard\": 2}, \"params\": {}}", new MacSigner(METABASE_SECRET_KEY));

           String url = METABASE_SITE_URL + "/embed/dashboard/" + token.getEncoded() + "#theme=night&bordered=true&titled=true";
          System.out.println("Rugute url metabase "+url);
            User userdetails = (User) session.getAttribute("user");
            Date nowDate = new Date();
            modelAndView.addObject("url",url);
            modelAndView.setViewName("maindashboard");
            return modelAndView;
       }
        else{
            return new ModelAndView("redirect:/auth/login");
        }

    }
}
