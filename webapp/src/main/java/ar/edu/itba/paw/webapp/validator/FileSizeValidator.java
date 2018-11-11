package ar.edu.itba.paw.webapp.validator;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

   private Long min;

   private Long max;

   public void initialize(FileSize constraint) {
      min = constraint.min();
      max = constraint.max();
   }

   public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
      if(file == null)
         return false;
      return file.getSize() <= max && file.getSize() >= min;
   }
}
