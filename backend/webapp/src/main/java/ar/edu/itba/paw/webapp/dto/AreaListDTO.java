package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Area;

import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
public class AreaListDTO {

    private List<AreaDTO> areas;
    private int count;
    private long totalCount;

    public AreaListDTO() {
    }

    public AreaListDTO(final List<Area> areas, long totalCount, final URI uri) {
        this.areas = new LinkedList<>();
        areas.forEach(area -> this.areas.add(new AreaDTO(area, uri)));
        this.count = areas.size();
        this.totalCount = totalCount;
    }

    public List<AreaDTO> getAreas() {
        return areas;
    }

    public void setAreas(List<AreaDTO> areas) {
        this.areas = areas;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
