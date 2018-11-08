package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.ClassRequest;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;

public interface ClassReservationDao {

    ClassRequest reserve(int day, int startHour, int endHour, Professor professor, User student);

    ClassRequest confirm(ClassRequest classRequest);

    ClassRequest deny(ClassRequest classRequest);
}
