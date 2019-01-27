package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.CourseFile;

import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
public class CourseFileListDTO {

    private List<CourseFileDTO> files;
    private int count;

    public CourseFileListDTO() {
    }

    public CourseFileListDTO(final List<CourseFile> files, final URI uri) {
        this.files = new LinkedList<>();
        files.forEach(file -> this.files.add(new CourseFileDTO(file, uri)));
        this.count = files.size();
    }

    public List<CourseFileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<CourseFileDTO> files) {
        this.files = files;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
