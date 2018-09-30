package ar.edu.itba.paw.webapp.validator;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileTypeValidator implements ConstraintValidator<FileType, MultipartFile> {
   public void initialize(FileType constraint) {
   }

   public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
      return file.getContentType().equals("image/jpeg");
   }
}
