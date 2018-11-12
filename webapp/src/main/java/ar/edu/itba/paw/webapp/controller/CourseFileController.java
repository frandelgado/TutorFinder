package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.UserAuthenticationException;
import ar.edu.itba.paw.interfaces.service.CourseFileService;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.CourseFile;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.UploadClassFileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class CourseFileController extends BaseController {

    @Autowired
    CourseFileService cfs;

    @Autowired
    CourseService cs;

    @RequestMapping(value = "/courseFiles", method = RequestMethod.GET)
    public ModelAndView getCourseFiles(@RequestParam("professor") final Long professorId,
                               @RequestParam("subject") final Long subjectId,
                               @ModelAttribute("currentUser") final User currentUser,
                               @ModelAttribute("uploadClassFileForm") final UploadClassFileForm form) {
        Course course = cs.findCourseByIds(professorId, subjectId);
        if(course == null) {
            return  redirectToErrorPage("nonExistentCourse");
        }
        List<CourseFile> courseFiles = null;
        try {
            courseFiles = cfs.findForCourse(course, currentUser);
        } catch (UserAuthenticationException e) {
            return redirectToErrorPage("403");
        }
        ModelAndView mav = new ModelAndView("courseFiles");
        mav.addObject("courseFiles", courseFiles);
        return mav;
    }


    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public void downloadFile(@RequestParam("courseFile")final long courseFileId,
                                     @ModelAttribute("currentUser") final User currentUser,
                                     HttpServletResponse response,
                                     HttpServletRequest request) {

        CourseFile courseFile;
        try {
            courseFile = cfs.findByIdForUser(courseFileId, currentUser);
        } catch (UserAuthenticationException e) {
            return;
        }

        //No file present
        if(courseFile == null) {
            return;
        }

        response.setContentType(courseFile.getType());
        response.setContentLength(courseFile.getContent().length);
        response.setHeader("Content-Disposition","attachment; filename=\"" + courseFile.getName() +"\"");

        try {
            FileCopyUtils.copy(courseFile.getContent(), response.getOutputStream());
        } catch (IOException e) {
            //TODO: handle error
        }

    }


    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ModelAndView uploadFile(@Valid @ModelAttribute("uploadClassFileForm") final UploadClassFileForm form,
                                   BindingResult result,
                                   @RequestParam("professor") final Long professorId,
                                   @RequestParam("subject") final Long subjectId,
                                   @ModelAttribute("currentUser") final User currentUser) {

        if(result.hasErrors()) {
            return getCourseFiles(professorId, subjectId, currentUser, form);
        }
        Course course = cs.findCourseByIds(professorId, subjectId);

        if(course.getProfessor().getId().compareTo(currentUser.getId()) != 0) {
            return redirectToErrorPage("403");
        }

        CourseFile document = new CourseFile();

        document.setName(form.getFile().getOriginalFilename());
        document.setDescription(form.getDescription());
        document.setType(form.getFile().getContentType());
        try {
            document.setContent(form.getFile().getBytes());
        } catch (IOException e) {
            return redirectToErrorPage("somethingWentWrong");
        }
        document.setCourse(course);
        cfs.save(document);
        return getCourseFiles(professorId, subjectId, currentUser, form);
    }

    @RequestMapping("/deleteFile")
    public ModelAndView deleteFile(@ModelAttribute("currentUser") final User currentUser,
                                   @RequestParam("professor") final Long professorId,
                                   @RequestParam("subject") final Long subjectId,
                                   @RequestParam("file") final Long fileId) {

        try {
            cfs.deleteById(fileId, currentUser);
        } catch (UserAuthenticationException e) {
            return redirectToErrorPage("403");
        }
        return getCourseFiles(professorId, subjectId, currentUser, new UploadClassFileForm());
    }
}
