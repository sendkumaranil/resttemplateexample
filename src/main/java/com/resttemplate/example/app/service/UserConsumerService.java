package com.resttemplate.example.app.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.resttemplate.example.app.model.AuthTokenInfo;
import com.resttemplate.example.app.model.User;


/**
 * 
 * @author anilkumar
 *
 */
@Service
public class UserConsumerService {

	public static final String REST_SERVICE_URI = "http://localhost:8181/";
	     
    public static final String AUTH_SERVER_URI = "http://localhost:8181/oauth/token";
     
    public static final String QPM_PASSWORD_GRANT = "?grant_type=password&username=anil&password=kumar";
     
    public static final String QPM_ACCESS_TOKEN = "?access_token=";
    
    /*
     * Prepare HTTP Headers.
     */
    private static HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }
    
    /*
     * Add HTTP Authorization header, using Basic-Authentication to send client-credentials.
     */
    private static HttpHeaders getHeadersWithClientCredentials(){
        String plainClientCredentials="my-trusted-client:secret";
        String base64ClientCredentials = new String(Base64.encodeBase64(plainClientCredentials.getBytes()));
         
        HttpHeaders headers = getHeaders();
        headers.add("Authorization", "Basic " + base64ClientCredentials);
        return headers;
    } 
    
    /*
     * Send a POST request [on /oauth/token] to get an access-token, which will then be send with each request.
     */
    @SuppressWarnings({ "unchecked"})
    private static AuthTokenInfo sendTokenRequest(){
        RestTemplate restTemplate = new RestTemplate(); 
         
        HttpEntity<String> request = new HttpEntity<String>(getHeadersWithClientCredentials());
        ResponseEntity<Object> response = restTemplate.exchange(AUTH_SERVER_URI+QPM_PASSWORD_GRANT, HttpMethod.POST, request, Object.class);
        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>)response.getBody();
        AuthTokenInfo tokenInfo = null;
         
        if(map!=null){
            tokenInfo = new AuthTokenInfo();
            tokenInfo.setAccess_token((String)map.get("access_token"));
            tokenInfo.setToken_type((String)map.get("token_type"));
            tokenInfo.setRefresh_token((String)map.get("refresh_token"));
            tokenInfo.setExpires_in((int)map.get("expires_in"));
            tokenInfo.setScope((String)map.get("scope"));
            System.out.println(tokenInfo);
           
        }else{
            System.out.println("No user exist----------");
             
        }
        return tokenInfo;
    }
    
    /*
     * Send a GET request to get list of all users.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public  List<User> listAllUsers(){
    	    AuthTokenInfo tokenInfo = sendTokenRequest();
        Assert.notNull(tokenInfo, "Authenticate first please......");
 
        RestTemplate restTemplate = new RestTemplate(); 
         
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        ResponseEntity<List> response = restTemplate.exchange(REST_SERVICE_URI+"/user/"+QPM_ACCESS_TOKEN+tokenInfo.getAccess_token(),
                HttpMethod.GET, request, List.class);
        List<LinkedHashMap<String, Object>> usersMap = (List<LinkedHashMap<String, Object>>)response.getBody();
         
        List<User> users=new ArrayList<>();
        if(usersMap!=null){
            for(LinkedHashMap<String, Object> map : usersMap){
            	    User user=new User(
            	    		String.valueOf(map.get("username")),
            	    		String.valueOf(map.get("emailid")),
            	    		String.valueOf(map.get("contact"))
            	    	);
            	    users.add(user);
            }
        }else{
            System.out.println("No user exist----------");
        }
        return users;
    }
    
    /*
     * Send a GET request to get a specific user.
     */
    public User getUser(String username){
    		AuthTokenInfo tokenInfo = sendTokenRequest();
        Assert.notNull(tokenInfo, "Authenticate first please......");
        
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        ResponseEntity<User> response = restTemplate.exchange(REST_SERVICE_URI+"/user/"+username+QPM_ACCESS_TOKEN+tokenInfo.getAccess_token(),
                HttpMethod.GET, request, User.class);
        User user = response.getBody();
        return user;
    }
    
    /*
     * Send a POST request to create a new user.
     */
    public URI createUser(User user) {
    		AuthTokenInfo tokenInfo = sendTokenRequest();
        Assert.notNull(tokenInfo, "Authenticate first please......");
        
        RestTemplate restTemplate = new RestTemplate();
        
        HttpEntity<Object> request = new HttpEntity<Object>(user, getHeaders());
        URI uri = restTemplate.postForLocation(REST_SERVICE_URI+"/user/"+QPM_ACCESS_TOKEN+tokenInfo.getAccess_token(),
        		request, Void.class);
        return uri;
    }
    
    /*
     * Send a PUT request to update an existing user.
     */
    public  void updateUser(User user) {
    		AuthTokenInfo tokenInfo = sendTokenRequest();
        Assert.notNull(tokenInfo, "Authenticate first please......");
        
        RestTemplate restTemplate = new RestTemplate();
        
        HttpEntity<Object> request = new HttpEntity<Object>(user, getHeaders());
        restTemplate.exchange(REST_SERVICE_URI+"/user/"+QPM_ACCESS_TOKEN+tokenInfo.getAccess_token(),
                HttpMethod.PUT, request,Void.class);
    }
    
    /*
     * Send a DELETE request to delete a specific user.
     */
    public void deleteUser(String username) {
    		AuthTokenInfo tokenInfo = sendTokenRequest();
        Assert.notNull(tokenInfo, "Authenticate first please......");
        
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(getHeaders());
        System.out.println(REST_SERVICE_URI+"/user/"+username+QPM_ACCESS_TOKEN+tokenInfo.getAccess_token());
        restTemplate.exchange(REST_SERVICE_URI+"/user/"+username+QPM_ACCESS_TOKEN+tokenInfo.getAccess_token(),
                HttpMethod.DELETE, request, Void.class);
    }
}
