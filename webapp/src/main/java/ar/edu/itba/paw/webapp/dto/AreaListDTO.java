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
    private int totalCount;

    public AreaListDTO() {
    }

    public AreaListDTO(final List<Area> areas, final URI uri) {
        this.areas = new LinkedList<>();
        for (Area area: areas) {
            this.areas.add(new AreaDTO(area, uri));
        }
        this.count = areas.size();
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
}
