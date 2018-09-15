package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.interfaces.service.SubjectService;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.webapp.form.CourseForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;


@Controller
public class CourseController {

    @Autowired
    @Qualifier("courseServiceImpl")
    private CourseService courseService;

    @Autowired
    @Qualifier("subjectServiceImpl")
    private SubjectService subjectService;

    @Autowired
    @Qualifier("professorServiceImpl")
    private ProfessorService professorService;

    @RequestMapping("/Course")
    public ModelAndView course(
            @RequestParam(value="professor", required=true) final Long professor_id,
            @RequestParam(value="subject", required=true) final Long subject_id
    ){
        final ModelAndView mav = new ModelAndView("course");
        mav.addObject("course", courseService.findCourseByIds(professor_id, subject_id));
        return mav;
    }

    @RequestMapping("/createCourse")
    public ModelAndView createCourse(@ModelAttribute("CourseForm") final CourseForm form) {
        final ModelAndView mav = new ModelAndView("createCourse");
        mav.addObject("subjects", subjectService.filterSubjectsByName(""));
        return mav;
    }

    @RequestMapping(value = "/createCourse", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("CourseForm") final CourseForm form,
                               final BindingResult errors) {
        if(errors.hasErrors()) {
            return createCourse(form);
        }

        final Subject subject = subjectService.findSubjectById(form.getSubjectId());

        //TODO: Catch null && replace professor null

        final Course course = courseService.create(null, subject, form.getDescription(), form.getPrice());

        return new ModelAndView("redirect:/Course/?professor=" + course.getProfessor().getId() +
                "&subject=" + course.getSubject().getId());
    }

}
