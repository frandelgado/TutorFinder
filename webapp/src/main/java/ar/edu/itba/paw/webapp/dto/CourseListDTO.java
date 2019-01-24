package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Course;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
public class CourseListDTO {

    private List<CourseDTO> courses;
    private int count;
    private int totalCount;

    public CourseListDTO() {
    }

    public CourseListDTO(final List<Course> courses, final URI baseUri) {
        this.courses = new LinkedList<>();
        for (Course course: courses) {
            this.courses.add(new CourseDTO(course, baseUri));
        }
        this.count = courses.size();
    }

    @XmlElement
    public List<CourseDTO> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseDTO> courses) {
        this.courses = courses;
    }

    @XmlElement
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @XmlElement
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
