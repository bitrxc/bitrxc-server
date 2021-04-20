package cn.edu.bit.ruixin.community.service;


import cn.edu.bit.ruixin.community.domain.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/6
 */
public interface AppointmentService {
    List<Appointment> getAllAppointmentByUsername(String username);

    Appointment getAppointmentById(Integer id);

    void addANewAppointment(Appointment appointment);

    void cancelAppointmentById(Integer id);

    List<Appointment> getAllAppointment(String status);

    void checkOutAppointment(Integer id, String status, String conductor, String checkNote);

    Page<Appointment> getAppointmentPages(Pageable pageable, String status);

    Page<Appointment> getAppointmentsBySchoolId(Pageable pageable, String schoolId);
}
