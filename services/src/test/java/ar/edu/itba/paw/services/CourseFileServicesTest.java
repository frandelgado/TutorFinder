package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.CourseFileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class CourseFileServicesTest {

    @Spy
    @InjectMocks
    @Autowired
    private CourseFileService cfs;

    @Test
    public void userWithAccessCanGetFile(){

    }

    @Test
    public void userWithoutAccessCannotGetFile(){

    }

    @Test
    public void userWithAccessCanGetCourseFiles(){

    }

    @Test
    public void userWithoutAccessCannotGetCourseFiles(){

    }

}
