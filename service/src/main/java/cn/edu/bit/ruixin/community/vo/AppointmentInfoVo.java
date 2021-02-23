package cn.edu.bit.ruixin.community.vo;

import cn.edu.bit.ruixin.community.domain.Appointment;

import javax.persistence.Column;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/6
 */
public class AppointmentInfoVo {

    private Integer id;
    private Integer begin;
    private Integer end;
    private Integer roomId;
    private Integer launchTime;
    private String launcher;
    private String status;
    private String conductor;
    private String note;

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
        String note = infoVo.getNote();

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
        if (note != null && !note.equals("")) {
            appointment.setNote(note);
        }

        return appointment;
    }

    public static AppointmentInfoVo convertToVo(Appointment appointment) {
        AppointmentInfoVo appointmentInfoVo = new AppointmentInfoVo();

        Integer id = appointment.getId();
        Integer begin = appointment.getBegin();
        Integer end = appointment.getEnd();
        Integer roomId = appointment.getRoomId();
        Integer launchTime = appointment.getLaunchTime();
        String launcher = appointment.getLauncher();
        String status = appointment.getStatus();
        String conductor = appointment.getConductor();
        String note = appointment.getNote();

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
        if (note != null && !note.equals("")) {
            appointmentInfoVo.setNote(note);
        }

        return appointmentInfoVo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBegin() {
        return begin;
    }

    public void setBegin(Integer begin) {
        this.begin = begin;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(Integer launchTime) {
        this.launchTime = launchTime;
    }

    public String getLauncher() {
        return launcher;
    }

    public void setLauncher(String launcher) {
        this.launcher = launcher;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "AppointmentInfoVo{" +
                "id=" + id +
                ", begin=" + begin +
                ", end=" + end +
                ", roomId=" + roomId +
                ", launchTime='" + launchTime + '\'' +
                ", launcher='" + launcher + '\'' +
                ", status='" + status + '\'' +
                ", conductor='" + conductor + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
