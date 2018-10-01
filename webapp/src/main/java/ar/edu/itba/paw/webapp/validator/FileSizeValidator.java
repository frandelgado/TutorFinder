package ar.edu.itba.paw.webapp.validator;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {
   public void initialize(FileSize constraint) {
   }

   public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
      return file.getSize() <= 81920 && file.getSize() > 0;
   }
}
