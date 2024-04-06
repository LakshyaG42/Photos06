package photosfx.models;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;


public class User implements Serializable{
    private static final long serialVersionUID = 1L;

    private String username;
    private List<Album> albums;

    public User(String username) {
        this.username = username;
        this.albums = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public void addAlbum(Album album) {
        this.albums.add(album);
    }

    public void removeAlbum(Album album) {
        this.albums.remove(album);
    }

    public void renameAlbum(Album album, String newName) {
        album.setName(newName);
    }

}
