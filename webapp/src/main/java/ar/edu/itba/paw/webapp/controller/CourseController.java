package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.interfaces.service.SubjectService;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.webapp.form.ContactForm;
import ar.edu.itba.paw.webapp.form.CourseForm;
import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private EmailService emailService;

    @RequestMapping("/Course")
    public ModelAndView course(
            @ModelAttribute("contactForm") final ContactForm form,
            @RequestParam(value="professor", required=true) final Long professorId,
            @RequestParam(value="subject", required=true) final Long subjectId
    ){
        final ModelAndView mav = new ModelAndView("course");
        mav.addObject("course", courseService.findCourseByIds(professorId, subjectId));
        return mav;
    }

    @RequestMapping("/contact")
    public ModelAndView contactProfessor(
            @Valid @ModelAttribute("ContactForm") final ContactForm form,
            @RequestParam(value="professorEmail", required = true) final String professorEmail
    ){
        //TODO: deberiamos checkear que sea valido el email aca?
        emailService.sendEmail(professorEmail, form.getSubject(), form.getBody());
        return new ModelAndView("redirect:/");
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

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String professorName = auth.getName();

        final Professor professor = professorService.findByUsername(professorName);

        final Subject subject = subjectService.findSubjectById(form.getSubjectId());

        //TODO: Catch null

        final Course course = courseService.create(professor, subject, form.getDescription(), form.getPrice());

        return new ModelAndView("redirect:/Course/?professor=" + course.getProfessor().getId() +
                "&subject=" + course.getSubject().getId());
    }

}
