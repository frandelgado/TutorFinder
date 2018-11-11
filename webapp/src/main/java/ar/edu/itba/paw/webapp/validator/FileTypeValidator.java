package ar.edu.itba.paw.webapp.validator;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileTypeValidator implements ConstraintValidator<FileType, MultipartFile> {
   public void initialize(FileType constraint) {
   }

   public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
      if(file != null && file.getSize() > 0)
         return (file.getContentType().equals("image/jpeg")||file.getContentType().equals("image/png"));
      return true;
   }
}
