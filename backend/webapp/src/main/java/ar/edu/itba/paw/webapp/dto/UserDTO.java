package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User;

import java.net.URI;

public class UserDTO {

    private Long id;
    private String username;
    private String name;
    private String lastName;
    private String email;
    private URI url;

    public UserDTO(){}

    public UserDTO(final User user, final URI baseUri){
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.lastName = user.getLastname();
        this.email = user.getEmail();
        this.url = baseUri;
    }

    public UserDTO(Long id, String username, String name, String lastName, String email, URI url) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }
}
