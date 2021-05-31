package cn.edu.bit.ruixin.community.vo;

import cn.edu.bit.ruixin.community.annotation.FieldNeedCheck;
import cn.edu.bit.ruixin.community.domain.Appointment;
import cn.edu.bit.ruixin.community.exception.GlobalParamException;
import lombok.Data;

//import javax.validation.constraints.Pattern;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/6
 */
@Data
public class AppointmentInfoVo {

    private Integer id;
    private Integer begin; // 起始时间段
    private String username;
    private String phone;
    private String schoolId;
    private Integer end; // 结束时间段
    private Integer roomId;
    private String roomName;
    private Integer launchTime; // 废弃
    private String launcher;
    private String status;
    private int attendance;
    @FieldNeedCheck
    private String conductor;
    @FieldNeedCheck
    private String checkNote;
    @FieldNeedCheck
    private String userNote;
    private long checkDate;
    private long launchDate;
//    @Pattern(regexp = "^\\d{4}-\\d{1,2}-\\d{1,2}", message = "日期格式：yyyy-MM-dd")
    private String execDate;

    public static Appointment convertToPo(AppointmentInfoVo infoVo) {
        Appointment appointment = new Appointment();

        Integer id = infoVo.getId();
        Integer begin = infoVo.getBegin();
        Integer end = infoVo.getEnd();
        Integer roomId = infoVo.getRoomId();
        Integer launchTime = infoVo.getLaunchTime();
        String launcher = infoVo.getLauncher();
        String status = infoVo.getStatus();
        String conductor = infoVo.getConductor();
        String userNote = infoVo.getUserNote();
        String checkNote = infoVo.getCheckNote();
//        @Pattern(regexp = "^\\d{4}-\\d{1,2}-\\d{1,2}", message = "日期格式：yyyy-MM-dd")
        String execDate = infoVo.getExecDate();
        int attendance = infoVo.getAttendance();

        if (id!=null && id != 0) {
            appointment.setId(id);
        }
        if (begin != null && begin != 0) {
            appointment.setBegin(begin);
        }
        if (end!= null && end != 0) {
            appointment.setEnd(end);
        }
        if (roomId != null && roomId != 0) {
            appointment.setRoomId(roomId);
        }
        if (launchTime != null && !launchTime.equals("")) {
            appointment.setLaunchTime(launchTime);
        }
        if (launcher != null && !launcher.equals("")) {
            appointment.setLauncher(launcher);
        }
        if (status != null && !status.equals("")) {
            appointment.setStatus(status);
        }
        if (conductor != null && !conductor.equals("")) {
            appointment.setConductor(conductor);
        }
        if (userNote != null && !userNote.equals("")) {
            appointment.setUserNote(userNote);
        }
        if (checkNote != null && !checkNote.equals("")) {
            appointment.setCheckNote(checkNote);
        }
        if (execDate != null && !execDate.equals("")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = dateFormat.parse(execDate);
            } catch (ParseException e) {
                throw new GlobalParamException("格式日期有误，请重新预约！");
            }
            appointment.setExecDate(date);
        }
        if (attendance != 0) {
            appointment.setAttendance(attendance);
        }

        return appointment;
    }

    public static AppointmentInfoVo convertToVo(Appointment appointment) {
        AppointmentInfoVo appointmentInfoVo = new AppointmentInfoVo();

        Integer id = appointment.getId();
        Integer begin = appointment.getBegin();
        Integer end = appointment.getEnd();
        int attendance = appointment.getAttendance();
        Integer roomId = appointment.getRoomId();
        Integer launchTime = appointment.getLaunchTime();
        String launcher = appointment.getLauncher();
        String status = appointment.getStatus();
        String conductor = appointment.getConductor();
        String checkNote = appointment.getCheckNote();
        String userNote = appointment.getUserNote();
        Date execDate = appointment.getExecDate();
        Date checkDate = appointment.getCheckDate();
        Date launchDate = appointment.getLaunchDate();

        if (id!=null && id != 0) {
            appointmentInfoVo.setId(id);
        }
        if (begin != null && begin != 0) {
            appointmentInfoVo.setBegin(begin);
        }
        if (end!= null && end != 0) {
            appointmentInfoVo.setEnd(end);
        }
        if (roomId != null && roomId != 0) {
            appointmentInfoVo.setRoomId(roomId);
        }
        if (launchTime != null && launchTime != 0) {
            appointmentInfoVo.setLaunchTime(launchTime);
        }
        if (launcher != null && !launcher.equals("")) {
            appointmentInfoVo.setLauncher(launcher);
        }
        if (status != null && !status.equals("")) {
            appointmentInfoVo.setStatus(status);
        }
        if (conductor != null && !conductor.equals("")) {
            appointmentInfoVo.setConductor(conductor);
        }
        if (userNote != null && !userNote.equals("")) {
            appointmentInfoVo.setUserNote(userNote);
        }
        if (checkNote != null && !checkNote.equals("")) {
            appointmentInfoVo.setCheckNote(checkNote);
        }
        if (checkDate != null) {
            appointmentInfoVo.setCheckDate(checkDate.getTime());
        }
        if (launchDate != null) {
            appointmentInfoVo.setLaunchDate(launchDate.getTime());
        }
        if (attendance != 0) {
            appointmentInfoVo.setAttendance(attendance);
        }
        if (execDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(execDate);
            System.out.printf(date);
            appointmentInfoVo.setExecDate(date);
        }

        return appointmentInfoVo;
    }

}
