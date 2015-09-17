/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cresterp.school;

import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.MultipartRequest;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author user
 */
public class StudentLogin {
    
    private String username;
    private String token;
    private String URL = "";
    
    private ConnectionRequest sendRequest(Object[] params) throws IOException {
        boolean isMultipart = false;
        int plen = params.length;
        for (int i=0; i < params.length; i+=2) {
            if (params[i+1] instanceof Image) {
                isMultipart = true;
                break;
            }
        }
        ConnectionRequest req = isMultipart ? new MultipartRequest() : new ConnectionRequest();
        req.setUrl(URL+"/index.php");
        req.setPost(true);
        req.setHttpMethod("POST");
        req.addArgument("-action", "friends_api");
        
        for (int i=0; i<plen; i+=2) {
            if (isMultipart && params[i+1] instanceof Image) {
                Image img = (Image)params[i+1];
                EncodedImage enc = null;
                if (img instanceof EncodedImage) {
                    enc = (EncodedImage)img;
                } else {
                    enc = EncodedImage.createFromImage(img, false);
                }
                ((MultipartRequest)req).addData((String)params[i], enc.getImageData(), "image/png");
                
            } else {
                //req.addArgumentNoEncoding(Util.encodeUrl((String)params[i]), Util.encodeUrl((String)params[i+1]));
                req.addArgumentNoEncoding((String)params[i], (String)params[i+1]);
            }
        }
        NetworkManager.getInstance().addToQueueAndWait(req);
        return req;
    }
    
    private Map getResponse(Object[] params) throws IOException {
        ConnectionRequest req = sendRequest(params);
        if (req.getResponseCode() == 200) {
            System.out.println(new String(req.getResponseData(), "UTF-8"));
            Map out = new HashMap();
            IOException[] err = new IOException[1];
            Display.getInstance().invokeAndBlock(() -> {
                JSONParser p = new JSONParser();
                try (InputStreamReader r = new InputStreamReader(new ByteArrayInputStream(req.getResponseData()))) {
                    out.putAll(p.parseJSON(r));
                } catch (IOException ex) {
                    System.out.println("Failed to parse JSON ");
                    err[0] = ex;
                }
            });
            
            if (out != null) {
                return out;
            } else {
                throw err[0];
            }
        } else {
            throw new IOException("Request failed with response "+req.getResponseCode());
        }
    }
    
    public void login(String username, String password) throws IOException {
        Map res = getResponse(new String[]{
            "-do" , "login",
            "username", username,
            "password", password
        });
        
        int code = (int)(double)res.get("code");
        if (code != 200) {
            throw new IOException((String)res.get("message"));
        } else {
            token = (String)res.get("token");
            this.username = username;
            if (token == null) {
                throw new IOException("No token received after login");
            }
        }
    }
    
}
