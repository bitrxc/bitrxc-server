package cn.edu.bit.ruixin.community.service;


import cn.edu.bit.ruixin.community.domain.Appointment;

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

    void checkOutAppointment(Integer id, String status, String conductor);
}
